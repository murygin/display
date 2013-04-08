package display.test;

import java.applet.Applet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Event;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Color;
//import java.awt.event.KeyEvent;

import java.util.Vector;

import display.base.*;


public class Prompt extends Applet implements Runnable {

	Dimension offDimension;
	Image offImage;
	Graphics offGraphics;

	Thread animation;
	boolean m_bIsRunning;

  private int m_nAnimationSpeed = 10;

  private int m_nSpace;
  private Point m_ptTextPos = new Point( 2,2 );

  String m_sURL;

  Vector m_vecText;
  Text m_Prompt = new Text( "_", new Dimension( 5,5 ) );
  Text m_ForkBerlin = new Text( "fork # berlin", new Dimension( 5,5 ) );
  boolean m_bPromtMode=false;
  boolean m_bReady=false;
  boolean m_bMouseMode=false;

	Color m_colDisplay;

  private int m_nArc;


	//---------------------------------------------------------

	public void init() {
    setBackground( Color.black );
    Pkt.setDisplaySize( size() );
    m_vecText = new Vector();
    setSpace( 5 );
    m_colDisplay= new Color( new Integer( getParameter( "bgR" ) ).intValue(),
														 new Integer( getParameter( "bgG" ) ).intValue(),
														 new Integer( getParameter( "bgB" ) ).intValue() );
    if( getParameter( "speed" )!=null )
      Pkt.setSpeed( new Double( getParameter( "speed" ) ).doubleValue() );
    if( getParameter( "timeout" )!=null )
      setAnimationSpeed( new Integer( getParameter( "timeout" ) ).intValue() );
    if( getParameter( "arc" )!=null )
      setArc( new Integer( getParameter( "arc" ) ).intValue() );
    m_Prompt.setPosition( new Point( getTextWidth(),
                                     3 ));
    m_ForkBerlin.setPosition( new Point( Math.round( (size().width - m_ForkBerlin.getSize().width) / 2 ),
																		     Math.round( size().height/2 ) - Math.round( m_ForkBerlin.getSize().height/2 ) ) );
    m_Prompt.setAllPoints();
	} //end of void init

  //---------------------------------------------------------

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

  public int getSpace() {
    return m_nSpace;
  }
  public void setSpace( int p_nSpace ) {
    m_nSpace = p_nSpace;
  }

  public boolean isPromtMode() {
    return m_bPromtMode;
  }
  public void setPromtMode( boolean p_bNewPromtMode ) {
    m_bPromtMode=p_bNewPromtMode;
  }

  public boolean isMouseMode() {
    return m_bMouseMode;
  }
  public void setMouseMode( boolean p_bMouseMode ) {
    m_bMouseMode=p_bMouseMode;
  }

  public boolean isReady() {
    return m_bReady;
  }
  public void setReady( boolean p_bReady ) {
    m_bReady=p_bReady;
  }

	//---------------------------------------------------------

  public void start() {
	} //end of void start


	public void stop() {
		animation=null;
	} //end of void stop

	public void run() {
		int nds=20;
    setPromtMode( false );
    repaint();
    boolean bReady=false;
    while( !bReady ) {
      bReady=true;
      for( int i=0; i<m_vecText.size(); i++ ) {
        if( !((Text) m_vecText.elementAt( i )).nextPosition() )
          bReady = false;
      }
      if( isMouseMode() ) {
        if( !m_ForkBerlin.nextPosition() )
          bReady = false;
      }
      repaint();
      try {
					Thread.sleep( getAnimationSpeed() );
      }
      catch( InterruptedException ie ) {}
    }
    if( isReady() ) {
      try {
					Thread.sleep( 2000 );
      }
      catch( InterruptedException ie ) {}
      m_vecText.removeAllElements();
      Text currText = new Text( "mouse mode is on", new Dimension( 5,5 ) );
      currText.setPosition( new Point( getTextWidth(), m_ptTextPos.y ));
      currText.setPaintStatus( true );
      currText.setAllPointsToX( m_ptTextPos.x );
      m_vecText.addElement( currText );
      animation=null;
      animation = new Thread( this );
      animation.setPriority( 3 );
      animation.start();
      setMouseMode( true );
      setReady( false );
    }

    m_Prompt.setPosition( new Point( getTextWidth(), 3 ));
    System.out.println( getTextWidth() );
    m_Prompt.setAllPoints();
    setPromtMode( true );
    while( isPromtMode() ) {
      m_Prompt.setPaintStatus( !m_Prompt.getPaintStatus() );
      repaint();
      try {
					Thread.sleep( 654 );
      }
      catch( InterruptedException ie ) {}
    }

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

    for( int i=0; i<m_vecText.size(); i++ ) {
      if( m_vecText.elementAt( i )!=null )
        ((Text) m_vecText.elementAt( i )).paint( offGraphics );
    }
    m_Prompt.paint( offGraphics );
    if( isMouseMode() )
      m_ForkBerlin.paint( offGraphics );

		g.drawImage(offImage, 0, 0, this);

	} //end of void update

