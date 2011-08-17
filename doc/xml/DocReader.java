/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.sun.org.apache.bcel.internal.classfile.Attribute;

import doc.DatabaseOfGroupedObjects;
import doc.Document;
import doc.Page;
import doc.mathobjects.AnswerBoxObject;
import doc.mathobjects.AttributeException;
import doc.mathobjects.CubeObject;
import doc.mathobjects.ExpressionObject;
import doc.mathobjects.GraphObject;
import doc.mathobjects.Grouping;
import doc.mathobjects.HexagonObject;
import doc.mathobjects.MathObject;
import doc.mathobjects.NumberLineObject;
import doc.mathobjects.OvalObject;
import doc.mathobjects.ParallelogramObject;
import doc.mathobjects.ProblemObject;
import doc.mathobjects.RectangleObject;
import doc.mathobjects.TextObject;
import doc.mathobjects.TrapezoidObject;
import doc.mathobjects.TriangleObject;
import doc_gui.attributes.BooleanAttribute;
import doc_gui.attributes.ColorAttribute;
import doc_gui.attributes.DoubleAttribute;
import doc_gui.attributes.GridPointAttribute;
import doc_gui.attributes.IntegerAttribute;
import doc_gui.attributes.MathObjectAttribute;
import doc_gui.attributes.StringAttribute;


public class DocReader extends DefaultHandler {
	
	private Document doc;
	private Page page;
	private Grouping group;
	private DatabaseOfGroupedObjects database;
	MathObject mObj;
	boolean hadAttributeError;
	String attributeNameInError;
	String attributeValueInError;
	
	public DocReader(){
		doc = null;
		page = null;
		mObj = null;
		group = null;
		hadAttributeError = false;
		attributeNameInError = null;
		attributeValueInError = null;
	}
	
	public Document readFile (File file) throws SAXException, IOException{
		XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setContentHandler(this);
		reader.setErrorHandler(this);
		
		FileReader fileReader = new FileReader(file);
		reader.parse(new InputSource(fileReader));
		if (doc == null){
			System.out.println("error 1");
			throw new IOException("improper document format");
		}
		if (hadAttributeError){
			System.out.println("error 2");
			throw new IOException("improper document format, error with attribute '" + 
					attributeNameInError + "' with a value of '" + attributeValueInError + "'");
		}
		return doc;
	}
	
	public void startDocument(){
		
	}
	
	public void endDocument(){
	}
	
