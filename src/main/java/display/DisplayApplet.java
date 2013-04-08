package display;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.net.MalformedURLException;
import java.net.URL;

import display.base.Pkt;
import display.base.Text;

public class DisplayApplet extends Applet implements Runnable {

    Dimension offDimension;
    Image offImage;
    Graphics offGraphics;

    Thread animation;
    boolean isRunning;

    private int animationSpeed = 10;

    String url;

    Text text1, text2, text3, text4, displayedText;

    String word1, word2, word3;

    Color colorDisplay;

    private int arc;

    boolean isMouseMode;

    // ---------------------------------------------------------

    @Override
    public void init() {
        setURLString(getParameter("URL"));
        setBackground(Color.black);
        colorDisplay = new Color(new Integer(getParameter("bgR")).intValue(), new Integer(getParameter("bgG")).intValue(), new Integer(getParameter("bgB")).intValue());
        if (getParameter("URL") != null) {
            setURLString(getParameter("URL"));
        }
        if (getParameter("speed") != null) {
            Pkt.setSpeed(new Double(getParameter("speed")).doubleValue());
        }
        if (getParameter("timeout") != null) {
            setAnimationSpeed(new Integer(getParameter("timeout")).intValue());
        }
        if (getParameter("arc") != null) {
            setArc(new Integer(getParameter("arc")).intValue());
        } else {
            setArc(30);
        }
        isMouseMode = true;
        isRunning = true;
        Pkt.setDisplaySize(size());
        dispalyTextInit();
    } // end of void init

    private void dispalyTextInit() {
        text1 = new Text(getParameter("string1"), new Dimension(5, 5));
        text1.setPosition(new Point(Math.round((size().width - text1.getSize().width) / 2), Math.round(size().height / 2) - Math.round(text1.getSize().height / 2)));
        text1.setPaintStatus(false);

        text2 = new Text(getParameter("string2"), new Dimension(5, 5));
        text2.setPosition(new Point(Math.round((size().width - text2.getSize().width) / 2), Math.round(size().height / 2) - Math.round(text2.getSize().height / 2)));
        text2.setPaintStatus(false);

        text3 = new Text(getParameter("string3"), new Dimension(5, 5));
        text3.setPosition(new Point(Math.round((size().width - text3.getSize().width) / 2), Math.round(size().height / 2) - Math.round(text3.getSize().height / 2)));
        text3.setPaintStatus(false);

        text4 = new Text(getParameter("string4"), new Dimension(5, 5));
        text4.setPosition(new Point(Math.round((size().width - text4.getSize().width) / 2), Math.round(size().height / 2) - Math.round(text4.getSize().height / 2) - 10));
        text4.setPaintStatus(false);

        displayedText = new Text();
        displayedText = text1;
    } // end displayTextInit

    // ---------------------------------------------------------

    public String getURLString() {
        return url;
    }

    public void setURLString(String p_sURL) {
        url = p_sURL;
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(int p_nSpeed) {
        animationSpeed = p_nSpeed;
    }

    public int getArc() {
        return arc;
    }

    public void setArc(int p_nArc) {
        arc = p_nArc;
    }

    // ---------------------------------------------------------

    @Override
    public void start() {
    } // end of void start

    @Override
    public void stop() {
        animation = null;
    } // end of void stop

    @Override
    public void run() {
        int nds = 20;
        Text oldText = new Text();
        while (!displayedText.nextPosition() || !displayedText.isComplete()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ie) {
            }
            repaint();
        }
        isMouseMode = false;

        try {
            Thread.sleep(nds * 100);
        } catch (InterruptedException ie) {
        }
        nds = 20;
        while (isRunning) {
            if (displayedText.getText().equals(text1.getText())) {
                oldText = text1;
                switchTo(text2);
            } else {
                if (displayedText.getText().equals(text2.getText())) {
                    oldText = text2;
                    switchTo(text3);
                } else {
                    if (displayedText.getText().equals(text3.getText())) {
                        oldText = text3;
                        switchTo(text4);
                    } else {
                        if (displayedText.getText().equals(text4.getText())) {
                            oldText = text4;
                            dispalyTextInit();
                        }
                    }
                }
            }

            /*
             * for( int i=0; i<oldText.getNumberOfZeichen(); i++ ) { Zeichen
             * oldZeichen = oldText.getZeichen(i); for( int k=0;
             * k<oldZeichen.getNumberOfPkt(); k++ ) m_DisplayedText.addPunkt(
             * new Point( oldZeichen.getPkt(k).getPos().x,
             * oldZeichen.getPkt(k).getPos().y ), Color.black ); }
             * 
             * int mittelindex = Math.round(oldText.getText().length() / 2);
             * while( m_DisplayedText.addPunkt( new Point( Math.round(
             * size().width/2 ), Math.round( size().height/2 ) ), Color.black )
             * ) { //nichts }
             */
            displayedText.switchTo(oldText);

            while (!displayedText.nextPosition()) {
                try {
                    Thread.sleep(getAnimationSpeed());
                } catch (InterruptedException ie) {
                }
                repaint();
            }
            try {
                Thread.sleep(nds * 100);
            } catch (InterruptedException ie) {
            }
        } // ende xmal Schleife
    } // end of void run

    private void switchTo(Text newText) {
        displayedText = newText;
        displayedText.setCurrNumberOfPoints(0);
        displayedText.setPaintStatus(false);
    }

    // ---------------------------------------------------------

    @Override
    public void paint(Graphics g) {
        update(g);
    }

    @Override
    public void update(Graphics g) {
        Dimension d = size();
        if ((offGraphics == null) || (d.width != offDimension.width) || (d.height != offDimension.height)) {
            offDimension = d;
            offImage = createImage(d.width, d.height);
            offGraphics = offImage.getGraphics();
        }
        // Erase the previous image.
        offGraphics.setColor(getBackground());
        offGraphics.fillRect(0, 0, d.width, d.height);

        offGraphics.setColor(colorDisplay);
        offGraphics.fillRoundRect(0, 0, size().width, size().height, getArc(), getArc());
        offGraphics.setColor(Color.black);
        displayedText.paint(offGraphics);

        g.drawImage(offImage, 0, 0, this);

    } // end of void update

    // ---------------------------------------------------------

    @Override
    public boolean mouseMove(Event evt, int x, int y) {
        if (isMouseMode) {
            displayedText.addPunkt(new Point(x, y), Color.black);
        }
        if (animation == null) {
            animation = new Thread(this);
            animation.setPriority(3);
            animation.start();
        }
        return true;
    }

    @Override
    public boolean mouseDrag(Event evt, int x, int y) {
        mouseDown(evt, x, y);
        return true;
    }

    @Override
    public boolean mouseDown(Event evt, int x, int y) {
        if (getURLString() != null) {
            try {
                getAppletContext().showDocument(new URL(getURLString()));
            } catch (MalformedURLException mue) {
                System.out.println("Applet Berlin, MalformedURLException, URL: " + getURLString() + ", message: " + mue.getMessage());
            }
        }
        return true;
    }

} // end of class Berlin
