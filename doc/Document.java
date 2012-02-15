/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc;

import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import doc.attributes.AttributeException;
import doc.attributes.Date;
import doc.attributes.DateAttribute;
import doc.attributes.IntegerAttribute;
import doc.attributes.MathObjectAttribute;
import doc.attributes.StringAttribute;
import doc.mathobjects.GeneratedProblem;
import doc.mathobjects.Grouping;
import doc.mathobjects.MathObject;
import doc.mathobjects.ProblemGenerator;
import doc.mathobjects.ProblemNumberObject;
import doc.mathobjects.TextObject;
import doc_gui.DocViewerPanel;

public class Document {

	/**
	 * The standard 8.5 inch page width. Given as an integer, 612, in the
	 * standard user space 72 dpi.
	 */
	public static final int DEFAULT_PAGE_WIDTH = 612;

	/**
	 * The standard 11 inch page height. Given as an integer, 792, in the
	 * standard user space 72 dpi.
	 */
	public static final int DEFAULT_PAGE_HEIGHT = 792;

	/**
	 * The standard half-inch page margin. Given as an integer, 36, in the
	 * standard user space 72 dpi.
	 */
	public static final int DEFAULT_MARGIN = 36;

	private int orientation;
	public static final int PORTRAIT = 1;
	public static final int LANDSCAPE = 2;
	private int xMargin, yMargin, pageWidth, pageHeight;

	// will likely want to make standard database of concepts, which will be
	// pulled down
	// and stored in application, to prevent misspellings and different
	// terminology for
	// the same concept cluttering the database
	private Vector<String> subjectsCovered;

	// actual values stored in MathObjectAttribute objects these are standard
	// strings for the 'name' fields of those attributes
	public static final String FILENAME = "filename", HEADER = "header",
			FOOTER = "footer", AUTHOR = "author", DATE = "date",
			AUTHOR_ID = "authorID", GENERATORS = "generators",
			OPEN_NOTEBOOK_DOC = "OpenNotebookDoc", LAST_PROBLEM_NUMBER = "lastProblemNumber";

	private Vector<Page> pages;
	private Vector<ProblemGenerator> generators;
	private Vector<ProblemGenerator> problemGenerators;

	// this should not be exported to files, it is a bridge between the front
	// end and back end
	private DocViewerPanel docPanel;
	private static ProblemDatabase problemDatabase;

	// stores all of the data for the document, allows for easy creation of a
	// panel for setting document properties, just like the MathObject
	// properties panel
	private Vector<MathObjectAttribute> attributes;

	// used for automatically laying out problems on a document and giving them
	// extra space
	private static final int minimumBufferSpace = 40;

	// cloned versions of previous documents are used for the undo/redo stack
	// to maintain what object had focus at each undo state, it is stored here
	// and when a document is cloned, the cloned equivalent is set as the
	// focused object of the clone. The actual handling of focusing while
	// clicking on the interface is handled in the front-end in DocViewerPanel
	private MathObject lastFocused;

	private static final Random random = new Random();

	public Document(String name) {
		attributes = new Vector<MathObjectAttribute>();
		generators = new Vector<ProblemGenerator>();
		addAttributes();
		pages = new Vector<Page>();
		subjectsCovered = new Vector<String>();
		getAttributeWithName(FILENAME).setValue(name);
		pageWidth = DEFAULT_PAGE_WIDTH;
		pageHeight = DEFAULT_PAGE_HEIGHT;
		xMargin = DEFAULT_MARGIN;
		yMargin = DEFAULT_MARGIN;
	}

	private void addAttributes() {
		addAttribute(new StringAttribute(FILENAME));
		addAttribute(new StringAttribute(AUTHOR));
		addAttribute(new DateAttribute(DATE));
		addAttribute(new IntegerAttribute(LAST_PROBLEM_NUMBER, 1));
	}

	private MathObjectAttribute getAttributeWithName(String n) {
		for (MathObjectAttribute mathAtt : attributes) {
			if (mathAtt.getName().equals(n)) {
				return mathAtt;
			}
		}
		return null;
	}

