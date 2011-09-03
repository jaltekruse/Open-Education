package doc;

import java.util.Vector;

import doc.mathobjects.Grouping;
import doc.mathobjects.MathObject;

public class DatabaseOfGroupedObjects {

	private Vector<Grouping> groups;
	public static final String NAME = "DatabaseOfGroupedObjects";
	
	public DatabaseOfGroupedObjects(){
		setListOfGroupings(new Vector<Grouping>());
	}

	public void setListOfGroupings(Vector<Grouping> objects) {
		this.groups = objects;
	}
	
	public void removeGrouping(MathObject mObj){
		groups.remove(mObj);
	}

	public Vector<Grouping> getGroupings() {
		return groups;
	}
	
	/**
	 * Add a MathObject to this page.
	 * @param group - object to add
	 * @return true if add was successful, if object did not fit in printable area it is not added
	 */
	public boolean addGrouping(Grouping group){
		
		if ( ! groups.contains(group)){
			groups.add(group);
		}
		return true;
	}
	
	public String exportToXML(){
		String output = "";
		output += "<" + NAME + "\n";
		for (Grouping group : groups){
			output += group.exportToXML();
		}
		output += "</" + NAME + "\n";
		return output;
	}
}
