package doc_gui;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import doc.DatabaseOfGroupedObjects;
import doc.mathobjects.Grouping;

public class DatabasePanel extends JPanel{
	
	private DatabaseOfGroupedObjects database;
	private DocViewerPanel docPanel;
    private JList list;
    private DefaultListModel listModel;
	
	public DatabasePanel(DatabaseOfGroupedObjects database, DocViewerPanel docPanel){
		this.database = database;
		this.docPanel = docPanel;
		this.setLayout(new GridBagLayout());
		
        listModel = new DefaultListModel();
        for (Grouping g : database.getGroupings()){
        	listModel.addElement(g.getName());
        }
 
        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setSelectedIndex(0);
//        list.addListSelectionListener(makeListListener());
        list.setVisibleRowCount(5);
        JScrollPane scrollPane = new JScrollPane(list);
        
        OCButton generate = new OCButton("Generate List",
        		"Generate a list of problems, using the problem formulas selected above",
        		0, 1, 0, 1, this);
        
        GridBagConstraints pCon = new GridBagConstraints();
        
        pCon.fill = GridBagConstraints.BOTH;
        pCon.weightx = 1;
        pCon.gridx = 0;
        pCon.gridy = 0;
		add(scrollPane, pCon);
	}
	
//	private ListSelectionListener makeListListener(){
//		
//	}
	
}
