/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package tree;

public class ConstantStorage extends ValueStorage {

	public ConstantStorage(){
		addGroup(new ValStorageGroup("math"));
		storeInGroup("math", new Constant("pi", new Decimal (Math.PI)));
		storeInGroup("math", new Constant("e", new Decimal(Math.E)));
		addGroup(new ValStorageGroup("physics"));
		storeInGroup("physics", new Constant("g", new Decimal(-9.8)));
		addGroup(new ValStorageGroup("chem"));
		storeInGroup("chem", new Constant("mol", new Decimal(6.022E23)));
		storeInGroup("chem", new Constant("mwC", new Decimal(14)));
	}
	
	public Number getConstVal(String s){
		ValueWithName tempElm = findIfStored(s);
		if (tempElm instanceof Constant){
			return ((Constant) tempElm).getValue();
		}
		return null;
	}
}
