import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 * This is a modification of the general purpose Canvas, specially made for
 * the BlueJ "shapes" example. 
 * Extended to support CSS color names (case insensitive) and hexadecimal
 * colors.
 *
 * @author: Bruce Quig
 * @author: Michael Kolling (mik)
 *
 * @version: 1.6 (shapes)
 */
public class Canvas{
    // Note: The implementation of this class (specifically the handling of
    // shape identity and colors) is slightly more complex than necessary. This
    // is done on purpose to keep the interface and instance fields of the
    // shape objects in this project clean and simple for educational purposes.

    private static Canvas canvasSingleton;

    /**
     * CSS color names (in lowercase) and their colors.
     */
    private static final HashMap<String, Color> COLORS = new HashMap<String, Color>();

    static {
        String[] names = {
            "red", "blue", "green", "yellow", "orange", "purple", "pink", "brown",
            "black", "cyan", "magenta", "lime", "navy", "teal", "olive", "maroon",
            "slategray", "gold", "coral", "salmon", "orchid", "violet", "indigo",
            "turquoise", "khaki", "crimson", "tomato", "chocolate", "tan", "plum",
            "beige", "lavender", "springgreen", "mediumpurple", "skyblue",
            "seagreen", "slateblue", "steelblue", "darkred", "darkgreen",
            "darkblue", "darkorange", "hotpink", "deeppink", "royalblue",
            "forestgreen", "sienna", "peru", "orangered", "yellowgreen",
            "white", "gray", "lightgray", "darkgray"
        };
        int[] rgb = {
            0xFF0000, 0x0000FF, 0x008000, 0xFFFF00, 0xFFA500, 0x800080, 0xFFC0CB, 0xA52A2A,
            0x000000, 0x00FFFF, 0xFF00FF, 0x00FF00, 0x000080, 0x008080, 0x808000, 0x800000,
            0x708090, 0xFFD700, 0xFF7F50, 0xFA8072, 0xDA70D6, 0xEE82EE, 0x4B0082,
            0x40E0D0, 0xF0E68C, 0xDC143C, 0xFF6347, 0xD2691E, 0xD2B48C, 0xDDA0DD,
            0xF5F5DC, 0xE6E6FA, 0x00FF7F, 0x9370DB, 0x87CEEB,
            0x2E8B57, 0x6A5ACD, 0x4682B4, 0x8B0000, 0x006400,
            0x00008B, 0xFF8C00, 0xFF69B4, 0xFF1493, 0x4169E1,
            0x228B22, 0xA0522D, 0xCD853F, 0xFF4500, 0x9ACD32,
            0xFFFFFF, 0x808080, 0xC0C0C0, 0x404040
        };
        for (int i = 0; i < names.length; i++) {
            COLORS.put(names[i], new Color(rgb[i]));
        }
    }

    /**
     * Factory method to get the canvas singleton object.
     */
    public static Canvas getCanvas(){
        if(canvasSingleton == null) {
            canvasSingleton = new Canvas("BlueJ Shapes Demo", 9999, 9999, 
                                         Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    //  ----- instance part -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List <Object> objects;
    private HashMap <Object,ShapeDescription> shapes;
    
    /**
     * Create a Canvas.
     * @param title  title to appear in Canvas Frame
     * @param width  the desired width for the canvas
     * @param height  the desired height for the canvas
     * @param bgClour  the desired background colour of the canvas
     */
    private Canvas(String title, int width, int height, Color bgColour){
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList <Object>();
        shapes = new HashMap <Object,ShapeDescription>();
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     * @param visible  boolean value representing the desired visibility of
     * the canvas (true or false) 
     */
    public void setVisible(boolean visible){
        if(graphic == null) {
            // first time: instantiate the offscreen image and fill it with
            // the background colour
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Draw a given shape onto the canvas.
     * @param  referenceObject  an object to define identity for this shape
     * @param  color            the color of the shape
     * @param  shape            the shape object to be drawn on the canvas
     */
     // Note: this is a slightly backwards way of maintaining the shape
     // objects. It is carefully designed to keep the visible shape interfaces
     // in this project clean and simple for educational purposes.
    public void draw(Object referenceObject, String color, Shape shape){
        objects.remove(referenceObject);   // just in case it was already there
        objects.add(referenceObject);      // add at the end
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }
 
    /**
     * Erase a given shape's from the screen.
     * @param  referenceObject  the shape object to be erased 
     */
    public void erase(Object referenceObject){
        objects.remove(referenceObject);   // just in case it was already there
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Set the foreground colour of the Canvas.
     * Accepts CSS color names (case insensitive, e.g. "skyBlue") or
     * hexadecimal colors (e.g. "#FF7F50"). Unknown names are drawn in
     * black.
     * @param  colorString   the new colour for the foreground of the Canvas 
     */
    public void setForegroundColor(String colorString){
        if (colorString.startsWith("#")) {
            graphic.setColor(Color.decode(colorString));
        } else {
            Color color = COLORS.get(colorString.toLowerCase());
            graphic.setColor(color != null ? color : Color.black);
        }
    }

    /**
     * Wait for a specified number of milliseconds before finishing.
     * This provides an easy way to specify a small delay which can be
     * used when producing animations.
     * @param  milliseconds  the number 
     */
    public void wait(int milliseconds){
        try{
            Thread.sleep(milliseconds);
        } catch (Exception e){
            // ignoring exception at the moment
        }
    }

    /**
     * Redraw ell shapes currently on the Canvas.
     */
    private void redraw(){
        erase();
        for(Iterator i=objects.iterator(); i.hasNext(); ) {
                       shapes.get(i.next()).draw(graphic);
        }
        canvas.repaint();
    }
       
    /**
     * Erase the whole canvas. (Does not repaint.)
     */
    private void erase(){
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }


    /************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel{
        public void paint(Graphics g){
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
    
    /************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class ShapeDescription{
        private Shape shape;
        private String colorString;

        public ShapeDescription(Shape shape, String color){
            this.shape = shape;
            colorString = color;
        }

        public void draw(Graphics2D graphic){
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }

}