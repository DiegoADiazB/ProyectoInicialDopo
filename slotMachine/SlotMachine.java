import javax.swing.JOptionPane;
import java.util.TreeMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Set;

/**
 * The SlotMachine class represents a slot machine composed of a body,
 * a lever handle, a set of wheels, and a set of symbols.
 * 
 * The machine's shape is built from four rectangles and a circle
 * representing the lever handle. Wheels and symbols are stored in
 * separate TreeMaps, indexed by their position, which allows the
 * machine to manage, add, remove, and update them individually or
 * as a whole.
 * 
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
    public TreeMap <Integer, Symbol> symbols;
    
    /**
     * Indicate if the app can do the last action.
     */
    private boolean isOk = true;
    
    private boolean isVisible = false;
    
    public static final int WHEELS_NUMBER = 50;
    
    public static final int LINES_OF_WHEELS = 5;
    
    public static final int INTERVAL = 20;
    
    private static final int HEIGHT = Wheel.height*LINES_OF_WHEELS + INTERVAL*(LINES_OF_WHEELS+1);
    
    private static final int WIDTH =  Wheel.width*(WHEELS_NUMBER/LINES_OF_WHEELS) + INTERVAL*((WHEELS_NUMBER/LINES_OF_WHEELS)+1);
    
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
        body[0].changeSize(HEIGHT,WIDTH);
        body[0].changeColor("lightGray");
        body[1].changeSize(HEIGHT/20,WIDTH-2*INTERVAL);
        body[1].moveVertical(HEIGHT);
        body[1].moveHorizontal(INTERVAL);
        body[1].changeColor("darkGray");
        body[2].changeSize(INTERVAL*2,WIDTH/10);
        body[2].moveVertical(HEIGHT/2);
        body[2].moveHorizontal(WIDTH);
        body[2].changeColor("darkGray");
        body[3].changeSize(WIDTH/10,INTERVAL*2);
        body[3].moveVertical(HEIGHT/2 - WIDTH/10);
        body[3].moveHorizontal(WIDTH - (2*INTERVAL) + WIDTH/10);
        body[3].changeColor("darkGray");
        handle.changeSize(WIDTH/10);
        handle.moveVertical(HEIGHT/2 - WIDTH/5 + 2*INTERVAL);
        handle.moveHorizontal(WIDTH + (WIDTH/10-INTERVAL*3));
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
        isOk = false;
        if (! wheels.containsKey(pos) && pos <= WHEELS_NUMBER && pos >= 1) {
            wheels.put(pos, new Wheel(pos, symbols));
            isOk = true;
            if (ok() && isVisible) {
                makeVisible();
            }
        }
        else if (isVisible) {
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
        isOk = false;
        if (wheels.containsKey(pos) && !wheels.get(pos).isLocked()) {
            wheels.get(pos).makeInvisible();
            wheels.remove(pos);
            isOk = true;
        }
        else if (isVisible && !wheels.containsKey(pos)){
            JOptionPane.showMessageDialog(null, "This wheel don't exist.");
        }else {
            JOptionPane.showMessageDialog(null, "This wheel is locked.");
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
        boolean existSymbol = false;
        for (Map.Entry<Integer, Symbol> i : symbols.entrySet()) {
            if (color == i.getValue().getSymbol()) {
                existSymbol = true;
                break;
            }
        }
        isOk = false;
        if (!symbols.containsKey(pos) && !existSymbol && pos > 0) {
            symbols.put(pos, new Symbol(color));
            if (symbols.size() == 1) {
                for (Wheel wheel : wheels.values()) {
                    wheel.placeSymbol(color);
                }
            }
            isOk = true;
        }
        else if (symbols.containsKey(pos) && isVisible) {
            JOptionPane.showMessageDialog(null, "A symbol already exists in this position.");
        }
        else if (pos < 1 && isVisible) {
            JOptionPane.showMessageDialog(null, "You only can add symbols in positives positions.");
        }
        else if (isVisible) {
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
        boolean existSymbol = false;
        int pos = -1;
        isOk = false;
        for (Map.Entry<Integer, Symbol> i : symbols.entrySet()) {
            if (symbol == i.getValue().getSymbol()) {
                existSymbol = true;
                pos = i.getKey();
                break;
            }
        }
        if (existSymbol) {
            if (symbols.size() == 1) {
                for (Wheel wheel : wheels.values()) {
                    wheel.placeSymbol("white");
                }
            }
            else {
                for (Integer key : wheels.keySet()) {
                    if (symbol == wheels.get(key).getSymbol()) {
                        spin(key);
                    }
                }
            }
            symbols.remove(pos);
            isOk = true;
        }
        else if (isVisible) {
            JOptionPane.showMessageDialog(null, "This symbol doesn't exists in any position.");
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
        boolean existSymbol = false;
        isOk = false;
        for (Map.Entry<Integer, Symbol> i : symbols.entrySet()) {
            if (symbol == i.getValue().getSymbol()) {
                existSymbol = true;
                break;
            }
        }
        if (existSymbol && wheels.containsKey(wheel) && !wheels.get(wheel).isLocked()) {
            wheels.get(wheel).placeSymbol(symbol);
            if (isJackpot() && isVisible) {
                body[0].changeColor("yellow");
                makeVisible();
            }
            else if (isVisible) {
                body[0].changeColor("lightGray");
                makeVisible();
            }
            isOk = true;
        }
        else if (!wheels.containsKey(wheel) && isVisible) {
            JOptionPane.showMessageDialog(null, "This wheel doesn't exist.");
        }
        else if (wheels.get(wheel).isLocked() && isVisible) {
            JOptionPane.showMessageDialog(null, "This wheel is locked.");
        }
        else if (isVisible) {
            JOptionPane.showMessageDialog(null, "This symbol doesn't exist.");
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
        isOk = false;
        if (wheels.containsKey(wheel) && symbols.size() != 0 && !wheels.get(wheel).isLocked()) {            
            wheels.get(wheel).spin();
            isOk = true;
            if (isJackpot() && isVisible) {
                body[0].changeColor("yellow");
                makeVisible();
            }
            else if (isVisible) {
                body[0].changeColor("lightGray");
                makeVisible();
            }
        }
        else if (wheels.get(wheel).isLocked() && isVisible){
            JOptionPane.showMessageDialog(null, "This wheel is locked.");
        }
        else if (isVisible) {
            JOptionPane.showMessageDialog(null, "This wheel doesn't exist or don't exists symbols.");
        }
    }
    
    /**
     * Spins all the wheels in the slot machine.
     * Iterates through every wheel and spins it. Once all wheels have
     * finished spinning, checks whether the jackpot has been achieved in
     * order to update the machine's state.
     */

    public void spin() {
        isOk = false;
        if (wheels.size() != 0) {
            for (Integer key : wheels.keySet()) {
                spin(key);
                isOk = true;
            }
        }
        else if (isVisible) {
            JOptionPane.showMessageDialog(null, "In this moment doesn't exist any wheel to spin.");
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
        isOk = true;
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
        isOk = true;
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
        isOk = true;
        if (distinctSymbols() == 1 && isVisible) {
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
        isOk = true;
        return conf;
    }
    
    /**
     * Makes the slot machine and its components visible.
     */
    public void makeVisible() {
        isVisible = true;
        body[0].makeVisible();
        body[1].makeVisible();
        body[2].makeVisible();
        body[3].makeVisible();
        handle.makeVisible();
        for (Integer key : wheels.keySet()) {
            wheels.get(key).makeVisible();
        }
        isOk = true;
    }
    
    /**
     * Makes the slot machine and its components invisible.
     */
    public void makeInvisible() {
        isVisible = false;
        body[0].makeInvisible();
        body[1].makeInvisible();
        body[2].makeInvisible();
        body[3].makeInvisible();
        handle.makeInvisible();
        for (Integer key : wheels.keySet()) {
            wheels.get(key).makeInvisible();
        }
        isOk = true;
    }
    
    /**
     * Ends the interaction with the slot machine.
     * Terminates the machine and removes the object.
     */
    public void exit(){
        makeInvisible();
        isOk = true;
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
        return isOk;
    }
    
    //Nuevos metodos del ciclo 2
    /**
     * Locks a wheel so that it cannot be interacted with until it is unlocked.
     * 
     * @param the wheel to be locked.
     */
    public void lock(int wheel) {
        isOk = false;
        if (!wheels.containsKey(wheel)) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "This wheel does not exist.");
            }
        } else if (wheels.get(wheel).isLocked()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "This wheel is already locked.");
            }
        } else {
            wheels.get(wheel).setLock();
            isOk = true;
        }
    }
    
    /**
     * Unlocks an already locked wheel.
     * 
     * @param the wheel to be unlocked.
     */
    public void unlock(int wheel){
        isOk= false;
        if (!wheels.containsKey(wheel)){
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "This wheel does not exist.");
            }
        } else if (!wheels.get(wheel).isLocked()){
            if (isVisible){
                JOptionPane.showMessageDialog(null, "This wheel is already unlocked.");
            }
        } else {    
            wheels.get(wheel).setUnlock();
            isOk = true;
        }
    }
    
    /**
     * Change the symbols on 2 wheels.
     * 
     * @param the wheels to be swapped.
     */
    public void swap(int wheel1, int wheel2) {
        isOk = false;
        Wheel newWheel1 = wheels.get(wheel1);
        Wheel newWheel2 = wheels.get(wheel2);
        if (newWheel1 == null) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "The first wheel sent does not exist.");
            }
        } else if (newWheel2 == null) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "The second wheel sent does not exist.");
            }
        } else if (newWheel1.isLocked()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "The first wheel is locked.");
            }
        } else if (newWheel2.isLocked()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "The second wheel is locked.");
            }
        } else {
            newWheel1.swapWheel(newWheel2);
            isOk = true;
       }
    }
    
    /**
     * Allows the wheel to spin a determined number of times.
     * @param the wheel that is going to spin and the number of spins.
     */
    public void spin(int wheel, int steps) {
        isOk = false;
        if (!wheels.containsKey(wheel)) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "This wheel does not exist.");
            }
        } else if (wheels.get(wheel).isLocked()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "This wheel is locked.");
            }
        } else {        
            for (int i = 0; i < steps; i++) {
                spin(wheel);
                if (isVisible) {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            isOk = true;
        }
    }
    
    /**
     * Leave the slotMachine in a given configuration.
     * 
     * @param the set that contains the symbols to each wheel.
     */
    public void spin(String[] setSymbols) {
        isOk = false;
        boolean theresAWheelLocked = false;
        for (Wheel wheel : wheels.values()) {
            theresAWheelLocked = wheel.isLocked();
            if (theresAWheelLocked) {
                break;
            }
        }
        if (wheels.size() != setSymbols.length) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "The number of wheels differs from the number of symbols sent.");
            }
        } else if (theresAWheelLocked) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "There is at least one wheel locked.");
            }
        } else {
            Integer[] llaves = wheels.keySet().toArray(new Integer[0]);
            for (int i = 0; i < llaves.length; i++) {
                placeSymbol(llaves[i], setSymbols[i]);
            }
            isOk = true;
        }
    }
}