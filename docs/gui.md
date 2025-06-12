# Implementation Notes: runthenumbers.gui

The proposed list of features relevant to the GUI include:

- Multi-line input/output display
- Persistent calculation history
- Bulk evaluation and solving

## UML Diagram

```plantuml
set separator none
package runthenumbers.gui {
    class MainGUI
    class CalculatorGUI
    class CalculationHistoryGUI
    class BulkOperationsGUI
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
MainGUI ..> BulkOperationsGUI : manages

class CalculatorGUI {
    +constructPanel() : JPanel
    -constructDisplay() : JPanel
    -constructNumpadPanel() : JPanel
    -constructOperatorPanel() : JPanel
    -setDisplayLine(int row, String contents) : void
    -handleButton(String id) : void
}

class CalculationHistoryGUI {
    +constructPanel(ArrayList<HistoryEntry> existingHistory) : JPanel
}

class BulkOperationsGUI {
    +constructFrame() : JFrame
}
```

Words.

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

    private static JPanel constructOperatorPanel {
        panel = new JPanel();
        for key in ("+", "-", "*", "/", "^", "=") {
            // <same logic as constructNumpad>
        }
        return panel;
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
    }
}
```
