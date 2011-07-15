/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import tree.EvalException;
import tree.ParseException;
import tree.ValStorageGroup;
import tree.Expression;
import tree.ValueNotStoredException;
import tree.ValueStorage;
import tree.Constant;
import tree.Number;
import tree.Var;

public class ValStoragePanel extends SubPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5863708616289679710L;
	
	private MainApplet mainApp;
	private OCButton varButton, makeVar;
	private ValueStorage elements;
	private SubPanel groups, elmButtons, addElm;
	private OCTextField name, groupName, value;
	private OCLabel elmName, val, group;
	private ArrayList<JCheckBox> groupSelect;
	private int i;


	public ValStoragePanel(final MainApplet mainApp, TopLevelContainerOld topLevelComp, ValueStorage e) {
		super(topLevelComp);
		this.mainApp = mainApp;
		elements = e;
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		groups = new SubPanel(getTopLevelContainer());
		//groups.setBorder(BorderFactory.createTitledBorder("Groups"));
		elmButtons = new SubPanel(getTopLevelContainer());
		addElm = new SubPanel(getTopLevelContainer());
		//addElm.setBorder(BorderFactory.createTitledBorder("Make/Set"));
		
		elmName = new OCLabel("Name:", 1, 1, 0, 0, addElm, mainApp);
		name = new OCTextField(getTopLevelContainer(), true, 9, 1, 1, 1, 0, addElm, mainApp){
			public void associatedAction(){
				//do nothing, must hit button 
			}
		};
		
		val = new OCLabel("Val:", 1, 1, 2, 0, addElm, mainApp);
		value = new OCTextField(getTopLevelContainer(), true, 9, 1, 1, 3, 0, addElm, mainApp){
			public void associatedAction(){
				//do nothing, must hit button
			}
		};
		
		group = new OCLabel("Group:", 1, 1, 0, 1, addElm, mainApp);
		groupName = new OCTextField(getTopLevelContainer(), true, 9, 1, 1, 1, 1, addElm, mainApp){
			public void associatedAction(){
				//do nothing, must hit button
			}
		};
		
		makeVar = new OCButton("make/set", 1, 1, 3, 1, addElm, mainApp){
			public void associatedAction() throws ParseException, ValueNotStoredException, EvalException{
				name.primaryAction();
				value.primaryAction();
				groupName.primaryAction();
				String elmStr = name.getField().getText();
				String valStr = value.getField().getText();
				String groupStr = groupName.getField().getText();
				Expression newVarVal;
				try{ //evaluate the value being given to the Var
					newVarVal = mainApp.getParser().ParseExpression(valStr);
					newVarVal = newVarVal.eval();
				}
				catch(Exception e){
					groupName.getField().setText("error calculating");
					return;
				}
				if(elmStr.equals(""))
				{//if there is no value given
					if(!groupStr.equals(""))
					{//a group name was entered
						if(elements.findGroupIndex(groupStr) == Integer.MAX_VALUE)
						{//the group currently does not exist
							elements.addGroup(new ValStorageGroup(groupStr));
						}
					}
					else{
						return;
					}
				}
				else
				{
					if(groupStr.equals("") || groupStr.equals("var exists") || 
							groupStr.equals("const exists") || groupStr.equals("error calculating"))
					{//no group given, or input matches one of the messages put in group box
						groupStr = "user";//assume they want it group user
						groupName.getField().setText(groupStr);
					}
					if(elements.findGroupIndex(groupStr) == Integer.MAX_VALUE)
					{//if the group given does not exist
						elements.addGroup(new ValStorageGroup(groupStr));
					}
					
					if(mainApp.getParser().CONSTLIST.findIfStored(elmStr) != null)
					{//if the name given is already a constant
						groupName.getField().setText("constant exists");
						value.getField().setText("no reassignment");
						return;
					}
					if(mainApp.getParser().VARLIST.findIfStored(elmStr) != null)
					{//if var exists, cannot be moved to another group
						groupName.getField().setText("var exists");
							value.getField().setText(newVarVal.toString());
							mainApp.getParser().VARLIST.setVarVal(elmStr, (Number) newVarVal);
							mainApp.getGraphObj().repaint();
							mainApp.getGridProps().refreshAttributes();
						return;
					}
					
					if(elements.getTypeStorage() == 1)
					{//if this panel is for Variables
						value.getField().setText(newVarVal.toString());
						elements.storeInGroup(groupStr, new Var(elmStr, (Number) newVarVal));
						mainApp.getGraphObj().repaint();
						mainApp.getGridProps().refreshAttributes();
					}
					else if(elements.getTypeStorage() == 2)
					{//if this panel is for Constants
							value.getField().setText(newVarVal.toString());
							elements.storeInGroup(groupStr, new Constant(elmStr, (Number) newVarVal));
						}
						else{
							value.getField().setText("needs val");
						}
					}
				refreshButtons();
				}
			};
		
		groupSelect = new ArrayList<JCheckBox>();
		
		for(i = 0; i < elements.getGroups().size(); i++){
			final int tempInt = i;
			String name = e.findGroupName(i);
			groupSelect.add(tempInt, new JCheckBox(name));
			groupSelect.get(tempInt).setSelected(true);
			groupSelect.get(tempInt).addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (e.getStateChange() == ItemEvent.SELECTED){
						elements.getGroups().get(tempInt).setSelected(true);
						refreshButtons();
					}
					else if (e.getStateChange() == ItemEvent.DESELECTED){
						elements.getGroups().get(tempInt).setSelected(false);
						refreshButtons();
					}
				}
			});
			GridBagConstraints bCon = new GridBagConstraints();
			bCon.fill = GridBagConstraints.BOTH;
			bCon.weightx = 1;
			bCon.weighty = 1;
			bCon.gridheight = 1;
			bCon.gridwidth = 1;
			bCon.gridx = i;
			bCon.gridy = 0;
			groups.add(groupSelect.get(i), bCon);
		}
		
		GridBagConstraints bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = .5;
		bCon.weighty = 1;
		bCon.gridheight = 1;
		bCon.gridwidth = 1;
		bCon.gridx = 0;
		bCon.gridy = 0;
		this.add(addElm, bCon);
		bCon.gridy = 1;
		this.add(groups, bCon);
		bCon.gridheight = 1;
		bCon.weightx = 1;
		bCon.weighty = 1;
		bCon.gridx = 0;
		bCon.gridy = 2;
		this.add(elmButtons, bCon);
		refreshButtons();
	}
	
	public void refreshButtons() { 
		
		groups.removeAll();
		groupSelect = new ArrayList<JCheckBox>();
		for(i = 0; i < elements.getGroups().size(); i++){
			final int tempInt = i;
			String name = elements.findGroupName(i);
			groupSelect.add(tempInt, new JCheckBox(name, elements.getGroups().get(i).isSelected()));
			groupSelect.get(tempInt).addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (e.getStateChange() == ItemEvent.SELECTED){
						elements.getGroups().get(tempInt).setSelected(true);
						refreshButtons();
					}
					else if (e.getStateChange() == ItemEvent.DESELECTED){
						elements.getGroups().get(tempInt).setSelected(false);
						refreshButtons();
					}
				}
			});
			GridBagConstraints bCon = new GridBagConstraints();
			bCon.fill = GridBagConstraints.BOTH;
			bCon.weightx = 1;
			bCon.weighty = 1;
			bCon.gridheight = 1;
			bCon.gridwidth = 1;
			bCon.gridx = i;
			bCon.gridy = 0;
			groups.add(groupSelect.get(i), bCon);
		}
		
		int i = 0;
		int buttonPaneHeight = 0;
		elmButtons.removeAll();
		String last = elements.getFirstStored();
		for (i = 0; last != null; i++){
			new OCButton(last, 1, 1, i%3, i/3, elmButtons, mainApp);
			last = elements.getNextStored();
		}
		
		if (i%3 == 0)
			buttonPaneHeight = ((i/3) - 1) * 33;
		else
			buttonPaneHeight = i/3 * 33;
		
		elmButtons.setPreferredSize(new Dimension(280, buttonPaneHeight));
		this.setPreferredSize(new Dimension(280, buttonPaneHeight + 100));
		
		elmButtons.revalidate();
		elmButtons.repaint();
	}
}
