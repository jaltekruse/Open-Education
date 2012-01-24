/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.xml;

import java.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import doc.DatabaseOfGroupedObjects;
import doc.Document;
import doc.Page;
import doc.attributes.AttributeException;
import doc.attributes.BooleanAttribute;
import doc.attributes.ColorAttribute;
import doc.attributes.DateAttribute;
import doc.attributes.DoubleAttribute;
import doc.attributes.EmailAttribute;
import doc.attributes.GridPointAttribute;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;
import doc.attributes.StringAttribute;
import doc.attributes.UUIDAttribute;
import doc.mathobjects.AnswerBoxObject;
import doc.mathobjects.ArrowObject;
import doc.mathobjects.CubeObject;
import doc.mathobjects.CylinderObject;
import doc.mathobjects.ExpressionObject;
import doc.mathobjects.GeneratedProblem;
import doc.mathobjects.GraphObject;
import doc.mathobjects.Grouping;
import doc.mathobjects.MathObject;
import doc.mathobjects.MathObjectContainer;
import doc.mathobjects.NumberLineObject;
import doc.mathobjects.OvalObject;
import doc.mathobjects.ParallelogramObject;
import doc.mathobjects.PolygonObject;
import doc.mathobjects.ProblemGenerator;
import doc.mathobjects.ProblemObject;
import doc.mathobjects.RectangleObject;
import doc.mathobjects.RegularPolygonObject;
import doc.mathobjects.TextObject;
import doc.mathobjects.TrapezoidObject;
import doc.mathobjects.TriangleObject;


public class DocReader extends DefaultHandler {

	private Document doc;
	private Page page;
	private Vector<Grouping> containerStack;
	private boolean DEBUG = false;

	// objects added to Groupings ( or subclasses of Grouping ) need to have their attributes 
	// read in before being added, so their positions can be calculated relative
	// to the size of the group. Below is a vector to store the objects temporarily, they will
	// only be added when a grouping tag is closed

	// this does not work for groups within groups, because the list is destroyed when the new one is vreated
	// thinking of making it a vector of vectors
	private Vector<Vector<MathObject>> objectsInGroup;

	private DatabaseOfGroupedObjects database;
	private MathObject mObj;
	private ListAttribute list;
	boolean hadAttributeError, foundBadTag, readingGenerators, readingList, readingProblemDatabase;
	int versionNumber;
	String badTag;
	String attributeNameInError;
	String attributeValueInError;
	String objectWithError;
	String fileName;

	public DocReader(){
		doc = null;
		page = null;
		mObj = null;
		hadAttributeError = false;
		foundBadTag = false;
		objectWithError = null;
		attributeNameInError = null;
		attributeValueInError = null;
	}

	public DatabaseOfGroupedObjects readDatabase(InputStreamReader file)
			throws SAXException, IOException{
		readFile(file, "");
		return database;
	}

	public Document readDoc (InputStreamReader file, String docName) throws SAXException, IOException{
		readFile(file, docName);
		return doc;
	}

	public void readFile (InputStreamReader file, String docName) throws SAXException, IOException{
		doc = null;
		readingProblemDatabase = false;
		page = null;
		mObj = null;
		hadAttributeError = false;
		foundBadTag = false;
		readingGenerators = false;
		readingList = false;
		list = null;
		attributeNameInError = null;
		attributeValueInError = null;
		objectWithError = null;
		fileName = docName;

		containerStack = new Vector<Grouping>();
		objectsInGroup = new Vector<Vector<MathObject>>();

		XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setContentHandler(this);
		reader.setErrorHandler(this);

		reader.parse(new InputSource(file));
		try { 
			if ( doc == null && ! readingProblemDatabase ){
				throw new IOException("improper document format");
			}
			if ( database == null && readingProblemDatabase){
				throw new IOException("improper problem database format");	
			}
			if (hadAttributeError){
				throw new IOException("improper document format, error with attribute '" + 
						attributeNameInError + "' with a value of '" + attributeValueInError + "'" 
						+ " in object '" + objectWithError + "'");
			}
			if (foundBadTag){
				throw new IOException("found bad tag, " + badTag);
			}
		} catch (IOException e){
			e.printStackTrace();

			OldReader oldReader = new OldReader();
			doc = oldReader.readFile(file);
		}
	}

