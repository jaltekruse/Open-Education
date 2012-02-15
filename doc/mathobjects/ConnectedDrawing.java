package doc.mathobjects;

import doc.GridPoint;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class ConnectedDrawing extends MathObject {

	protected GridPoint[] points;
	
	public ConnectedDrawing(MathObjectContainer p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
	}
	
	public ConnectedDrawing(MathObjectContainer p){
		super(p);
	}

	@Override
	public void addDefaultAttributes() {
		
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public ConnectedDrawing clone() {
		ConnectedDrawing o = new ConnectedDrawing(getParentContainer());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		o.removeAllLists();
		for ( ListAttribute list : getLists()){
			o.addList(list.clone());
		}
		return o;
	}

}
