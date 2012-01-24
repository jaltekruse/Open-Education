package doc;

import java.util.HashMap;
import java.util.Vector;

import doc.mathobjects.Grouping;
import doc.mathobjects.MathObject;

public class DatabaseOfGroupedObjects {

	private Vector<Grouping> groups;
	private CaseInsensitiveMap tagIndex;
	public static final String NAME = "DatabaseOfGroupedObjects";
	
	private class CaseInsensitiveMap extends HashMap<String, Grouping> {

	    public Grouping put(String key, Grouping value) {
	       return super.put(key.toLowerCase(), value);
	    }

	    public Grouping get(String key) {
	       return super.get(key.toLowerCase());
	    }
	}
	
	public DatabaseOfGroupedObjects(){
		setListOfGroupings(new Vector<Grouping>());
		tagIndex = new CaseInsensitiveMap();
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
		output += "<" + NAME + ">\n";
		for (Grouping group : groups){
			output += group.exportToXML();
		}
		output += "</" + NAME + ">\n";
		return output;
	}
}
