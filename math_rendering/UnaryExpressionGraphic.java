/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package math_rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Vector;

import expression.Expression;
import expression.Node;

public class UnaryExpressionGraphic extends ExpressionGraphic {

	private int space;
	private NodeGraphic childGraphic;
	
	public UnaryExpressionGraphic(Expression v,
			RootNodeGraphic compExGraphic) {
		super(v, compExGraphic);
		setMostInnerNorth(this);
		setMostInnerSouth(this);
	}

	@Override
	public void draw() {
		if (isSelected()){
			getRootNodeGraphic().getGraphics().setColor(getSelectedColor());
			getRootNodeGraphic().getGraphics().fillRect(symbolX1, symbolY1, symbolX2 - symbolX1, symbolY2 - symbolY1);
			getRootNodeGraphic().getGraphics().setColor(Color.black);
		}
		getRootNodeGraphic().getGraphics().setFont(getFont());
		getRootNodeGraphic().getGraphics().drawString(getValue().getOperator().getSymbol(),
				symbolX1, symbolY2);
	}
	
	public void drawCursor(){
		String opString = getValue().getOperator().getSymbol();
		
		int xPos = symbolX1 + super.getRootNodeGraphic().getGraphics().getFontMetrics().stringWidth(
				opString.substring(0, super.getRootNodeGraphic().getCursor().getPos()));
		
		if ( super.getRootNodeGraphic().getCursor().getPos() == getMaxCursorPos()){
			xPos += space;
		}
		super.getRootNodeGraphic().getGraphics().setColor(Color.BLACK);
		super.getRootNodeGraphic().getGraphics().fillRect(xPos, getY1() - 3, 2, getY2() - getY1() + 5);
		
	}
	
	public int findCursorXPos(){
		super.getRootNodeGraphic().getGraphics().setFont(getFont());
		String numberString = getValue().toString();
		return getX1() + super.getRootNodeGraphic().getGraphics().getFontMetrics().stringWidth(
				numberString.substring(0, super.getRootNodeGraphic().getCursor().getPos()));
	}
	
	public int getMaxCursorPos(){
		return getValue().getOperator().getSymbol().length();
	}
	
	public void setCursorPos(int xPixelPos){
		
		String valueString = getValue().getOperator().getSymbol();
		System.out.println("set Unary Cursor pos");
		
		if (xPixelPos < super.symbolX1){
			super.getRootNodeGraphic().getCursor().setPos(0);
			super.getRootNodeGraphic().getCursor().setValueGraphic(this);
			return;
		}
			
		else if (xPixelPos > super.symbolX2){
			getChildGraphic().setCursorPos(xPixelPos);
			return;
		}
		
		int startX, endX, xWidth;
		for (int i = 0; i < valueString.length() ; i++){
			
			startX = super.getRootNodeGraphic().getGraphics().getFontMetrics().stringWidth(
					valueString.substring(0, i)) + getX1();
			endX = super.getRootNodeGraphic().getGraphics().getFontMetrics().stringWidth(
					valueString.substring(0, i + 1)) + getX1();
			xWidth = endX - startX;
			if (startX <= xPixelPos && endX >= xPixelPos)
			{//if the x position is inside of a character, check if it is on the first or second
				//half of the character and set the cursor accordingly
				if (endX - xPixelPos > xWidth/2){
					super.getRootNodeGraphic().getCursor().setPos( i );
				}
				else{
					super.getRootNodeGraphic().getCursor().setPos( i + 1 );
				}
				super.getRootNodeGraphic().getCursor().setValueGraphic(this);
				return;
			}
		}
		
	}
	
	public void moveCursorWest(){
		if (super.getRootNodeGraphic().getCursor().getPos() > 0){
			super.getRootNodeGraphic().getCursor().setPos( super.getRootNodeGraphic().getCursor().getPos() - 1); 
		}
		else{
			if (getWest() == null)
			{
				return;
			}
			else
			{
				getWest().sendCursorInFromEast((getY2() - getY1())/2, this);
				return;
			}
		}
	}
	
