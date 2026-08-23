import javax.swing.JOptionPane;
import java.util.TreeMap;
import java.util.Map;
import java.util.Arrays;

/**
 * The SlotMachine class represents a slot machine composed of a body,
 * a lever handle, a set of wheels, and a set of symbols.
 * <p>
 * The machine's shape is built from four rectangles and a circle
 * representing the lever handle. Wheels and symbols are stored in
 * separate TreeMaps, indexed by their position, which allows the
 * machine to manage, add, remove, and update them individually or
 * as a whole.
 * <p>
 * The class also provides functionality to spin individual wheels or
 * all wheels at once, place specific symbols on a wheel, and check
 * whether a jackpot condition has been reached (i.e. when all wheels
 * display the same symbol).
 *
 * @author Juan Diego Cardozo Beltrán
 * @author Diego Alejandro Díaz Boada
 * @version 22/08/26
 */
public class SlotMachine
{
    /**
     * The four rectangles that make up the shape/body of the slot machine.
     */
    private Rectangle[] body = new Rectangle[4];
    
    /**
     * The circle representing the lever handle of the slot machine.
     */
    private Circle handle = new Circle();
    
    /**
     * Stores the wheels of the slot machine, indexed by their position.
     */
    private TreeMap <Integer, Wheel> wheels;
    
    /**
     * Stores the symbols available for the slot machine, indexed by their
     * position.
     */
    private TreeMap <Integer, Symbol> symbols;
    
    /**
     * Indicate if the app can do the last action.
     */
    private boolean ok = true;
    
    /**
     * Constructs a new SlotMachine.
     * Initializes the machine's shape using an array of 4 rectangles and a
     * circle representing the lever handle. Also initializes the TreeMap
     * used to store the symbols and the TreeMap used to store the wheels.
     */
    public SlotMachine(){
        for (int i = 0; i < 4; i++) {
            body[i] = new Rectangle();
        }
        body[0].changeSize(600,1100);
        body[0].changeColor("lightGray");
        body[1].changeSize(80,1000);
        body[1].moveVertical(600);
        body[1].moveHorizontal(50);
        body[1].changeColor("darkGray");
        body[2].changeSize(40,140);
        body[2].moveVertical(300);
        body[2].moveHorizontal(1100);
        body[2].changeColor("darkGray");
        body[3].changeSize(120,40);
        body[3].moveVertical(180);
        body[3].moveHorizontal(1200);
        body[3].changeColor("darkGray");
        handle.changeSize(120);
        handle.moveVertical(80);
        handle.moveHorizontal(1160);
        handle.changeColor("red");
        wheels = new TreeMap<>();
        symbols = new TreeMap<>();
    }
    
    /**
     * Adds a new wheel to the slot machine at the given position.
     * The wheel is only created if the position is between 1 and 50
     * (inclusive) and no other wheel already exists at that position.
     * If there are existing symbols when the wheel is created, the wheel
     * is assigned (colored with) the symbol with the lowest position.
     *
     * @param pos the position where the new wheel will be placed
     */
    public void addWheel(int pos) {
        ok = false;
        if (! wheels.containsKey(pos) && pos <= 50 && pos >= 1) {
            wheels.put(pos, new Wheel());
            wheels.get(pos).moveVertical((int) (pos-1)/10);
            wheels.get(pos).moveHorizontal((pos-1)%10);
            ok = true;
            if (symbols.size() != 0) {
                wheels.get(pos).changeSymbol(symbols.get(symbols.firstKey()).getSymbol());
            }
            if (ok() && body[0].getIsVisible()) {
                makeVisible();
            }
        }
        else if (body[0].getIsVisible()) {
            JOptionPane.showMessageDialog(null, "This wheel cannot be created.");
        }
    }
    
    /**
     * Removes an existing wheel from the slot machine.
     * The wheel is removed from the TreeMap that stores the wheels.
     *
     * @param pos the position of the wheel to be removed
     */
    public void delWheel(int pos) {
        ok = false;
        if (wheels.containsKey(pos)) {
            wheels.get(pos).makeInvisible();
            wheels.remove(pos);
            ok = true;
        }
        else if (body[0].getIsVisible()){
            JOptionPane.showMessageDialog(null, "This wheel don't exist.");
        }
    }
    
