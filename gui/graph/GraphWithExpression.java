/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import tree.Decimal;
import tree.EvalException;
import tree.Expression;
import tree.Number;
import tree.ExpressionParser;
import tree.ParseException;
import tree.Expression;
import tree.Var;

public class GraphWithExpression extends SingleGraph{

/**
 * A Function stores all of the data necessary for graphing.
 * 
 * @author jason
 *
 */
	private String funcEqtn;
	private boolean graphing;
	private boolean connected;
	private Var independentVar;
	private Var dependentVar;
	private ExpressionParser parser;
	private Expression expression;
	
	/**
	 * The default constructor, set the equation equal to an empty string,
	 * makes it not currently graphing, integral and tracing values are
	 * false.
	 */
	public GraphWithExpression(ExpressionParser ep, Graph g, Color c) {
		super(g);
		setParser(ep);
		funcEqtn = "";
		graphing = false;
		connected = true;
		setColor(c);
		setIndependentVar("x");
		setDependentVar("y");
	}
	
	public GraphWithExpression(String s, ExpressionParser ep, Graph g, Color c) {
		super(g);
		setParser(ep);
		funcEqtn = s;
		graphing = false;
		connected = true;
		setColor(c);
		setIndependentVar("x");
		setDependentVar("y");
		try {
			expression = parser.ParseExpression(funcEqtn);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor that takes all attributes of a function.
	 * 
	 * @param exParser- associated 
	 * @param eqtn - string of equation
	 * @param ind - string of independent var
	 * @param dep - string of dependent var
	 * @param connected - boolean for points to be connected when graphed
	 * @param trace - boolean for tracing
	 * @param tracePt - double for value to trace
	 * @param integral - boolean for taking an integral
	 * @param startInt - double for start of integral
	 * @param endInt - double for end of integral
	 * @param derive - boolean for deriving
	 * @param dervative - the point to derive at
	 * @param c - a color to display the function with
	 */
	public GraphWithExpression(ExpressionParser exParser, Graph g, String eqtn, String ind, String dep, 
			boolean connected, Color c) {
		super(g);
		setParser(exParser);
		setIndependentVar(ind);
		setDependentVar(dep);
		graphing = true;
		this.connected = connected;
		funcEqtn = eqtn;
		setColor(c);
		try {
			expression = parser.ParseExpression(funcEqtn);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFuncEqtn(String s) {
		funcEqtn = s;
	}

	public String getFuncEqtn() {
		return funcEqtn;
	}

	public void setGraphing(boolean graphing) {
		this.graphing = graphing;
	}

	public boolean isGraphing() {
		return graphing;
	}

	public void setIndependentVar(Var independentVar) {
		this.independentVar = independentVar;
	}
	
	public void setIndependentVar(String varName) {
		independentVar = getParser().getVarList().storeVar(varName);
	}
	
	public void setDependentVar(String varName) {
		dependentVar = getParser().getVarList().storeVar(varName);
	}


	public Var getIndependentVar() {
		return independentVar;
	}

	public void setDependentVar(Var dependentVar) {
		this.dependentVar = dependentVar;
	}

	public Var getDependentVar() {
		return dependentVar;
	}

	public void setConneted(boolean conneted) {
		this.connected = conneted;
	}

	public boolean isConnected() {
		return connected;
	}

	@Override
	public void draw(Graphics g) throws EvalException, ParseException {
		// TODO Auto-generated method stub
		
	}

	public void setParser(ExpressionParser parser) {
		this.parser = parser;
	}

	public ExpressionParser getParser() {
		return parser;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}
}