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

    private static String STRING_1_DEFAULT = "murygin.wordpress.com";
    private static String STRING_2_DEFAULT = "applet art";
    private static String STRING_3_DEFAULT = "made in berlin";
    private static String STRING_4_DEFAULT = "#";
    
    private static final int RED_DEFAULT = 255;
    private static final int GEEN_DEFAULT = 255;
    private static final int BLUE_DEFAULT = 255;
    
    private static final int ANIMATION_SPEED_DEFAULT = 10;
    private static final int PIXEL_SPEED_DEFAULT = 5;
    
    private static final int ARC_DEFAULT = 30;
    
    private static String URL_DEFAULT = "http://murygin.wordpress.com";
    
    private Dimension offDimension;
    private Image offImage;
    private Graphics offGraphics;

    private Thread animation;
    private boolean isRunning;

    private int animationSpeed = ANIMATION_SPEED_DEFAULT;

    private String url;

    private Text text1, text2, text3, text4, displayedText;

    private Color colorDisplay;

    private int arc;

    private boolean isMouseMode;

    // ---------------------------------------------------------

    @Override
    public void init() {
        setBackground(Color.black);
        isMouseMode = true;
        isRunning = true;
        Pkt.setDisplaySize(size());
        loadParameter();
    } // end of void init

    private void loadParameter() {
        loadColorParameter();     
        loadSpeedParameter();
        loadTextParameter();
        setURLString(URL_DEFAULT);
        if (getParameter("URL") != null) {
            setURLString(getParameter("URL"));
        }
        setArc(ARC_DEFAULT);
        if (getParameter("arc") != null) {
            setArc(Integer.valueOf((getParameter("arc"))));
        } 
    }

    private void loadColorParameter() {
        colorDisplay = new Color(
                getIntParameter("bgR", RED_DEFAULT), 
                getIntParameter("bgG", GEEN_DEFAULT),
                getIntParameter("bgB", BLUE_DEFAULT)
        );       
    }
    
    private void loadSpeedParameter() {
        Pkt.setSpeed(PIXEL_SPEED_DEFAULT);
        if (getParameter("speed") != null) {
            Pkt.setSpeed(Double.valueOf(getParameter("speed")));
        }
        setAnimationSpeed(ANIMATION_SPEED_DEFAULT);
        if (getParameter("timeout") != null) {
            setAnimationSpeed(Integer.valueOf(getParameter("timeout")));
        }
    }
    
    private void loadTextParameter() {
        text1 = new Text(getStringParameter("string1",STRING_1_DEFAULT), new Dimension(5, 5));
        text1.setPosition(new Point(Math.round((size().width - text1.getSize().width) / 2), Math.round(size().height / 2) - Math.round(text1.getSize().height / 2)));
        text1.setPaintStatus(false);

        text2 = new Text(getStringParameter("string2",STRING_2_DEFAULT), new Dimension(5, 5));
        text2.setPosition(new Point(Math.round((size().width - text2.getSize().width) / 2), Math.round(size().height / 2) - Math.round(text2.getSize().height / 2)));
        text2.setPaintStatus(false);

        text3 = new Text(getStringParameter("string3",STRING_3_DEFAULT), new Dimension(5, 5));
        text3.setPosition(new Point(Math.round((size().width - text3.getSize().width) / 2), Math.round(size().height / 2) - Math.round(text3.getSize().height / 2)));
        text3.setPaintStatus(false);

        text4 = new Text(getStringParameter("string4",STRING_4_DEFAULT), new Dimension(5, 5));
        text4.setPosition(new Point(Math.round((size().width - text4.getSize().width) / 2), Math.round(size().height / 2) - Math.round(text4.getSize().height / 2) - 10));
        text4.setPaintStatus(false);

        displayedText = new Text();
        displayedText = text1;
    } // end displayTextInit
    
    private int getIntParameter(String name, int defaultValue) {
        int value = defaultValue;
        if(getParameter(name)!=null) {
            value = Integer.valueOf(getParameter(name));
        }
        return value;
    }
    
    private String getStringParameter(String name, String defaultValue) {
        String value = defaultValue;
        if(getParameter(name)!=null) {
            value = getParameter(name);
        }
        return value.toLowerCase();
    }

    // ---------------------------------------------------------

    public String getURLString() {
        return url;
    }

    public void setURLString(String url) {
        this.url = url;
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(int speed) {
        animationSpeed = speed;
    }

    public int getArc() {
        return arc;
    }

    public void setArc(int arc) {
        this.arc = arc;
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
                            loadTextParameter();
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

} // end of class
