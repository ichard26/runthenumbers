package runthenumbers.gui;

/**
 * Interface Name: CalculationCallback
 * Description: Callable interface invoked when a calculation entry is created.
 * Programmer: Richard Si
 * Date: June 12, 2025.
 */
@FunctionalInterface
public interface CalculationCallback {
    /**
     * Method Name: run
     * Description: The code to run upon the creation of a calculation entry.
     * @param e The newly created calculation entry.
     */
    public void run(CalculationEntry e);
}
