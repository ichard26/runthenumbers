package runthenumbers.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import runthenumbers.math.Evaluator;
import runthenumbers.math.ast.Equation;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.Parser;
import runthenumbers.math.ast.RootNode;
import runthenumbers.math.solve.LinearSolver;
import runthenumbers.math.solve.Simplifier;
import runthenumbers.math.tokenize.TokenizeError;
import static runthenumbers.utils.Random.formatNumber;

// https://stackoverflow.com/questions/7971178/find-out-if-text-of-jlabel-exceeds-label-size
// https://planetjon.ca/java-global-jframe-key-listener-3089

enum AutomaticClear {OFF, ACTIVE_LINE, DISPLAY};

/**
 *
 * @author Richard Si
 */
class CalculatorKeyListener implements KeyListener {
    @Override
    public void keyPressed(KeyEvent event) {
        CalculatorGUI.handleKeyboard(event);
    }

    @Override
    public void keyTyped(KeyEvent ke) {}
    @Override
    public void keyReleased(KeyEvent ke) {}
}

/**
 * @date May 23, 2025
 * @author Richard Si
 */
public class CalculatorGUI {
    // Constant GUI styling values.
    private static final Font KEY_FONT = new Font("Ubuntu Bold", Font.BOLD, 22);
    private static final Font INPUT_FONT = new Font("Ubuntu Mono", Font.PLAIN, 22);
    private static final Border BORDER_10PX = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    // Components that must be managed globally.
    private static final JLabel[] displayLabels = new JLabel[3];
    private static JLabel activeLine;
    private static JButton clearButton;
    private static JButton backspaceButton;
    private static JButton modeButton;

    // Calculator global state.
    private static int activeLineIndex;
    private static AutomaticClear clearOnNumpadOrVariable = AutomaticClear.OFF;
    private static String previousButton = "";
    private static String mathMode = "evaluate";
    private static final HashMap<String, Double> variables = new HashMap<>();

