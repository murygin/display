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
     * Der Text dieses Display-Text Objektes oder ein code f�r eine Grafik
     */
    private String m_sText;

    /**
     * Die Position dieses Display-Text Objektes auf dem Diaplay
     */
    private Point m_ptPosition;

    /**
     * Die einzelnen Zeichen dieses Display-Text Objektes in einem Array
     */
    private Zeichen m_zenText[];

    /**
     * Die Anzahl der Display-Punkte in diesem Display-Text Objekt
     */
    private int m_nNumberOfPoints;

    /**
     * Die momentane Anzahl der Display-Punkte in diesem Display-Text Objekt
     */
    private int m_nCurrentNumberOfPoints;

    /**
     * Der Paint-Status des Display-Text Objektes true, wenn das es gezeichnet
     * werden soll, false wenn nicht
     */
    private boolean m_bPaintStatus;

    // ___________________________________________________________________________

    public Text() {
    }

    public Text(String p_sText, Dimension p_dimPktSize) {
        m_sText = p_sText;
        m_zenText = new Zeichen[p_sText.length()];
        for (int i = 0; i < p_sText.length(); i++) {
            m_zenText[i] = new Zeichen(p_sText.charAt(i), p_dimPktSize);
        }
        m_nCurrentNumberOfPoints = 0;
        for (int i = 0; i < m_zenText.length; i++) {
            if (m_zenText[i] != null) {
                m_nNumberOfPoints = m_nNumberOfPoints + m_zenText[i].getNumberOfPkt();
            }
        }
    }

    public Text(String p_sText, Point p_ptTextPos, Dimension p_dimPktSize) {
        int nCurentTextPos = 0;
        m_sText = p_sText;
        m_ptPosition = p_ptTextPos;
        m_zenText = new Zeichen[p_sText.length()];
        for (int i = 0; i < p_sText.length(); i++) {
            m_zenText[i] = new Zeichen(
                    p_sText.charAt(i), 
                    new Point(p_ptTextPos.x + nCurentTextPos, p_ptTextPos.y), 
                    p_dimPktSize);
            nCurentTextPos = nCurentTextPos + N_ZEICHENABSTAND + (m_zenText[i].getZielpunkte()[0].x + 1) * p_dimPktSize.width;
        }
        m_nCurrentNumberOfPoints = 0;
        for (int i = 0; i < m_zenText.length; i++) {
            if (m_zenText[i] != null) {
                m_nNumberOfPoints = m_nNumberOfPoints + m_zenText[i].getNumberOfPkt();
            }
        }
    }

    // ___________________________________________________________________________

    public int getNumberOfPoints() {
        return m_nNumberOfPoints;
    }

    public int getNumberOfZeichen() {
        return m_zenText.length;
    }

    public String getText() {
        return m_sText;
    }

    public Dimension getSize() {
        int width = 0;
        int height = 0;
        for (int i = 0; i < m_zenText.length; i++) {
            width = width + m_zenText[i].getSize().width;
            width = width + N_ZEICHENABSTAND;
            if (height < m_zenText[i].getSize().height) {
                height = m_zenText[i].getSize().height;
            }
        }
        width = width - N_ZEICHENABSTAND;

        return new Dimension(width, height);
    }

    public Zeichen getZeichen(int p_nIndex) {
        if (p_nIndex < m_zenText.length && m_zenText.length > -1) {
            return m_zenText[p_nIndex];
        } else {
            return null;
        }
    }

    public int getCurrentNumberOfPoints() {
        return m_nCurrentNumberOfPoints;
    }

    public boolean isComplete() {
        return getCurrentNumberOfPoints() >= getNumberOfPoints();
    }

    public boolean getPaintStatus() {
        return m_bPaintStatus;
    }

    // ___________________________________________________________________________

    public Point getPosition() {
        return m_ptPosition;
    }

    public void setPosition(Point p_ptNewPosition) {
        int nCurentX = 0;
        m_ptPosition = p_ptNewPosition;
        for (int i = 0; i < m_zenText.length; i++) {
            m_zenText[i].setPosition(new Point(p_ptNewPosition.x + nCurentX, p_ptNewPosition.y));
            nCurentX = nCurentX + N_ZEICHENABSTAND + m_zenText[i].getSize().width;
        }
    }

    /**
     * fuegt dem text neue punkte hinzu bi er voll ist. die startkoordinaten der
     * punkte snd die zile koordinaten vom text p_oldText
     */
    public void switchTo(Text p_oldText) {
        for (int i = 0; i < p_oldText.getNumberOfZeichen(); i++) {
            Zeichen oldZeichen = p_oldText.getZeichen(i);
            for (int k = 0; k < oldZeichen.getNumberOfPkt(); k++) {
                addPunkt(new Point(oldZeichen.getPkt(k).getPos().x, oldZeichen.getPkt(k).getPos().y), Color.black);
            }
        }

        int mittelindex = Math.round(p_oldText.getText().length() / 2);
        while (addPunkt(new Point(Math.round(p_oldText.getSize().width / 2), Math.round(p_oldText.getSize().height / 2)), Color.black)) {
        }
    }

    public void setAllPoints() {
        for (int i = 0; i < m_zenText.length; i++) {
            m_zenText[i].setAllPoints();
        }
        m_nCurrentNumberOfPoints = m_nNumberOfPoints;
    }

    public void setAllPointsToX(int p_nX) {
        for (int i = 0; i < m_zenText.length; i++) {
            m_zenText[i].setAllPointsToX(p_nX);
        }
        m_nCurrentNumberOfPoints = m_nNumberOfPoints;
    }

    public void setAllPointsToY(int p_nY) {
        for (int i = 0; i < m_zenText.length; i++) {
            m_zenText[i].setAllPointsToY(p_nY);
        }
        m_nCurrentNumberOfPoints = m_nNumberOfPoints;
    }

    public void setPaintStatus(boolean p_bNewPaintStatus) {
        m_bPaintStatus = p_bNewPaintStatus;
        for (int i = 0; i < m_zenText.length; i++) {
            m_zenText[i].setPaintStatus(p_bNewPaintStatus);
        }
    }

    public void setCurrNumberOfPoints(int p_nNewCurrNumberOfPoints) {
        m_nCurrentNumberOfPoints = p_nNewCurrNumberOfPoints;
        for (int i = 0; i < m_zenText.length; i++) {
            m_zenText[i].setNumberOfPkt(p_nNewCurrNumberOfPoints);
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
    public boolean addPunkt(Point p_ptPos, Color p_clColor) {
        boolean notReady = false;
        for (int i = 0; i < m_zenText.length && !notReady; i++) {
            if (!m_zenText[i].isComplete()) {
                m_zenText[i].addPunkt(p_ptPos, p_clColor);
                m_nCurrentNumberOfPoints++;
                notReady = true;
            }
        }
        return notReady;
    }

    public boolean nextPosition() {
        boolean ready = true;
        if (m_zenText != null) {
            for (int i = 0; i < m_zenText.length; i++) {
                if (m_zenText[i] != null) {
                    if (!m_zenText[i].nextPosition()) {
                        ready = false;
                    }
                }
            }
        }
        return ready;
    }

    // ___________________________________________________________________________

    public void paint(Graphics p_G) {
        if (m_zenText != null) {
            for (int i = 0; i < m_zenText.length; i++) {
                if (m_zenText[i] != null) {
                    m_zenText[i].paint(p_G);
                }
            }
        }
    }

} // ens of class Text

