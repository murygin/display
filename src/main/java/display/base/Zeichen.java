package display.base;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

import java.util.Vector;

import display.base.font.Standart;
import display.base.font.Special;

public class Zeichen {

	//maximale breite/höhe + 1
	public static final int N_MAX_ZEICHEN_BREITE = 53;
	public static final int N_MAX_ZEICHEN_HOEHE = 20;

	protected Pkt m_pktMatrix[][];
	protected Point m_ptZielpunkte[];
	private int m_nNumberOfPkt=0;
	protected int m_nWidth, m_nHeight;
	int index=0;

	private Point m_ptZeichenPos;

	//___________________________________________________________________________

	/**
	* Konstruiert ein neues Display-Zeichen Objekt.
	* Es wird keine Position des Zeichens übergegen. Sie muß/kann später
	* gesetzt werden.
	* Wird keine neue Position gesetzt, hat das Display-Zeichen
	* die Positon 0,0 auf dem Diaplay.
	*
	* @param p_chZeichen Das dargerstellte Zeichen
	* @param p_dimPktSize Die Größe der Display-Punkte des Display-Zeichens
	*/
	public Zeichen( char p_chZeichen,
									Dimension p_dimPktSize ) {
		int x, y, maxX=0, maxY=0;
		setZielpunkte( p_chZeichen );
		m_pktMatrix = new Pkt[N_MAX_ZEICHEN_BREITE][N_MAX_ZEICHEN_HOEHE];
		for( int i=0; i<m_ptZielpunkte.length; i++ ) {
			x=m_ptZielpunkte[i].x;
			y=m_ptZielpunkte[i].y;
			if( x>maxX )
				maxX=x;
			if( y>maxY )
				maxY=y;
			m_pktMatrix[x][y] = new Pkt( new Point( x*p_dimPktSize.width,
																							y*p_dimPktSize.height ),
																	 null,
																	 p_dimPktSize );
		}
		m_nWidth = (maxX+1) * Pkt.DM_SIZE.width;
		m_nHeight = (maxY+1) * Pkt.DM_SIZE.height;
	}

	/**
	* Konstruiert ein neues Display-Zeichen Objekt mit der übegebenen
	* Positon auf dem Display und der übergebenen Display-Punkt Größe.
	*
	* @param p_chZeichen Das dargerstellte Zeichen
	* @param p_ptZeichenPos Die Position des Display-Zeichen auf dem Display
	* @param p_dimPktSize Die Größe der Display-Punkte des Display-Zeichens
	*/
	public Zeichen( char p_chZeichen,
									Point p_ptZeichenPos,
									Dimension p_dimPktSize ) {
		int x, y, maxX=0, maxY=0;
		Vector vecCurrRow;
		m_ptZeichenPos = p_ptZeichenPos;
		setZielpunkte( p_chZeichen );
		m_pktMatrix = new Pkt[N_MAX_ZEICHEN_BREITE][N_MAX_ZEICHEN_HOEHE];
		for( int i=0; i<m_ptZielpunkte.length; i++ ) {
			x=m_ptZielpunkte[i].x;
			y=m_ptZielpunkte[i].y;
			if( x>maxX )
				maxX=x;
			if( y>maxY )
				maxY=y;
			m_pktMatrix[x][y] = new Pkt( new Point( x*p_dimPktSize.width+m_ptZeichenPos.x,
																							y*p_dimPktSize.height+m_ptZeichenPos.y ),
																	 null,
																	 p_dimPktSize );
		}
		m_nWidth = (maxX+1) * Pkt.DM_SIZE.width;
		m_nHeight = (maxY+1) * Pkt.DM_SIZE.height;
	}

  //___________________________________________________________________________

	public Point[] getZielpunkte() {
		return m_ptZielpunkte;
	}

	public int getNumberOfPkt() {
		//??? Anzahl der Pkt-Instanzen immer gleich Anzahl der Zielpunkte ???
		return m_ptZielpunkte.length;
	}

	public Pkt getPkt( int p_nIndex ) {
		return m_pktMatrix[ m_ptZielpunkte[p_nIndex].x ][ m_ptZielpunkte[p_nIndex].y ];
	}

	public Dimension getSize() {
		return new Dimension( m_nWidth, m_nHeight );
	}

	//___________________________________________________________________________

