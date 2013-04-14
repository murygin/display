package display.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

import display.base.font.Special;
import display.base.font.Standart;

public class Zeichen {

    // maximale breite/h�he + 1
    public static final int N_MAX_ZEICHEN_BREITE = 53;
    public static final int N_MAX_ZEICHEN_HOEHE = 20;

    protected Pkt pktMatrix[][];
    protected Point zielpunkte[];
    private int numberOfPkt = 0;
    protected int width, height;
    int index = 0;

    private Point zeichenPos;

    // ___________________________________________________________________________

    /**
     * Konstruiert ein neues Display-Zeichen Objekt. Es wird keine Position des
     * Zeichens �bergegen. Sie mu�/kann sp�ter gesetzt werden. Wird keine neue
     * Position gesetzt, hat das Display-Zeichen die Positon 0,0 auf dem
     * Diaplay.
     * 
     * @param p_chZeichen
     *            Das dargerstellte Zeichen
     * @param p_dimPktSize
     *            Die Gr��e der Display-Punkte des Display-Zeichens
     */
    public Zeichen(char p_chZeichen, Dimension p_dimPktSize) {
        int x, y, maxX = 0, maxY = 0;
        setZielpunkte(p_chZeichen);
        pktMatrix = new Pkt[N_MAX_ZEICHEN_BREITE][N_MAX_ZEICHEN_HOEHE];
        for (int i = 0; i < zielpunkte.length; i++) {
            x = zielpunkte[i].x;
            y = zielpunkte[i].y;
            if (x > maxX) {
                maxX = x;
            }
            if (y > maxY) {
                maxY = y;
            }
            pktMatrix[x][y] = new Pkt(new Point(x * p_dimPktSize.width, y * p_dimPktSize.height), null, p_dimPktSize);
        }
        width = (maxX + 1) * Pkt.DM_SIZE.width;
        height = (maxY + 1) * Pkt.DM_SIZE.height;
    }

    /**
     * Konstruiert ein neues Display-Zeichen Objekt mit der �begebenen Positon
     * auf dem Display und der �bergebenen Display-Punkt Gr��e.
     * 
     * @param p_chZeichen
     *            Das dargerstellte Zeichen
     * @param p_ptZeichenPos
     *            Die Position des Display-Zeichen auf dem Display
     * @param p_dimPktSize
     *            Die Gr��e der Display-Punkte des Display-Zeichens
     */
    public Zeichen(char p_chZeichen, Point p_ptZeichenPos, Dimension p_dimPktSize) {
        int x, y, maxX = 0, maxY = 0;
        Vector vecCurrRow;
        zeichenPos = p_ptZeichenPos;
        setZielpunkte(p_chZeichen);
        pktMatrix = new Pkt[N_MAX_ZEICHEN_BREITE][N_MAX_ZEICHEN_HOEHE];
        for (int i = 0; i < zielpunkte.length; i++) {
            x = zielpunkte[i].x;
            y = zielpunkte[i].y;
            if (x > maxX) {
                maxX = x;
            }
            if (y > maxY) {
                maxY = y;
            }
            pktMatrix[x][y] = new Pkt(new Point(x * p_dimPktSize.width + zeichenPos.x, y * p_dimPktSize.height + zeichenPos.y), null, p_dimPktSize);
        }
        width = (maxX + 1) * Pkt.DM_SIZE.width;
        height = (maxY + 1) * Pkt.DM_SIZE.height;
    }

    // ___________________________________________________________________________

    public Point[] getZielpunkte() {
        return zielpunkte;
    }

    public int getNumberOfPkt() {
        // ??? Anzahl der Pkt-Instanzen immer gleich Anzahl der Zielpunkte ???
        return zielpunkte.length;
    }

