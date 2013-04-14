package display.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Class Text ist eine Display-Text Objekte des Displays
 */
public class Text {

    /**
     * Der statische Abstand zwischen allen Zeichen dieses Display-Text Objektes
     */
    public static int N_ZEICHENABSTAND = 10;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // -

    /**
     * Der Text dieses Display-Text Objektes oder ein code fuer eine Grafik
     */
    private String text;

    /**
     * Die Position dieses Display-Text Objektes auf dem Diaplay
     */
    private Point position;

    /**
     * Die einzelnen Zeichen dieses Display-Text Objektes in einem Array
     */
    private Zeichen zeichenArray[];

    /**
     * Die Anzahl der Display-Punkte in diesem Display-Text Objekt
     */
    private int numberOfPoints;

    /**
     * Die momentane Anzahl der Display-Punkte in diesem Display-Text Objekt
     */
    private int currentNumberOfPoints;

    /**
     * Der Paint-Status des Display-Text Objektes 
     * true, wenn es gezeichnet, werden soll, false wenn nicht
     */
    private boolean paintStatus;

    // ___________________________________________________________________________

    public Text() {
    }
    
    public Text(int displayWidth, int displayHeight) {
        super();
    }



    public Text(String textParam, Dimension pixelSize) {
        text = textParam;
        zeichenArray = new Zeichen[textParam.length()];
        for (int i = 0; i < textParam.length(); i++) {
            zeichenArray[i] = new Zeichen(textParam.charAt(i), pixelSize);
        }
        currentNumberOfPoints = 0;
        for (int i = 0; i < zeichenArray.length; i++) {
            if (zeichenArray[i] != null) {
                numberOfPoints = numberOfPoints + zeichenArray[i].getNumberOfPkt();
            }
        }
    }

    public Text(String p_sText, Point p_ptTextPos, Dimension p_dimPktSize) {
        int nCurentTextPos = 0;
        text = p_sText;
        position = p_ptTextPos;
        zeichenArray = new Zeichen[p_sText.length()];
        for (int i = 0; i < p_sText.length(); i++) {
            zeichenArray[i] = new Zeichen(
                    p_sText.charAt(i), 
                    new Point(p_ptTextPos.x + nCurentTextPos, p_ptTextPos.y), 
                    p_dimPktSize);
            nCurentTextPos = nCurentTextPos + N_ZEICHENABSTAND + (zeichenArray[i].getZielpunkte()[0].x + 1) * p_dimPktSize.width;
        }
        currentNumberOfPoints = 0;
        for (int i = 0; i < zeichenArray.length; i++) {
            if (zeichenArray[i] != null) {
                numberOfPoints = numberOfPoints + zeichenArray[i].getNumberOfPkt();
            }
        }
    }

    // ___________________________________________________________________________

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public int getNumberOfZeichen() {
        return zeichenArray.length;
    }

    public String getText() {
        return text;
    }

    public Dimension getSize() {
        int width = 0;
        int height = 0;
        for (int i = 0; i < zeichenArray.length; i++) {
            width = width + zeichenArray[i].getSize().width;
            width = width + N_ZEICHENABSTAND;
            if (height < zeichenArray[i].getSize().height) {
                height = zeichenArray[i].getSize().height;
            }
        }
        width = width - N_ZEICHENABSTAND;

        return new Dimension(width, height);
    }

    public Zeichen getZeichen(int p_nIndex) {
        if (p_nIndex < zeichenArray.length && zeichenArray.length > -1) {
            return zeichenArray[p_nIndex];
        } else {
            return null;
        }
    }

    public int getCurrentNumberOfPoints() {
        return currentNumberOfPoints;
    }

    public boolean isComplete() {
        return getCurrentNumberOfPoints() >= getNumberOfPoints();
    }

    public boolean getPaintStatus() {
        return paintStatus;
    }