	/**
	* Fügt einen neuen Punkt zum Zeichen hinzu, wenn das Zeichen noch
	* nicht vollständig ist. Im ersten Falle gibt die Methode true,
	* im anderen false zurück. Die Vollstängigkeit wird über den Zähler
	* m_nNumberOfPkt überprüft, der mit der Länge des Arrays m_ptZielpunkte
	* verglichen wird.
	* Der neue Punkt hat die Position p_ptPos und die Farbe p_clColor.
	*
	* @param p_ptPos Die Position des neuen Punktes
	* @param p_clColor Die Farbe des neuen Punktes
	*/
	public boolean addPunkt( Point p_ptPos, Color p_clColor ) {
		Pkt currPkt;
		if( m_nNumberOfPkt<m_ptZielpunkte.length ) {
			currPkt = m_pktMatrix[ m_ptZielpunkte[m_nNumberOfPkt].x ][ m_ptZielpunkte[m_nNumberOfPkt].y ];
			currPkt.setPos( p_ptPos );
			currPkt.setPaintStatus( true );
			m_nNumberOfPkt++;
			return true;
		}
		else
			return false;
	}

	/**
	* Setzt alle Punkte dieses Display-Zeichens an die Zielposition aus
	* dem Array m_ptZielpunkte.
	*/
	public void setAllPoints() {
		Pkt currPkt;
		Point ptCurrPos;
		for( int i=0; i<m_ptZielpunkte.length; i++ ) {
			currPkt = m_pktMatrix[ m_ptZielpunkte[i].x ][ m_ptZielpunkte[i].y ];
			ptCurrPos =  new Point( m_ptZeichenPos.x + m_ptZielpunkte[i].x*currPkt.getSize().width,
															m_ptZeichenPos.y + m_ptZielpunkte[i].y*currPkt.getSize().height );
			currPkt.setPos( ptCurrPos );
			currPkt.setPaintStatus( true );
		}
	}

  /**
	* Setzt alle Punkte dieses Display-Zeichens an die x-Zielposition aus
	* dem Array m_ptZielpunkte und an die y-position p_nY.
  *
  * @param p_nY die y-position der punkte
	*/
	public void setAllPointsToY( int p_nY ) {
		Pkt currPkt;
		Point ptCurrPos;
		for( int i=0; i<m_ptZielpunkte.length; i++ ) {
			currPkt = m_pktMatrix[ m_ptZielpunkte[i].x ][ m_ptZielpunkte[i].y ];
			ptCurrPos =  new Point( m_ptZeichenPos.x + m_ptZielpunkte[i].x*currPkt.getSize().width,
															p_nY );
			currPkt.setPos( ptCurrPos );
			currPkt.setPaintStatus( true );
		}
	}

  /**
	* Setzt alle Punkte dieses Display-Zeichens an die y-Zielposition aus
	* dem Array m_ptZielpunkte und an die x-position p_nX.
  *
  * @param p_nY die y-position der punkte
	*/
	public void setAllPointsToX( int p_nX ) {
		Pkt currPkt;
		Point ptCurrPos;
		for( int i=0; i<m_ptZielpunkte.length; i++ ) {
			currPkt = m_pktMatrix[ m_ptZielpunkte[i].x ][ m_ptZielpunkte[i].y ];
			ptCurrPos =  new Point( p_nX,
															m_ptZeichenPos.y + m_ptZielpunkte[i].y*currPkt.getSize().height );
			currPkt.setPos( ptCurrPos );
			currPkt.setPaintStatus( true );
		}
	}


	/**
	* Setzt die Position des Zeichens auf den übergebenen Wert, indem alle
	* Diplay-Punkte des Zeichens auf neue Positionen gesetzt werden.
	*
	* @param p_ptNewPosition Die neue Position des Display-Zeichens
	*/
	public void setPosition( Point p_ptNewPosition ) {
		Pkt currPkt;
		Point ptCurrPos;
		m_ptZeichenPos = p_ptNewPosition;
		for( int i=0; i<m_ptZielpunkte.length; i++ ) {
			currPkt = m_pktMatrix[ m_ptZielpunkte[i].x ][ m_ptZielpunkte[i].y ];
			ptCurrPos =  new Point( m_ptZeichenPos.x + m_ptZielpunkte[i].x*currPkt.getSize().width,
															m_ptZeichenPos.y + m_ptZielpunkte[i].y*currPkt.getSize().height );
			currPkt.setZiel( ptCurrPos );
		}
	}

	/**
	* Setzt den paintStatus des Display-Zeichens, indem der paintStatus
	* aller Display-Punkte gesetzt wird.
	* Ist der paintStatus true, wird das Display-Zeichen gemalt, andernfalls
	* nicht
	*
	* @param p_bNewPaintStatus Der neue paintStatus des Display-Zeichen
	*/
	public void setPaintStatus( boolean p_bNewPaintStatus ) {
		for( int i=0; i<m_ptZielpunkte.length; i++ )
			m_pktMatrix[ m_ptZielpunkte[i].x ][ m_ptZielpunkte[i].y ].setPaintStatus( p_bNewPaintStatus );
	}

