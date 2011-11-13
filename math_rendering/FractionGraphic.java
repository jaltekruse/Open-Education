/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package math_rendering;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Vector;

import tree.Expression;
import tree.Fraction;

public class FractionGraphic extends NodeGraphic<Fraction>{
	
	public static enum Style{
		SLASH, DIAGONAL, HORIZONTAL
	}
	
	private Style style;
	private int spaceAroundBar;
	private int sizeOverHang;
	
	public FractionGraphic(Fraction f, RootNodeGraphic gr) {
		super(f, gr);
		style = Style.HORIZONTAL;
		setMostInnerWest(this);
		setMostInnerEast(this);
		setMostInnerNorth(this);
		setMostInnerSouth(this);
	}

	@Override
	public void draw() {
		Graphics g = super.getRootNodeGraphic().getGraphics();
		g.setFont(getFont());
		FontMetrics fm = g.getFontMetrics();
		if (style == Style.SLASH || (style == Style.HORIZONTAL && getValue().getDenominator() == 1)){
			if (isSelected())
			{
				super.getRootNodeGraphic().getGraphics().setColor(getSelectedColor());
				super.getRootNodeGraphic().getGraphics().fillRect(getX1() - 2, getY1() - 2,
						getX2() - getX1() + 4, getY2() - getY1() + 4);
				super.getRootNodeGraphic().getGraphics().setColor(Color.black);
			}
			g.drawString(getValue().toString(), getX1(), getY2());
		}
		else if (style == Style.HORIZONTAL){
			g.drawString("" + getValue().getDenominator(),
					(int)Math.round(((getX2() - getX1()) - getRootNodeGraphic().getStringWidth("" 
							+ getValue().getDenominator(), getFont()))/2.0) + getX1(), getY2());
			int heightDenom = (int) Math.round((getY2() - getY1() - (2 * spaceAroundBar + 1))/2.0);
			int lineY = heightDenom + spaceAroundBar + getY1();
			
			super.getRootNodeGraphic().getGraphics().setStroke(new BasicStroke(
					(int) (1 * super.getRootNodeGraphic().DOC_ZOOM_LEVEL)));
			g.drawLine(getX1(), lineY, getX2(), lineY);
			super.getRootNodeGraphic().getGraphics().setStroke(new BasicStroke());
			
			g.drawString("" + getValue().getNumerator(),
					(int)Math.round(((getX2() - getX1()) - fm.stringWidth("" + getValue().getNumerator()))/2.0)
					+ getX1(), getY1() + heightDenom);
		}
	}
	
@Override
public void drawCursor(){
		
		int xPos = findCursorXPos();
		
		super.getRootNodeGraphic().getGraphics().setColor(Color.BLACK);
		super.getRootNodeGraphic().getGraphics().fillRect(xPos, getY1() - 3, 2, getY2() - getY1()+ 5);
		
	}
	
	@Override
	public int getMaxCursorPos(){
		return getValue().toString().length();
	}
	
	public int findCursorXPos(){
		super.getRootNodeGraphic().getGraphics().setFont(getFont());
		String numberString = getValue().toString();
		return getX1() + super.getRootNodeGraphic().getGraphics().getFontMetrics().stringWidth(
				numberString.substring(0, super.getRootNodeGraphic().getCursor().getPos()));
	}
	
