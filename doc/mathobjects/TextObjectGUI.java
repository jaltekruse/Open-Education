/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.TreeMap;

public class TextObjectGUI extends MathObjectGUI{


	public void drawMathObject(TextObject object, Graphics g, Point pageOrigin,
			float zoomLevel) {
		if ( ! object.getText().equals("")){
			Font f = g.getFont();
			
			int xOrigin = (int) (pageOrigin.getX() + object.getxPos() * zoomLevel);
			int yOrigin = (int) (pageOrigin.getY() + object.getyPos() * zoomLevel);
			int width = (int) (object.getWidth() * zoomLevel);
			int height = (int) (object.getHeight() * zoomLevel);
			float fontSize = object.getFontSize() * zoomLevel;
			
			g.setColor(Color.BLACK);
		    Graphics2D graphics2D = (Graphics2D)g;
	//		graphics2D.setFont(new Font(fontMap));
	//	    System.out.println("fontsize: " + fontSize);
			Font font = f.deriveFont(fontSize);
	//		System.out.println(font.getSize2D());
			g.setFont(font);
		    GraphicsEnvironment.getLocalGraphicsEnvironment();
		    String message = object.getText();
		    AttributedString messageAS = new AttributedString(message);
		    messageAS.addAttribute(TextAttribute.FONT, g.getFont());
		    AttributedCharacterIterator messageIterator = messageAS.getIterator();
		    FontRenderContext messageFRC = graphics2D.getFontRenderContext();
		    LineBreakMeasurer messageLBM = new LineBreakMeasurer(messageIterator, messageFRC);
	
		    Insets insets = new Insets(2,2,2,2);
		    float wrappingWidth = width - insets.left - insets.right;
		    float x = xOrigin + insets.left;
		    float y = yOrigin + insets.top;
	
		    while (messageLBM.getPosition() < messageIterator.getEndIndex()) {
		      TextLayout textLayout = messageLBM.nextLayout(wrappingWidth);
		      y += textLayout.getAscent();
		      textLayout.draw(graphics2D, x, y);
		      y += textLayout.getDescent() + textLayout.getLeading();
		      x = xOrigin + insets.left;
		    }
		    
			if ( ((BooleanAttribute)object.getAttributeWithName("showBox")).getValue()){
				g.setColor(Color.BLUE);
				g.drawRect(xOrigin, yOrigin, width, height);
			}

			g.setFont(f);
		}
	}
	

//	public void drawMathObject(TextObject object, Graphics g, Point pageOrigin,
//			float zoomLevel) {
//		// TODO Auto-generated method stub
//		
//		
////		System.out.println("draw text");
//		Font f = g.getFont();
//		
//		g.setColor(Color.BLACK);
//		int xOrigin = (int) (pageOrigin.getX() + object.getxPos() * zoomLevel);
//		int yOrigin = (int) (pageOrigin.getY() + object.getyPos() * zoomLevel);
//		int width = (int) (object.getWidth() * zoomLevel);
//		int height = (int) (object.getHeight() * zoomLevel);
//		int fontSize = (int) (object.getFontSize() * zoomLevel);
//		
//		Graphics2D g2d = (Graphics2D)g;
//		g2d.setFont(new Font("SansSerif", 0, fontSize));
//		//draw lines of text with loop
//		
//		int textyPos = yOrigin + g.getFontMetrics().getHeight() + (int) (3 * zoomLevel);
//		int textxPos = xOrigin + (int) (3 * zoomLevel);
//		String s = object.getText();
//		g.drawString(s, textxPos, textyPos);
//		textyPos += g.getFontMetrics().getHeight();
//		
//		if ( ((BooleanAttribute)object.getAttributeWithName("showBox")).getValue()){
//			g.setColor(Color.BLUE);
//			g.drawRect(xOrigin, yOrigin, width, height);
//		}
//
//		g.setFont(f);
//	}

}
