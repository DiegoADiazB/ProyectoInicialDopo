import javax.swing.JOptionPane;
import java.util.TreeMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


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
    
    /**
     * Number of wheels per line (rounded up if the division is not exact).
     */
    public static final int COLUMNS = (WHEELS_NUMBER + LINES_OF_WHEELS - 1) / LINES_OF_WHEELS;
    
    private static final int HEIGHT = Wheel.height * LINES_OF_WHEELS + INTERVAL * (LINES_OF_WHEELS + 1);
    
    private static final int WIDTH = Wheel.width * COLUMNS + INTERVAL * (COLUMNS + 1);
    
    private static final String[] COLORS = {
    "red", "blue", "green", "yellow", "orange", "purple", "pink", "brown",
    "black", "cyan", "magenta", "lime", "navy", "teal", "olive", "maroon",
    "silver", "gold", "coral", "salmon", "orchid", "violet", "indigo",
    "turquoise", "khaki", "crimson", "tomato", "chocolate", "tan", "plum",
    "beige", "lavender", "aqua", "fuchsia", "skyBlue", "seaGreen",
    "slateBlue", "steelBlue", "darkRed", "darkGreen", "darkBlue",
    "darkOrange", "hotPink", "deepPink", "royalBlue", "forestGreen",
    "sienna", "peru", "orangeRed", "yellowGreen"
    };
    /**
     * Constructs a new SlotMachine.
     * Every size and position is computed in pixels from WHEELS_NUMBER,
     * LINES_OF_WHEELS, INTERVAL and the size of a Wheel, so changing any of
     * those constants resizes the whole machine consistently. Also
     * initializes the TreeMaps used to store the symbols and the wheels.
     */
    public SlotMachine(){
        for (int i = 0; i < 4; i++) {
            body[i] = new Rectangle();
        }
        int armLength = 2 * INTERVAL;
        int stickHeight = HEIGHT / 3;
        int stickX = WIDTH + armLength - INTERVAL;
        int stickTop = HEIGHT / 2 - stickHeight;
        buildPart(body[0], HEIGHT, WIDTH, 0, 0, "lightGray");
        buildPart(body[1], INTERVAL, WIDTH - 2 * INTERVAL, INTERVAL, HEIGHT, "darkGray");
        buildPart(body[2], INTERVAL, armLength, WIDTH, HEIGHT / 2, "darkGray");
        buildPart(body[3], stickHeight + INTERVAL, INTERVAL, stickX, stickTop, "darkGray");
        int knob = 2 * INTERVAL;
        handle.changeSize(knob);
        handle.moveHorizontal(stickX + INTERVAL / 2 - knob / 2);
        handle.moveVertical(stickTop - knob);
        handle.changeColor("red");
        wheels = new TreeMap<>();
        symbols = new TreeMap<>();
    }
    
    /**
     * Constructs a slot machine with n wheels and n symbols.
     * The symbols are n different colors chosen at random, stored in a
     * random order, and every wheel starts on a random symbol.
     * If n is lower than 1, 1 is used; if it is greater than the maximum
     * number of wheels (or of available colors), the maximum is used.
     *
     * @param n the number of wheels and symbols of the machine
     */
    public SlotMachine(int n) {
        this();
        int max = WHEELS_NUMBER;
        if (COLORS.length < max) {
            max = COLORS.length;
        }
        if (n < 1) {
            n = 1;  
        }
        if (n > max) {
            n = max;
        }
        List<String> colors = new ArrayList<>(Arrays.asList(COLORS));
        Collections.shuffle(colors);
        for (int i = 1; i <= n; i++) {
            addSymbol(i, colors.get(i - 1));
        }
        Random random = new Random();
        for (int i = 1; i <= n; i++) {
            addWheel(i);
            int steps = random.nextInt(n);
            for (int j = 0; j < steps; j++) {
                wheels.get(i).spin();
            }
        }
         isOk = true;
    }   
    
    /**
     * Sets the size, position and color of one rectangle of the body.
     *
     * @param part   the rectangle to configure
     * @param height the height in pixels
     * @param width  the width in pixels
     * @param x      the horizontal offset in pixels
     * @param y      the vertical offset in pixels
     * @param color  the color of the rectangle
     */
    private void buildPart(Rectangle part, int height, int width, int x, int y, String color) {
        part.changeSize(height, width);
        part.moveHorizontal(x);
        part.moveVertical(y);
        part.changeColor(color);
    }
    
    /**
     * Adds a new wheel to the slot machine at the given position.
     * The wheel is only created if the position is between 1 and WHEELS_NUMBER
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
     * The wheel itself decides whether it can be removed (it cannot if it
     * is locked). If it can, it is removed from the TreeMap that stores
     * the wheels.
     *
     * @param pos the position of the wheel to be removed
     */
    public void delWheel(int pos) {
        isOk = false;
        if (!wheels.containsKey(pos)) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "This wheel don't exist.");
            }
        }
        else if (wheels.get(pos).remove()) {
            wheels.remove(pos);
            isOk = true;
        }
        else if (isVisible) {
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
                    wheel.setSymbol(color);
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
                    wheel.setSymbol("white");
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
     * corresponding data structures, and then asks the wheel to place
     * the symbol (the wheel refuses if it is locked). Afterwards, checks
     * whether the jackpot conditions have been met in order to activate
     * it (changing the machine's color to indicate the new state).
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
        if (!wheels.containsKey(wheel) && isVisible) {
            JOptionPane.showMessageDialog(null, "This wheel doesn't exist.");
        }
        else if (!existSymbol) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "This symbol doesn't exist.");
            }
        }
        else if (wheels.get(wheel).placeSymbol(symbol)) {
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
        else if (isVisible) {
            JOptionPane.showMessageDialog(null, "This wheel is locked.");
        }
    }
    
    /**
     * Spins a single wheel.
     * If the wheel exists, it is asked to move to the next symbol in the
     * symbol sequence (the wheel refuses if it is locked). Afterwards,
     * checks whether the jackpot has been achieved in order to update the
     * machine's state.
     *
     * @param wheel the position of the wheel to spin
     */
    public void spin(int wheel) {
        isOk = false;
        if (symbols.size() == 0) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "There are no symbols.");
            }
        }
        else if (!wheels.containsKey(wheel) && isVisible) {
            JOptionPane.showMessageDialog(null, "This wheel doesn't exist.");
        }
        else if (wheels.get(wheel).spin()) {
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
        else if (isVisible) {
            JOptionPane.showMessageDialog(null, "This wheel is locked.");
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
        if (distinctSymbols() == 1) {
            jackpot = true;
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "You got a jackpot.");
            }
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
     * Locks a wheel so that it cannot be interacted with until it is
     * unlocked. The wheel itself refuses if it is already locked.
     * 
     * @param wheel the position of the wheel to be locked
     */
    public void lock(int wheel) {
        isOk = false;
        if (!wheels.containsKey(wheel)) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "This wheel does not exist.");
            }
        } else if (wheels.get(wheel).lock()) {
            isOk = true;
        } else if (isVisible) {
            JOptionPane.showMessageDialog(null, "This wheel is already locked.");
        }
    }
    
    /**
     * Unlocks an already locked wheel. The wheel itself refuses if it is
     * not locked.
     * 
     * @param wheel the position of the wheel to be unlocked
     */
    public void unlock(int wheel){
        isOk = false;
        if (!wheels.containsKey(wheel)) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "This wheel does not exist.");
            }
        } else if (wheels.get(wheel).unlock()) {
            isOk = true;
        } else if (isVisible) {
            JOptionPane.showMessageDialog(null, "This wheel is already unlocked.");
        }
    }
    
    /**
     * Exchanges the symbols of 2 wheels. The wheels themselves refuse the
     * exchange if any of them is locked.
     * 
     * @param wheel1 the position of the first wheel
     * @param wheel2 the position of the second wheel
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
        } else if (newWheel1.swapWheel(newWheel2)) {
            isOk = true;
        } else if (isVisible) {
            JOptionPane.showMessageDialog(null, "At least one of the wheels is locked.");
        }
    }
    
    /**
     * Allows the wheel to spin a determined number of times.
<<<<<<< HEAD
     * The wheel is guaranteed not to end on the symbol it had before
     * spinning: if after the requested steps it landed back on its
     * initial symbol, it keeps spinning until it shows a different one.
=======
     * Stops as soon as the wheel refuses to spin (because it is locked).
>>>>>>> 4f76094920d50006c6be111a20e7f33bb3fbbfce
     *
     * @param wheel the position of the wheel that is going to spin
     * @param steps the number of spins to perform
     */
    public void spin(int wheel, int steps) {
        isOk = false;
        if (!wheels.containsKey(wheel)) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "This wheel does not exist.");
            }
        } else if (steps < 1) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "The number of spins must be at least 1.");
            }