	@Override
	public void setCursorPos(int xPixelPos){
		
		String numberString = getValue().toString();
		super.getRootNodeGraphic().getCursor().setValueGraphic(this);
		super.getRootNodeGraphic().getGraphics().setFont(getFont());
		
		if (xPixelPos <= getX1())
		{//if the pixel is in front of the graphic
			super.getRootNodeGraphic().getCursor().setPos(0);
			super.getRootNodeGraphic().getCursor().setValueGraphic(this);
			return;
		}
			
		else if (xPixelPos >= getX2())
		{// if the pixel is after the graphic
			super.getRootNodeGraphic().getCursor().setPos(numberString.length());
			super.getRootNodeGraphic().getCursor().setValueGraphic(this);
			return;
		}
		
		int startX, endX, xWidth;
		for (int i = 0; i < numberString.length() ; i++){
			
			startX = super.getRootNodeGraphic().getGraphics().getFontMetrics().stringWidth(
					numberString.substring(0, i)) + getX1();
			endX = super.getRootNodeGraphic().getGraphics().getFontMetrics().stringWidth(
					numberString.substring(0, i + 1)) + getX1();
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
	
	@Override
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
	
	@Override
	public void moveCursorEast(){
		if (super.getRootNodeGraphic().getCursor().getPos() < getValue().toString().length()){
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
	
	@Override
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
	
	@Override
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
	
	@Override
	public void sendCursorInFromEast(int yPos, NodeGraphic vg)
	{
		super.getRootNodeGraphic().getCursor().setValueGraphic(this);
		super.getRootNodeGraphic().getCursor().setPos(getMaxCursorPos() - 1);
	}
	
	@Override
	public void sendCursorInFromWest(int yPos, NodeGraphic vg)
	{
		super.getRootNodeGraphic().getCursor().setValueGraphic(this);
		super.getRootNodeGraphic().getCursor().setPos(1);
	}

	@Override
	public void sendCursorInFromNorth(int xPos, NodeGraphic vg){
//		super.getCompExGraphic().getCursor().setValueGraphic(this);
		setCursorPos(xPos);
		System.out.println("Dec graphic in from north, cursor: " +
				super.getRootNodeGraphic().getCursor().getValueGraphic().getValue().toString());
	}
	
	@Override
	public void sendCursorInFromSouth(int xPos, NodeGraphic vg){
//		super.getCompExGraphic().getCursor().setValueGraphic(this);
		setCursorPos(xPos);
	}

	@Override
	public int[] requestSize(Graphics g, Font f, int x1, int y1) throws Exception {
		// TODO right now prints toString and horizontal representation,
			// need to implement slash representations soon
		g.setFont(f);
		setFont(f);
		
		spaceAroundBar = (int) (2 * getRootNodeGraphic().DOC_ZOOM_LEVEL);
		sizeOverHang = (int) (2 * getRootNodeGraphic().DOC_ZOOM_LEVEL);
		
		if (style == Style.SLASH){
			String s = getValue().toString();
			int[] size = new int[2];
			size[0] = getRootNodeGraphic().getStringWidth(s, f);
			size[1] = getRootNodeGraphic().getFontHeight(f);
			setUpperHeight((int) Math.round(size[1]/2.0));
			setLowerHeight(getUpperHeight());
			super.setX1(x1);
			super.setY1(y1);
			super.setX2(x1 + size[0]);
			super.setY2(y1 + size[1]);
			return size;
		}
		else if (style == Style.HORIZONTAL){
			int numerator = getValue().getNumerator();
			int denominator = getValue().getDenominator();
			
			if (denominator != 1){
				int[] size = new int[2];
				int numerWidth = getRootNodeGraphic().getStringWidth("" + numerator, f);
				
				int denomWidth = getRootNodeGraphic().getStringWidth("" + denominator, f);
				if (numerWidth > denomWidth){
					size[0] = numerWidth + 2 * sizeOverHang;
				}
				else
				{
					size[0] = denomWidth + 2 * sizeOverHang;
				}
				size[1] = getRootNodeGraphic().getFontHeight(f) * 2 + 2 * spaceAroundBar + 1;
				setUpperHeight((int) Math.round(size[1]/2.0));
				setLowerHeight(getUpperHeight());
				super.setX1(x1);
				super.setY1(y1);
				super.setX2(x1 + size[0]);
				super.setY2(y1 + size[1]);
				return size;
			}
			else{
				String s = getValue().toString();
				int[] size = new int[2];
				size[0] = getRootNodeGraphic().getStringWidth(s, f);
				size[1] = getRootNodeGraphic().getFontHeight(f);
				setUpperHeight((int) Math.round(size[1]/2.0));
				setLowerHeight(getUpperHeight());
				super.setX1(x1);
				super.setY1(y1);
				super.setX2(x1 + size[0]);
				super.setY2(y1 + size[1]);
				return size;
			}
		}
		throw new Exception("error rendering fraction");
	}

	@Override
	public int[] requestSize(Graphics g, Font f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<NodeGraphic> getComponents() {
		// TODO Auto-generated method stub
		return new Vector<NodeGraphic>();
	}
	
	
}
