package display.base;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import java.awt.Dimension;

public class Pkt3D extends Pkt {

	private int m_nIndex;
	private double m_db3DWinkel = 45;

	public Pkt3D( Point p_ptPos,
								Point p_ptZiel,
								int p_nIndex,
								Color p_clColor ) {
		super( p_ptPos, p_ptZiel, p_clColor );
		m_nIndex = p_nIndex;
	}

  public Pkt3D( Point p_ptZiel,
							  Color p_clColor,
							  Dimension p_dimSize,
                int p_nIndex  ) {
    super( p_ptZiel,p_clColor,p_dimSize );
    m_nIndex=p_nIndex;
	}

	public void paint( Graphics p_G ) {
    int nX[];
    int nY[];
		//switch( m_nIndex ) {
			//case 1: {
				p_G.setColor( Color.gray );
				p_G.fillRect( new Long( Math.round(m_dbPos[0]) ).intValue(),
											new Long( Math.round(m_dbPos[1]) ).intValue(),
											m_dmSize.width,
											m_dmSize.height );
				//break;
			//}
			//case 2: {
				//p_G.setColor( m_clColor.brighter() );
        p_G.setColor( Color.darkGray  );
				nX = new int[] {new Long( Math.round(m_dbPos[0]+m_dmSize.width) ).intValue(),
										new Long( Math.round(m_dbPos[0]+m_dmSize.width+m_dmSize.width*Math.cos(m_db3DWinkel)) ).intValue(),
										new Long( Math.round(m_dbPos[0]+m_dmSize.width+m_dmSize.width*Math.cos(m_db3DWinkel)) ).intValue(),
										new Long( Math.round(m_dbPos[0]+m_dmSize.width) ).intValue()};
				nY = new int[] {new Long( Math.round(m_dbPos[1]) ).intValue(),
										new Long( Math.round(m_dbPos[1]-m_dmSize.height*Math.sin(m_db3DWinkel)) ).intValue(),
										new Long( Math.round(m_dbPos[1]+m_dmSize.height-m_dmSize.height*Math.sin(m_db3DWinkel)) ).intValue(),
										new Long( Math.round(m_dbPos[1]+m_dmSize.height) ).intValue()};
				p_G.fillPolygon( nX, nY, 4 );
				//break;
			//}
			//case 3: {
				p_G.setColor( m_clColor );
				nX = new int[] {new Long( Math.round(m_dbPos[0]) ).intValue(),
										new Long( Math.round(m_dbPos[0]+m_dmSize.width*Math.cos(m_db3DWinkel)) ).intValue(),
										new Long( Math.round(m_dbPos[0]+m_dmSize.width+m_dmSize.width*Math.cos(m_db3DWinkel)) ).intValue(),
										new Long( Math.round(m_dbPos[0]+m_dmSize.width) ).intValue()};
				nY = new int[] {new Long( Math.round(m_dbPos[1]) ).intValue(),
										new Long( Math.round(m_dbPos[1]-m_dmSize.height*Math.sin(m_db3DWinkel)) ).intValue(),
										new Long( Math.round(m_dbPos[1]-m_dmSize.height*Math.sin(m_db3DWinkel)) ).intValue(),
										new Long( Math.round(m_dbPos[1]) ).intValue()};
				p_G.fillPolygon( nX, nY, 4 );
				//break;
			//}
		//} //end switch
	} //end paint
} //end Pkt3D
