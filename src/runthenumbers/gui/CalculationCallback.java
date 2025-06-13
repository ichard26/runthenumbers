package runthenumbers.gui;

/**
 *
 * @author Richard Si
 */
@FunctionalInterface
public interface CalculationCallback {
    public void run(CalculationEntry e);
}
