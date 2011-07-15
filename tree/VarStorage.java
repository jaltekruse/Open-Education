/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package tree;


public class VarStorage extends ValueStorage {

	private ExpressionParser parser;
	
	public VarStorage(ExpressionParser exParser) {
		super(1);
		parser = exParser;
		
		//these should be stored and read in from an external file
		addGroup(new ValStorageGroup("math"));
		storeInGroup("math", new Var("a", new Decimal(0)));
		storeInGroup("math", new Var("ans", new Decimal(0)));
		storeInGroup("math", new Var("b", new Decimal(0)));
		storeInGroup("math", new Var("c", new Decimal(0)));
		addGroup(new ValStorageGroup("user"));
		addGroup(new ValStorageGroup("physics"));
		storeInGroup("physics", new Var("speed", new Decimal(0)));
		storeInGroup("physics", new Var("weight", new Decimal(0)));
		storeInGroup("physics", new Var("v0", new Decimal(0)));
		storeInGroup("physics", new Var("vT", new Decimal(0)));
		addGroup(new ValStorageGroup("graph"));
		storeInGroup("graph", new Var("thetaMin", new Decimal(0)));
		storeInGroup("graph", new Var("thetaStep", new Decimal(Math.PI/360)));
		storeInGroup("graph", new Var("thetaMax", new Decimal(2*Math.PI)));
		storeInGroup("graph", new Var("theta", new Decimal(0)));
		storeInGroup("graph", new Var("r", new Decimal(0)));
		storeInGroup("graph", new Var("yMin", new Decimal(-10)));
		storeInGroup("graph", new Var("xMin", new Decimal(-10)));
		storeInGroup("graph", new Var("yMax", new Decimal(10)));
		storeInGroup("graph", new Var("xMax", new Decimal(10)));
		storeInGroup("graph", new Var("xStep", new Decimal(1)));
		storeInGroup("graph", new Var("yStep", new Decimal(1)));
		storeInGroup("graph", new Var("n", new Decimal(10)));
		storeInGroup("graph", new Var("U_n", new Decimal(10)));
		storeInGroup("graph", new Var("x", new Decimal(0)));
		storeInGroup("graph", new Var("y", new Decimal(0)));
		storeInGroup("graph", new Var("z", new Decimal(0)));
	}
	
	public Var storeVar(String s, Number v){
		if (findIfStored(s) != null)
			return (Var) findIfStored(s);
		Var newVar = (Var) storeInGroup("user", new Var(s, v));
		try{
			getStorageGUI().refreshButtons();
		}
		catch(Exception e){
			System.out.println("error");
		}
		return newVar;
	}

	public Var storeVar(String s){
		if (findIfStored(s) != null)
			return (Var) findIfStored(s);
		Var newVar = (Var) storeInGroup("user", new Var(s, new Decimal(0)));
		try{
			getStorageGUI().refreshButtons();
		}
		catch(Exception e){
			System.out.println("error");
		}
		return newVar;
	}
	
	public Number getVarVal(String s){
		ValueWithName tempElm = findIfStored(s);
		if (tempElm instanceof Var){
			return ((Var) tempElm).getValue();
		}
		return null;
	}
	
	public Expression setVarVal(String s, Expression v){
		ValueWithName tempElm = findIfStored(s);
		if (tempElm instanceof Var){
			if (v instanceof Var){
				((Var) tempElm).setValue(((Var)v).getValue());
			}
			else
			{
				((Var) tempElm).setValue((Number) v);
			}
		}
		//this was intended to redraw the graph if the value of a variable was
		//reassigned in the calculator, and involved in the genreation of the graph
		//such as reassigning xMin, etc.
		//DONT DO THIS, CAUSES INFINATE LOOPS WHEN THE GRAPH REASSIGNS VALUES
		//CONTINUOUSLY REDRAWS ITSELF!!!!!!
//		if(isInGroup("graph", s) && parser.getGUI() != null 
//				&& parser.getGUI().getGridProps() != null){
//			parser.getGUI().getGridProps().refreshAttributes();
//			parser.getGUI().getGraphObj().repaint();
//		}
		return v;
	}
	
	public void updateVarVal(String s, double val) throws EvalException{
		ValueWithName tempElm = findIfStored(s);
		if (tempElm instanceof Var){
			((Var) tempElm).setValue( (Number)
					((Var) tempElm).getValue().add(new Decimal(val)));
		}
		if(isInGroup("graph", s) && parser.getGUI() != null
				&& parser.getGUI().getGridProps() != null){
			parser.getGUI().getGridProps().refreshAttributes();
		}
	}
}