	public Vector<MathObjectAttribute> getAttributes() {
		return attributes;
	}

	private void addAttribute(MathObjectAttribute mAtt) {
		for (MathObjectAttribute mathAtt : attributes) {
			if (mathAtt.getName().equals(mAtt.getName())) {
				return;
			}
		}
		attributes.add(mAtt);
	}

	public Vector<ProblemGenerator> getGenerators() {
		return generators;
	}

	public ProblemGenerator getGeneratorWithID(UUID id) {
		for (ProblemGenerator gen : generators) {
			if (gen.getUUID().compareTo(id) == 0) {
				return gen;
			}
		}
		return getProblemDatabase().getProblemWithUUID(id);
	}

	public void addGenerator(ProblemGenerator generator) throws Exception {
		for (ProblemGenerator gen : generators) {
			if (gen.getUUID().compareTo(generator.getUUID()) == 0) {
				throw new Exception("UUID already in use");
			}
		}
		generator.setProblemHoldingDocument(this);
		generators.add(generator);
	}

	public void setFilename(String s) {
		getAttributeWithName(FILENAME).setValue(s);
	}

	public Document clone() {
		Document newDoc = new Document(new String(getName()));
		newDoc.attributes = new Vector<MathObjectAttribute>();
		MathObject lastWithFocus = this.getLastFocused();
		for (MathObjectAttribute mAtt : getAttributes()) {
			newDoc.addAttribute(mAtt.clone());
		}
		for (ProblemGenerator gen : generators) {
			try {
				newDoc.addGenerator(gen.clone());
			} catch (Exception e) {
				System.out.println("UUID already in use.");
			}
		}
		for (Page p : pages) {
			newDoc.addPage(p.clone());
		}

		// the last focused member of this document is used to pass back
		// its cloned version from the page cloning method
		newDoc.setLastFocused(this.getLastFocused());
		// the last focused object is saved at the beginning of the method
		// to prevent being overridden as described in the above comment
		// this.setLastFocused(lastWithFocus);
		newDoc.setDocViewerPanel(getDocViewerPanel());
		return newDoc;
	}

	public String getName() {
		return ((StringAttribute) getAttributeWithName(FILENAME)).getValue();
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
		return ((StringAttribute) getAttributeWithName(AUTHOR)).getValue();
	}

	public PointInDocument findFirstWhitespace() {
		Page lastPageWithStuff = getPage(0);
		for (Page p : getPages()) {
			if (p.getObjects().size() > 0) {
				lastPageWithStuff = p;
			}
		}
		int lastObjectYPos = getyMargin() + 10;
		for (MathObject mObj : lastPageWithStuff.getObjects()) {
			if (mObj.getyPos() + mObj.getHeight() > lastObjectYPos) {
				lastObjectYPos = mObj.getyPos() + mObj.getHeight();
			}
		}
		return new PointInDocument(getPageIndex(lastPageWithStuff), getxMargin() + 10,
				lastObjectYPos + 10);
	}

	public void generateProblems(Vector<ProblemGenerator> generators,
			Vector<Integer> frequencies, int numberOfProblems, String directions) {
		GeneratedProblem[] newProblems = new GeneratedProblem[numberOfProblems];
		int difficulty;
		for (ProblemGenerator gen : generators){
			gen.setProblemHoldingDocument(this);
			try {
				addGenerator(gen);
			} catch (Exception e) {
				// problem formula has already been added
			}
		}
		int j = 0;
		newProblems = new GeneratedProblem[numberOfProblems / 3];
		for (int i = 0; j < numberOfProblems / 3; j++, i++) {
			newProblems[i] = generators.get(pickRandomIndex(frequencies))
					.generateProblem(ProblemGenerator.EASY);
		}
		layoutProblems(newProblems, directions, this);
		newProblems = new GeneratedProblem[numberOfProblems / 3];
		for (int i = 0; j < (int) numberOfProblems * 2/3.0; j++, i++) {
			newProblems[i] = generators.get(pickRandomIndex(frequencies))
					.generateProblem(ProblemGenerator.MEDIUM);
		}
		layoutProblems(newProblems, null, this);
		newProblems = new GeneratedProblem[numberOfProblems - 2 * (numberOfProblems / 3)];
		for (int i = 0; j < numberOfProblems; j++, i++) {
			newProblems[i] = generators.get(pickRandomIndex(frequencies))
					.generateProblem(ProblemGenerator.HARD);
		}
		layoutProblems(newProblems, null, this);
		docPanel.repaintDoc();
	}

