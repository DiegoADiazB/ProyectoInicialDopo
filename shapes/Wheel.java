import javax.swing.JOptionPane;
/**
 * Write a description of class Wheel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wheel
{
    private Rectangle[] body = new Rectangle[4];
    private Circle sym = new Circle();
    
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
        body[0].makeVisible();
        body[1].makeVisible();
        body[2].makeVisible();
        body[3].makeVisible();
        sym.makeVisible();
    }
    
    public void moveVertical(int y) {
        for (int i = 0; i < 4; i++) {
            body[i].moveVertical(y*118);
        }
        sym.moveVertical(y*118);
    }
    
    public void moveHorizontal(int x) {
        for (int i = 0; i < 4; i++) {
            body[i].moveHorizontal(x*109);
        }
        sym.moveHorizontal(x*109);
    }
    
    private void makeWheel(int x){
        
    }
    public void makeVisible(){
        
    }
    public void makeInvisible(){
        
    }
    public void spin(){
        
    }
    public void changeSymbol(String color) {
        sym.changeColor(color);
    }
    public String getSymbol() {
        return sym.getColor();
    }
    public boolean isActionOk(boolean ok){
        return true;
    }
}