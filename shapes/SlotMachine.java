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
        body[0].changeSize(150,220);
        body[0].changeColor("blue");
        body[1] = new Rectangle();
        body[1].changeSize(20,200);
        body[1].moveVertical(150);
        body[1].moveHorizontal(10);
        body[1].changeColor("black");
        body[2] = new Rectangle();
        body[2].changeSize(10,30);
        body[2].moveVertical(70);
        body[2].moveHorizontal(220);
        body[2].changeColor("black");
        body[3] = new Rectangle();
        body[3].changeSize(30,10);
        body[3].moveVertical(45);
        body[3].moveHorizontal(240);
        body[3].changeColor("black");
        handle.changeSize(30);
        handle.moveVertical(20);
        handle.moveHorizontal(230);
        handle.changeColor("red");
    }
    
}