	private int pickRandomIndex(Vector<Integer> frequencies) {
		int total = 0;
		for (int i = 0; i < frequencies.size(); i++) {
			total += frequencies.get(i);
		}
		int chosen = random.nextInt(total) + 1;
		total = 0;
		for (int i = 0; i < frequencies.size(); i++) {
			if (chosen > total && chosen <= total + frequencies.get(i)) {
				return i;
			}
			total += frequencies.get(i);
		}
		// this next line should never be reached
		System.out.println("this line should never be reached");
		return 1;
	}

	public void refactorPageNumbers(){
		Vector<ObjectAndPosition> allNumbers = getProblemsInOrder();
		int lastProblemNumber = 1;
		for ( ObjectAndPosition obj : allNumbers){
			try {
				obj.mObj.setAttributeValue(ProblemNumberObject.NUMBER, lastProblemNumber);
			} catch (AttributeException e) {
				// error should not be thrown
			}
			lastProblemNumber++;
		}
		setLastProblemNumber(lastProblemNumber);
	}

	private Vector<ObjectAndPosition> getProblemsInOrder(){
		Vector<ObjectAndPosition> allNumbers = new Vector<ObjectAndPosition>();
		for ( Page p : getPages()){
			for (MathObject mObj : p.getObjects()){
				if ( mObj instanceof ProblemNumberObject){
					allNumbers.add(new ObjectAndPosition(mObj.getPositionInDoc(), mObj));
				}
				else if ( mObj instanceof Grouping){
					allNumbers.addAll(0, findAllProblemNumbersInGroup(((Grouping)mObj)));
				}
			}
		}
		Collections.sort(allNumbers);
		return allNumbers;
	}

	public Document getAnswerKey(){

		Vector<ObjectAndPosition> allNumbers = getProblemsInOrder();

		// change the objects that have answers

		// update group that problems are in to accommodate bigger expressions

		// create new doc

		// layout problems on new doc
	}

	private Vector<ObjectAndPosition> findAllProblemNumbersInGroup(Grouping g){
		Vector<ObjectAndPosition> allNumbers = new Vector<ObjectAndPosition>();
		for (MathObject mObj : g.getObjects()){
			if ( mObj instanceof ProblemNumberObject){
				allNumbers.add(new ObjectAndPosition(mObj.getPositionInDoc(), mObj));
			}
			else if ( mObj instanceof Grouping){
				allNumbers.addAll(0, findAllProblemNumbersInGroup(((Grouping)mObj)));
			}
		}
		return allNumbers;
	}

	private class ObjectAndPosition implements Comparable<ObjectAndPosition>{

		private PointInDocument pt;
		private MathObject mObj;
		private ObjectAndPosition(PointInDocument pt, MathObject mObj){
			this.pt = pt;
			this.mObj = mObj;
		}
		@Override
		public int compareTo(ObjectAndPosition o) {
			return pt.compareTo(o.pt);
		}
	}

