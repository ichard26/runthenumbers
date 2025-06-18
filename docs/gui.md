# Implementation Notes: runthenumbers.gui

The proposed list of features relevant to the GUI include:

- Multi-line input/output display
- Programmable variables (A, B, C)
- Persistent calculation history
- Bulk evaluation and solving

## UML Diagram

```{plantuml}
set separator none
package runthenumbers.gui {
    class MainGUI
    class CalculatorGUI
    class CalculationHistoryGUI
    class HistoryEntry
}

class HistoryEntry {
    -String inputType
    -String input
    -double solution
    +Getters/Setters()
    .. (De)serialization ..
    +fromLine(String line) : HistoryEntry
    +toLine() : String
    ..
}

class MainGUI {
    The main application GUI.
    Manages profile selection and the construction
    and organisation of the application panels.

    +loadProfiles() : HashMap<String, ArrayList<HistoryEntry>>
    +run(HashMap<String, ArrayList<HistoryEntry>> profiles)
}
MainGUI ..> CalculatorGUI : manages
MainGUI ..> CalculationHistoryGUI : manages

class CalculatorGUI {
    The actual calculator that does math.
    --
    -JPanel rootPanel
    -JLabel[3] displayLabels
    -JLabel activeLine
    -int activeLineIndex
    -String previousButton
    -String mathMode
    -HashMap<String, Double> variables;
    --
    +constructPanel() : JPanel
    -constructDisplay() : JPanel
    -constructNumpadPanel() : JPanel
    -constructOperatorVariablePanel() : JPanel
    -appendDisplayLine(String contents) : void
    -nextDisplayLine() : void
    -handleButton(String id) : void
    -bulkCalculations() : void
}

class CalculationHistoryGUI {
    Manages the calculation history and history file.
    --
    -ArrayList<HistoryEntry> entries
    --
    +constructPanel() : JPanel
    +addEntry(HistoryEntry e) : void
    +renderEntries() : void
    -constructEntryPanel() : JPanel
    -saveToFile() : void
    -sortByAnswer(ArrayList<HistoryEntry> entries) : void
}
```

## HistoryEntry Pseudocode

```java
class HistoryEntry {
    private String inputType;
    private String input;
    private double answer;

    // <Getsets>.
    // <Arg-constructor>.

    public HistoryEntry fromLine(String line) {
        inputType, input, answer = line.split(" | ");
        return new HistoryEntry(inputType, input, answer);
    }

    public HistoryEntry toLine() {
        return mode + " | " + input + " | " + answer;
    }
}
```

## MainGUI Pseudocode

```java
class MainGUI {
    public static void run() {
        // Ask which profile should be loaded.
        presentProfileSelectionGUI();

        // Switch to main GUI.
        frame = new JFrame("RunTheNumbers");
        // <Configure frame style, position, size, etc.>
        frame.add(CalculatorGUI.constructPanel());
        frame.add(CalculationHistory.constructPanel(this.profile));
        bulkFrame = BulkOperationsGUI.constructFrame();
        // <Add buttons for switching between calculator
        // and bulk operations panels>
        frame.setVisibility(true);
    }

    private static void presentProfileSelectionGUI() {
        profiles = loadProfiles();
        frame = new JFrame();
        frame.add(/*profile buttons which set this.profile appropriately*/);
    }

    private static HashMap loadProfiles() {
        profiles = new HashMap();
        file = // <load data file>
        for (line : file) {
            user, entry = line.split(" | ", 1);
            profiles[user].add(HistoryEntry.fromLine(entry));
        }
        return profiles;
    }
}
```

## CalculatorGUI Pseudocode

