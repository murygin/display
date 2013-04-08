import java.applet.Applet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Event;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Color;

import java.net.URL;
import java.net.MalformedURLException;

import display.base.*;


public class Gedanken extends Applet implements Runnable {

	Dimension offDimension;
	Image offImage;
	Graphics offGraphics;

	Thread animation;
	boolean m_bIsRunning;

  private int m_nAnimationSpeed = 4;

  String m_sURL;

	Text m_Text1, m_Text2, m_Text3, m_Text4, m_DisplayedText;

	String m_sWord1, m_sWord2, m_sWord3;

	Color m_colDisplay;

  private int m_nArc;

  /*
  * die groesse der displaypunkte
  *
  * ACHTUNG:
  * beim aendern muss auch in Pkt die variable DM_SIZE
  * geaendert werden
  */
  Dimension m_dmPktSize = new Dimension( 4,4 );

	boolean m_bMouseMode;

  boolean m_bIsOneWord=false;

	//---------------------------------------------------------

	public void init() {
    setURLString( getParameter( "URL" ) );
		setBackground( Color.white );
		m_colDisplay= new Color( new Integer( getParameter( "bgR" ) ).intValue(),
														 new Integer( getParameter( "bgG" ) ).intValue(),
														 new Integer( getParameter( "bgB" ) ).intValue() );
    if( getParameter( "URL" )!=null )
      setURLString( getParameter( "URL" ) );
    if( getParameter( "speed" )!=null )
      Pkt.setSpeed( new Double( getParameter( "speed" ) ).doubleValue() );
    if( getParameter( "timeout" )!=null )
      setAnimationSpeed( new Integer( getParameter( "timeout" ) ).intValue() );
    if( getParameter( "arc" )!=null )
      setArc( new Integer( getParameter( "arc" ) ).intValue() );
    else
      setArc( 30 );
		m_bMouseMode = true;
		m_bIsRunning = true;
		Pkt.setDisplaySize( size() );
		dispalyTextInit();
	} //end of void init

	private void dispalyTextInit() {
    String sWort0, sWort1, sWort2, sWort3;
    sWort0=getParameter( "string1" );
    sWort1=getParameter( "string2" );
    sWort2=getParameter( "string3" );
    sWort3=getParameter( "string4" );
    if( sWort1.equals( "null" ) )
      m_bIsOneWord=true;
    if( sWort2.equals( "null" ) ) {
      sWort2=sWort0;
      sWort3=sWort1;
    }
    if( sWort3.equals( "null" ) )
      sWort3="_";


  	m_Text1 = new Text( sWort0, m_dmPktSize );
		m_Text1.setPosition( new Point( 1,190 ) );
		m_Text1.setPaintStatus( false );


		m_Text2 = new Text( sWort1, m_dmPktSize );
		m_Text2.setPosition( new Point( 1,190 ) );
		m_Text2.setPaintStatus( false );


		m_Text3 = new Text( sWort2, m_dmPktSize );
		m_Text3.setPosition( new Point( 1,190 ) ) ;
		m_Text3.setPaintStatus( false );

		m_Text4 = new Text( sWort3, m_dmPktSize );
		m_Text4.setPosition( new Point( 1,190 ) );
		m_Text4.setPaintStatus( false );

		m_DisplayedText = new Text();
		m_DisplayedText = m_Text1;
	} // end displayTextInit

  //---------------------------------------------------------

  public String getURLString() {
    return m_sURL;
  }
  public void setURLString( String p_sURL ) {
    m_sURL = p_sURL;
  }

  public int getAnimationSpeed() {
    return m_nAnimationSpeed;
  }
  public void setAnimationSpeed( int p_nSpeed ) {
    m_nAnimationSpeed = p_nSpeed;
  }

  public int getArc() {
    return m_nArc;
  }
  public void setArc( int p_nArc ) {
    m_nArc = p_nArc;
  }

	//---------------------------------------------------------

  public void start() {
	} //end of void start


	public void stop() {
		animation=null;
	} //end of void stop