    public static JPanel constructPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(380, 500));
        // Piece together the calculator UI.
        panel.add(constructDisplayPanel(), BorderLayout.PAGE_START);
        panel.add(constructNumpad(), BorderLayout.CENTER);
        panel.add(constructOperatorVariablePanel(), BorderLayout.LINE_END);
        panel.add(constructCalcButtons(), BorderLayout.PAGE_END);
        // Install keyboard event handler.
        panel.addKeyListener(new CalculatorKeyListener());
        return panel;
    }

    /**
     * Method Name: constructDisplayPanel
     * Description: Construct display UI.
     * @return The UI component panel.
     */
    private static JPanel constructDisplayPanel() {
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));
        rootPanel.setBorder(BORDER_10PX);

        // Construct the display itself (which consists of three JLabels).
        JPanel displayPanel = new JPanel();
        Border padding = BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
                BORDER_10PX);

        displayPanel.setBackground(Color.white);
        displayPanel.setBorder(padding);
        displayPanel.setLayout(new GridLayout(3, 1));
        for (int i = 0; i < 3; i++) {
            JLabel label = new JLabel("");
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            // Ensure the line is tall enough.
            label.setPreferredSize(new Dimension(0, 30));
            label.setFont(INPUT_FONT);
            displayPanel.add(label);
            displayLabels[i] = label;
        }
        activeLine = displayLabels[0];

        // Construct the clear/delete button panel.
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        clearButton = new JButton("Clear");
        clearButton.addActionListener((e) -> handleButton("display", "clear"));
        commandPanel.add(clearButton);
        backspaceButton = new JButton("Delete");
        backspaceButton.addActionListener((e) -> handleButton("display", "backspace"));
        commandPanel.add(backspaceButton);

        rootPanel.add(displayPanel);
        rootPanel.add(commandPanel);
        return rootPanel;
    }

    /**
     * Method Name: constructNumpad
     * Description: Construct numpad buttons and panel.
     * @return The UI component panel.
     */
    public static JPanel constructNumpad() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 3, 5, 5));
        panel.setBorder(BORDER_10PX);

        // Add buttons in a 5 by 3 grid as shown below.
        // Empty string represents an empty cell.
        for (String value : List.of(
                "7", "8", "9",
                "4", "5", "6",
                "1", "2", "3",
                "(", "0", ")",
                "", ".", "")) {
            if (value.isBlank())
                panel.add(new JLabel());
            else {
                JButton button = new JButton(value);
                button.setFont(KEY_FONT);
                button.addActionListener((e) -> handleButton("numpad", value));
                // Color bracket buttons differently.
                if (value.equals("(") || value.equals(")"))
                    button.setBackground(Color.LIGHT_GRAY);
                panel.add(button);
            }
        }

        return panel;
    }

    /**
     * Method Name: constructOperatorVariablePanel
     * Description: Construct operator / variable entry panel.
     * @return The UI component panel.
     */
    public static JPanel constructOperatorVariablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 5, 5));
        panel.setPreferredSize(new Dimension(130, 0));
        panel.setBorder(BORDER_10PX);

        // Add buttons in a 5 by 2 grid as shown below.
        for (String value : List.of(
                "+", "A",
                "-", "B",
                "*", "C",
                "/", "STO",
                "^", "RCL")) {
            JButton button = new JButton(value);
            button.setFont(KEY_FONT);
            if (List.of("A", "B", "C", "STO" ,"RCL").contains(value)) {
                // Variable button.
                button.setBackground(Color.orange);
                button.setForeground(Color.white);
                button.setFont(KEY_FONT.deriveFont(18f));
                button.addActionListener((e) -> handleButton("variable", value));
            } else {
                // Operator button.
                button.addActionListener((e) -> handleButton("operator", value));
            }
            button.setMargin(new Insets(0, 0, 0, 0));  // Remove inner margin
            panel.add(button);                         // to shrink them.
        }
        return panel;
    }

    /**
     * Method Name: constructCalcButtons
     * Description Construct mode switcher and "Calc" button UI.
     * @return The UI component panel.
     */
    public static JPanel constructCalcButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2, 30, 50));
        panel.setPreferredSize(new Dimension(0, 70));
        panel.setBorder(BORDER_10PX);

        // Calculator mode switcher.
        modeButton = new JButton("Mode: evaluate");
        modeButton.setFont(KEY_FONT.deriveFont(14f));
        modeButton.addActionListener((e) -> handleButton("mode", null));
        panel.add(modeButton);

        // "Calc" button. Its function depends on the mode.
        JButton equalButton = new JButton("=");
        // White on green for equals key.
        equalButton.setBackground(new Color(51, 204, 51));
        equalButton.setForeground(Color.white);
        equalButton.setFont(KEY_FONT);
        equalButton.addActionListener((e) -> handleButton("equal-sign", null));
        panel.add(equalButton);

        return panel;
    }

    /**
     * Method Name: nextDisplayLine
     * Description: Mark the next display line as active (accepting input).
     */
    public static void nextDisplayLine() {
        // If switching back to the top line, clear the entire screen first.
        if (activeLineIndex == 2) {
            for (JLabel line : displayLabels)
                line.setText("");
        }
        activeLineIndex = (activeLineIndex + 1) % 3;
        activeLine = displayLabels[activeLineIndex];
    }

    /**
     * Method Name: appendToDisplay
     * Description: Add text to the active display line.
     * @param text The text to add.
     */
    private static void appendToDisplay(String text) {
        JLabel line = displayLabels[activeLineIndex];
        line.setText(line.getText() + text);
    }

    /**
     * Method Name: handleButton
     * Description: Consolidated action handling for all calculator buttons.
     * @param type The button type (which part of the calculator it belongs to).
     * @param name The button value.
     */
    public static void handleButton(String type, String name) {
        // Clear the current line on input if needed unless it's an operator
        // being entered, in which case assume it's being applied to (likely)
        // the answer of the previous calculation
        if (clearOnNumpadOrVariable != AutomaticClear.OFF && (
                type.equals("numpad") || type.equals("variable"))) {
            clearButton.doClick();
            // Everything needs to be cleared so issue a double press.
            if (clearOnNumpadOrVariable == AutomaticClear.DISPLAY)
                clearButton.doClick();
        }

        clearOnNumpadOrVariable = AutomaticClear.OFF;
        // Perform the appropriate action given the button type.
        switch (type) {
            case "numpad" -> appendToDisplay(name);
            case "operator" -> {
                // Wrap operators in spaces as it's prettier (except powers).
                if (name.equals("^"))
                    appendToDisplay(name);
                else
                    appendToDisplay(" " + name + " ");
            }
            case "variable" -> {
                if (name.equals("RCL") || name.equals("STO"))
                    // Do nothing as RCL/STO must be followed by a variable button.
                    break;

                // Check if the RCL/STO operation is active first
                if (previousButton.equals("RCL"))
                    appendToDisplay(formatNumber(variables.getOrDefault(name, .0)));
                else if (previousButton.equals("STO")) {
                    // HACK: treating the line as a calculation is arguably
                    // overkill but it will handle answers like A = 5 (from solving).
                    CalculationEntry entry = calculateLine();
                    if (entry == null || entry.getAnswer() == null)
                        break;
                    variables.put(name, entry.getAnswer());
                    nextDisplayLine();
                    appendToDisplay("set " + name + " = " + variables.get(name));
                    nextDisplayLine();
                }
                // ... otherwise simply add the variable to the display.
                else
                    appendToDisplay(name.toLowerCase());
            }
            case "mode" -> {
                // Toggle between expression evaluation / equation solving.
                mathMode = mathMode.equals("evaluate") ? "solve" : "evaluate";
                modeButton.setText("Mode: " + mathMode);
            }
            case "equal-sign" -> {
                // In equation mode, the input needs an equal sign, so the first
                // press enters a literal equal sign. Subsequent presses will
                // solve the equation as normal, though.
                if (mathMode.equals("solve") && !activeLine.getText().contains("=")) {
                    appendToDisplay(" = ");
                    break;
                }
                calculateLineAndShow();
            }
            case "display" -> {
                if (name.equals("clear")) {
                    // If the active line is already blank, then clear the entire screen.
                    if (activeLine.getText().isEmpty())
                        for (JLabel line : displayLabels)
                            line.setText("");
                    else
                        activeLine.setText("");
                }
                else if (name.equals("backspace")) {
                    // Strip trailing spaces before removing the last character.
                    String contents = activeLine.getText().stripTrailing();
                    if (contents.isEmpty())
                        return;
                    activeLine.setText(contents.substring(0, contents.length() - 1));
                }
            }
            default -> throw new AssertionError("unexpected button type: " + type);
        }

        // Keep track of the last button pressed for compound actions (aka RCL/STO).
        previousButton = name;
    }

    protected static void handleKeyboard(KeyEvent event) {
        char value = event.getKeyChar();
        switch (event.getKeyCode()) {
            case KeyEvent.VK_ENTER -> calculateLineAndShow();
            case KeyEvent.VK_BACK_SPACE -> backspaceButton.doClick();
            case KeyEvent.VK_M -> modeButton.doClick();
            case KeyEvent.VK_E -> clearButton.doClick();
            case KeyEvent.VK_R -> handleButton("variable", "RCL");
            case KeyEvent.VK_S -> handleButton("variable", "STO");
            default -> {
                if (Set.of('1', '2', '3', '4', '5', '7', '8',
                        '9', '0', '.', ' ', '=', '+', '-',
                        '*', '/', '^', '(', ')').contains(value))
                    handleButton("numpad", String.valueOf(value));
                else if (Set.of('a', 'b', 'c').contains(value))
                    handleButton("variable", String.valueOf(value));
            }
        }
    }

    private static void calculateLineAndShow() {
        CalculationEntry entry = calculateLine();
        if (entry != null && entry.getAnswer() != null) {
            nextDisplayLine();
            appendToDisplay(formatNumber(entry.getAnswer()));
            clearOnNumpadOrVariable = AutomaticClear.ACTIVE_LINE;
        }
    }

    private static CalculationEntry calculateLine() {
        // TODO: error handling
        String input = activeLine.getText();
        RootNode root;
        Double answer;

        if (input.isBlank())
            return null;

        try {
            root = Parser.parse(input);
        } catch (TokenizeError e) {
            String[] lines = e.getMessage().split("\n");
            // Clear the whole display.
            clearButton.doClick();
            clearButton.doClick();
            // Write the error message to the display.
            activeLineIndex = 0;
            clearOnNumpadOrVariable = AutomaticClear.DISPLAY;
            displayLabels[0].setText("ERROR: " + lines[0]);
            displayLabels[1].setText(lines[1]);
            displayLabels[2].setText(lines[2]);
            return null;
        }

        Simplifier.simplify(root);
        answer = switch (root) {
            case Expression expr -> Evaluator.evaluate(expr, variables);
            case Equation eqn -> new LinearSolver().solve(eqn);
        };

        return new CalculationEntry(mathMode, input, answer);
    }

}
