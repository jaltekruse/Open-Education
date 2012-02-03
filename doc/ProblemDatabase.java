package doc;

import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

import doc.expression_generators.BasicArithmeticExpression;
import doc.expression_generators.ExpressionGenerator;
import doc.mathobjects.Grouping;
import doc.mathobjects.MathObject;
import doc.mathobjects.ProblemGenerator;

public class ProblemDatabase {

	private Vector<ProblemGenerator> problems;
	private static final ExpressionGenerator[] expressionGenerators = {
		new BasicArithmeticExpression()
	};
	private CaseInsensitiveMap tagIndex;
	public static final String NAME = "ProblemDatabase";
	
	private class CaseInsensitiveMap extends HashMap<String, ProblemGenerator> {

	    public ProblemGenerator put(String key, ProblemGenerator value) {
	       return super.put(key.toLowerCase(), value);
	    }

	    public ProblemGenerator get(String key) {
	       return super.get(key.toLowerCase());
	    }
	}
	
	public ProblemDatabase(){
		setListOfProblems(new Vector<ProblemGenerator>());
		tagIndex = new CaseInsensitiveMap();
	}

	public void setListOfProblems(Vector<ProblemGenerator> objects) {
		this.problems = objects;
	}
	
	public void removeGrouping(MathObject mObj){
		problems.remove(mObj);
	}

	public Vector<ProblemGenerator> getAllProblems() {
		Vector<ProblemGenerator> allProblems = (Vector<ProblemGenerator>) problems.clone();
		for ( ExpressionGenerator ex : expressionGenerators){
			allProblems.add(ex);
		}
		return allProblems;
	}
	
	public ProblemGenerator getProblemWithUUID(UUID uuid){
		for ( ProblemGenerator ex : getAllProblems()){
			if ( ex.getUUID().equals(uuid)){
				return ex;
			}
		}
		return null;
	}
	
	public Vector<ProblemGenerator> getExportableProblems(){
		return problems;
	}
	
	/**
	 * Add a MathObject to this {@code DatabaseOfProblems}
	 * @param problem - problem to add
	 * @return true if add was successful
	 */
	public boolean addProblem(ProblemGenerator problem){
		
		if ( ! problems.contains(problem)){
			problems.add(problem);
		}
		return true;
	}
	
	public String exportToXML(){
		String output = "";
		output += "<" + NAME + ">\n";
		for (ProblemGenerator problem : getExportableProblems()){
			output += problem.exportToXML();
		}
		output += "</" + NAME + ">\n";
		return output;
	}
}
