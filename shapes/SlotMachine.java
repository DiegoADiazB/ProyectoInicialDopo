import java.util.HashMap;
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
    private HashMap <Integer, Wheel> wheels =new HashMap<>();
    
    /**
     * DIEGO, EL CONSTRUCTOR DE SLOTMACHINE ES UN PUTO INUTIL
     * SlotMachine se va creando poquito a poquito, por ende, el constructor no sirve pa un culo
     */
    public SlotMachine(){
        body[0] = new Rectangle();
        body[0].changeSize(600,1100);
        body[0].changeColor("lightGray");
        body[1] = new Rectangle();
        body[1].changeSize(80,1000);
        body[1].moveVertical(600);
        body[1].moveHorizontal(50);
        body[1].changeColor("darkGray");
        body[2] = new Rectangle();
        body[2].changeSize(40,140);
        body[2].moveVertical(300);
        body[2].moveHorizontal(1100);
        body[2].changeColor("darkGray");
        body[3] = new Rectangle();
        body[3].changeSize(120,40);
        body[3].moveVertical(180);
        body[3].moveHorizontal(1200);
        body[3].changeColor("darkGray");
        handle.changeSize(120);
        handle.moveVertical(80);
        handle.moveHorizontal(1160);
        handle.changeColor("red");
    }
    
}