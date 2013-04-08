package display.base;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Component;

import java.util.Vector;

/**
* Klasse Display stellt ein Dispaly dar. Das Display kann
* abgeleitet von <code>java.awt.Component</code>
*/
public class Display extends Component {

	protected Vector m_vecText;

	public Display() {
		super();
	}

  public Display( Dimension p_dimSize,
									Color p_clBgdColor ) {
		super();
    m_vecText = new Vector();
		resize( p_dimSize.width,  p_dimSize.height );
		Pkt.setDisplaySize( size() );
		setBackground( p_clBgdColor );
		setPktSpeed( 5 );
	}

	public Display( Dimension p_dimSize,
									Color p_clBgdColor,
									double p_dbSpeed ) {
		super();
    m_vecText = new Vector();
		resize( p_dimSize.width,  p_dimSize.height );
		Pkt.setDisplaySize( size() );
		setBackground( p_clBgdColor );
		setPktSpeed( p_dbSpeed );
	}

	public void setPktSpeed( double p_dbSpeed ) {
		Pkt.setSpeed( p_dbSpeed );
	}

	public void addTextElement( String p_sText,
															Dimension p_dimSize,
															Point p_ptPos) {
		m_vecText.addElement( new Text( p_sText,
																		p_dimSize ) );
		getLastTextElement().setPosition( p_ptPos );
		getLastTextElement().setPaintStatus( false );
	} //end addText

	public Text getTextElement( int p_nIndex ) {
		return (Text) m_vecText.elementAt( p_nIndex );
	}

	public Text getLastTextElement() {
		return (Text) m_vecText.lastElement();
	}

  public boolean nextPosition() {
    boolean bReady=true;
    for( int i=0; i<m_vecText.size(); i++ )
      if( !getTextElement( i ).nextPosition() )
						bReady=false;
    return bReady;
  }

	public void paint( Graphics p_G ) {
		for( int i=0; i<m_vecText.size(); i++ )
			if( getTextElement( i ).getPaintStatus() )
				getTextElement( i ).paint( p_G );
	}
} //end class Display




