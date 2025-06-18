package runthenumbers.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static runthenumbers.utils.Random.formatNumber;

@FunctionalInterface
interface Callback {
    public void run(Component c);
}

/**
 * Class Name: CalculationHistoryGUI
 * Description: Manages the history GUI panel.
 * Programmer: Richard Si
 * Date: June 13, 2025.
 */
public class CalculationHistoryGUI {
    private static final Font ENTRY_FONT = CalculatorGUI.KEY_FONT.deriveFont(Font.PLAIN).deriveFont(16f);
    private static final File HISTORY_FILE = new File("history.txt");
    private JPanel rootPanel;
    private JPanel allEntryPanel;
    private final ArrayList<CalculationEntry> entries = new ArrayList<>();
    private boolean sortByTime = true;

    // No getters or setters as no outside code should be inspecting or messing
    // with these fields.

    public CalculationHistoryGUI() {
        Scanner reader;
        try {
            reader = new Scanner(HISTORY_FILE);
        } catch (FileNotFoundException ex) {
            // Log error, but keep going.
            ex.printStackTrace(System.err);
            return;
        }
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (!line.isBlank())
                entries.add(CalculationEntry.fromLine(reader.nextLine()));
        }
    }

    /**
     * Method Name: constructPanel
     * Description: Construct Swing panel for the history GUI.
     * @return The scrollable panel.
     */
    public JScrollPane constructPanel() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));

        // History management top panel.
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        // Sort mode switcher.
        JButton sortButton = new JButton("Sort by answer");
        sortButton.addActionListener((e) -> {
            sortByTime = !sortByTime;
            sortButton.setText("Sort by " + (sortByTime ? "answer" : "time"));
            renderEntries();
        });
        controlPanel.add(sortButton);
        // Remove all button.
        JButton clearButton = new JButton("Remove all");
        clearButton.addActionListener((e) -> {
            entries.clear();
            saveToFile();
            renderEntries();
        });
        controlPanel.add(clearButton);
        rootPanel.add(controlPanel);

        // Create root scrollable panel to contain entries.
        allEntryPanel = new JPanel();
        allEntryPanel.setLayout(new BoxLayout(allEntryPanel, BoxLayout.PAGE_AXIS));
        rootPanel.add(allEntryPanel);
        JScrollPane scrollFrame = new JScrollPane(rootPanel);
        scrollFrame.setPreferredSize(new Dimension(350, 500));

        // Render any entries loaded from the history file now.
        renderEntries();
        return scrollFrame;
    }

    /**
     * Method Name: addEntry
     * Description: Add a calculation to the history (and save to disk).
     * @param entries The calculation entries to add.
     */
    public void addEntry(CalculationEntry... entries) {
        this.entries.addAll(List.of(entries));
        saveToFile();
        renderEntries();
    }

    /**
     * Method Name: renderEntries
     * Description: Render the stored calculation entries to the GUI panel.
     */
    private void renderEntries() {
        // Remove all existing entries from the GUI.
        allEntryPanel.removeAll();

        ArrayList<CalculationEntry> sortedEntries;
        if (sortByTime)
            sortedEntries = entries;
        else
            sortedEntries = sortByAnswer(entries);

        for (CalculationEntry e : sortedEntries.reversed()) {
            JPanel entryPanel = constructEntryPanel(e, entries.indexOf(e) + 1, (c) -> {
                // Upon the press of "remove", the entry needs to be removed
                // from history (and synced with the file) and the panel needs
                // to be rerendered.
                entries.remove(e);
                saveToFile();
                renderEntries();
            });
            allEntryPanel.add(entryPanel);
        }
        rootPanel.updateUI();
    }

    /**
     * Method Name: constructEntryPanel
     * Description: Construct the Swing component for one calculation.
     * @param entry The calculation entry to render.
     * @param index Which calculation is this?
     * @param removeCallback A callable invoked when an entry needs to be removed.
     * @return The constructed Swing component.
     */
    private JPanel constructEntryPanel(CalculationEntry entry, int index, Callback removeCallback) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Entry removal button.
        JButton removeButton = new JButton("Remove");
        removeButton.setFont(ENTRY_FONT.deriveFont(12f));
        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.addActionListener((ae) -> {removeCallback.run(panel); });
        panel.add(removeButton);
        // Add some horizontal buffer space before the next component.
        panel.add(Box.createHorizontalStrut(20));

        // Entry # label.
        JLabel entryIndexLabel = new JLabel(Integer.toString(index));
        entryIndexLabel.setFont(ENTRY_FONT);
        panel.add(entryIndexLabel);

        // Entry type (evaluate/solve) label.
        JLabel modeLabel = new JLabel("(" + entry.getMode() + ")");
        modeLabel.setFont(ENTRY_FONT.deriveFont(12f));
        modeLabel.setForeground(Color.gray);
        panel.add(modeLabel);

        // Entry input label.
        JLabel inputLabel = new JLabel(entry.getInput());
        inputLabel.setFont(ENTRY_FONT.deriveFont(12f));
        panel.add(Box.createHorizontalStrut(10));
        panel.add(inputLabel);

        // Answer label.
        JLabel answerLabel = new JLabel("ANS: " + formatNumber(entry.getAnswer()));
        answerLabel.setFont(ENTRY_FONT.deriveFont(12f));
        panel.add(Box.createHorizontalStrut(10));
        panel.add(answerLabel);

        return panel;
    }

    /**
     * Method Name: saveToFile
     * Description: Save current history contents to the history file.
     */
    private void saveToFile() {
        PrintWriter writer;
        try {
            writer = new PrintWriter(HISTORY_FILE);
        } catch (FileNotFoundException ex) {
            // Log error, but don't crash the program.
            ex.printStackTrace(System.err);
            return;
        }

        for (CalculationEntry e : entries)
            writer.println(e.toLine());
        writer.close();
    }

    /**
     * Method Name: sortByAnswer
     * Description: Sort calculation entries by answer in ascending order.
     * @param originalEntries The calculation entries.
     * @return A sorted copy of the original array list.
     */
    private static ArrayList<CalculationEntry> sortByAnswer(ArrayList<CalculationEntry> originalEntries) {
        // This is bog standard bubble sort.
        ArrayList<CalculationEntry> entries = new ArrayList<>(originalEntries);
        CalculationEntry dummy;
        boolean shouldSwap;
        boolean doneSorting = false;

        while (!doneSorting) {
            doneSorting = true;
            for (int i = 0; i < entries.size() - 1; i++) {
                shouldSwap = entries.get(i).getAnswer() > entries.get(i + 1).getAnswer();
                if (shouldSwap) {
                    dummy = entries.get(i);
                    entries.set(i, entries.get(i + 1));
                    entries.set(i + 1, dummy);
                    doneSorting = false;
                }
            }
        }
        return entries;
    }

}
