import javax.swing.JOptionPane;
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
    
    /**
     * Constructs a new Wheel.
     * The wheel is made up of a list of 4 rectangles that give shape to
     * the slot, and a circle in the middle representing the symbol. Since
     * the wheel has no symbol assigned at the start, it is left in white.
     */
    public Wheel(){
        for (int i = 0; i < 4; i++) {
            body[i] = new Rectangle();
        }
        body[0].changeSize(108, 99);
        body[0].moveVertical(10);
        body[0].moveHorizontal(10);
        body[0].changeColor("darkGray");
        body[1].changeSize(98, 89);
        body[1].moveVertical(15);
        body[1].moveHorizontal(15);
        body[1].changeColor("white");
        body[2].changeSize(20, 99);
        body[2].moveVertical(10);
        body[2].moveHorizontal(10);
        body[2].changeColor("darkGray");
        body[3].changeSize(20, 99);
        body[3].moveVertical(98);
        body[3].moveHorizontal(10);
        body[3].changeColor("darkGray");
        sym.changeSize(39);
        sym.moveVertical(44);
        sym.moveHorizontal(40);
        sym.changeColor("white");
    }
    
    /**
     * Positions the wheel's shapes according to their vertical position
     * on the machine, moving them enough to leave a 10 pixel vertical
     * gap between wheels.
     *
     * @param y the vertical position to move the wheel to
     */
    public void moveVertical(int y) {
        for (int i = 0; i < 4; i++) {
            body[i].moveVertical(y*118);
        }
        sym.moveVertical(y*118);
    }
    
    /**
     * Positions the wheel's shapes according to their horizontal position
     * on the machine, moving them enough to leave a 10 pixel horizontal
     * gap between wheels.
     *
     * @param x the horizontal position to move the wheel to
     */
    public void moveHorizontal(int x) {
        for (int i = 0; i < 4; i++) {
            body[i].moveHorizontal(x*109);
        }
        sym.moveHorizontal(x*109);
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
    public void changeSymbol(String color) {
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
}