	/**
	* Setzt die Anzahl der Punkte auf einen neuen Wert.
	* Sinnvoll z.B. wenn die Animation von vorne Beginnen soll:
	* Die Anzahl der Punkte wird auf 0 gesetzt -> es können wieder Punkte
	* mit neune Positionen hinzugefügt werden -> nextPosition() liefert true
	* -> Animation beginnt wieder
	*/
	public void setNumberOfPkt( int p_nNewNumberOfPkt ) {
		m_nNumberOfPkt = p_nNewNumberOfPkt;
	}


	protected void setZielpunkte( char p_chZeichen ) {
		switch( p_chZeichen ) {
			//Brandenburger Tor:
			case '#': {
				m_ptZielpunkte = Special.PT_BT; break;
			}
      //vierspur
      case '%': {
				m_ptZielpunkte = Special.PT_VIERSPUR; break;
			}
      //Pfeil
      case '&': {
				m_ptZielpunkte = Special.PT_PFEIL; break;
			}
      //Blank
      case ' ': {
				m_ptZielpunkte = Standart.PT_BLANK; break;
			}
			//Sonderzeichen:
			case '+': {
				m_ptZielpunkte = Special.PT_PLUS; break;
			}
      case '_': {
				m_ptZielpunkte = Special.PT_PROMPT; break;
			}
			//Zahlen:
			case '0': {
				m_ptZielpunkte = Standart.PT_0; break;
			}
			case '2': {
				m_ptZielpunkte = Standart.PT_2; break;
			}
			case '9': {
				m_ptZielpunkte = Standart.PT_9; break;
			}
			//Buchstaben:
			case 'a': {
				m_ptZielpunkte = Standart.PT_A; break;
			}
			case 'b': {
				m_ptZielpunkte = Standart.PT_B; break;
			}
			case 'c': {
				m_ptZielpunkte = Standart.PT_C; break;
			}
			case 'd': {
				m_ptZielpunkte = Standart.PT_D; break;
			}
			case 'e': {
				m_ptZielpunkte = Standart.PT_E; break;
			}
      case 'f': {
				m_ptZielpunkte = Standart.PT_F; break;
			}
      case 'g': {
				m_ptZielpunkte = Standart.PT_G; break;
			}
      case 'h': {
				m_ptZielpunkte = Standart.PT_H; break;
			}
			case 'i': {
				m_ptZielpunkte = Standart.PT_I; break;
			}
      case 'j': {
				m_ptZielpunkte = Standart.PT_J; break;
			}
			case 'k': {
				m_ptZielpunkte = Standart.PT_K; break;
			}
			case 'l': {
				m_ptZielpunkte = Standart.PT_L; break;
			}
			case 'm': {
				m_ptZielpunkte = Standart.PT_M; break;
			}
			case 'n': {
				m_ptZielpunkte = Standart.PT_N; break;
			}
			case 'o': {
				m_ptZielpunkte = Standart.PT_O; break;
			}
			case 'p': {
				m_ptZielpunkte = Standart.PT_P; break;
			}
      case 'q': {
				m_ptZielpunkte = Standart.PT_Q; break;
			}
			case 'r': {
				m_ptZielpunkte = Standart.PT_R; break;
			}
			case 's': {
				m_ptZielpunkte = Standart.PT_S; break;
			}
			case 't': {
				m_ptZielpunkte = Standart.PT_T; break;
			}
			case 'u': {
				m_ptZielpunkte = Standart.PT_U; break;
			}
			case 'v': {
				m_ptZielpunkte = Standart.PT_V; break;
			}
			case 'w': {
				m_ptZielpunkte = Standart.PT_W; break;
			}
			case 'x': {
				m_ptZielpunkte = Standart.PT_X; break;
			}
      case 'y': {
				m_ptZielpunkte = Standart.PT_Y; break;
			}
      case 'z': {
				m_ptZielpunkte = Standart.PT_Z;
			}
		} //end switch
	} //end setZielpunkte

	public boolean isComplete() {
		return( m_nNumberOfPkt>=m_ptZielpunkte.length );
	}

	public boolean nextPosition() {
		boolean ready=true;
		for( int i=0; i<m_ptZielpunkte.length; i++ )
			if( !m_pktMatrix[ m_ptZielpunkte[i].x ][ m_ptZielpunkte[i].y ].nextPosition() )
				ready=false;
		return ready;
	}

	//___________________________________________________________________________

	public void paint( Graphics p_G ) {
		for( int i=0; i<m_ptZielpunkte.length; i++ )
				m_pktMatrix[ m_ptZielpunkte[i].x ][ m_ptZielpunkte[i].y ].paint( p_G );
	}

} //end class Zeichen
