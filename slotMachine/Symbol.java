/**
 * The Symbol class represents a single symbol that can be assigned to
 * a wheel within a SlotMachine.
 * <p>
 * A symbol is defined simply by its color, which is used to identify
 * and display it on the wheels of the machine.
 *
 * @author Juan Diego Cardozo Beltrán
 * @author Diego Alejandro Díaz Boada
 * @version 22/08/26
 */
public class Symbol {
    /**
     * The color that represents this symbol.
     */
    private String color;

    /**
     * Constructs a new Symbol with the given color.
     *
     * @param color the color assigned to this symbol
     */
    public Symbol(String color) {
        this.color = color;
    }
    
    /**
     * Returns the color of this symbol.
     *
     * @return the color assigned to this symbol
     */
    public String getSymbol() {
        return color;
    }
}