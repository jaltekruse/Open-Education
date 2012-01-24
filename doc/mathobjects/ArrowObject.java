package doc.mathobjects;

import doc.GridPoint;
import doc.Page;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class ArrowObject extends PolygonObject {
	
	private static GridPoint[] vertices = {new GridPoint(0, .25), new GridPoint(.75, .25),
		new GridPoint(.75, 0), new GridPoint(1, .5), new GridPoint(.75, 1),
		new GridPoint(.75, .75), new GridPoint(0, .75) };

	public ArrowObject(MathObjectContainer p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h, t);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}
	
	public ArrowObject(MathObjectContainer p){
		super(p);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}

	public ArrowObject() {
		addAction(PolygonObject.FLIP_HORIZONTALLY);
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

	@Override
	public GridPoint[] getVertices() {
		// TODO Auto-generated method stub
		return vertices;
	}

}
