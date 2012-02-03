package doc.mathobjects;

import doc.GridPoint;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class ConnectedDrawing extends MathObject {

	protected GridPoint[] points;
	
	public ConnectedDrawing(MathObjectContainer p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		// TODO Auto-generated constructor stub
	}
	
	public ConnectedDrawing(MathObjectContainer p){
		super(p);
	}

	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectedDrawing clone() {
		// TODO Auto-generated method stub
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