```java
class CalculatorGUI {
    // <Attributes>

    private static void constructPanel() {
        panel = new JPanel();
        panel.add(/*display labels A, B, C;
                   numpad panel, operator panel, etc.*/);
        panel.add(/*other buttons, like clear, delete, and bulk CALC*/);
        return panel;
    }

    private static JPanel constructDisplay() {
        panel = new JPanel();
        loop 3 times {
            line = new JLabel();
            // <Configure line width, height, etc.>
            panel.add(line);
        }
        return panel;
    }

    private static JPanel constructNumpad() {
        panel = new JPanel();
        for key in (1...9, "(", ")", ".") {
            button = new JButton();
            // <Configure button style>
            button.addHandler(() -> handleKeypress(key));
            panel.add(button);
        }
        return panel;
    }

    private static JPanel constructOperatorVariablePanel {
        panel = new JPanel();
        for key in ("+", "-", "*", "/", "^", "=") {
            // <same logic as constructNumpad>
        }
        for key in ("A", "B", "C", "STO", "RCL") {
            // <same logic as constructNumpad>
        }
        return panel;
    }

    private static nextDisplayLine() {
        if (on last line)
            clear whole display
        this.activeLine = next line (label) down;
    }

    private static appendToDisplay(String text) {
        JLabel line = displayLabels[active line index];
        line += text;
    }

    private static handleButton(String key) {
        if (key is a number OR operator) {
            appendDisplayLine(currentLine, key);
        }
        else if (key is equal sign) {
            evaluateOrSolve();
            currentLine = (currentLine + 1) % 3;
        }
        else if (key is CLEAR) {
            if (previousKey is CLEAR)
                clear all lines
            else
                clear current line
        }
        else if (previousKey is a variable && key is STO) {
            this.variables[previousKey] = value of current line
        }
        else if (previousKey is a variable && key is RCL) {
            // <write this.variables[previousKey] to display>
        }
        else if (key is variable) {
            appendDisplayLine(variable);
        }
        else if (key is equal sign) {
            input = current line
            node = Parser.parse(input)
            if (node is an equation)
                solve(node) -> write to display
            else
                evaluate(node, this.variables) -> write to display
            nextDisplayLine();
            entry = new HistoryEntry(...);
            notify history GUI of new entry
        }
    }

    private static void bulkCalculations() {
        ArrayList<Double> answers;
        // Step one: ask the user to provide a file with a list expressions
        // to evaluate or equations to solve.
        open file dialog
        open the file and parse each line

        // Step two: actually evaluate/solve these inputs.
        for (node : parsedLines) {
            if (node is an equation)
                solve(node) -> answers.add(...)
            else
                evaluate(node, this.variables) -> answers.add(...)
        }

        // Step three: present the results by adding them to the history.
        historyGUI.add(answers);
    }
}
```

### CalculationHistoryGUI Pseudocode

```java
class CalculationHistoryGUI {
    public CalculationHistoryGUI() {
        // Open history file and add each entry to this.entries.
    }

    private JPanel constructPanel() {
        this.rootPanel = new JPanel();

        // Add sub-panels for buttons and entry list.
        // Add sort mode button to buttons sub-panel.
        sortModeButton.addActionListener(/*
            this.sortByTime = !sortByTime;
            sortModeButton.setText("sort by {answer or time}")
            renderEntries();
            */)
        // Add remove all button to buttons sub-panel.
        removeAllButton.addActionListener(/*
            this.entries.clear();
            saveToFile();
            renderEntries();
            */)

        // Make root panel scrollable
        return this.rootPanel;
    }

    private void addEntry(HistoryEntry e) {
        this.entries.add(e);
        saveToFile();
        renderEntries();
    }

    private void renderEntries() {
        this.rootPanel.removeAllChildren();

        // Sort entries as appropriate (by time or answer).
        // Entries are inserted in time order, only answer
        // order needs extra sorting.

        for (e : this.entries) {
            this.rootPanel.add(constructEntryPanel(e));
        }
        rootPanel.forceUpdate();
    }

    private JPanel constructEntryPanel(HistoryEntry e) {
        JPanel panel = new JPanel();
        // Add entry removal button.
        removeButton.addActionListener(/*
            remove entry from this.entries
            saveToFile();
            renderEntries();
            */)
        // Add horizontal buffer (aka an empty box).
        // Add entry # number label.
        // Add entry type (evaluate/solve) label.
        // Add entry input label.
        // Add entry answer label.
        return panel.
    }

    private void saveToFile() {
        PrintWriter writer = new ...;
        for (e : this.entries) {
            writer.println(e.toLine())
        }
    }

    private static ArrayList<HistoryEntry> sortByAnswer(ArrayList<HistoryEntry> original) {
        // normal bubble sort with HistoryEntry.getAnswer() as the key.
    }
}
```