	public void startElement (String uri, String name,
		      String qName, Attributes atts){

		boolean justAddedObject = false;
		
		if (qName.equals("OpenNotebookDoc")){
			doc = new Document(atts.getValue(Document.FILENAME));
			doc.setAuthor(atts.getValue(Document.AUTHOR));
			doc.setFooter(atts.getValue(Document.FOOTER));
			doc.setHeader(atts.getValue(Document.HEADER));
		}
		if (doc != null){
			if (qName.equals("Page")){
				page = new Page(doc);
				doc.addPage(page);
				return;
			}
			if (page != null){
				if (mObj != null){
					readAttribute(uri, name, qName, atts);
					return;
				}
				
				if (qName.equals(MathObject.ANSWER_BOX)){
					mObj = new AnswerBoxObject(page);
					justAddedObject = true;
				}
				if (qName.equals(MathObject.CUBE_OBJECT)){
					mObj = new CubeObject(page);
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.EXPRESSION_OBJECT)){
					mObj = new ExpressionObject(page);
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.GRAPH_OBJECT)){
					mObj = new GraphObject(page);
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.HEXAGON_OBJECT)){
					mObj = new HexagonObject(page);
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.NUMBER_LINE_OBJECT)){
					mObj = new NumberLineObject(page);
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.OVAL_OBJECT)){
					mObj = new OvalObject(page);
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.PARALLELOGRAM_OBJECT)){
					mObj = new ParallelogramObject(page);
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.RECTANGLE_OBJECT)){
					mObj = new RectangleObject(page);
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.TEXT_OBJECT)){
					mObj = new TextObject(page);
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.TRAPEZOID_OBJECT)){
					mObj = new TrapezoidObject(page);
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.TRIANGLE_OBJECT)){
					mObj = new TriangleObject(page);
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.GROUPING)){
					if (page == null){
						group = new Grouping();
					}
					else{
						group = new Grouping(page);
					}
					mObj = group;
					justAddedObject = true;
				}
				else if (qName.equals(MathObject.PROBLEM_OBJECT)){
					if (page == null){
						group = new ProblemObject();
					}
					else{
						group = new ProblemObject(page);
					}
					mObj = group;
					justAddedObject = true;
				}
				
				if (justAddedObject){
					if ( page != null){
						System.out.println("add group:" + group);
						if ( group != null)
						{// if a tag was found in a group
							System.out.println(mObj);
							if (mObj != null && group != mObj){
								System.out.println("add to group");
								group.addObject(mObj);
							}
							else{
								System.out.println("add group to page");
								page.addObject(group);
							}
						}
						else{
							page.addObject(mObj);
						}
					}
				}
			}
		}
	}
	
	public void readAttribute(String uri, String name,
		      String qName, Attributes atts){
		MathObjectAttribute mAtt = null;
		boolean justAddedAttribute = false;

		if (qName.equals(MathObjectAttribute.BOOLEAN_ATTRIBUTE)){
			mAtt = new BooleanAttribute(atts.getValue("name"));
			justAddedAttribute = true;
		}
		else if (qName.equals(MathObjectAttribute.DOUBLE_ATTRIBUTE)){
			mAtt = new DoubleAttribute(atts.getValue("name"));
			justAddedAttribute = true;
		}
		else if (qName.equals(MathObjectAttribute.GRID_POINT_ATTRIBUTE)){
			mAtt = new GridPointAttribute(atts.getValue("name"));
			justAddedAttribute = true;
		}
		else if (qName.equals(MathObjectAttribute.INTEGER_ATTRIBUTE)){
			mAtt = new IntegerAttribute(atts.getValue("name"));
			justAddedAttribute = true;

		}
		else if (qName.equals(MathObjectAttribute.STRING_ATTRIBUTE)){
			mAtt = new StringAttribute(atts.getValue("name"));
			justAddedAttribute = true;

		}
		else if (qName.equals(MathObjectAttribute.COLOR_ATTRIBUTE)){
			mAtt = new ColorAttribute(atts.getValue("name"));
			justAddedAttribute = true;

		}
		else{
			if (mObj == group){
				return;
			}
			System.out.println("bad attribute found! " + qName);
		}
		if (justAddedAttribute){
			try {
				mAtt.setValueWithString(atts.getValue("value"));
				mObj.addAttribute(mAtt);
				mObj.setAttributeValue(mAtt.getName(), mAtt.getValue());
			} catch (AttributeException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				hadAttributeError = true;
				attributeNameInError = atts.getValue("name");
				attributeValueInError = atts.getValue("value");
				justAddedAttribute = false;
				System.out.println("error1");
				return;
			}
		}
		else{
			for (int i = 0; i < atts.getLength(); i++){
				System.out.println(atts.getValue(i).toString());
			}
			hadAttributeError = true;
			attributeNameInError = atts.getValue("name");
			attributeValueInError = atts.getValue("value");
			justAddedAttribute = false;
			return;
		}
	}
	
	public void endElement(String uri, String localName, String qName){
		if (qName.equals(MathObject.EXPRESSION_OBJECT) || 
				qName.equals(MathObject.GRAPH_OBJECT) ||
				qName.equals(MathObject.HEXAGON_OBJECT) ||
				qName.equals(MathObject.NUMBER_LINE_OBJECT) ||
				qName.equals(MathObject.OVAL_OBJECT) ||
				qName.equals(MathObject.RECTANGLE_OBJECT) ||
				qName.equals(MathObject.TEXT_OBJECT) ||
				qName.equals(MathObject.TRAPEZOID_OBJECT) ||
				qName.equals(MathObject.TRIANGLE_OBJECT) ||
				qName.equals(MathObject.CUBE_OBJECT) ||
				qName.equals(MathObject.PARALLELOGRAM_OBJECT) ||
				qName.equals(MathObject.GROUPING) ||
				qName.equals(MathObject.PROBLEM_OBJECT) ||
				qName.equals(MathObject.ANSWER_BOX)
				)
		{
			mObj = null;
		}
		if (qName.equals(MathObject.GROUPING) ||
				qName.equals(MathObject.PROBLEM_OBJECT) ){
			group = null;
		}
		if (qName.equals("Page")){
			page = null;
		}
		
		if (qName.equals("OpenNotebookDoc")){
		}
	}
	
	private class OldDocReader extends DefaultHandler {
		
		private Document doc;
		private Page page;
		private Grouping group;
		private DatabaseOfGroupedObjects database;
		MathObject mObj;
		boolean hadAttributeError;
		String attributeNameInError;
		String attributeValueInError;
		
		public OldDocReader(){
			doc = null;
			page = null;
			mObj = null;
			group = null;
			hadAttributeError = false;
			attributeNameInError = null;
			attributeValueInError = null;
		}
		
		public Document readFile (File file) throws SAXException, IOException{
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(this);
			reader.setErrorHandler(this);
			
			FileReader fileReader = new FileReader(file);
			reader.parse(new InputSource(fileReader));
			if (doc == null){
				System.out.println("error 1");
				throw new IOException("improper document format");
			}
			if (hadAttributeError){
				System.out.println("error 2");
				throw new IOException("improper document format, error with attribute '" + 
						attributeNameInError + "' with a value of '" + attributeValueInError + "'");
			}
			return doc;
		}
		
		public void startDocument(){
			
		}
		
		public void endDocument(){
		}
		
		public void startElement (String uri, String name,
			      String qName, Attributes atts){

			boolean justAddedObject = false;
			
			if (qName.equals("OpenNotebookDoc")){
				doc = new Document(atts.getValue(Document.FILENAME));
				doc.setAuthor(atts.getValue(Document.AUTHOR));
				doc.setFooter(atts.getValue(Document.FOOTER));
				doc.setHeader(atts.getValue(Document.HEADER));
			}
			if (doc != null){
				if (qName.equals("Page")){
					page = new Page(doc);
					doc.addPage(page);
					return;
				}
				if (page != null){
					if (mObj != null){
						readAttribute(uri, name, qName, atts);
					}
					
					if (qName.equals(MathObject.ANSWER_BOX)){
						mObj = new AnswerBoxObject(page);
						justAddedObject = true;
					}
					if (qName.equals(MathObject.CUBE_OBJECT)){
						mObj = new CubeObject(page);
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.EXPRESSION_OBJECT)){
						mObj = new ExpressionObject(page);
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.GRAPH_OBJECT)){
						mObj = new GraphObject(page);
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.HEXAGON_OBJECT)){
						mObj = new HexagonObject(page);
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.NUMBER_LINE_OBJECT)){
						mObj = new NumberLineObject(page);
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.OVAL_OBJECT)){
						mObj = new OvalObject(page);
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.PARALLELOGRAM_OBJECT)){
						mObj = new ParallelogramObject(page);
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.RECTANGLE_OBJECT)){
						mObj = new RectangleObject(page);
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.TEXT_OBJECT)){
						mObj = new TextObject(page);
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.TRAPEZOID_OBJECT)){
						mObj = new TrapezoidObject(page);
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.TRIANGLE_OBJECT)){
						mObj = new TriangleObject(page);
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.GROUPING)){
						if (page == null){
							group = new Grouping();
						}
						else{
							group = new Grouping(page);
						}
						mObj = group;
						justAddedObject = true;
					}
					else if (qName.equals(MathObject.PROBLEM_OBJECT)){
						if (page == null){
							group = new ProblemObject();
						}
						else{
							group = new ProblemObject(page);
						}
						mObj = group;
						justAddedObject = true;
					}
					
					if (justAddedObject){
						if ( page != null){
							System.out.println("add group:" + group);
							if ( group != null)
							{// if a tag was found in a group
								System.out.println(mObj);
								if (mObj != null && group != mObj){
									System.out.println("add to group");
									group.addObject(mObj);
								}
								else{
									System.out.println("add group to page");
									page.addObject(group);
								}
							}
							else{
								page.addObject(mObj);
							}
						}
					}
					
				}
			}
		}
		
		public void readAttribute(String uri, String name,
			      String qName, Attributes atts){
			MathObjectAttribute mAtt = null;
			boolean justAddedAttribute = false;

			if (qName.equals(MathObjectAttribute.BOOLEAN_ATTRIBUTE)){
				mAtt = new BooleanAttribute(atts.getValue("name"));
				justAddedAttribute = true;
			}
			else if (qName.equals(MathObjectAttribute.DOUBLE_ATTRIBUTE)){
				mAtt = new DoubleAttribute(atts.getValue("name"));
				justAddedAttribute = true;
			}
			else if (qName.equals(MathObjectAttribute.GRID_POINT_ATTRIBUTE)){
				mAtt = new GridPointAttribute(atts.getValue("name"));
				justAddedAttribute = true;
			}
			else if (qName.equals(MathObjectAttribute.INTEGER_ATTRIBUTE)){
				mAtt = new IntegerAttribute(atts.getValue("name"));
				justAddedAttribute = true;

			}
			else if (qName.equals(MathObjectAttribute.STRING_ATTRIBUTE)){
				mAtt = new StringAttribute(atts.getValue("name"));
				justAddedAttribute = true;

			}
			else if (qName.equals(MathObjectAttribute.COLOR_ATTRIBUTE)){
				mAtt = new ColorAttribute(atts.getValue("name"));
				justAddedAttribute = true;

			}
			else{
				if (mObj == group){
					return;
				}
				System.out.println("bad attribute found! " + qName);
			}
			if (justAddedAttribute){
				try {
					mAtt.setValueWithString(atts.getValue("value"));
					mObj.addAttribute(mAtt);
					mObj.setAttributeValue(mAtt.getName(), mAtt.getValue());
				} catch (AttributeException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					hadAttributeError = true;
					attributeNameInError = atts.getValue("name");
					attributeValueInError = atts.getValue("value");
					justAddedAttribute = false;
					System.out.println("error1");
					return;
				}
			}
			else{
				for (int i = 0; i < atts.getLength(); i++){
					System.out.println(atts.getValue(i).toString());
				}
				hadAttributeError = true;
				attributeNameInError = atts.getValue("name");
				attributeValueInError = atts.getValue("value");
				justAddedAttribute = false;
				return;
			}
		}
		
		public void endElement(String uri, String localName, String qName){
			if (qName.equals(MathObject.EXPRESSION_OBJECT) || 
					qName.equals(MathObject.GRAPH_OBJECT) ||
					qName.equals(MathObject.HEXAGON_OBJECT) ||
					qName.equals(MathObject.NUMBER_LINE_OBJECT) ||
					qName.equals(MathObject.OVAL_OBJECT) ||
					qName.equals(MathObject.RECTANGLE_OBJECT) ||
					qName.equals(MathObject.TEXT_OBJECT) ||
					qName.equals(MathObject.TRAPEZOID_OBJECT) ||
					qName.equals(MathObject.TRIANGLE_OBJECT) ||
					qName.equals(MathObject.CUBE_OBJECT) ||
					qName.equals(MathObject.PARALLELOGRAM_OBJECT) ||
					qName.equals(MathObject.GROUPING) ||
					qName.equals(MathObject.PROBLEM_OBJECT) ||
					qName.equals(MathObject.ANSWER_BOX)
					)
			{
				mObj = null;
			}
			if (qName.equals(MathObject.GROUPING) ||
					qName.equals(MathObject.PROBLEM_OBJECT) ){
				group = null;
			}
			if (qName.equals("Page")){
				page = null;
			}
			
			if (qName.equals("OpenNotebookDoc")){
			}
		}

	}

}
