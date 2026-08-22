import java.util.HashMap;
import javax.swing.JOptionPane;
import java.util.TreeMap;

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
    private HashMap <Integer, Wheel> wheels = new HashMap<>();
    private TreeMap <Integer, Symbol> symbols = new TreeMap<>();
    
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
        body[0].makeVisible();
        body[1].makeVisible();
        body[2].makeVisible();
        body[3].makeVisible();
        handle.makeVisible();
    }
    
    public void addWheel(int pos) {
        if (! wheels.containsKey(pos)) {
            wheels.put(pos, new Wheel());
            wheels.get(pos).moveVertical((int) (pos-1)/10);
            wheels.get(pos).moveHorizontal((pos-1)%10);
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

    }
}