    /**
     * Adds a new symbol to the slot machine.
     * The symbol is stored in the TreeMap at the given position and is
     * created using the specified color. If this is the first symbol
     * created, it is assigned to all existing wheels.
     *
     * @param pos   the position at which the symbol will be stored
     * @param color the color assigned to the new symbol
     */
    public void addSymbol(int pos, String color) {
        boolean band = false;
        for (Map.Entry<Integer, Symbol> i : symbols.entrySet()) {
            if (color == i.getValue().getSymbol()) {
                band = true;
                break;
            }
        }
        ok = false;
        if (!symbols.containsKey(pos) && !band && pos > 0) {
            symbols.put(pos, new Symbol(color));
            if (symbols.size() == 1) {
                for (Integer key : wheels.keySet()) {
                    wheels.get(key).changeSymbol(color);
                }
            }
            ok = true;
        }
        else if (symbols.containsKey(pos) && body[0].getIsVisible()) {
            JOptionPane.showMessageDialog(null, "A symbol already exists in this position.");
        }
        else if (pos < 1 && body[0].getIsVisible()) {
            JOptionPane.showMessageDialog(null, "You only can add symbols in positives positions.");
        }
        else if (body[0].getIsVisible()) {
            JOptionPane.showMessageDialog(null, "This symbol already exists in any position.");
        }
    }
    
    /**
     * Removes a symbol from the slot machine.
     * Searches the TreeMap for a symbol matching the given color. If found,
     * its position is stored and every wheel currently displaying that
     * symbol is spun. If it was the last remaining symbol, all wheels are
     * left without a symbol (set to white).
     *
     * @param symbol the color of the symbol to be removed
     */
    public void delSymbol(String symbol) {
        boolean band = false;
        int pos = -1;
        ok = false;
        for (Map.Entry<Integer, Symbol> i : symbols.entrySet()) {
            if (symbol == i.getValue().getSymbol()) {
                band = true;
                pos = i.getKey();
                break;
            }
        }
        if (band) {
            for (Integer key : wheels.keySet()) {
                if (symbol == wheels.get(key).getSymbol()) {
                    spin(key);
                }
            }
            if (symbols.size() == 1) {
                for (Integer key : wheels.keySet()) {
                    wheels.get(key).changeSymbol("white");
                }
            }
            symbols.remove(pos);
            ok = true;
        }
        else if (body[0].getIsVisible()) {
            JOptionPane.showMessageDialog(null, "This symbol don't exists in any position.");
        }
    }
    
    /**
     * Places a symbol on a specific wheel.
     * Verifies that both the wheel and the symbol exist in their
     * corresponding data structures before assigning the symbol to the
     * wheel. Afterwards, checks whether the jackpot conditions have been
     * met in order to activate it (changing the machine's color to
     * indicate the new state).
     *
     * @param wheel  the position of the wheel to update
     * @param symbol the color of the symbol to place on the wheel
     */
    public void placeSymbol(int wheel, String symbol) {
        boolean band = false;
        ok = false;
        for (Map.Entry<Integer, Symbol> i : symbols.entrySet()) {
            if (symbol == i.getValue().getSymbol()) {
                band = true;
                break;
            }
        }
        if (band && wheels.containsKey(wheel)) {
            wheels.get(wheel).changeSymbol(symbol);
            if (isJackpot() && body[0].getIsVisible()) {
                body[0].changeColor("yellow");
                makeVisible();
            }
            else if (body[0].getIsVisible()) {
                body[0].changeColor("lightGray");
                makeVisible();
            }
            ok = true;
        }
        else if (!wheels.containsKey(wheel) && body[0].getIsVisible()) {
            JOptionPane.showMessageDialog(null, "This wheel don´t exist.");
        }
        else if (body[0].getIsVisible()) {
            JOptionPane.showMessageDialog(null, "This symbol don´t exist.");
        }
    }
    
    /**
     * Spins a single wheel.
     * If the wheel exists, it is assigned the next symbol in the symbol
     * sequence. Afterwards, checks whether the jackpot has been achieved
     * in order to update the machine's state.
     *
     * @param wheel the position of the wheel to spin
     */
    public void spin(int wheel) {
        String color;
        int pos = -1;
        ok = false;
        if (wheels.containsKey(wheel) && symbols.size() != 0) {
            color = wheels.get(wheel).getSymbol();
            for (Map.Entry<Integer, Symbol> i : symbols.entrySet()) {
                if (color == i.getValue().getSymbol()) {
                    pos = i.getKey();
                }
            }
            if (symbols.higherKey(pos) != null) {
                color = symbols.get(symbols.higherKey(pos)).getSymbol();
            }
            else {
                color = symbols.get(symbols.firstKey()).getSymbol();
            }
            wheels.get(wheel).changeSymbol(color);
            ok = true;
            if (isJackpot() && body[0].getIsVisible()) {
                body[0].changeColor("yellow");
                makeVisible();
            }
            else if (body[0].getIsVisible()) {
                body[0].changeColor("lightGray");
                makeVisible();
            }
        }
        else if (body[0].getIsVisible()) {
            JOptionPane.showMessageDialog(null, "This wheel don´t exist or don't exists symbols.");
        }
    }
    
