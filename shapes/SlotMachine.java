import java.util.HashMap;
import javax.swing.JOptionPane;
import java.util.TreeMap;
import java.util.Map;
import java.util.Arrays;

/**
 * This class have the propour to make like a simulator from the proble slotMachine from 
 *
 * @author Juan Diego Cardozo Beltrán - Diego Alejandro Díaz Boada
 * @version 21/08/26 
 */
public class SlotMachine
{
    
    private Rectangle[] body = new Rectangle[4];
    private Circle handle = new Circle();
    private HashMap <Integer, Wheel> wheels;
    private TreeMap <Integer, Symbol> symbols;
    
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
        wheels = new HashMap<>();
        symbols = new TreeMap<>();
    }
    
    public void addWheel(int pos) {
        if (! wheels.containsKey(pos)) {
            wheels.put(pos, new Wheel());
            wheels.get(pos).moveVertical((int) (pos-1)/10);
            wheels.get(pos).moveHorizontal((pos-1)%10);
            if (symbols.size() != 0) {
                wheels.get(pos).changeSymbol(symbols.get(symbols.firstKey()).getSymbol());
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "This wheel has already been created.");
        }
    }
    
    public void delWheel(int pos) {
        if (wheels.containsKey(pos)) {
            wheels.get(pos).makeInvisible();
            wheels.remove(pos);
        }
        else {
            JOptionPane.showMessageDialog(null, "This wheel has not been created.");
        }
    }
    
    public void addSymbol(int pos, String color) {
        boolean band = false;
        for (Map.Entry<Integer, Symbol> i : symbols.entrySet()) {
            if (color == i.getValue().getSymbol()) {
                band = true;
                break;
            }
        }
        if (!symbols.containsKey(pos) && !band) {
            symbols.put(pos, new Symbol(color));
            if (symbols.size() == 1) {
                for (Integer key : wheels.keySet()) {
                    wheels.get(key).changeSymbol(color);
                }
            }
        }
        else if (symbols.containsKey(pos)) {
            JOptionPane.showMessageDialog(null, "A symbol already exists in this position.");
        }
        else {
            JOptionPane.showMessageDialog(null, "This symbol already exists in any position.");
        }
    }

    public void delSymbol(String symbol) {
        boolean band = false;
        int pos = -1;
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
        }
        else {
            JOptionPane.showMessageDialog(null, "This symbol don't exists in any position.");
        }
    }
    
    public void placeSymbol(int wheel, String symbol) {
        boolean band = false;
        for (Map.Entry<Integer, Symbol> i : symbols.entrySet()) {
            if (symbol == i.getValue().getSymbol()) {
                band = true;
                break;
            }
        }
        if (band && wheels.containsKey(wheel)) {
            wheels.get(wheel).changeSymbol(symbol);
        }
    }
    
    public void spin(int wheel) {
        String color;
        String newColor;
        int pos = -1;
        if (wheels.containsKey(wheel) && symbols.size() != 0) {
            color = wheels.get(wheel).getSymbol();
            for (Map.Entry<Integer, Symbol> i : symbols.entrySet()) {
                if (color == i.getValue().getSymbol()) {
                    pos = i.getKey();
                    break;
                }
            }
            if (symbols.higherKey(pos) != null) {
                newColor = symbols.get(symbols.higherKey(pos)).getSymbol();
            }
            else {
                newColor = symbols.get(symbols.firstKey()).getSymbol();
            }
            wheels.get(wheel).changeSymbol(newColor);
        }
        if (isJackpot()) {
            body[0].changeColor("yellow");
        }
        else {
            body[0].changeColor("lightGray");
        }
    }
    
    public void spin() {
        for (Integer key : wheels.keySet()) {
            spin(key);
        }
        if (isJackpot()) {
            body[0].changeColor("yellow");
        }
        else {
            body[0].changeColor("lightGray");
        }
    }
    
    public String[] symbols() {
        String[] symbols = new String[this.symbols.size()];
        int j = 0;
        for (Map.Entry<Integer, Symbol> i : this.symbols.entrySet()) {
            symbols[j] = i.getValue().getSymbol();
            j += 1;
        }
        return symbols;
    }
    
    public int distinctSymbols() {
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
    
    public boolean isJackpot() {
        boolean jackpot = false;
        if (distinctSymbols() == 1) {
            jackpot = true;
        }
        return jackpot;
    }
    
    public String[] configuration() {
        String[] conf = new String[wheels.size()];
        int j = 0;
        for (Integer key : wheels.keySet()) {
            conf[j] = wheels.get(key).getSymbol();
            j += 1;
        }        
        return conf;
    }
    
    public void makeVisible() {
        body[0].makeVisible();
        body[1].makeVisible();
        body[2].makeVisible();
        body[3].makeVisible();
        handle.makeVisible();
        for (Integer key : wheels.keySet()) {
            wheels.get(key).makeVisible();
        } 
    }
    
    public void makeInvisible() {
        body[0].makeInvisible();
        body[1].makeInvisible();
        body[2].makeInvisible();
        body[3].makeInvisible();
        handle.makeInvisible();
        for (Integer key : wheels.keySet()) {
            wheels.get(key).makeInvisible();
        } 
    }
    
    public void exit(){
        makeInvisible();
        System.exit(0);
    }
    
    public boolean ok() {
        return true;
    }
}