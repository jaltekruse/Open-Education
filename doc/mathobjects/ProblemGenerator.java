package doc.mathobjects;

import java.util.UUID;

import doc.Document;
import doc.attributes.Date;
import doc.attributes.DateAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;
import doc.attributes.StringAttribute;
import doc.attributes.UUIDAttribute;

public abstract class ProblemGenerator extends Grouping implements Cloneable{

	public static final int EASY = 10, MEDIUM = 20, HARD = 30;
	
	public static final String SCRIPTS = "scripts", UUID_STR = "uuid",
			TAGS = "tags", DIRECTIONS = "directions", DATE = "date",
			AUTHOR = "author", NAME = "group name", GROUP_UUID = "groupUUID";
	public static final String REMOVE_PROBLEM = "Remove Problem",
			GENERATE_NEW = "Generate Problems", STORE_IN_DATABASE = "Store in Database";
	private Document problemHoldingDoc;
	private boolean userEditable = true;

	public ProblemGenerator(MathObjectContainer p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		addProblemActions();
		addProblemAttributes();
	}

	public ProblemGenerator(MathObjectContainer p) {
		super(p);
		addProblemActions();
		addProblemAttributes();
	}

	public ProblemGenerator() {
		addProblemActions();
		addProblemAttributes();
	}

	private void addProblemAttributes() {
		addAttribute(new StringAttribute(NAME, false));
		addAttribute(new StringAttribute(DIRECTIONS, false));
		addAttribute(new StringAttribute(AUTHOR, false));
		addAttribute(new DateAttribute(DATE, false));
		addAttribute(new UUIDAttribute(UUID_STR, UUID.randomUUID(), false));
		addList(new ListAttribute<StringAttribute>(TAGS,
				new StringAttribute(""), false));
		addList(new ListAttribute<StringAttribute>(SCRIPTS,
				new StringAttribute("")));
	}

	private void addProblemActions() {
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		removeAction(Grouping.BRING_TO_LEFT);
		removeAction(Grouping.BRING_TO_TOP);
		removeAction(Grouping.BRING_TO_RIGHT);
		removeAction(Grouping.BRING_TO_BOTTOM);
		addAction(STORE_IN_DATABASE);
		addAction(REMOVE_PROBLEM);
		addAction(GENERATE_NEW);
	}

	public ListAttribute<StringAttribute> getScripts() {
		return (ListAttribute<StringAttribute>) getListWithName(SCRIPTS);
	}

	public abstract GeneratedProblem generateProblem(int difficuty);

	public UUID getUUID() {
		return ((UUIDAttribute) getAttributeWithName(UUID_STR)).getValue();
	}
	
	public ListAttribute<StringAttribute> getTags(){
		return (ListAttribute<StringAttribute>) getListWithName(TAGS);
	}

	public void setName(String s) {
		getAttributeWithName(NAME).setValue(s);
	}

	public String getName() {
		return ((StringAttribute) getAttributeWithName(NAME)).getValue();
	}
	
	public void setDirections(String s) {
		getAttributeWithName(DIRECTIONS).setValue(s);
	}

	public String getDirections() {
		return ((StringAttribute) getAttributeWithName(DIRECTIONS)).getValue();
	}


	public void setAuthor(String s) {
		getAttributeWithName(AUTHOR).setValue(s);
	}

	public String getAuthor() {
		return ((StringAttribute) getAttributeWithName(AUTHOR)).getValue();
	}

	public void setDate(Date d) {
		getAttributeWithName(DATE).setValue(d);
	}

	public Date getDate() {
		return ((DateAttribute) getAttributeWithName(DATE)).getValue();
	}

	public Document getProblemHoldingDocument() {
		return problemHoldingDoc;
	}

	public void setProblemHoldingDocument(Document parentDocument) {
		this.problemHoldingDoc = parentDocument;
	}

	@Override
	public ProblemGenerator clone() {
		VariableValueInsertionProblem o = new VariableValueInsertionProblem(
				getParentContainer());
		o.removeAllAttributes();
		for (MathObjectAttribute<?> mAtt : getAttributes()) {
			o.addAttribute(mAtt.clone());
		}
		o.removeAllLists();
		for (ListAttribute<?> list : getLists()) {
			o.addList(list.clone());
		}
		for (MathObject mObj : getObjects()) {
			mObj.setParentContainer(null);
			o.addObjectFromPage(mObj.clone());
		}
		return o;
	}

	public boolean isUserEditable() {
		return userEditable;
	}

	protected void setUserEditable(boolean userEditable) {
		this.userEditable = userEditable;
	}
}
