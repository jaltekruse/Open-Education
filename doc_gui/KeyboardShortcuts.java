package doc_gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class KeyboardShortcuts {

	public static void addKeyboardShortcuts(final NotebookPanel notebookPanel){
		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE")
				, "delete");
		notebookPanel.getActionMap().put("delete", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.delete();
			}
		});     

		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S")
				, "save");
		notebookPanel.getActionMap().put("save", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.save();
			}
		});  

		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control O")
				, "open");
		notebookPanel.getActionMap().put("open", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.open();
			}
		});  

		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control P")
				, "print");
		notebookPanel.getActionMap().put("print", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.print();
			}
		});
		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control C")
				, "copy");
		notebookPanel.getActionMap().put("copy", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.copy();
			}
		});  
		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control X")
				, "cut");
		notebookPanel.getActionMap().put("cut", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.cut();
			}
		});
		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control V")
				, "paste");
		notebookPanel.getActionMap().put("paste", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.paste();
			}
		});
		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control N")
				, "add page");
		notebookPanel.getActionMap().put("add page", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.addPage();
			}
		});
		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z")
				, "undo");
		notebookPanel.getActionMap().put("undo", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.undo();
			}
		});

		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y")
				, "redo");
		notebookPanel.getActionMap().put("redo", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.redo();
			}
		});
		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control EQUALS")
				, "zoomIn");
		notebookPanel.getActionMap().put("zoomIn", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.zoomIn();
			}
		});
		notebookPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control MINUS")
				, "zoomOut");
		notebookPanel.getActionMap().put("zoomOut", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.zoomOut();
			}
		});
	}
}
