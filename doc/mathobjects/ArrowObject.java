package doc.mathobjects;

import doc.GridPoint;
import doc.Page;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class ArrowObject extends PolygonObject {

	public ArrowObject(MathObjectContainer p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h, t);
		addInitialPoints();
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}
	
	public ArrowObject(MathObjectContainer p){
		super(p);
		addInitialPoints();
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}

	public ArrowObject() {
		addInitialPoints();
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}
	
	@Override
	public void addInitialPoints() {
		// TODO Auto-generated method stub
		addVertex(new GridPoint(0, .25));
		addVertex(new GridPoint(.75, .25));
		addVertex(new GridPoint(.75, 0));
		addVertex(new GridPoint(1, .5));
		addVertex(new GridPoint(.75, 1));
		addVertex(new GridPoint(.75, .75));
		addVertex(new GridPoint(0, .75));
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return ARROW_OBJECT;
	}

	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrowObject clone() {
		ArrowObject o = new ArrowObject(getParentContainer());
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
