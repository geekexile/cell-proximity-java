package proximity;

//KNOWN BUG: There's an issue with dealing 
//with modifying the upper-left token (Position 0)
//- Need to troubleshoot.  BLF

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class GameMIDlet extends MIDlet {

	MyGameCanvas gCanvas;
	
	public GameMIDlet() {
		gCanvas = new MyGameCanvas();
	}

	public void startApp() {
		Display display = Display.getDisplay(this);

		gCanvas.start();
		
		display.setCurrent(gCanvas);
        
		
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
			
	}
}