	public static void layoutProblems(MathObject[] objects, String directions, Document doc) {

		int extraMarginForDirections = 5;
		PointInDocument pt = doc.findFirstWhitespace();
		Page currentPage = doc.getPage(pt.getPage());

		int greatestWidth = 0, greatestHeight = 0;
		for (MathObject mObj : objects) {
			if (mObj.getWidth() > greatestWidth) {
				greatestWidth = mObj.getWidth();
			}
			if (mObj.getHeight() > greatestHeight) {
				greatestHeight = mObj.getHeight();
			}
		}
		int curryPos = pt.getyPos();
		if (directions != null){
			// add some extra space between lists of problems
			pt.setyPos(pt.getyPos() + minimumBufferSpace/2);
			TextObject directionText = new TextObject(currentPage, extraMarginForDirections + currentPage.getxMargin(),
					pt.getyPos(), currentPage.getWidth() - 2 * currentPage.getxMargin() - 
					2 * extraMarginForDirections, 20, 12, directions);
			curryPos = pt.getyPos() + directionText.getHeight() + minimumBufferSpace/2;
			// draw the text in the background, so it has its height set correctly
			doc.getDocViewerPanel().drawObjectInBackgorund(directionText);
			if ( curryPos + greatestHeight < currentPage.getHeight() - currentPage.getyMargin())
			{// the first row of objects will fit on this page, otherwise move the directions down a page
				currentPage.addObject(directionText);
			}
			else{
				currentPage = nextPage(currentPage);
				curryPos = currentPage.getyMargin() + minimumBufferSpace;
				directionText.setyPos(curryPos);
				currentPage.addObject(directionText);
				directionText.setParentContainer(currentPage);
				curryPos += directionText.getHeight() + minimumBufferSpace/2;
			}
		}

		int numColumns = (doc.getWidth() - 2 * doc.getxMargin() - 4 * extraMarginForDirections) 
				/ (greatestWidth + minimumBufferSpace);
		int totalExtraSpace = (doc.getWidth() - 2 * doc.getxMargin() - 4 * extraMarginForDirections)
				% (greatestWidth + minimumBufferSpace);

		int extraColumnSpace = totalExtraSpace / (numColumns);
		int currColumn = 0;

		ProblemNumberObject problemNumber;
		int lastProblemNumber = doc.getLastProblemNumber();

		for (MathObject mObj : objects) {
			mObj.setxPos(currentPage.getxMargin() + minimumBufferSpace
					+ currColumn * (greatestWidth + minimumBufferSpace + extraColumnSpace));
			mObj.setyPos(curryPos);

			mObj.setParentContainer(currentPage);
			if (!mObj.isOnPage()) {
				currentPage = nextPage(currentPage);
				curryPos = currentPage.getyMargin() + minimumBufferSpace;
				mObj.setyPos(curryPos);
			}
			// add a number for this problem
			problemNumber = new ProblemNumberObject(currentPage, mObj.getxPos() - 38,
					mObj.getyPos(), 35, 20, lastProblemNumber);
			// draw it in the background so its height is set properly
			doc.getDocViewerPanel().drawObjectInBackgorund(problemNumber);
			// to accommodate problems of different heights, the number should be layed out either in the
			// center of the height of the problem, or at the top if it is very large
			if ( problemNumber.getHeight() * 4 >= mObj.getHeight()){
				problemNumber.setyPos(mObj.getyPos() + (int)
						Math.round( (mObj.getHeight()/2.0) - (problemNumber.getHeight()/2.0)));
				//				problemNumber.setyPos(mObj.getyPos() + (mObj.getHeight() - problemNumber.getHeight())/2);
			}
			else{
				problemNumber.setyPos(mObj.getyPos() + problemNumber.getHeight());
			}
			currentPage.addObject(problemNumber);
			lastProblemNumber++;
			currentPage.addObject(mObj);
			mObj.setParentContainer(currentPage);
			currColumn++;
			if (currColumn > numColumns - 1) {
				curryPos += greatestHeight + minimumBufferSpace/3;
				currColumn = 0;
			}
		}
		doc.setLastProblemNumber(lastProblemNumber);
	}