    /**
     * Spins all the wheels in the slot machine.
     * Iterates through every wheel and spins it. Once all wheels have
     * finished spinning, checks whether the jackpot has been achieved in
     * order to update the machine's state.
     */

    public void spin() {
        ok = false;
        if (wheels.size() != 0) {
            for (Integer key : wheels.keySet()) {
                spin(key);
            }
        }
        else if (body[0].getIsVisible()) {
            JOptionPane.showMessageDialog(null, "In this moment don't exist any wheel to spin.");
        }
    }
    
    /**
     * Returns all the possible symbols that can appear on the wheels.
     * These are the symbols currently stored in the TreeMap.
     *
     * @return an array containing all the symbols (colors) currently
     *         registered in the slot machine
     */
    public String[] symbols() {
        ok = true;
        String[] symbols = new String[this.symbols.size()];
        int j = 0;
        for (Map.Entry<Integer, Symbol> i : this.symbols.entrySet()) {
            symbols[j] = i.getValue().getSymbol();
            j += 1;
        }
        return symbols;
    }
    
    /**
     * Counts how many distinct symbols are currently assigned among all
     * the wheels. If there are no wheels to check, returns 0.
     *
     * @return the number of distinct symbols found across all wheels,
     *         or 0 if there are no wheels
     */
    public int distinctSymbols() {
        ok = true;
        int distinct = 0;
        String[] symbols = new String[this.symbols.size()];
        for (Integer key : wheels.keySet()) {
            if (!Arrays.asList(symbols).contains(wheels.get(key).getSymbol())) {
                symbols[distinct] = wheels.get(key).getSymbol();
                distinct += 1;
            }
        }
        return distinct;
    }
    
    /**
     * Checks whether the slot machine has achieved the jackpot.
     * Uses the distinctSymbols() method to determine whether there is
     * only 1 distinct symbol among all the wheels (meaning every wheel
     * shows the same symbol).
     *
     * @return true if all wheels share the same symbol, false otherwise
     */
    public boolean isJackpot() {
        boolean jackpot = false;
        ok = true;
        if (distinctSymbols() == 1) {
            jackpot = true;
            JOptionPane.showMessageDialog(null, "You got a jackpot.");
        }
        return jackpot;
    }
    
    /**
     * Returns the current configuration of the slot machine.
     * Goes through each wheel and retrieves the symbol (color) currently
     * assigned to it, ordered from the lowest wheel position to the
     * highest.
     *
     * @return an array with the symbols (colors) of every wheel, ordered
     *         from the lowest to the highest wheel position
     */
    public String[] configuration() {
        String[] conf = new String[wheels.size()];
        int j = 0;
        for (Integer key : wheels.keySet()) {
            conf[j] = wheels.get(key).getSymbol();
            j += 1;
        }    
        ok = true;
        return conf;
    }
    
    /**
     * Makes the slot machine and its components visible.
     */
    public void makeVisible() {
        body[0].makeVisible();
        body[1].makeVisible();
        body[2].makeVisible();
        body[3].makeVisible();
        handle.makeVisible();
        for (Integer key : wheels.keySet()) {
            wheels.get(key).makeVisible();
        }
        ok = true;
    }
    
    /**
     * Makes the slot machine and its components invisible.
     */
    public void makeInvisible() {
        body[0].makeInvisible();
        body[1].makeInvisible();
        body[2].makeInvisible();
        body[3].makeInvisible();
        handle.makeInvisible();
        for (Integer key : wheels.keySet()) {
            wheels.get(key).makeInvisible();
        }
        ok = true;
    }
    
    /**
     * Ends the interaction with the slot machine.
     * Terminates the machine and removes the object.
     */
    public void exit(){
        makeInvisible();
        ok = true;
        if (ok()) {
            JOptionPane.showMessageDialog(null, "You exit from the slot machine.");
        }
        System.exit(0);
    }
    
    /**
     * Indicates whether the last operation performed on the slot machine
     * was executed successfully.
     *
     * @return true if the last method call completed successfully,
     *         false otherwise
     */
    public boolean ok() {
        return ok;
    }
}