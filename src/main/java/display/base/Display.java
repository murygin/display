package display.base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

/**
 * Klasse Display stellt ein Dispaly dar.
 */
@SuppressWarnings({ "deprecation", "serial" })
public class Display extends Component {

    protected Vector<Text> textList;

    public Display() {
        super();
    }

    public Display(Dimension size, Color backgroundColor) {
        super();
        textList = new Vector<Text>();
        resize(size.width, size.height);
        Pkt.setDisplaySize(size());
        setBackground(backgroundColor);
        setPktSpeed(5);
    }

    public Display(Dimension size, Color backgroundColor, double speed) {
        super();
        textList = new Vector();
        resize(size.width, size.height);
        Pkt.setDisplaySize(size());
        setBackground(backgroundColor);
        setPktSpeed(speed);    
    }

    public void setPktSpeed(double speed) {
        Pkt.setSpeed(speed);
    }

    public void addTextElement(String text, Dimension size, Point position) {
        textList.addElement(new Text(text, size));
        getLastTextElement().setPosition(position);
        getLastTextElement().setPaintStatus(false);
    } // end addText

    public Text getTextElement(int index) {
        return textList.elementAt(index);
    }

    public Text getLastTextElement() {
        return textList.lastElement();
    }

    public boolean nextPosition() {
        boolean bReady = true;
        for (int i = 0; i < textList.size(); i++) {
            if (!getTextElement(i).nextPosition()) {
                bReady = false;
            }
        }
        return bReady;
    }

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < textList.size(); i++) {
            if (getTextElement(i).getPaintStatus()) {
                getTextElement(i).paint(g);
            }
        }
    }
} // end class Display