	public void moveCursorEast(){
		if (super.getRootNodeGraphic().getCursor().getPos() < getMaxCursorPos()){
			super.getRootNodeGraphic().getCursor().setPos( super.getRootNodeGraphic().getCursor().getPos() + 1); 
		}
		else{
			if (getEast() == null)
			{
				return;
			}
			else
			{
				getEast().sendCursorInFromWest((getY2() - getY1())/2, this);
				return;
			}
		}
	}
	
	public void moveCursorNorth(){
		if (getNorth() == null)
		{
			System.out.println("nothing to north");
			return;
		}
		else
		{
			getNorth().sendCursorInFromSouth(findCursorXPos(), this);
			return;
		}
	}
	
	public void moveCursorSouth(){
		if (getSouth() == null)
		{
			System.out.println("nothing to south");
			return;
		}
		else
		{
			getSouth().sendCursorInFromNorth(findCursorXPos(), this);
			return;
		}
	}
	
	public void sendCursorInFromEast(int yPos, NodeGraphic vg){
		super.getRootNodeGraphic().getCursor().setValueGraphic(this);
		super.getRootNodeGraphic().getCursor().setPos(getMaxCursorPos() - 1);
	}
	
	public void sendCursorInFromWest(int yPos, NodeGraphic vg){
		super.getRootNodeGraphic().getCursor().setValueGraphic(this);
		super.getRootNodeGraphic().getCursor().setPos(1);
	}
	
	public void sendCursorInFromNorth(int xPos, NodeGraphic vg){
		setCursorPos(xPos);
	}
	
	public void sendCursorInFromSouth(int xPos, NodeGraphic vg){
		setCursorPos(xPos);
	}
	

	@Override
	public int[] requestSize(Graphics g, Font f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] requestSize(Graphics g, Font f, int x1, int y1)
			throws Exception {
		g.setFont(f);
		setFont(f);
		
		space = (int) (2 * super.getRootNodeGraphic().DOC_ZOOM_LEVEL);
		Node tempChild = super.getValue().getChild(0);
		NodeGraphic childValGraphic = null;
		int[] childSize = {0,0};
		int[] symbolSize = {0, 0};
		int[] totalSize = {0, 0};
		
		System.out.println("UnaryValue: " + value.toString());
		symbolSize[0] = super.getRootNodeGraphic().getStringWidth(value.getOperator().getSymbol(), f) + space;
		symbolSize[1] = super.getRootNodeGraphic().getFontHeight(f);
		childValGraphic = makeNodeGraphic(tempChild);
		
		setChildGraphic(childValGraphic);
		super.getRootNodeGraphic().getComponents().add(childValGraphic);
		
		childSize = childValGraphic.requestSize(g, f, x1 + symbolSize[0], y1);
		
		//set the west and east fields for inside and outside of the expression
		setMostInnerWest(this);
		setEast(childValGraphic);
		childValGraphic.setWest(this);
		setMostInnerEast(childValGraphic);
		
		symbolY1 = y1 + childValGraphic.getUpperHeight() - (int) Math.round(symbolSize[1]/2.0);
		symbolY2 = symbolY1 + symbolSize[1];
		symbolX1 = x1;
		symbolX2 = x1 + symbolSize[0];
		
		setUpperHeight(childValGraphic.getUpperHeight());
		setLowerHeight(childValGraphic.getLowerHeight());
		
		totalSize[0] = symbolSize[0] + childSize[0];
		totalSize[1] = childSize[1];
		super.setX1(x1);
		super.setY1(y1);
		super.setX2(x1 + totalSize[0]);
		super.setY2(y1 + totalSize[1]);
		// TODO Auto-generated method stub
		return totalSize;
	}
	
	public NodeGraphic getChildGraphic(){
		return childGraphic;
	}
	
	protected void setChildGraphic(NodeGraphic vg){
		childGraphic = vg;
	}

	@Override
	public Vector<NodeGraphic> getComponents() {
		// TODO Auto-generated method stub
		Vector<NodeGraphic> children = new Vector<NodeGraphic>();
		children.add(childGraphic);
		return children;
	}

}