    // ___________________________________________________________________________

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point p_ptNewPosition) {
        int nCurentX = 0;
        position = p_ptNewPosition;
        for (int i = 0; i < zeichenArray.length; i++) {
            zeichenArray[i].setPosition(new Point(p_ptNewPosition.x + nCurentX, p_ptNewPosition.y));
            nCurentX = nCurentX + N_ZEICHENABSTAND + zeichenArray[i].getSize().width;
        }
    }

    /**
     * fuegt dem text neue punkte hinzu bis er voll ist. die startkoordinaten der
     * punkte sind die ziele koordinaten vom text oldText
     */
    public void switchTo(Text oldText, int maxX, int maxY) {
        for (int i = 0; i < oldText.getNumberOfZeichen(); i++) {
            Zeichen oldZeichen = oldText.getZeichen(i);
            for (int k = 0; k < oldZeichen.getNumberOfPkt(); k++) {
                addPunkt(new Point(oldZeichen.getPkt(k).getPos().x, oldZeichen.getPkt(k).getPos().y), Color.black);
            }
        }
        // adds missing pixels
        while (addPunkt(new Point(getRandom(maxX),getRandom(maxY)), Color.black)) {
        }
        
    }
    
    private int getRandom(int max) {
        return Long.valueOf(Math.round(Math.random()*max)).intValue();
     }

    public void setAllPoints() {
        for (int i = 0; i < zeichenArray.length; i++) {
            zeichenArray[i].setAllPoints();
        }
        currentNumberOfPoints = numberOfPoints;
    }

    public void setAllPointsToX(int p_nX) {
        for (int i = 0; i < zeichenArray.length; i++) {
            zeichenArray[i].setAllPointsToX(p_nX);
        }
        currentNumberOfPoints = numberOfPoints;
    }

    public void setAllPointsToY(int p_nY) {
        for (int i = 0; i < zeichenArray.length; i++) {
            zeichenArray[i].setAllPointsToY(p_nY);
        }
        currentNumberOfPoints = numberOfPoints;
    }

    public void setPaintStatus(boolean p_bNewPaintStatus) {
        paintStatus = p_bNewPaintStatus;
        for (int i = 0; i < zeichenArray.length; i++) {
            zeichenArray[i].setPaintStatus(p_bNewPaintStatus);
        }
    }

    public void setCurrNumberOfPoints(int p_nNewCurrNumberOfPoints) {
        currentNumberOfPoints = p_nNewCurrNumberOfPoints;
        for (int i = 0; i < zeichenArray.length; i++) {
            zeichenArray[i].setNumberOfPkt(p_nNewCurrNumberOfPoints);
        }
    }

    // ___________________________________________________________________________

    /**
     * fuegt einen neuen punkt zum text element hinzu. der punkt wird zu ersten
     * zeichen hinzugefuegt, das noch nicht voll ist. es wird true
     * zurueckgegeben wenn noch platz fuer den punkt war und false, wenn der
     * nicht hinzugefuegt werden konnte, weil das textelement komplett ist
     * 
     * @return true wenn der punkt hizugefuegt wurde, false wenn der text
     *         komplett ist
     */
    public boolean addPunkt(Point position, Color color) {
        boolean notReady = false;
        for (int i = 0; i < zeichenArray.length && !notReady; i++) {
            if (!zeichenArray[i].isComplete()) {
                zeichenArray[i].addPunkt(position, color);
                currentNumberOfPoints++;
                notReady = true;
            }
        }
        return notReady;
    }

    /**
     * Sets the next position of all pixels of all characters in this text.
     * 
     * @return true if all pixels are set, false if not 
     */
    public boolean nextPosition() {
        boolean ready = true;
        if (zeichenArray != null) {
            for (int i = 0; i < zeichenArray.length; i++) {
                if (zeichenArray[i] != null) {
                    if (!zeichenArray[i].nextPosition()) {
                        ready = false;
                    }
                }
            }
        }
        return ready;
    }

    // ___________________________________________________________________________

    public void paint(Graphics p_G) {
        if (zeichenArray != null) {
            for (int i = 0; i < zeichenArray.length; i++) {
                if (zeichenArray[i] != null) {
                    zeichenArray[i].paint(p_G);
                }
            }
        }
    }

} // ens of class Text

