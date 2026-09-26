import javax.swing.JOptionPane;
import java.util.TreeMap;
/**
 * The Wheel class represents a single wheel/slot within a SlotMachine.
 * <p>
 * Each wheel is made up of a set of rectangles that give it its shape,
 * along with a circle that represents the symbol currently assigned
 * to it. When a wheel is first created, it has no symbol assigned and
 * is displayed in white.
 * <p>
 * The wheel knows whether it is locked, so it is the one that decides
 * whether it can be spun, changed, swapped or removed.
 *
 * @author Juan Diego Cardozo Beltrán
 * @author Diego Alejandro Díaz Boada
 * @version 22/08/26
 */
public class Wheel
{
    /**
     * The four rectangles that make up the shape of the wheel's slot.
     */
    private Rectangle[] body = new Rectangle[4];
    
    /**
     * The circle representing the symbol currently displayed on the wheel.
     */
    private Circle sym = new Circle();
    
    public static final int height = 100;
    
    public static final int width = 90;
    
    private TreeMap <Integer, Symbol> symbols;
    
    private boolean locked = false;
    
    /**
     * Width in pixels of the white inner border of the wheel.
     */
    private static final int BORDER = 5;
    
    /**
     * Constructs a new Wheel at the given position of the machine.
     * Sizes are computed in pixels from height and width, and the position
     * from SlotMachine.COLUMNS and SlotMachine.INTERVAL. Since the wheel
     * has no symbol assigned at the start, it is left in white.
     *
     * @param number  the position of the wheel (starting at 1)
     * @param symbols the symbols available in the machine
     */
    public Wheel(int number, TreeMap<Integer, Symbol> symbols){
        this.symbols = symbols;
        int bar = height / 5;
        int diameter = Math.min(width, height - 2 * bar) * 3 / 4;
        for (int i = 0; i < 4; i++) {
            body[i] = new Rectangle();
            body[i].changeColor("darkGray");
        }
        body[0].changeSize(height, width);
        body[1].changeSize(height - 2 * BORDER, width - 2 * BORDER);
        body[1].moveHorizontal(BORDER);
        body[1].moveVertical(BORDER);
        body[1].changeColor("white");
        body[2].changeSize(bar, width);
        body[3].changeSize(bar, width);
        body[3].moveVertical(height - bar);
        sym.changeSize(diameter);
        sym.moveHorizontal((width - diameter) / 2);
        sym.moveVertical((height - diameter) / 2);
        if (symbols.size() != 0) {
            sym.changeColor(symbols.get(symbols.firstKey()).getSymbol());
        }
        else {
            sym.changeColor("white");
        }
        int column = (number - 1) % SlotMachine.COLUMNS;
        int line = (number - 1) / SlotMachine.COLUMNS;
        moveTo(SlotMachine.INTERVAL + column * (width + SlotMachine.INTERVAL),
               SlotMachine.INTERVAL + line * (height + SlotMachine.INTERVAL));
    }
    
    /**
     * Moves the wheel to the next symbol of the machine, only if the
     * wheel is not locked.
     *
     * @return true if the wheel spun, false if it is locked
     */
    public boolean spin() {
        if (locked) {
            return false;
        }
        int symbolKey = -1;
        for (Integer key : symbols.keySet()) {
            if (symbols.get(key).getSymbol() == sym.getColor()) {
                symbolKey = key;
            }
        }
        if (symbols.higherKey(symbolKey) != null) {
            setSymbol(symbols.get(symbols.higherKey(symbolKey)).getSymbol());
        }
        else {
            setSymbol(symbols.get(symbols.firstKey()).getSymbol());
        }
        return true;
    }
    
    /**
     * Moves every shape of the wheel by the given offset in pixels.
     *
     * @param x the horizontal offset in pixels
     * @param y the vertical offset in pixels
     */
    private void moveTo(int x, int y) {
        for (int i = 0; i < 4; i++) {
            body[i].moveHorizontal(x);
            body[i].moveVertical(y);
        }
        sym.moveHorizontal(x);
        sym.moveVertical(y);
    }
    
    /**
     * Makes the wheel visible.
     */
    public void makeVisible(){
        body[0].makeVisible();
        body[1].makeVisible();
        body[2].makeVisible();
        body[3].makeVisible();
        sym.makeVisible();
    }
    
    /**
     * Makes the wheel invisible.
     */
    public void makeInvisible(){
        body[0].makeInvisible();
        body[1].makeInvisible();
        body[2].makeInvisible();
        body[3].makeInvisible();
        sym.makeInvisible();
    }
    
    /**
     * Places a symbol on the wheel, only if the wheel is not locked.
     *
     * @param color the color of the symbol to place
     * @return true if the symbol was placed, false if the wheel is locked
     */
    public boolean placeSymbol(String color) {
        if (locked) {
            return false;
        }
        setSymbol(color);
        return true;
    }
    
    /**
     * Changes the symbol shown by the wheel regardless of its lock.
     * Used by the machine when its own set of symbols changes.
     *
     * @param color the new color to assign to the wheel's symbol
     */
    public void setSymbol(String color) {
        sym.changeColor(color);
    }
    
    /**
     * Returns the symbol (color) currently assigned to the wheel.
     *
     * @return the color of the symbol currently displayed on the wheel
     */
    public String getSymbol() {
        return sym.getColor();
    }
    
    /**
     * Locks the wheel.
     *
     * @return true if the wheel was locked, false if it was already locked
     */
    public boolean lock() {
        if (locked) {
            return false;
        }
        locked = true;
        return true;
    }
    
    /**
     * Unlocks the wheel.
     *
     * @return true if the wheel was unlocked, false if it was not locked
     */
    public boolean unlock() {
        if (!locked) {
            return false;
        }
        locked = false;
        return true;
    }
    
    /**
     * Exchanges the symbol of this wheel with the symbol of another one,
     * only if neither of them is locked.
     * 
     * @param wheel2 the wheel to swap symbols with
     * @return true if the symbols were swapped, false if a wheel is locked
     */
    public boolean swapWheel(Wheel wheel2) {
        if (locked || wheel2.locked) {
            return false;
        }
        String symTemp = this.getSymbol();
        this.setSymbol(wheel2.getSymbol());
        wheel2.setSymbol(symTemp);
        return true;
    }
    
    /**
     * Hides the wheel so it can be removed, only if it is not locked.
     *
     * @return true if the wheel can be removed, false if it is locked
     */
    public boolean remove() {
        if (locked) {
            return false;
        }
        makeInvisible();
        return true;
    }
}