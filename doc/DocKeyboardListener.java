package doc;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

public class DocKeyboardListener implements KeyListener{
	
	private Vector<Integer> keysPressed;
	
	private DocViewerPanel docPanel;										
	
	public DocKeyboardListener(DocViewerPanel docPanel){
		this.docPanel = docPanel;
		keysPressed = new Vector<Integer>();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		keysPressed.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		keysPressed.remove(e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_DELETE){
			//check if object selected
			
			//check if page selected
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
