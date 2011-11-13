/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package tree;

import java.util.ArrayList;


/**
 * Stored elements that are associated with a name for later recall. This includes
 * variables and constants, but the system with work with anything extended from
 * <code> ElementWithIdentifer </code>. Variables are stored in an ArrayList of
 * Storage groups. This class is used to keep track of groups and search an all
 * the <code>Element</code>s of a type.
 * @author Jason
 *
 */

public abstract class ValueStorage {
	
	ArrayList<ValStorageGroup> groups;
	//typeStorage:  1- Var     2-Const
	int firstIndexWithElm, numGroupsRemaining, typeStorage;
	String nameOfLast;
	
	/**
	 * Constructor takes the associated <code>calc</code> object, as well
	 * as an int for the type of storage(Var=1 Const=2).
	 * @param basicCalc
	 * @param type
	 */
	public ValueStorage(int type){
		groups = new ArrayList<ValStorageGroup>();
		typeStorage = type;
	}
	
	public ValueStorage(){
		groups = new ArrayList<ValStorageGroup>();
	}
	
	/**
	 * Takes a string that can identify an Element. Searches all contained
	 * lists for the name, returns the object if the name is already in use
	 * otherwise returns null.
	 * @param s 
	 * @return 
	 */
	public ValueWithName findIfStored(String s){
		ValueWithName tempElm;
		for(int i = 0; i < groups.size(); i++){
			tempElm = (groups.get(i)).findIfStored(s);
			if(tempElm != null)
				return tempElm;
		}
		return null;
	}
	
	/**
	 * Takes a string of a group name and an ValueWithName. If the group
	 * does not exist, returns null. Otherwise hands back the stored
	 * ElementWithIdentifer.
	 * 
	 * @param group  name of the group to be stored in
	 * @param constant   the object to be stored
	 * @return  null if group does not exist, else the Element passed in
	 */
	public ValueWithName storeInGroup(String group, ValueWithName n){
		n.setMaster(true);
		int index = findGroupIndex(group);
		ValueWithName val = null;
		if (index != Integer.MAX_VALUE){
			val =  groups.get(index).storeElm(n);
		}
		return val;
	}
	
	/**
	 * Checks if string is in used as a Element name in a group.
	 * @param group  name of group
	 * @param var  name of identifier
	 * @return boolean - true if stored, else false
	 */
	public boolean isInGroup(String group, String elm){
		if(findGroupIndex(group) != Integer.MAX_VALUE){
			if (groups.get(findGroupIndex(group)).findIfStored(elm) != null)
				return true;
		}
			return false;
	}
	
	/**
	 * Finds the groups index in the ArrayList.
	 * @param s name of the group
	 * @return - index of group, if not created returns maximum integer value
	 */
	public int findGroupIndex(String s){
		for(int i = 0; i < groups.size(); i++){
			if(groups.get(i).getGroupName().equals(s))
				return i;
		}
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Changes the selection value of a group. Takes the group of the number passed
	 * and sets it selected or not selected. Based on current value of selection.
	 * @param index
	 */
	public void toggleSelect(int index){
		if(groups.get(index).isSelected()){
			groups.get(index).setSelected(false);
		}
		else{
			groups.get(index).setSelected(true);
		}
	}
	
	/**
	 * Finds the name of group at an index.
	 * @param index  position in list
	 * @return String - name of group
	 */
	public String findGroupName(int index){
		return groups.get(index).getGroupName();
	}
	
	/**
	 * Used to find the first term in the storage system. Groups are sorted
	 * alphabetically, so their first terms are simply called and then compared
	 * in this function.
	 * @return String - the name of the identifier
	 */
	public String getFirstStored(){
		String currFirst = null;
		firstIndexWithElm = 0;
		int index = 0, i;
		for (i = firstIndexWithElm; i < groups.size(); i++){
			if(groups.get(i).getFirst() == null){
				continue;
			}
			else if(currFirst == null){
				currFirst = groups.get(i).getFirst().getName();
				index = i;
				continue;
			}
			
			if (currFirst.compareToIgnoreCase(groups.get(i).getFirst().getName()) > 0){
				currFirst = groups.get(i).getLastReturned().getName();
				index = i;
			}
		}
		groups.get(index).getNext();
		return currFirst;
	}
	
	/**
	 * Used to find the next Element in the storage system. Should be called after the
	 * <code>getFirstStored</code> method. Will return objects until the last object has
	 * already been returned, then returns null.
	 * @return String - name of nest element, null after last has already been returned
	 */
	public String getNextStored(){
		int i;
		int nextIndex = 0;
		String name;
		String currFirst = null;
		for (i = firstIndexWithElm; i < groups.size(); i++){
			if(!groups.get(i).hasNext()){
				continue;
			}
			else if(currFirst == null){
				currFirst = groups.get(i).getLastReturned().getName();
				nextIndex = i;
				continue;
			}
			if (currFirst.compareToIgnoreCase(groups.get(i).getLastReturned().getName()) > 0){
				currFirst = groups.get(i).getLastReturned().getName();
				nextIndex = i;
			}
		}
		if(currFirst != null){
			name = groups.get(nextIndex).getLastReturned().getName();
			if (groups.get(nextIndex).getNext() == null){
				nameOfLast = null;
			}
			return name;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Stores a newly created group in this storage system.
	 * 
	 * @param group - an ValStorageGroup
	 */
	public void addGroup(ValStorageGroup group){
		groups.add(group);
	}
	
	/**
	 * Gets the list of groups currently created.
	 * 
	 * @return ArrayList of group objects
	 */
	public ArrayList<ValStorageGroup> getGroups(){
		return groups;
	}
	
	/**
	 * Gets an int representing the type of Element stored in a given
	 * instance of ElementStorage.
	 * 
	 * @return 1 = Var; 2 = Const
	 */
	public int getTypeStorage(){
		return typeStorage;
	}
	
	/**
	 * Sets the type of Element Stored in this instance of ElementStorage.
	 * 
	 * @param i - the new type, 1 = Var; 2 = Const
	 */
	public void setTypeStorage(int i){
		typeStorage = i;
	}
}