	/**
	 * Returns the next page, if there is none, one is added and it is returned.
	 * 
	 * @param currentPage - the previous page
	 * @return - the next page
	 */
	public static Page nextPage(Page currentPage){
		if (currentPage.getParentDoc().getNumPages() < currentPage
				.getParentDoc().getPageIndex(currentPage) + 2) 
		{// a page must be added to add the objects
			currentPage.getParentDoc().addBlankPage();
			currentPage = currentPage.getParentDoc().getPage(
					currentPage.getParentDoc().getNumPages() - 1);
			currentPage.getParentDoc().getDocViewerPanel()
			.resizeViewWindow();
		} else {// there is a next page on the document that the new
			// objects can be added to
			currentPage = currentPage.getParentDoc().getPage(
					currentPage.getParentDoc()
					.getPageIndex(currentPage) + 1);
		}
		return currentPage;
	}

	public void setWidth(int pageWidth) {
		this.pageWidth = pageWidth;
	}

	public int getWidth() {
		return pageWidth;
	}

	public void setHeight(int pageHeight) {
		this.pageHeight = pageHeight;
	}

	public int getHeight() {
		return pageHeight;
	}

	public void setxMargin(int xMargin) {
		this.xMargin = xMargin;
	}

	public int getxMargin() {
		return xMargin;
	}

	public void setyMargin(int yMargin) {
		this.yMargin = yMargin;
	}

	public int getyMargin() {
		return yMargin;
	}

	public Date getDate() {
		return ((DateAttribute) getAttributeWithName(DATE)).getValue();
	}

	public int getLastProblemNumber() {
		return (Integer) getAttributeWithName(LAST_PROBLEM_NUMBER).getValue();
	}

	public void setLastProblemNumber(int i){
		((IntegerAttribute) getAttributeWithName(LAST_PROBLEM_NUMBER)).setValue(i);
	}

	public String exportToXML() {
		String output = "";
		output += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		output += "<" + OPEN_NOTEBOOK_DOC + " " + "version=\"0.1\" " + FILENAME
				+ "=\"" + getName() + "\" " + AUTHOR + "=\"" + getAuthor()
				+ "\" " + DATE + "=\"" + getDate() + "\" " + 
				LAST_PROBLEM_NUMBER + "=\"" + getLastProblemNumber() + "\">\n";
		for (String s : subjectsCovered) {
			output += "<subject name=\"" + s + "\"></subject>";
		}
		output += "<" + GENERATORS + ">\n";
		for (ProblemGenerator gen : generators) {
			output += gen.exportToXML();
		}
		output += "</" + GENERATORS + ">\n";
		for (Page p : pages) {
			output += p.exportToXML();
		}
		output += "</" + OPEN_NOTEBOOK_DOC + ">";
		return output;
	}

	/**
	 * Returns a page object stored at the given index. The indices start at 0.
	 * 
	 * @param index
	 *            - the index of the page to retrieve
	 * @return the page object at the selected index
	 */
	public Page getPage(int index) {
		if (index < 0 || index > pages.size() - 1) {
			return null;
		}
		return pages.get(index);
	}

	public Page getLastPage() {
		return pages.get(pages.size() - 1);
	}

	public void addPage(Page p) {
		if (!pages.contains(p)) {
			pages.add(p);
			p.setParentDoc(this);
		} else {
			// System.out.println("Page is already contained in specified document");
		}
	}

	public void removePage(Page p) {
		if (pages.contains(p)) {
			pages.remove(p);
		} else {
			// System.out.println("Page is not contained in specified document");
		}
	}

	public int getNumPages() {
		return pages.size();
	}

	public void addBlankPage() {
		pages.add(new Page(this));
	}

	public int getPageIndex(Page p) {
		return pages.indexOf(p);
	}

	public Vector<Page> getPages() {
		return pages;
	}

	public int lastPageIndex() {
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

	public MathObject getLastFocused() {
		return lastFocused;
	}

	public void setLastFocused(MathObject lastFocused) {
		this.lastFocused = lastFocused;
	}

	public static ProblemDatabase getProblemDatabase() {
		return problemDatabase;
	}

	public static void setProblemDatabase(ProblemDatabase problemDatabase) {
		Document.problemDatabase = problemDatabase;
	}

	public void removeAllPages() {
		pages = new Vector<Page>();
	}

}
