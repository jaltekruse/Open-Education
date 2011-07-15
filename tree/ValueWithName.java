/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package tree;

public interface ValueWithName{
	
	/**
	 * Gets the stored name.
	 * @return String - the name.
	 */
	public abstract String getName();
	
	//this should not be public, but had problems making it package access
	//when overriding it in the subclasses
	public abstract boolean isMaster();
	
	//this should not be public, but had problems making it package access
	//when overriding it in the subclasses
	public abstract void setMaster(boolean b);
}