<<<<<<< HEAD
        } else if (steps < 1) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "The number of spins must be at least 1.");
            }
        } else if (symbols.size() < 2) {
            if (isVisible) {
            JOptionPane.showMessageDialog(null, "There are not enough symbols to change the wheel.");
            }
        } else {
            String initialSymbol = wheels.get(wheel).getSymbol();
            int i = 0;
            while (i < steps || wheels.get(wheel).getSymbol().equals(initialSymbol)) {
=======
        } else if (symbols.size() < 2) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "There are not enough symbols to change the wheel.");
            }
        } else {
            boolean spun = true;
            int i = 0;
            while (i < steps && spun) {
>>>>>>> 4f76094920d50006c6be111a20e7f33bb3fbbfce
                spin(wheel);
                spun = isOk;
                if (isVisible) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                i++;
            }
            isOk = spun;
        }
    }
    
    /**
     * Leaves the slot machine in a given configuration.
     * Each wheel decides whether it accepts its new symbol. If any wheel
     * refuses (because it is locked), every wheel goes back to the symbol
     * it had before, so the configuration is applied completely or not
     * at all. Symbols that do not exist are ignored.
     * 
     * @param setSymbols the symbols to place on each wheel, from left to
     *                   right
     */
    public void spin(String[] setSymbols) {
        isOk = false;
        if (wheels.size() != setSymbols.length) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "The number of wheels differs from the number of symbols sent.");
            }
        } else {
            String[] before = configuration();
            List<String> validSymbols = Arrays.asList(symbols());
            Wheel[] list = wheels.values().toArray(new Wheel[0]);
            boolean done = true;
            for (int i = 0; i < list.length && done; i++) {
                String next = validSymbols.contains(setSymbols[i]) ? setSymbols[i] : before[i];
                done = list[i].placeSymbol(next);
            }
            if (done) {
                if (isJackpot() && isVisible) {
                    body[0].changeColor("yellow");
                    makeVisible();
                }
                else if (isVisible) {
                    body[0].changeColor("lightGray");
                    makeVisible();
                }
                isOk = true;
            } else {
                for (int i = 0; i < list.length; i++) {
                    list[i].setSymbol(before[i]);
                }
                isOk = false;
                if (isVisible) {
                    JOptionPane.showMessageDialog(null, "There is at least one wheel locked.");
                }
            }
        }
    }
}