	@Override
	public void startDocument(){

	}

	@Override
	public void endDocument(){
	}

	@Override
	public void startElement (String uri, String name,
			String qName, Attributes atts){

		if (qName.equals(Document.OPEN_NOTEBOOK_DOC)){
			// the name of the file in the filesystem
			// will allow documents that are renamed outside of
			// the application to take their new name
			doc = new Document(fileName);
			doc.setAuthor(atts.getValue(Document.AUTHOR));
			return;
		}
		else if ( qName.equals(DatabaseOfGroupedObjects.NAME)){
			database = new DatabaseOfGroupedObjects();
			readingProblemDatabase = true;
			return;
		}
		else if (qName.equals(Document.GENERATORS)){
			readingGenerators = true;
			return;
		}
		else if (qName.equals(ListAttribute.LIST)){
			if (mObj != null)
			{// grab the list from the current object with the specified name
				readingList = true;
				list = mObj.getListWithName(atts.getValue(ListAttribute.NAME));
				if ( DEBUG ){
					System.out.println("list added: " + list);
				}
				if ( list == null){
					foundBadTag = true;
					badTag = "Had problem finding list in object";
					return;
				}
				list.removeAll();
				return;
			}
		}
		else if ( readingList && qName.equals(ListAttribute.ENTRY)){
			try {
				list.addValueWithString(atts.getValue(ListAttribute.VAL));
				return;
			} catch (AttributeException e) {
				// TODO Auto-generated catch block
				hadAttributeError = true;
				attributeNameInError = atts.getValue("name");
				attributeValueInError = atts.getValue("value");
				objectWithError = mObj.getClass().getSimpleName();
				return;
			}
		}
		else if (doc != null || readingProblemDatabase){
			if (qName.equals("Page")){
				page = new Page(doc);
				doc.addPage(page);
				return;
			}
			if (page != null || readingGenerators || readingProblemDatabase){
				if (mObj != null){
					if ( DEBUG ){
						System.out.println("in object, should be finding attributes");
					}
					if (readAttribute(uri, name, qName, atts))
					{// if the current tag was an attribute
						if (DEBUG){
							System.out.println("return found attribute");
						}
						return;
					}
				}
				if (DEBUG){
					System.out.println("tag was not attribute " + qName);
				}
				mObj = MathObject.getNewInstance(qName);
				if ( mObj != null){
					if (page != null){
						mObj.setParentContainer(page);
					}
				}
				else{
					foundBadTag = true;
					badTag = qName;
					return;
				}

				if ( containerStack.size() > 0)
				{// if a tag was found in a group, there is an object in a group
					if (mObj != null){
						objectsInGroup.get(objectsInGroup.size() - 1 ).add(mObj);
						if (mObj instanceof Grouping){
							containerStack.add( (Grouping) mObj);
							objectsInGroup.add(new Vector<MathObject>());
						}
					}
				}
				else{
					if (readingGenerators){
						try {
							doc.addGenerator( (ProblemGenerator) mObj );
						} catch (Exception e) {
							// TODO Auto-generated catch block
							if (DEBUG){
								System.out.println("Generator UUID already in use");
							}
						}
					}
					else if ( readingProblemDatabase ){
						database.addGrouping((Grouping) mObj);
					}
					else{
						page.addObject(mObj);
					}
					if (mObj instanceof Grouping){
						containerStack.add( (Grouping) mObj);
						objectsInGroup.add(new Vector<MathObject>());
					}
				}
			}

		}
	}

