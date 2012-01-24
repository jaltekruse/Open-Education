package doc_gui;

import javax.swing.ImageIcon;
import javax.swing.JToolBar;

import doc.mathobjects.AnswerBoxObject;
import doc.mathobjects.ArrowObject;
import doc.mathobjects.CubeObject;
import doc.mathobjects.CylinderObject;
import doc.mathobjects.ExpressionObject;
import doc.mathobjects.GraphObject;
import doc.mathobjects.MathObject;
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

	public ObjectToolBar(NotebookPanel p) {
		notebookPanel = p;
		OCButton objButton;
		ImageIcon objIcon;

		for (final String s : MathObject.objectTypes) {
			if (s.equals(MathObject.GROUPING)
					|| s.equals(MathObject.PROBLEM_OBJ)
					|| s.equals(MathObject.GENERATED_PROBLEM)) {
				// all classes that extend grouping cannot be created with a
				// button and mouse drag they do not need buttons
				continue;
			}
			if (s.equals(MathObject.CONE_OBJECT)
					|| s.equals(MathObject.PYRAMID_OBJECT)) {
				// these objects have not been created yet, they will be added
				// soon
				continue;
			}
			objIcon = notebookPanel.getIcon("img/"
					+ MathObject.getObjectImageName(s));

			objButton = new OCButton(objIcon, "Add " + s, 1, 1, 0, 0, this) {

				@Override
				public void associatedAction() {
					// pass even down to current doc window for placing a
					// mathObj
					notebookPanel.getCurrentDocViewer().createMathObject(
							MathObject.getNewInstance(s));
				}
			};
		}
//		objButton = new OCButton("Answer Box", "Add Answer box", 1, 1, 0, 0,
//				this) {
//
//			@Override
//			public void associatedAction() {
//				// pass even down to current doc window for placing a mathObj
//				notebookPanel.getCurrentDocViewer().createMathObject(
//						new AnswerBoxObject());
//			}
//		};
//		OCButton generateButton = new OCButton("Generate Problems", "Generate Probelms",
//				1, 1, 3, 0, this){
//			
//			@Override
//			public void associatedAction(){
//				notebookPanel.createProbelmDialog();
//				notebookPanel.setProblemDialogVisible(true);
//			}
//		};
		
//		OCButton cloneButton = new OCButton("Generate Problems", "Generate Probelms",
//				1, 1, 3, 0, this){
//			
//			@Override
//			public void associatedAction(){
//				notebookPanel.addDoc(notebookPanel.getCurrentDocViewer().getDoc().clone());
//			}
//		};
	}
}
