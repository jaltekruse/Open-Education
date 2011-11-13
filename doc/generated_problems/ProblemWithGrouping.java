package doc.generated_problems;

import doc.mathobjects.GeneratedProblem;
import doc.mathobjects.Grouping;
import doc.mathobjects.ProblemGenerator;

public class ProblemWithGrouping extends Grouping implements GeneratedProblem{

	public static String GENERATE_NEW_PROBLEM = "Generate new";
	public static String EDIT_PROBLEM_GENERATOR = "Edit generator";
	
	private ProblemGenerator generator;
	
	public ProblemWithGrouping(ProblemGenerator gen){
		generator = gen;
		this.getActions().removeAllElements();
		this.addAction(GENERATE_NEW_PROBLEM);
	}
	
	@Override
	public ProblemGenerator getProblemGenerator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public GeneratedProblem generateNewProblem() {
		// TODO Auto-generated method stub
		return null;
	}
}
