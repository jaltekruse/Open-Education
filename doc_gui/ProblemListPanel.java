package doc_gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import doc.attributes.StringAttribute;
import doc.mathobjects.Grouping;

public class ProblemListPanel extends JPanel {

	private String[] documents = { "Teacher Mode Tutorial",
			"Problem Generation Tutorial", "Student Mode Tutorial",
			"Factoring", "Parabola Graphs", "Sine Graphs", "Proportions",
			"Linear Graphs", "Word Problems", "Random Quiz" };
	private JList list;
	private NotebookPanel notebookPanel;
	private static final String LOW = "low", AVERAGE = "average", HIGH = "high";
	private static final String[] frequencies = { LOW, AVERAGE, HIGH };
	private JPanel problemList;
	private JScrollPane scrollPane;

	public ProblemListPanel(NotebookPanel notebook) {
		notebookPanel = notebook;
		this.addComponentListener(new ComponentListener(){
			@Override
			public void componentHidden(ComponentEvent arg0) {}
			@Override
			public void componentMoved(ComponentEvent arg0) {}
			@Override
			public void componentResized(ComponentEvent arg0) {
				problemList.setPreferredSize(new Dimension(scrollPane.getViewport().getWidth(),
						scrollPane.getViewport().getHeight()));
				problemList.revalidate();
			}
			@Override
			public void componentShown(ComponentEvent arg0) {}
		});
		
		problemList = createPanelForProblems();
		setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.gridx = 0;
		con.gridy = 0;
		con.fill = GridBagConstraints.HORIZONTAL;
		con.weightx = 1;
		con.weighty = .01;
		con.gridwidth = 2;
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JLabel label = new JLabel("<html>This menu allows you to generate lists " +
				"of problems on your documents.<html>");
		label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		add(label , con);
		con.gridy++;
		label = new JLabel("<html>Select one or more of the formulas, " +
				"then specify how frequently you would like each to appear.<html>");
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		add(label, con);
		con.gridy++;

		con.weightx = .1;
		con.gridy++;
		con.gridx = 0;
		con.gridwidth = 1;
		add(new JLabel("Search"), con);
		JTextField field = new JTextField();
		con.weightx = 1;
		con.gridx = 1;
		con.insets = new Insets(0, 10, 0, 5);
		add(field, con);

		con.fill = GridBagConstraints.BOTH;
		con.weighty = 1;
		con.gridx = 0;
		con.gridwidth = 2;
		con.gridy++;
		con.insets = new Insets(0, 0, 0, 0);
		scrollPane = new JScrollPane(problemList);
//		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add( scrollPane, con);
		revalidate();
	}

	public JPanel createPanelForProblems() {
		JPanel panel = new JPanel();
		GridBagConstraints con = new GridBagConstraints();
		JLabel label;
		float fontSize;
		JComboBox frequencyChoices;
		JCheckBox checkbox;
		Component[] othersInRow = new Component[2];;
		
		int numProblemsToShow = notebookPanel.getDatabase().getGroupings().size();
		if (numProblemsToShow > 20) {// limit the number of problems in the list
			// to 20 at a time
			numProblemsToShow = 20;
		}
		con.gridy = 0;
		for ( final Grouping g : notebookPanel.getDatabase().getGroupings()) {
			panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
			panel.setLayout(new GridBagLayout());
			
			// add checkbox
			con.fill = GridBagConstraints.HORIZONTAL;
			con.gridx = 0;
			con.weightx = .01;
			checkbox = new JCheckBox();
			checkbox.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
				}
			});
			frequencyChoices = new JComboBox(frequencies);
			panel.add(checkbox, con);
			othersInRow[0] = checkbox;
			othersInRow[1] = frequencyChoices;		
			
			con.weightx = 1;
			con.gridx = 1;
			panel.add(new ProblemDescriptionPanel(g, othersInRow), con);
			
			// add frequency selection menu
			con.gridx = 2;
			con.weightx = .01;
			con.insets = new Insets(0, 5, 0, 5);
			frequencyChoices.setSelectedItem(AVERAGE);
			panel.add(frequencyChoices, con);
			
			con.gridy++;
		}
		return panel;
	}
	
	private class ProblemDescriptionPanel extends JPanel{
		
		private int height;
		private Grouping group;
		private Component[] othersInRow;
		
		
		public ProblemDescriptionPanel(final Grouping group, final Component[] othersInRow){
			this.group = group;
			this.othersInRow = othersInRow;
			height = 50;
			GridBagConstraints con = new GridBagConstraints();
			JLabel label;
			float fontSize;
			JComboBox frequencyChoices;
			JCheckBox checkbox;
			
			this.setLayout(new GridBagLayout());
			
			// add problem name label
			label = new JLabel(group.getName());
			fontSize = label.getFont().getSize();
			label.setHorizontalTextPosition(JLabel.LEFT);
			label.setVerticalTextPosition(JLabel.BOTTOM);
			con.fill = GridBagConstraints.HORIZONTAL;
			con.gridheight = 1;
			con.gridx++;
			con.weightx = 1;
			this.add(label, con);
			
			// add problem author label
			con.gridx++;
			con.weightx = .3;
			label = new JLabel(group.getAuthor());
			label.setFont(label.getFont().deriveFont( fontSize * 0.8f));
			label.setHorizontalTextPosition(JLabel.RIGHT);
			label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
			this.add(label, con);
			
			// add problem tags label
			con.gridx = 0;
			con.gridy = 1;
			con.gridwidth = 2;
			String tags = "Tags: ";
			int i = 0;
			for (   ; i < group.getTags().getValues().size() - 1; i++){
				tags += group.getTags().getValue(i).getValue() + ", ";
			}
			if ( group.getTags().getValues().size() > 0){
				tags += group.getTags().getValue(i).getValue();
			}
			label = new JLabel(tags);
			label.setFont(label.getFont().deriveFont( fontSize * 0.8f));
			label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
			this.add(label, con);
		}
		
		@Override
		public Dimension getPreferredSize(){
			int width = scrollPane.getViewport().getWidth();
			for (Component comp : othersInRow){
				width -= comp.getWidth();
			}
			return new Dimension( width, height );
		}
		
		@Override
		public Dimension getMinimumSize(){
			int width = scrollPane.getViewport().getWidth();
			for (Component comp : othersInRow){
				width -= comp.getWidth();
			}
			return new Dimension( width, height );
		}
		
	}
}
