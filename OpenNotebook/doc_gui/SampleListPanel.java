package doc_gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class SampleListPanel extends JPanel{

	private String[] documents = { "teacher_overview", "Teacher Mode Tutorial", "Problem Generation Tutorial",
			"Student Mode Tutorial", "Factoring", "Parabola Graphs",
			"Sine Graphs", "Proportions", "Linear Graphs",
			"Word Problems", "Random Quiz"};
	private JList list;
	private NotebookPanel notebookPanel;
	
	public SampleListPanel(NotebookPanel notebook){
		// TODO - stopwatch and logging
//		System.out.println("start sample:" + (new java.util.Date().getTime() - notebook.getOpenNotebook().timeAtStart));
		this.notebookPanel = notebook;
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(new JLabel("Select a document below to open."));
		this.add(Box.createRigidArea(new Dimension(0,5)));
		list = new JList(documents);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(list);
		this.add(scrollPane);
		new OCButton("Open", 0, 0, 0, 0, this){
			public void associatedAction(){
				if ( list.getSelectedIndex() >= 0){
					notebookPanel.openSample(documents[list.getSelectedIndex()] + ".mdoc");
					notebookPanel.setSampleDialogVisible(false);
				}
			}
		};
//		System.out.println("end sample constructor:" + (new java.util.Date().getTime() - notebook.getOpenNotebook().timeAtStart));
	}
}
