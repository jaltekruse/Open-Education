package doc_gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import doc.Document;

public class NotebookMenuBar extends JMenuBar {

	JMenu mode;
	OpenNotebook openBook;
	
	public NotebookMenuBar(OpenNotebook book){
		super();
		
		openBook = book;
		JMenu file = new JMenu("File");
		this.add(file);
		JMenuItem newDoc = new JMenuItem("New Document");
		file.add(newDoc);
		newDoc.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Document tempDoc = new Document("Untitled Document");
				tempDoc.addBlankPage();
				openBook.getNotebookPanel().addDoc(tempDoc);
			}
		});
		JMenuItem open = new JMenuItem("Open");
		file.add(open);
		open.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openBook.getNotebookPanel().open();
			}
		});
		JMenuItem save = new JMenuItem("Save");
		file.add(save);
		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openBook.getNotebookPanel().save();
			}
		});
		
		JMenu edit = new JMenu("Edit");
		this.add(edit);
		
		JMenuItem addPage = new JMenuItem("Add Page");
		edit.add(addPage);
		addPage.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openBook.getNotebookPanel().addPage();
			}
		});
		
		edit.addSeparator();
		
		JMenuItem cut = new JMenuItem("Cut");
		edit.add(cut);
		cut.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openBook.getNotebookPanel().cut();
			}
		});
		
		JMenuItem copy = new JMenuItem("Copy");
		edit.add(copy);
		copy.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openBook.getNotebookPanel().copy();
			}
		});
		
		JMenuItem paste = new JMenuItem("Paste");
		edit.add(paste);
		paste.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openBook.getNotebookPanel().paste();
			}
		});
		
		JMenuItem delete = new JMenuItem("Delete");
		edit.add(delete);
		delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openBook.getNotebookPanel().delete();
			}
		});
		
		edit.addSeparator();
		
		JMenuItem sendForward = new JMenuItem("Send Forward");
		edit.add(sendForward);
		sendForward.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openBook.getNotebookPanel().sendForward();
			}
		});
		
		JMenuItem bringToFront = new JMenuItem("Bring to Front");
		edit.add(bringToFront);
		bringToFront.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openBook.getNotebookPanel().bringToFront();
			}
		});
		
		JMenuItem sendBackward = new JMenuItem("Send Backward");
		edit.add(sendBackward);
		sendBackward.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openBook.getNotebookPanel().sendBackward();
			}
		});
		
		JMenuItem bringToBack = new JMenuItem("Bring to Back");
		edit.add(bringToBack);
		bringToBack.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openBook.getNotebookPanel().bringToBack();
			}
		});
		

	
		mode = new JMenu("Mode");
		this.add(mode);

		if (openBook.isInStudentMode()){
			JMenuItem teacherMode = new JMenuItem("Teacher");
			mode.add(teacherMode);
			teacherMode.addActionListener(new ActionListener(){
	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Object[] options = {"Switch Now", "Cancel"};
					int n = JOptionPane.showOptionDialog(null,
					    "Switching modes will discard any unsaved changes.",
					    "Information may be Lost",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.WARNING_MESSAGE,
					    null,
					    options,
					    options[1]);
	
					if (n == 0){ 
						openBook.setInStudentMode(false);
					}
				}
			});
		}

		else{
			JMenuItem studentMode = new JMenuItem("Student");
			mode.add(studentMode);
			studentMode.addActionListener(new ActionListener(){
	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Object[] options = {"Switch Now", "Cancel"};
					int n = JOptionPane.showOptionDialog(null,
					    "Switching modes will discard any unsaved changes.",
					    "Information may be Lost",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.WARNING_MESSAGE,
					    null,
					    options,
					    options[1]);
	
					if (n == 0){ 
						openBook.setInStudentMode(true);
					}
				}
			});
		}
	}
}
