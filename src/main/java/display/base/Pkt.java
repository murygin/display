package display.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class Pkt {

	protected static Dimension DIM_DISPLAY_SIZE;

	public static double DB_V = 3;
	public static Dimension DM_SIZE = new Dimension( 4,4 );
	public static Color CL_COLOR = Color.black;

  private static double DB_XTOLERANCE = 3;
  private static double DB_YTOLERANCE = 3;


	protected boolean m_bPaintStatus;

	//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	protected double[] m_dbPos = new double[2];
	protected Point m_ptZiel;

	private int m_nXDir=1;
	private int m_nYDir=1;

	private double m_dbWinkel;

	private double m_dbV;

	//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	protected Dimension m_dmSize;
	protected Color m_clColor;

	//___________________________________________________________________________

	public Pkt( Point p_ptZiel, Color p_clColor  ) {
		m_ptZiel = p_ptZiel;
		//default Geschwindigkeit
		m_dbV = DB_V;
		m_dmSize = DM_SIZE;
		if( p_clColor!=null )
			m_clColor = p_clColor;
		else
			m_clColor = CL_COLOR;
		m_bPaintStatus=false;
	}

	public Pkt( Point p_ptZiel,
							Color p_clColor,
							Dimension p_dimSize  ) {
		m_ptZiel = p_ptZiel;
		//default Geschwindigkeit
		m_dbV = DB_V;
		m_dmSize = p_dimSize;
		if( p_clColor!=null )
			m_clColor = p_clColor;
		else
			m_clColor = CL_COLOR;
		m_bPaintStatus=false;
	}

	public Pkt( Point p_ptPos, Point p_ptZiel, Color p_clColor ) {
		m_dbPos[0] = p_ptPos.x;
		m_dbPos[1] = p_ptPos.y;
		m_bPaintStatus = true;
		m_ptZiel = p_ptZiel;
		if( m_dbPos[0] > m_ptZiel.x )
			m_nXDir = -1;
		if( m_dbPos[1] > m_ptZiel.y )
			m_nYDir = -1;
		m_dbV = DB_V;
		m_dmSize = DM_SIZE;
		if( p_clColor!=null )
			m_clColor = p_clColor;
		else
			m_clColor = CL_COLOR;
		//Berechnung des Winkels m_dbWinkel zwischen Verbindungslinie von p_ptPos
		//und p_ptZiel und der Horizontalen
		m_dbWinkel = Math.atan( ( p_ptPos.y*1.0 - p_ptZiel.y*1.0 ) / ( p_ptPos.x*1.0 - p_ptZiel.x*1.0 ) ) ;
	}

	public Pkt( Point p_ptPos,
							Point p_ptZiel,
							double p_dbV,
							Dimension p_dmSize,
							Color p_clColor ) {
		m_dbPos[0] = p_ptPos.x;
		m_dbPos[1] = p_ptPos.y;
		m_bPaintStatus = true;
		m_ptZiel = p_ptZiel;
		if( m_dbPos[0] > m_ptZiel.x )
			m_nXDir = -1;
		if( m_dbPos[1] > m_ptZiel.y )
			m_nYDir = -1;
		m_dbV = p_dbV;
		m_dmSize = p_dmSize;
		m_clColor = p_clColor;
		//Berechnung des Winkels m_dbWinkel zwischen Verbindungslinie von p_ptPos
		//und p_ptZiel und der Horizontalen
		m_dbWinkel = Math.atan( ( p_ptPos.y*1.0 - p_ptZiel.y*1.0 ) / ( p_ptPos.x*1.0 - p_ptZiel.x*1.0 ) ) ;
	}

	//___________________________________________________________________________

	public Point getPos() {
		return new Point( new Long( Math.round(m_dbPos[0]) ).intValue(),
											new Long( Math.round(m_dbPos[1]) ).intValue() );
	}

	public Point getZiel() {
		return m_ptZiel;
	}

	public Dimension getSize() {
		return m_dmSize;
	}

	private double[] getV() {
		double[] result = new double[2];
		result[0] =  m_nXDir * Math.abs( Math.cos(m_dbWinkel) * m_dbV );
		result[1] =  m_nYDir * Math.abs( Math.sin(m_dbWinkel) * m_dbV );
		return result;
	}

	public static Dimension getDisplaySize() {
		return DIM_DISPLAY_SIZE;
	}

	public boolean getPaintStatus() {
		 return m_bPaintStatus;
	}

	//___________________________________________________________________________

	public void setSize( Dimension p_dimNewSize ) {
		m_dmSize = p_dimNewSize;
	}

	public void setPos( Point p_ptPos ) {
		m_dbPos[0] = p_ptPos.x;
		m_dbPos[1] = p_ptPos.y;
		if( m_dbPos[0] > m_ptZiel.x )
			m_nXDir = -1;
		if( m_dbPos[1] > m_ptZiel.y )
			m_nYDir = -1;
		//Berechnung des Winkels m_dbWinkel zwischen Verbindungslinie von p_ptPos
		//und p_ptZiel und der Horizontalen
		m_dbWinkel = Math.atan( ( m_dbPos[1] - getZiel().y*1.0 ) / ( m_dbPos[0] - getZiel().x*1.0 ) );
	}

	public void setPaintStatus( boolean p_bPaintStatus ) {
		m_bPaintStatus = p_bPaintStatus;
	}

	public void setZiel( Point p_ptZiel ) {
		m_ptZiel = p_ptZiel;
	}

	public static void setDisplaySize( Dimension p_dimSize ) {
		DIM_DISPLAY_SIZE = p_dimSize;
	}

	public static void setSpeed( double p_dbNewSpeed ) {
		DB_V = p_dbNewSpeed;
	}

  public void setInstanceSpeed( double p_dbNewSpeed ) {
    m_dbV = p_dbNewSpeed;
	}

	//___________________________________________________________________________

	public boolean isReady() {
		if( !getPaintStatus() )
			return false;
		return( m_dbPos[0] == m_ptZiel.x && m_dbPos[1] == m_ptZiel.y );
	}

	public boolean nextPosition() {
		if( getPaintStatus() ) {
			if( m_dbPos[0] > m_ptZiel.x-DB_XTOLERANCE && m_dbPos[0] < m_ptZiel.x+DB_XTOLERANCE )
				m_dbPos[0]=m_ptZiel.x;
			if( m_dbPos[1] > m_ptZiel.y-DB_YTOLERANCE && m_dbPos[1] < m_ptZiel.y+DB_YTOLERANCE )
				m_dbPos[1]=m_ptZiel.y;
			if( m_dbPos[0] != m_ptZiel.x ) {
				m_dbPos[0] = m_dbPos[0] + getV()[0];
				if( m_dbPos[0]<0 || m_dbPos[0]>getDisplaySize().width ) {
					m_nXDir = m_nXDir*-1;
				}
			}
			if( m_dbPos[1] != m_ptZiel.y ) {
				m_dbPos[1] = m_dbPos[1] + getV()[1];
				if( m_dbPos[1]<0 || m_dbPos[1]>getDisplaySize().height ) {
					m_nYDir = m_nYDir*-1;
				}
			}
		}
    /*
    DB_XTOLERANCE+=0.00001;
    DB_YTOLERANCE+=0.00001;
    */
		return isReady();
	}

	//___________________________________________________________________________

	public void paint( Graphics p_G ) {
		if( getPaintStatus() ) {
			p_G.setColor( m_clColor );
			p_G.fillRoundRect( new Long( Math.round(m_dbPos[0]) ).intValue()+1,
										new Long( Math.round(m_dbPos[1]) ).intValue()+1,
										m_dmSize.width-1,
										m_dmSize.height-1,
										0,
										0 );
		}
	}

} //end class Pkt