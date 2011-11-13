/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc;

import java.util.Vector;


import doc.mathobjects.ProblemGenerator;
import doc_gui.DocViewerPanel;
import doc_gui.DocumentException;
import doc_gui.attributes.MathObjectAttribute;
import doc_gui.attributes.StringAttribute;

public class Document {
	
	//will likely want to make standard database of concepts, which will be pulled down
	// and stored in application, to prevent misspellings and different terminology for
	//the same concept cluttering the database
	private Vector<String> subjectsCovered;
	
	//actual values stored in MathObjectAttribute objects these are standard
	//strings for the 'name' fields of those attributes
	public static final String FILENAME = "filename";
	public static final String HEADER = "header";
	public static final String FOOTER = "footer";
	public static final String AUTHOR = "author";
	public static final String DATE = "date";
	public static final String AUTHOR_ID = "authorID";
	
	private Vector<Page> pages;
	
	private Vector<ProblemGenerator> problemGenerators;
	
	//this should not be exported to files, it is a bridge between the front end and back end
	private DocViewerPanel docPanel;
	
	//stores all of the data for the document, allows for easy creation of a
	//panel for setting document properties, just like the MathObject properties panel
	private Vector<MathObjectAttribute> attributes;
	
	public Document(String name){
		attributes = new Vector<MathObjectAttribute>();
		addAttributes();
		pages = new Vector<Page>();
		subjectsCovered = new Vector<String>();
		getAttributeWithName(FILENAME).setValue(name);
	}
	
	private void addAttributes(){
		addAttribute(new StringAttribute(FILENAME));
		addAttribute(new StringAttribute(AUTHOR));
		addAttribute(new StringAttribute(HEADER));
		addAttribute(new StringAttribute(FOOTER));
		addAttribute(new StringAttribute(DATE));
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

	public void setFilename(String s) {
		getAttributeWithName(FILENAME).setValue(s);
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

	public void setHeader(String s) {
		getAttributeWithName(HEADER).setValue(s);
	}

	public String getHeader() {
		return ((StringAttribute)getAttributeWithName(HEADER)).getValue();
	}

	public void setFooter(String s) {
		getAttributeWithName(FOOTER).setValue(s);
	}

	public String getFooter() {
		return ((StringAttribute)getAttributeWithName(FOOTER)).getValue();
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
	
	public String getDate(){
		return ((StringAttribute)getAttributeWithName(DATE)).getValue();
	}
	
	public String exportToXML(){
		String output = "";
//		output += "<?XML version=\"1.0\" encoding=\"\"?>\n";
		output += "<OpenNotebookDoc " + "version=\"0.1\" " + FILENAME + "=\"" + getName() + "\" " + AUTHOR + "=\"" + getAuthor() +
				"\" " + HEADER + "=\"" + getHeader() + "\" " + FOOTER + "=\"" + getFooter() + "\" "
				+ DATE + "=\"" + getDate() + "\">\n";
		for (String s : subjectsCovered){
			output += "<subject name=\"" + s + "\"></subject>";
		}
		for (Page p : pages){
			output += p.exportToXML();
		}
		output += "</OpenNotebookDoc>";
		return output;
	}
	
	/**
	 * Returns a page object stored at the given index. The indices start at 1.
	 * @param index - the index of the page to retrieve
	 * @return the page object at the selected index
	 * @throws DocumentException - if index is out of range
	 */
	public Page getPage(int index) throws DocumentException
	{
		if (index < 0 || index > pages.size() - 1){
			throw new DocumentException("Invaid page requested");
		}
		return pages.get(index);
	}
	
	public String exportDoc(){
		String docString = new String();
		return null;
	}
	
	public void addPage(Page p){
		if ( ! pages.contains(p)){
			pages.add(p);
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
	
	public int getPageIndex(Page p) throws DocumentException{
		
		int index = pages.indexOf(p);
		if (index == -1){
			throw new DocumentException("page does not belong to this document");
		}
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
