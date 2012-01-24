/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc;

import java.util.UUID;
import java.util.Vector;


import doc.attributes.Date;
import doc.attributes.DateAttribute;
import doc.attributes.MathObjectAttribute;
import doc.attributes.StringAttribute;
import doc.mathobjects.ProblemGenerator;
import doc_gui.DocViewerPanel;
import doc_gui.DocumentException;

public class Document {
	
	//will likely want to make standard database of concepts, which will be pulled down
	// and stored in application, to prevent misspellings and different terminology for
	//the same concept cluttering the database
	private Vector<String> subjectsCovered;
	
	//actual values stored in MathObjectAttribute objects these are standard
	//strings for the 'name' fields of those attributes
	public static final String FILENAME = "filename", HEADER = "header", FOOTER = "footer", 
			AUTHOR = "author", DATE = "date", AUTHOR_ID = "authorID", GENERATORS = "generators",
			OPEN_NOTEBOOK_DOC = "OpenNotebookDoc";
	
	private Vector<Page> pages;
	
	private Vector<ProblemGenerator> generators;
	
	private Vector<ProblemGenerator> problemGenerators;
	
	//this should not be exported to files, it is a bridge between the front end and back end
	private DocViewerPanel docPanel;
	
	//stores all of the data for the document, allows for easy creation of a
	//panel for setting document properties, just like the MathObject properties panel
	private Vector<MathObjectAttribute> attributes;
	
	public Document(String name){
		attributes = new Vector<MathObjectAttribute>();
		generators = new Vector<ProblemGenerator>();
		addAttributes();
		pages = new Vector<Page>();
		subjectsCovered = new Vector<String>();
		getAttributeWithName(FILENAME).setValue(name);
	}
	
	private void addAttributes(){
		addAttribute(new StringAttribute(FILENAME));
		addAttribute(new StringAttribute(AUTHOR));
		addAttribute(new DateAttribute(DATE));
	}
	
	private MathObjectAttribute getAttributeWithName(String n){
		for (MathObjectAttribute mathAtt : attributes){
			if (mathAtt.getName().equals(n)){
				return mathAtt;
			}
		}
		return null;
	}
	
	public Vector<MathObjectAttribute> getAttributes(){
		return attributes;
	}
	
	private void addAttribute(MathObjectAttribute mAtt){
		for (MathObjectAttribute mathAtt : attributes){
			if (mathAtt.getName().equals(mAtt.getName())){
				return;
			}
		}
		attributes.add(mAtt);
	}
	
	public Vector<ProblemGenerator> getGenerators() {
		return generators;
	}
	
	public ProblemGenerator getGeneratorWithID(UUID id){
		for ( ProblemGenerator gen : generators){
			if ( gen.getUUID().compareTo(id) == 0){
				return gen;
			}
		}
		return null;
	}
	
	public void addGenerator(ProblemGenerator generator) throws Exception{
		for ( ProblemGenerator gen : generators){
			if ( gen.getUUID().compareTo(generator.getUUID()) == 0){
				throw new Exception("UUID already in use");
			}
		}
		generator.setParentDocument(this);
		generators.add(generator);
	}
	
	public void setFilename(String s) {
		getAttributeWithName(FILENAME).setValue(s);
	}
	
	public Document clone(){
		Document newDoc = new Document(new String(getName()));
		newDoc.attributes = new Vector<MathObjectAttribute>();
		for ( MathObjectAttribute mAtt : getAttributes()){
			newDoc.addAttribute(mAtt.clone());
		}
		for ( ProblemGenerator gen : generators){
			try {
				newDoc.addGenerator(gen.clone());
			} catch (Exception e) {
				System.out.println("UUID already in use.");
			}
		}
		for ( Page p : pages){
			newDoc.addPage(p.clone());
		}
		
		newDoc.setDocViewerPanel(getDocViewerPanel());
		return newDoc;
	}

	public String getName() {
		return ((StringAttribute)getAttributeWithName(FILENAME)).getValue();
	}

	public void setSubjectsCovered(Vector<String> subjectsCovered) {
		this.subjectsCovered = subjectsCovered;
	}

	public Vector<String> getSubjectsCovered() {
		return subjectsCovered;
	}

	public void setAuthor(String s) {
		getAttributeWithName(AUTHOR).setValue(s);
	}

	public String getAuthor() {
		return ((StringAttribute)getAttributeWithName(AUTHOR)).getValue();
	}
	
	public void setDate(int d, int m, int y){
		String date = "(" + m + "/" + d + "/" + y + ")";
		
	}
	
	public Date getDate(){
		return ((DateAttribute)getAttributeWithName(DATE)).getValue();
	}
	
	public String exportToXML(){
		String output = "";
//		output += "<?XML version=\"1.0\" encoding=\"\"?>\n";
		output += "<" + OPEN_NOTEBOOK_DOC + " " + "version=\"0.1\" " + FILENAME + "=\""
				+ getName() + "\" " + AUTHOR + "=\"" + getAuthor()				
				+ "\" " + DATE + "=\"" + getDate() + "\">\n";
		for (String s : subjectsCovered){
			output += "<subject name=\"" + s + "\"></subject>";
		}
		output += "<" + GENERATORS + ">\n";
		for ( ProblemGenerator gen : generators){
			output += gen.exportToXML();
		}
		output += "</" + GENERATORS + ">\n";
		for (Page p : pages){
			output += p.exportToXML();
		}
		output += "</" + OPEN_NOTEBOOK_DOC + ">";
		return output;
	}
	
	/**
	 * Returns a page object stored at the given index. The indices start at 1.
	 * @param index - the index of the page to retrieve
	 * @return the page object at the selected index
	 */
	public Page getPage(int index)
	{
		if (index < 0 || index > pages.size() - 1){
			return null;
		}
		return pages.get(index);
	}
	
	public Page getLastPage(){
		return pages.get(pages.size() - 1);
	}
	
	public void addPage(Page p){
		if ( ! pages.contains(p)){
			pages.add(p);
			p.setParentDoc(this);
		}
		else{
//			System.out.println("Page is already contained in specified document");
		}
	}
	
	public void removePage(Page p){
		if ( pages.contains(p)){
			pages.remove(p);
		}
		else{
//			System.out.println("Page is not contained in specified document");
		}
	}
	
	public int getNumPages(){
		return pages.size();
	}
	
	public void addBlankPage(){
		pages.add(new Page(this));
	}
	
	public int getPageIndex(Page p){
		
		int index = pages.indexOf(p);
		return index;
	}
	
	public Vector<Page> getPages(){
		return pages;
	}
	
	public int lastPageIndex(){
		return pages.size() + 1;
	}

	public void setDocViewerPanel(DocViewerPanel docPanel) {
		this.docPanel = docPanel;
	}

	public DocViewerPanel getDocViewerPanel() {
		return docPanel;
	}

	public Vector<ProblemGenerator> getProblemGenerators() {
		return problemGenerators;
	}

	public void setProblemGenerators(Vector<ProblemGenerator> problemGenerators) {
		this.problemGenerators = problemGenerators;
	}

}
