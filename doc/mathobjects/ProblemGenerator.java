package doc.mathobjects;

import java.util.UUID;

import doc.Document;

public interface ProblemGenerator {

	public GeneratedProblem generateProblem();
	
	public UUID getUUID();
	
	public String exportToXML();
	
	public void setParentDocument(Document doc);
	
	public Document getParentDoc();
}
