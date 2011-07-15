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
 * Stores all Elements with identifiers. There is no check to prevent
 * different types from storing in one group object, so you must keep
 * track of what it is storing.
 * @author Jason
 *
 */
public class ValStorageGroup {
	
	private int NUM_CHARS_TO_SCAN = 2;
	
	//letters
	private int NUM_POSSIBLE_CHARS = 37;
	private final int NUM_BUCKETS = NUM_POSSIBLE_CHARS * NUM_POSSIBLE_CHARS;
	private ArrayList<ValueWithName>[] elmTable;
	private String groupName;
	private boolean hasNext;
	private boolean isSelected;
	
	private int CURR_BUCKET_NUM;
	private int CURR_ELM_NUM;
	private ValueWithName lastReturned;

	/**
	 * Creates a new group. 
	 * @param name - String to identify this group
	 */
	
	public ValStorageGroup(String name) {
		elmTable = new ArrayList[NUM_BUCKETS];
		for (int i = 0; i < NUM_BUCKETS; i++) {
			elmTable[i] = new ArrayList<ValueWithName>();
		}
		groupName = name;
		setHasNext(false);
		setSelected(true);
	}

	/**
	 * Takes a string and returns a pseudo-hash value. Does not perform a
	 * typical hash!! Characters are given values in terms of ascending 
	 * alphabetic storage. Case is ignored and letters add values of 0-25.
	 * Numbers add values of 26-35 and an underscore adds 36. There is no
	 * check for improper names at this level(something that needs to be
	 * looked at). The parser checks for proper names, but the Element creator
	 * dialog does not. 
	 * @param s - a string to generate a hash for
	 * @return the int val of the hash
	 */
	private int generateHash(String s) {
		if (s.equals(""))
			return Integer.MAX_VALUE;
		s = s.toLowerCase();
		int hashVal = 0, numScanChars = NUM_CHARS_TO_SCAN;
		char currChar = '\0';
		if(s.length() == 1)
			numScanChars = 1;
		for (int i = 0; i < numScanChars; i++) {
			currChar = s.charAt(i);
			Character.toLowerCase(currChar);
			if(Character.isLetter(currChar))
				hashVal += Math.pow(36, 1-i) * (currChar - 'a');
			else if(Character.isDigit(currChar))
				hashVal += Math.pow(36, 1-i) * (currChar - '0' + 25);
			else if(currChar == '_')
				hashVal += Math.pow(36, 1-i) * 36;
		}
		return hashVal;
	}

	/**
	 * Stores the Element in the current storage system.
	 * 
	 * @param e - an ElementWithIdentifer to be stored
	 * @return the Element passed in
	 */
	public ValueWithName storeElm(ValueWithName e) {
		int bucket = generateHash(e.getName());
		if (elmTable[bucket].isEmpty()) {
			elmTable[bucket].add(e);
			return e;
		}
		else {
			if (hasNext = false){
				setHasNext(true);
			}
			String name = e.getName();
			for (int index = 0; index < elmTable[bucket].size(); index++) {
				if (name.compareTo(elmTable[bucket].get(index).getName()) == 0){
					return elmTable[bucket].get(index);
				}
				else if (name.compareTo(elmTable[bucket].get(index).getName()) < 0){
					elmTable[bucket].add(index, e);
					return e;
				}
				else if (name.compareTo(elmTable[bucket].get(index).getName()) > 0){
					continue;
				}
			}
			elmTable[bucket].add(e);
			return e;
		}
	}
	
	/**
	 * Returns the element represented by the string parameter, if it is stored.
	 * 
	 * @param s - the name of element
	 * @return the element with the name passed in, else null if not stored
	 */
	public ValueWithName findIfStored(String s){
		int bucket = generateHash(s);
		ValueWithName tempElm;
		for (int i = 0; i < elmTable[bucket].size(); i++) {
			tempElm = elmTable[bucket].get(i);
			if (s.equals(tempElm.getName())) {
				// String represents a previously stored Elm, which is
				// returned
				return tempElm;
			}
		}
		return null;
	}
	
	/**
	 * Gets the first element stored according to alphabetic sorting.
	 * 
	 * @return the first element
	 */
	public ValueWithName getFirst(){
		if (isSelected == false){
			return null;
		}
		for (int i = 0; i < elmTable.length - 1; i++){
			if(elmTable[i].isEmpty())
				continue;
			else{
				CURR_BUCKET_NUM = i;
				CURR_ELM_NUM = 0;
				lastReturned = elmTable[i].get(0);
				setHasNext(true);
				return lastReturned;
			}
		}
		return null;
	}
	
	/**
	 * Gets the next element stored. Will continue to return elements with lower
	 * alphabetic precedence until the end is reached.
	 * @return an ElementWithIdentifer after the last returned. If none left
	 * returns null
	 */
	public ValueWithName getNext(){
		if (isSelected == false){
			return null;
		}
		if(CURR_ELM_NUM >= elmTable[CURR_BUCKET_NUM].size() - 1){
			for (int i = CURR_BUCKET_NUM + 1; i < elmTable.length; i++){
				if(elmTable[i].isEmpty())
					continue;
				else{
					CURR_BUCKET_NUM = i;
					CURR_ELM_NUM = 0;
					lastReturned = elmTable[i].get(0);
					return lastReturned;
				}
			}
			lastReturned = null;
			setHasNext(false);
			return null;
		}
		else if (CURR_BUCKET_NUM == NUM_BUCKETS){
			setHasNext(false);
			return null;
		}
		else{
			CURR_ELM_NUM++;
			lastReturned = elmTable[CURR_BUCKET_NUM].get(CURR_ELM_NUM);
			return lastReturned;
		}
	}
	
	/**
	 * gets the last object that was returned, does NOT affect the index keeping
	 * track of what was recalled last/will be recalled next.
	 * @return the last returned Element by getNext()
	 */
	public ValueWithName getLastReturned(){
		return lastReturned;
	}
	
	/**
	 * Gets the name of this group
	 * 
	 * @return - a string
	 */
	public String getGroupName(){
		return groupName;
	}

	/**
	 * Sets a boolean to tell if there is another object to return by getNext()
	 * @param hasNext - a boolean
	 */
	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	/**
	 * Gets the boolean to tell if getNext() has reached the end of the list.
	 * @return - a boolean
	 */
	public boolean hasNext() {
		return hasNext;
	}

	/**
	 * Sets the value of selected. If it is selected, it will be looked through
	 * for returning values with getNext()/getFirst(). Otherwise they will
	 * always return null.
	 * 
	 * @param isSelected - a boolean
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * Returns whether the list is currently selected or not. Determines if the 
	 * list will be searched by getFirst()/getNext().
	 * @return
	 */
	public boolean isSelected() {
		return isSelected;
	}
}