    public Pkt getPkt(int p_nIndex) {
        return pktMatrix[zielpunkte[p_nIndex].x][zielpunkte[p_nIndex].y];
    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    // ___________________________________________________________________________

    /**
     * F�gt einen neuen Punkt zum Zeichen hinzu, wenn das Zeichen noch nicht
     * vollst�ndig ist. Im ersten Falle gibt die Methode true, im anderen false
     * zur�ck. Die Vollst�ngigkeit wird �ber den Z�hler m_nNumberOfPkt
     * �berpr�ft, der mit der L�nge des Arrays m_ptZielpunkte verglichen wird.
     * Der neue Punkt hat die Position p_ptPos und die Farbe p_clColor.
     * 
     * @param p_ptPos
     *            Die Position des neuen Punktes
     * @param p_clColor
     *            Die Farbe des neuen Punktes
     */
    public boolean addPunkt(Point p_ptPos, Color p_clColor) {
        Pkt currPkt;
        if (numberOfPkt < zielpunkte.length) {
            currPkt = pktMatrix[zielpunkte[numberOfPkt].x][zielpunkte[numberOfPkt].y];
            currPkt.setPos(p_ptPos);
            currPkt.setPaintStatus(true);
            numberOfPkt++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Setzt alle Punkte dieses Display-Zeichens an die Zielposition aus dem
     * Array m_ptZielpunkte.
     */
    public void setAllPoints() {
        Pkt currPkt;
        Point ptCurrPos;
        for (int i = 0; i < zielpunkte.length; i++) {
            currPkt = pktMatrix[zielpunkte[i].x][zielpunkte[i].y];
            ptCurrPos = new Point(zeichenPos.x + zielpunkte[i].x * currPkt.getSize().width, zeichenPos.y + zielpunkte[i].y * currPkt.getSize().height);
            currPkt.setPos(ptCurrPos);
            currPkt.setPaintStatus(true);
        }
    }

    /**
     * Setzt alle Punkte dieses Display-Zeichens an die x-Zielposition aus dem
     * Array m_ptZielpunkte und an die y-position p_nY.
     * 
     * @param p_nY
     *            die y-position der punkte
     */
    public void setAllPointsToY(int p_nY) {
        Pkt currPkt;
        Point ptCurrPos;
        for (int i = 0; i < zielpunkte.length; i++) {
            currPkt = pktMatrix[zielpunkte[i].x][zielpunkte[i].y];
            ptCurrPos = new Point(zeichenPos.x + zielpunkte[i].x * currPkt.getSize().width, p_nY);
            currPkt.setPos(ptCurrPos);
            currPkt.setPaintStatus(true);
        }
    }

    /**
     * Setzt alle Punkte dieses Display-Zeichens an die y-Zielposition aus dem
     * Array m_ptZielpunkte und an die x-position p_nX.
     * 
     * @param p_nY
     *            die y-position der punkte
     */
    public void setAllPointsToX(int p_nX) {
        Pkt currPkt;
        Point ptCurrPos;
        for (int i = 0; i < zielpunkte.length; i++) {
            currPkt = pktMatrix[zielpunkte[i].x][zielpunkte[i].y];
            ptCurrPos = new Point(p_nX, zeichenPos.y + zielpunkte[i].y * currPkt.getSize().height);
            currPkt.setPos(ptCurrPos);
            currPkt.setPaintStatus(true);
        }
    }

    /**
     * Setzt die Position des Zeichens auf den �bergebenen Wert, indem alle
     * Diplay-Punkte des Zeichens auf neue Positionen gesetzt werden.
     * 
     * @param p_ptNewPosition
     *            Die neue Position des Display-Zeichens
     */
    public void setPosition(Point p_ptNewPosition) {
        Pkt currPkt;
        Point ptCurrPos;
        zeichenPos = p_ptNewPosition;
        for (int i = 0; i < zielpunkte.length; i++) {
            currPkt = pktMatrix[zielpunkte[i].x][zielpunkte[i].y];
            ptCurrPos = new Point(zeichenPos.x + zielpunkte[i].x * currPkt.getSize().width, zeichenPos.y + zielpunkte[i].y * currPkt.getSize().height);
            currPkt.setZiel(ptCurrPos);
        }
    }

    /**
     * Setzt den paintStatus des Display-Zeichens, indem der paintStatus aller
     * Display-Punkte gesetzt wird. Ist der paintStatus true, wird das
     * Display-Zeichen gemalt, andernfalls nicht
     * 
     * @param p_bNewPaintStatus
     *            Der neue paintStatus des Display-Zeichen
     */
    public void setPaintStatus(boolean p_bNewPaintStatus) {
        for (int i = 0; i < zielpunkte.length; i++) {
            pktMatrix[zielpunkte[i].x][zielpunkte[i].y].setPaintStatus(p_bNewPaintStatus);
        }
    }

    /**
     * Setzt die Anzahl der Punkte auf einen neuen Wert. Sinnvoll z.B. wenn die
     * Animation von vorne Beginnen soll: Die Anzahl der Punkte wird auf 0
     * gesetzt -> es k�nnen wieder Punkte mit neune Positionen hinzugef�gt
     * werden -> nextPosition() liefert true -> Animation beginnt wieder
     */
    public void setNumberOfPkt(int p_nNewNumberOfPkt) {
        numberOfPkt = p_nNewNumberOfPkt;
    }

    protected void setZielpunkte(char zeichen) {
        switch (zeichen) {
        // Brandenburger Tor:
        case '#': {
            zielpunkte = Special.PT_BT;
            break;
        }
        // vierspur
        case '%': {
            zielpunkte = Special.PT_VIERSPUR;
            break;
        }
        // Pfeil
        case '&': {
            zielpunkte = Special.PT_PFEIL;
            break;
        }
        // Blank
        case ' ': {
            zielpunkte = Standart.PT_BLANK;
            break;
        }
        // Sonderzeichen:
        case '+': {
            zielpunkte = Special.PT_PLUS;
            break;
        }
        case '_': {
            zielpunkte = Special.PT_PROMPT;
            break;
        }
        case '.': {
            zielpunkte = Special.DOT;
            break;
        }
        // Zahlen:
        case '0': {
            zielpunkte = Standart.PT_0;
            break;
        }
        case '2': {
            zielpunkte = Standart.PT_2;
            break;
        }
        case '9': {
            zielpunkte = Standart.PT_9;
            break;
        }
        // Buchstaben:
        case 'a': {
            zielpunkte = Standart.PT_A;
            break;
        }
        case 'b': {
            zielpunkte = Standart.PT_B;
            break;
        }
        case 'c': {
            zielpunkte = Standart.PT_C;
            break;
        }
        case 'd': {
            zielpunkte = Standart.PT_D;
            break;
        }
        case 'e': {
            zielpunkte = Standart.PT_E;
            break;
        }
        case 'f': {
            zielpunkte = Standart.PT_F;
            break;
        }
        case 'g': {
            zielpunkte = Standart.PT_G;
            break;
        }
        case 'h': {
            zielpunkte = Standart.PT_H;
            break;
        }
        case 'i': {
            zielpunkte = Standart.PT_I;
            break;
        }
        case 'j': {
            zielpunkte = Standart.PT_J;
            break;
        }
        case 'k': {
            zielpunkte = Standart.PT_K;
            break;
        }
        case 'l': {
            zielpunkte = Standart.PT_L;
            break;
        }
        case 'm': {
            zielpunkte = Standart.PT_M;
            break;
        }
        case 'n': {
            zielpunkte = Standart.PT_N;
            break;
        }
        case 'o': {
            zielpunkte = Standart.PT_O;
            break;
        }
        case 'p': {
            zielpunkte = Standart.PT_P;
            break;
        }
        case 'q': {
            zielpunkte = Standart.PT_Q;
            break;
        }
        case 'r': {
            zielpunkte = Standart.PT_R;
            break;
        }
        case 's': {
            zielpunkte = Standart.PT_S;
            break;
        }
        case 't': {
            zielpunkte = Standart.PT_T;
            break;
        }
        case 'u': {
            zielpunkte = Standart.PT_U;
            break;
        }
        case 'v': {
            zielpunkte = Standart.PT_V;
            break;
        }
        case 'w': {
            zielpunkte = Standart.PT_W;
            break;
        }
        case 'x': {
            zielpunkte = Standart.PT_X;
            break;
        }
        case 'y': {
            zielpunkte = Standart.PT_Y;
            break;
        }
        case 'z': {
            zielpunkte = Standart.PT_Z;
        }
        } // end switch
    } // end setZielpunkte

    public boolean isComplete() {
        return (numberOfPkt >= zielpunkte.length);
    }

    /**
     * Sets the next position of all pixels in this charakter.
     * 
     * @return true if all pixels are set, false if not 
     */
    public boolean nextPosition() {
        boolean ready = true;
        for (int i = 0; i < zielpunkte.length; i++) {
            if (!pktMatrix[zielpunkte[i].x][zielpunkte[i].y].nextPosition()) {
                ready = false;
            }
        }
        return ready;
    }

    // ___________________________________________________________________________

    public void paint(Graphics p_G) {
        for (int i = 0; i < zielpunkte.length; i++) {
            pktMatrix[zielpunkte[i].x][zielpunkte[i].y].paint(p_G);
        }
    }

} // end class Zeichen