	public boolean readAttribute(String uri, String name,
			String qName, Attributes atts){
		boolean justAddedAttribute = true;

		if (DEBUG){
			System.out.println("check tag " + atts.getValue("name") + " " +
					"To see if it is an attribute");
		}

		if (MathObjectAttribute.isAttributeType(qName))
		{
			// if code is needed when an MathObjectAttribte tag is found
		}
		else{
			justAddedAttribute = false;
			if (containerStack.size() > 0 && mObj == containerStack.get(containerStack.size() - 1)){
				return false;
			}
			if (DEBUG){
				System.out.println("bad attribute found! " + qName);
			}
			justAddedAttribute = false;
		}
		if (justAddedAttribute){
			try {
				if ( mObj instanceof GraphObject && atts.getValue(
						"name").equalsIgnoreCase(GraphObject.EXPRESSION))
				{// temporary fix to import documents with old graphs
					mObj.getListWithName(GraphObject.EXPRESSIONS).removeAll();
					mObj.getListWithName(GraphObject.EXPRESSIONS)
					.addValueWithString(atts.getValue("value"));
					return true;
				}
				String attName = subInOldName(atts.getValue(MathObjectAttribute.NAME));
				if ( mObj.getAttributeWithName(attName) == null){
					// this attribute is no longer used in this object, ignore it
					// this is an attribute, so still return true, otherwise the parent
					// method will try to read it as a MathObject
					return true;
				}
				mObj.setAttributeValueWithString(
						attName,
						atts.getValue(MathObjectAttribute.VALUE));
				return true;
			} catch (AttributeException e) {
				// TODO Auto-generated catch block
				if (DEBUG){
					System.out.println(e.getMessage());
				}
				hadAttributeError = true;
				attributeNameInError = atts.getValue(MathObjectAttribute.NAME);
				attributeValueInError = atts.getValue(MathObjectAttribute.VALUE);
				objectWithError = mObj.getClass().getSimpleName();
				justAddedAttribute = false;
				return false;
			}
		}
		else{
			for (int i = 0; i < atts.getLength(); i++){
				if (DEBUG){
					System.out.println(atts.getValue(i).toString());
				}
			}
			hadAttributeError = true;
			attributeNameInError = atts.getValue("name");
			attributeValueInError = atts.getValue("value");
			objectWithError = mObj.getClass().getSimpleName();
			justAddedAttribute = false;
			return false;
		}
	}

	public String subInOldName(String s){
		if ( s.equals("fontSize"))
		{// this works for everything the had the old font size name
			// and was replaced by the new one
			// including graphs and number lines
			return TextObject.FONT_SIZE;
		}
		else if ( s.equals("numSides")){
			return RegularPolygonObject.NUM_SIDES;
		}
		else if ( s.equals("showBox")){
			return TextObject.SHOW_BOX;
		}
		else if ( s.equals("showAxis")){
			return GraphObject.SHOW_AXIS;
		}
		else if ( s.equals("showNumbers")){
			return GraphObject.SHOW_NUMBERS;
		}
		else if ( s.equals("showGrid")){
			return GraphObject.SHOW_GRID;
		}
		else if ( s.equals("thickness")){
			return PolygonObject.LINE_THICKNESS;
		}
		else{
			return s;
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName){
		if (MathObject.isMathObjectType(qName)){
			mObj = null;
		}
		if (qName.equals(MathObject.GROUPING) ||
				qName.equals(MathObject.PROBLEM_OBJ) ||
				qName.equals(MathObject.GENERATED_PROBLEM)){
			for ( MathObject mathObj : objectsInGroup.get(objectsInGroup.size() - 1)){
				containerStack.get(containerStack.size() - 1).addObjectFromPage(mathObj);
			}
			objectsInGroup.remove(objectsInGroup.size() - 1);
			containerStack.remove(containerStack.get(containerStack.size() - 1));
		}
		if (qName.equals("Page")){
			page = null;
		}
		else if (qName.equals(ListAttribute.LIST)){
			readingList = false;
		}
		else if (qName.equals(Document.OPEN_NOTEBOOK_DOC)){
		}
		else if (qName.equals(Document.GENERATORS)){
			readingGenerators = false;
		}
	}
}
