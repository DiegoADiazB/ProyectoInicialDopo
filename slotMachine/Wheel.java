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
 * The class provides functionality to position the wheel both
 * vertically and horizontally within the machine, show or hide the
 * wheel, and change or retrieve the symbol it currently displays.
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
     * Constructs a new Wheel.
     * The wheel is made up of a list of 4 rectangles that give shape to
     * the slot, and a circle in the middle representing the symbol. Since
     * the wheel has no symbol assigned at the start, it is left in white.
     */
    public Wheel(int number, TreeMap<Integer, Symbol> symbols){
        this.symbols = symbols;
        for (int i = 0; i < 4; i++) {
            body[i] = new Rectangle();
            body[i].changeColor("darkGray");
            body[i].moveHorizontal(SlotMachine.interval);
            body[i].moveVertical(SlotMachine.interval);
        }
        body[0].changeSize(height, width);
        body[1].changeSize(height-10, width-10);
        body[1].moveVertical(5);
        body[1].moveHorizontal(5);
        body[1].changeColor("white");
        body[2].changeSize(height/5, width);
        body[3].changeSize(height/5, width);
        body[3].moveVertical(height - height/5);
        sym.changeSize(width/2);
        sym.moveVertical(height/7 + 3*height/8);
        sym.moveHorizontal(width/4 + SlotMachine.interval);
        if (symbols.size() != 0) {
            sym.changeColor(symbols.get(symbols.firstKey()).getSymbol());
        }
        else {
            sym.changeColor("white");
        }
        moveVertical((number-1)/ (SlotMachine.wheelsNumber/SlotMachine.linesOfWheels));
        moveHorizontal((number-1)% (SlotMachine.wheelsNumber/SlotMachine.linesOfWheels));
    }
    
    public void spin() {
        int symbolKey = -1;
        for (Integer key : symbols.keySet()) {
            if (symbols.get(key).getSymbol() == sym.getColor()) {
                symbolKey = key;
            }
        }
        if (symbols.higherKey(symbolKey) != null) {
            placeSymbol(symbols.get(symbols.higherKey(symbolKey)).getSymbol());
        }
        else {
            placeSymbol(symbols.get(symbols.firstKey()).getSymbol());
        }
    }
    
    /**
     * Positions the wheel's shapes according to their vertical position
     * on the machine, moving them enough to leave a 10 pixel vertical
     * gap between wheels.
     *
     * @param y the vertical position to move the wheel to
     */
    private void moveVertical(int y) {
        for (int i = 0; i < 4; i++) {
            body[i].moveVertical(y*(height + SlotMachine.interval/2));
        }
        sym.moveVertical(y*(height + SlotMachine.interval/2));
    }
    
    /**
     * Positions the wheel's shapes according to their horizontal position
     * on the machine, moving them enough to leave a 10 pixel horizontal
     * gap between wheels.
     *
     * @param x the horizontal position to move the wheel to
     */
    private void moveHorizontal(int x) {
        for (int i = 0; i < 4; i++) {
            body[i].moveHorizontal(x*(width+ SlotMachine.interval));
        }
        sym.moveHorizontal(x*(width+ SlotMachine.interval));
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
     * Changes the symbol currently displayed on the wheel.
     * Changes the color of the circle to the color received as a
     * parameter.
     *
     * @param color the new color to assign to the wheel's symbol
     */
    public void placeSymbol(String color) {
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
    
    public void setLock(){
        locked = true;
    }
    
    public void setUnlock(){
        locked = false;
    }
    
    public boolean isLocked(){
        return locked;
    }
    
    public void swapWheel(Wheel wheel) {
        String symTemp = this.getSymbol();
        this.placeSymbol(wheel.getSymbol());
        wheel.placeSymbol(symTemp);
    }
}