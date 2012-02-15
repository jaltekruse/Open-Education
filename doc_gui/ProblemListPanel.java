package doc_gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Scrollable;

import doc.mathobjects.ProblemGenerator;

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
	private Vector<ProblemGenerator> selectedProblems;
	private Vector<Integer> selectedFrequencies;
	private Vector<ProblemGenerator> problemsMatchingSearch;

	public ProblemListPanel(NotebookPanel notebook) {
		notebookPanel = notebook;
		selectedProblems = new Vector<ProblemGenerator>();
		selectedFrequencies = new Vector<Integer>();
		addComponentListener(new ComponentListener(){
			@Override
			public void componentHidden(ComponentEvent arg0) {}
			@Override
			public void componentMoved(ComponentEvent arg0) {}
			@Override
			public void componentResized(ComponentEvent arg0) {
				adjustProblemList();
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
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getVerticalScrollBar().setUnitIncrement(12);
		problemList.revalidate();
		add( scrollPane, con);
		con.gridy++;
		con.weighty = 0;
		con.fill = GridBagConstraints.NONE;
		con.anchor = GridBagConstraints.LINE_END;
		con.insets = new Insets(5, 5, 0, 0);
		JButton generationButton = new JButton("Generate Probelms");
		add(generationButton, con);
		generationButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent ev) {
				if ( selectedProblems.size() == 0){
					JOptionPane.showMessageDialog(null,
							"No problems are selected.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int num = getNumberFromUser();
				if ( num == -1){// user cancelled the action
					return;
				}
				String directions = selectedProblems.get(0).getDirections();
				if (  selectedProblems.size() > 1)
				{// there is more than one problem, check to make sure they
					// have the same directions, or prompt the user for a
					// new set of directions that applies to all of them.
					boolean directionsMatch = true;
					for ( ProblemGenerator gen : selectedProblems){
						if ( ! gen.getDirections().equalsIgnoreCase(directions)){
							directionsMatch = false;
							break;
						}
					}
					if ( ! directionsMatch){
						// prompt user for new set of directions
						directions = promtForDirections(selectedProblems);
						if ( directions == null){
							return;
						}
					}
				}
				notebookPanel.getCurrentDocViewer().getDoc().generateProblems(
						selectedProblems, selectedFrequencies, num, directions);
				notebookPanel.getCurrentDocViewer().addUndoState();
			}
			
		});
	}

	public JPanel createPanelForProblems() {
		ProblemList panel = new ProblemList();
		GridBagConstraints con = new GridBagConstraints();
		JComboBox frequencyChoices;
		JCheckBox checkbox;
		Component[] othersInRow = new Component[2];;
		
		int numProblemsToShow = notebookPanel.getDatabase().getAllProblems().size();
		if (numProblemsToShow > 20) {// limit the number of problems in the list
			// to 20 at a time
			numProblemsToShow = 20;
		}
		con.gridy = 0;
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		panel.setLayout(new GridBagLayout());
		for ( final ProblemGenerator g : notebookPanel.getDatabase().getAllProblems()) {
		
			// add checkbox
			con.fill = GridBagConstraints.NONE;
			con.gridx = 0;
			con.weightx = 0;
			con.insets = new Insets(0,0,0,0);
			checkbox = new JCheckBox();
			checkbox.addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent ev) {
					if ( ev.getStateChange() == ItemEvent.SELECTED){
						selectedFrequencies.add(10);
						selectedProblems.add(g);
					}
					else{
						selectedFrequencies.remove(selectedProblems.indexOf(g));
						selectedProblems.remove(g);
					}
				}
				
			});
			frequencyChoices = new JComboBox(frequencies);
			panel.add(checkbox, con);
			othersInRow[0] = checkbox;
			othersInRow[1] = frequencyChoices;		
			
			con.fill = GridBagConstraints.HORIZONTAL;
			con.insets = new Insets(0,5,0,0);
			con.weightx = 1;
			con.gridx = 1;
			panel.add(new ProblemDescriptionPanel(g, othersInRow), con);
			
			// add frequency selection menu
			con.fill = GridBagConstraints.NONE;
			con.gridx = 2;
			con.weightx = 0;
			con.insets = new Insets(0, 5, 0, 5);
			frequencyChoices.setSelectedItem(AVERAGE);
			panel.add(frequencyChoices, con);
			
			con.gridy++;
		}
		return panel;
	}
	
	public void adjustProblemList(){
		problemList.setPreferredSize(new Dimension(scrollPane.getViewport().getWidth(),
				problemList.getHeight()));
		problemList.revalidate();
	}
	
	public String promtForDirections(Vector<ProblemGenerator> generators){
		final JComponent[] inputs = new JComponent[generators.size() + 3];
		inputs[0] = new JLabel("<html>The directions form the selected problems are different.<br>" +
				"Enter a set of directions suitable for all of the problems below,<br>" +
				"each individual problem's directions are listed for reference.<html>");
		int index = 1;
		for ( ProblemGenerator gen : generators){
			inputs[index] = new JLabel("<html><font size=-2>" + gen.getDirections() + "</font></html");
			index++;
		}
		inputs[index] = new JLabel("Directions for list of problems");
		index++;
		JTextArea directions = new JTextArea(3,10);
		inputs[index] = new JScrollPane(directions);
		directions.setWrapStyleWord(true);
		directions.setLineWrap(true);
		while (true){
			int input = JOptionPane.showConfirmDialog(notebookPanel, 
					inputs, "Enter Directions", JOptionPane.PLAIN_MESSAGE);
			if ( input == -1){
				return null;
			}
			if ( ! directions.getText().equals("")){
				return directions.getText();
			}
		}
	}
	
	private class ProblemList extends JPanel implements Scrollable{
		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return null;
		}
		@Override
		public int getScrollableBlockIncrement(Rectangle arg0, int arg1,
				int arg2) { return 16;}
		@Override
		public boolean getScrollableTracksViewportHeight() { return false;}
		@Override
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}
		@Override
		public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2)
		{return 0;}	
	}
	
	private int getNumberFromUser(){
		int number = 0;
		do {
			String num = (String)JOptionPane.showInputDialog(
					null,
					"Number of problems",
					"Number of problems.",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					null);
			if (num == null)
			{// the user hit cancel or the exit button on the dialog
				return -1;
			}
			try{
				number = Integer.parseInt(num);
				if (number < 1 || number > 50){
					JOptionPane.showMessageDialog(null,
							"Input must be an integer between 1 and 50",
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch ( Exception e){
				JOptionPane.showMessageDialog(null,
						"Input must be an integer between 1 and 50",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} while ( number < 1 || number > 50);
		return number;
	}
	
	private class ProblemDescriptionPanel extends JPanel{
		
		private int height;
		private ProblemGenerator problem;
		private Component[] othersInRow;
		
		
		public ProblemDescriptionPanel(final ProblemGenerator problem, final Component[] othersInRow){
			this.problem = problem;
			this.othersInRow = othersInRow;
			height = 50;
			GridBagConstraints con = new GridBagConstraints();
			JLabel label;
			float fontSize;
			JComboBox frequencyChoices;
			JCheckBox checkbox;
			
			this.setLayout(new GridBagLayout());
			
			// add problem name label
			label = new JLabel(problem.getName());
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
			con.weightx = .1;
			con.fill = GridBagConstraints.NONE;
			con.anchor = GridBagConstraints.LINE_END;
			label = new JLabel(problem.getAuthor());
			label.setFont(label.getFont().deriveFont( fontSize * 0.8f));
			label.setHorizontalTextPosition(JLabel.RIGHT);
			label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
			this.add(label, con);
			
			// add problem tags label
			con.fill = GridBagConstraints.HORIZONTAL;
			con.gridx = 0;
			con.gridy = 1;
			con.gridwidth = 2;
			con.anchor = GridBagConstraints.LINE_START;
			String tags = "Tags: ";
			int i = 0;
			for (   ; i < problem.getTags().getValues().size() - 1; i++){
				tags += problem.getTags().getValue(i).getValue() + ", ";
			}
			if ( problem.getTags().getValues().size() > 0){
				tags += problem.getTags().getValue(i).getValue();
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
				width -= comp.getPreferredSize().getWidth();
			}
			// subtract a bit of extra space for borders
			width -= 40;
			return new Dimension( width, height );
		}
		
		@Override
		public Dimension getMinimumSize(){
			int width = scrollPane.getViewport().getWidth();
			for (Component comp : othersInRow){
				width -= comp.getMinimumSize().getWidth();
			}
			// subtract a bit of extra space for borders
			width -= 40;
			return new Dimension( width, height );
		}
		
	}
}
