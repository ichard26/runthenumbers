package runthenumbers.utils;

/**
 * Interface Name: PrettyPrintable
 * Description: Interface for objects with custom formatting via the PrettyFormatter.
 * Programmer: Richard Si
 * Date: June 5, 2025.
 */
public interface PrettyPrintable {
    /**
     * Method Name: toPrettyString
     * Description: Format the node for display via the PrettyPrinter utility class.
     * @return The pretty string representation.
     */
    public String toPrettyString();
}