	public void run() {
		int nds=20;
		Text oldText=new Text();
		while( !m_DisplayedText.nextPosition() ||
					 !m_DisplayedText.isComplete() ) {
			try {
				Thread.sleep( 10 );
			}
			catch( InterruptedException ie ) {}
			repaint();
		}
		m_bMouseMode = false;

    //neu: nur wenn mehrere woerter eingegeben wurden, folg die
    //animation von einem zum naechsten wort
    if( !m_bIsOneWord ) {

		try {
			Thread.sleep( nds*100 );
		}
		catch( InterruptedException ie ) {}

		while( m_bIsRunning ) {
			if( m_DisplayedText.getText().equals( m_Text1.getText() ) ) {
				oldText = m_Text1;
				m_DisplayedText = m_Text2;
				m_DisplayedText.setCurrNumberOfPoints( 0 );
				m_DisplayedText.setPaintStatus( false );
        nds=13;
			}
			else {
				if( m_DisplayedText.getText().equals( m_Text2.getText() ) ) {
					oldText = m_Text2;
					m_DisplayedText = m_Text3;
					m_DisplayedText.setCurrNumberOfPoints( 0 );
					m_DisplayedText.setPaintStatus( false );
          nds=20;
				}
				else {
					if( m_DisplayedText.getText().equals( m_Text3.getText() ) ) {
						oldText = m_Text3;
						m_DisplayedText = m_Text4;
						m_DisplayedText.setCurrNumberOfPoints( 0 );
						m_DisplayedText.setPaintStatus( false );
						nds = 30;
					}
					else {
						if( m_DisplayedText.getText().equals( m_Text4.getText() ) ) {
							oldText = m_Text4;
							dispalyTextInit();
							nds = 30;
						}
					}
				}
			}

			for( int i=0; i<oldText.getNumberOfZeichen(); i++ ) {
				Zeichen oldZeichen = oldText.getZeichen(i);
				for( int k=0; k<oldZeichen.getNumberOfPkt(); k++ )
					m_DisplayedText.addPunkt( new Point( oldZeichen.getPkt(k).getPos().x,
																							 oldZeichen.getPkt(k).getPos().y ),
																		Color.black );
			}

			int mittelindex = Math.round(oldText.getText().length() / 2);
			while( m_DisplayedText.addPunkt( new Point( Math.round( size().width/2 ),
																									Math.round( size().height/2 ) ),
																				Color.black ) ) {
			//nichts
			}

			while( !m_DisplayedText.nextPosition() ) {
				try {
					Thread.sleep( getAnimationSpeed() );
				}
				catch( InterruptedException ie ) {}
				repaint();
			}
			try {
					Thread.sleep( nds*100 );
			}
			catch( InterruptedException ie ) {}
		} //ende xmal Schleife

    } //end if( !m_bIsOneWord )

	} //end of void run

	//---------------------------------------------------------

	public void paint( Graphics g ) {
		update( g );
	}

	public void update( Graphics g ) {
		Dimension d = size();
		if ( (offGraphics == null)||
				 (d.width != offDimension.width)||
				 (d.height != offDimension.height) ) {
			offDimension = d;
			offImage = createImage( d.width, d.height );
			offGraphics = offImage.getGraphics();
		}
		//Erase the previous image.
		offGraphics.setColor( getBackground() );
		offGraphics.fillRect( 0, 0, d.width, d.height );

		offGraphics.setColor( m_colDisplay );
		offGraphics.fillRoundRect( 0, 0, size().width, size().height, getArc(), getArc() );
		offGraphics.setColor( Color.black );
		m_DisplayedText.paint( offGraphics );

		g.drawImage(offImage, 0, 0, this);

	} //end of void update

	//---------------------------------------------------------

	public boolean mouseMove(Event evt, int x, int y) {
		if( m_bMouseMode )
			m_DisplayedText.addPunkt( new Point( x,y ), Color.black );
		if( animation == null ) {
			animation = new Thread( this );
			animation.setPriority( 3 );
			animation.start();
		}
		return true;
	}

  public boolean mouseDrag(Event evt, int x, int y) {
    mouseDown( evt,x,y );
    return true;
  }

  public boolean mouseDown(Event evt, int x, int y) {
    if( getURLString()!=null ) {
      try {
        getAppletContext().showDocument( new URL( getURLString() ) );
      } catch( MalformedURLException mue ) {
        System.out.println( "Applet Berlin, MalformedURLException, URL: " +
                            getURLString() + ", message: " + mue.getMessage() );
      }
    }
    return true;
  }


} //end of class Berlin
