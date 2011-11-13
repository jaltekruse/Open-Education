package doc_gui;


import javax.swing.ImageIcon;
import javax.swing.JToolBar;

import doc.mathobjects.AnswerBoxObject;
import doc.mathobjects.ArrowObject;
import doc.mathobjects.CubeObject;
import doc.mathobjects.CylinderObject;
import doc.mathobjects.ExpressionObject;
import doc.mathobjects.GraphObject;
import doc.mathobjects.NumberLineObject;
import doc.mathobjects.OvalObject;
import doc.mathobjects.ParallelogramObject;
import doc.mathobjects.RectangleObject;
import doc.mathobjects.RegularPolygonObject;
import doc.mathobjects.TextObject;
import doc.mathobjects.TrapezoidObject;
import doc.mathobjects.TriangleObject;

public class ObjectToolBar extends JToolBar {

	private NotebookPanel notebookPanel;
	
	public ObjectToolBar(NotebookPanel p){
		notebookPanel = p;
		
		ImageIcon rectIcon = notebookPanel.getIcon("img/rectangle.png");
		
		OCButton rectButton = new OCButton(rectIcon, "Add Rectangle", 1, 1, 0, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				notebookPanel.getCurrentDocViewer().createMathObject(new RectangleObject());
			}
		};
		
		ImageIcon ovalIcon = notebookPanel.getIcon("img/oval.png");
		
		OCButton ovalButton = new OCButton(ovalIcon, "Add Oval", 1, 1, 1, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj

				notebookPanel.getCurrentDocViewer().createMathObject(new OvalObject());
			}
		};

		ImageIcon triangleIcon = notebookPanel.getIcon("img/triangle.png");
		
		OCButton triangleButton = new OCButton(triangleIcon, "Add Triangle", 1, 1, 1, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new TriangleObject());
			}
		};
		
		
		ImageIcon regularPolygonIcon = notebookPanel.getIcon("img/regularPolygon.png");
		
		OCButton regularPoygonButton = new OCButton(regularPolygonIcon,
				"Add Regular Polygon", 1, 1, 1, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new RegularPolygonObject());
			}
		};
		
		ImageIcon trapezoidIcon = notebookPanel.getIcon("img/trapezoid.png");
		
		OCButton trapezoidButton = new OCButton(trapezoidIcon, "Add Trapezoid", 1, 1, 1, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new TrapezoidObject());
			}
		};
		
		
		ImageIcon parallelogramIcon = notebookPanel.getIcon("img/parallelogram.png");
		
		OCButton parallelogramButton = new OCButton(parallelogramIcon,
				"Add Parallelogram", 1, 1, 1, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new ParallelogramObject());
			}
		};
		
		ImageIcon arrowIcon = notebookPanel.getIcon("img/arrow.png");
		
		OCButton arrowButton = new OCButton(arrowIcon,
				"Add Arrow", 1, 1, 1, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new ArrowObject());
			}
		};
		
		ImageIcon cubeIcon = notebookPanel.getIcon("img/cube.png");
		
		OCButton cubeButton = new OCButton(cubeIcon, "Add Cube", 1, 1, 1, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new CubeObject());
			}
		};
		
		ImageIcon cylinderIcon = notebookPanel.getIcon("img/cylinder.png");
		
		OCButton cylinderButton = new OCButton(cylinderIcon, "Add Cube", 1, 1, 1, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new CylinderObject());
			}
		};
		
		ImageIcon gridIcon = notebookPanel.getIcon("img/grid.png");
		
		OCButton gridButton = new OCButton(gridIcon, "Add Graph", 1, 1, 2, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new GraphObject());
			}
		};
		
		ImageIcon numberLineIcon = notebookPanel.getIcon("img/numberLine.png");
		
		OCButton numberLineButton = new OCButton(numberLineIcon, "Add Number Line", 1, 1, 2, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new NumberLineObject());
			}
		};
		
		ImageIcon textIcon = notebookPanel.getIcon("img/text.png");
		
		OCButton textButton = new OCButton(textIcon, "Add Text", 1, 1, 6, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new TextObject());
			}
		};
		
		ImageIcon expressionIcon = notebookPanel.getIcon("img/expression.png");
		
		OCButton expressionButton = new OCButton(expressionIcon, "Add Expression", 1, 1, 6, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new ExpressionObject());
			}
		};
		
		OCButton answerBoxButton = new OCButton("answer box", "Add Answer Box", 1, 1, 6, 0, this){
			
			@Override
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				notebookPanel.getCurrentDocViewer().createMathObject(new AnswerBoxObject());
			}
		};
	}
}
