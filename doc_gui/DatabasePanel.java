package doc_gui;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import doc.DatabaseOfGroupedObjects;
import doc.mathobjects.Grouping;

public class DatabasePanel extends JPanel{
	
	DatabaseOfGroupedObjects database;
	
	 DocViewerPanel docPanel;
	
	public DatabasePanel(DatabaseOfGroupedObjects database, DocViewerPanel docPanel){
		this.database = database;
		this.docPanel = docPanel;
		this.setLayout(new GridBagLayout());
		
		Vector<Grouping> members = database.getGroupings();
		System.out.println("num items in database: " + members.size());
		
        Vector<String> columnNames = new Vector<String>();
        Vector<Vector<String>> data = new Vector<Vector<String>>();
        
        columnNames.add("Problem Name");
        columnNames.add("Tags");
        
        for (Grouping g : members){
        	Vector<String> singleRowData = new Vector<String>();
        	singleRowData.add(g.getName());
        	singleRowData.add(g.getTags());
        	data.add(singleRowData);
        }

        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(200, 70));
        table.setFillsViewportHeight(true);
        
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        
        OCButton generate = new OCButton("Generate List",
        		"Generate a list of problms, using the problem formulas selected above",
        		0, 1, 0, 1, this);
        
        GridBagConstraints pCon = new GridBagConstraints();
        
        pCon.fill = GridBagConstraints.BOTH;
        pCon.weightx = 1;
        pCon.gridx = 0;
        pCon.gridy = 0;
		add(scrollPane, pCon);
	}
	
	
}
