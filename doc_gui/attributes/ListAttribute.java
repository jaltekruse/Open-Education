package doc_gui.attributes;

import java.util.Vector;

/**
 * Class to associate a name with a list of attributes. All values should 
 * have a connection to one another and the name of the list. Examples include
 * a list of points to graph, or a list steps performed while solving an algebra
 * problem.
 * 
 * @author jason
 *
 */
public class ListAttribute <K> {

	private String name;
	
	private Vector<K> values;
	
	public ListAttribute(String s){
		setName(s);
		values = new Vector<K>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void addValue(K value){
		values.add(value);
	}

	public K getValue(int i){
		return values.get(i);
	}
	
	public boolean removeValue(K value){
		return values.remove(value);
	}
	
	public void removeAll(){
		values.removeAllElements();
	}
	
	public void setValues(Vector<K> values) {
		this.values = values;
	}

	public Vector<K> getValues() {
		return values;
	}
	
	
}