  //---------------------------------------------------------

  private int getTextWidth() {
    return getTextWidth( m_vecText.size() );
  }

  private int getTextWidth( int p_nNumber ) {
    int nWidth = 0;
    if( p_nNumber > m_vecText.size() )
      p_nNumber = m_vecText.size();
    for( int i=0; i<p_nNumber; i++ )
      nWidth += ( ((Text) m_vecText.elementAt( i )).getSize().width + getSpace() );
    return nWidth + m_ptTextPos.x;
  }

	//---------------------------------------------------------

	public boolean mouseMove(Event evt, int x, int y) {
    if( m_bMouseMode )
			m_ForkBerlin.addPunkt( new Point( x,y ), Color.black );
		return true;
	}

  public boolean mouseDrag(Event evt, int x, int y) {
    if( animation==null ) {
      animation = new Thread( this );
      animation.setPriority( 3 );
      animation.start();
    }
    return true;
  }

  public boolean mouseDown(Event evt, int x, int y) {
    mouseDrag( evt, x , y );
    return true;
  }

  public boolean keyDown( Event p_evt, int p_nKeycode ) {
    if( p_nKeycode==Event.ENTER ) {
      m_vecText.removeAllElements();
      Text currText = new Text( "willkommen", new Dimension( 5,5 ) );
      currText.setPosition( new Point( getTextWidth(), m_ptTextPos.y ));
      currText.setPaintStatus( true );
      currText.setAllPointsToX( m_ptTextPos.x );
      m_vecText.addElement( currText );
      setReady( true );
      /*Austauch der eingegebene zeichen durch sEnter
      for( int i=0; i<m_vecText.size(); i++ ) {
        Text currText = ((Text) m_vecText.elementAt(i));
        System.out.println( currText.getText() );
        Text newText = new Text( new Character( sEnter.charAt(i) ).toString(), new Dimension( 5,5 ) );
        newText.setPosition( currText.getPosition() );
        newText.setCurrNumberOfPoints( 0 );
        newText.setPaintStatus( true );
        newText.switchTo( currText );
        m_vecText.setElementAt( newText, i );
        for( int k=i; k<m_vecText.size(); k++ ) {
          ((Text) m_vecText.elementAt( k )).setPosition( new Point( getTextWidth(k), m_ptTextPos.y ) );
          //((Text) m_vecText.elementAt( k )).setAllPoints();
        }
      }
      */
    }
    else if( p_nKeycode==Event.BACK_SPACE ) {
      m_vecText.removeElementAt( m_vecText.size()-1 );
      m_Prompt.setPosition( new Point( getTextWidth(), m_ptTextPos.y ));
      m_Prompt.setAllPoints();
    }
    else {
      Text currText = new Text( new Character( (char) p_nKeycode ).toString(), new Dimension( 5,5 ) );
      currText.setPosition( new Point( getTextWidth(), m_ptTextPos.y ));
      currText.setPaintStatus( true );
      currText.setAllPointsToY( m_Prompt.getSize().height+m_ptTextPos.y );
      m_vecText.addElement( currText );
    }
    if( isPromtMode() || animation==null ) {
        animation=null;
        animation = new Thread( this );
        animation.setPriority( 3 );
        animation.start();
    }
    return true;
  }


} //end of class Berlin
