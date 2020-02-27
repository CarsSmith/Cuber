import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

/**
 * This class represents the main panel of the program, one that initially set to display the 
 * Cubes of the Rubix cube in 2D.
 * 
 * @author Carson Smith
 */
@SuppressWarnings("serial")
public class CuberPanel extends JPanel {

	private FacePanel[] cubeFacePanels = new FacePanel[6];
	private ButtonPanel buttonPanel;
	private JPanel infoPanel;
	private JPanel titlePanel;
	private JPanel optionsPanel;
	private JPanel threeDPanel;
	private int DIM = 4;
	private String mode;
	
	/**
	 * CONSTRUCTOR - Actions done upon the creation of a CuberPanel.
	 * 
	 * Creates the entire main panel of the program.
	 */
	public CuberPanel() {
		mode = "2D";
		this.setLayout(new GridBagLayout());
		createFacePanels();
		setReferences();
		createOtherPanels();
	}

	/**
	 * This method creates each of the 6 face panels that represent the faces of a
	 * rubix cube.
	 */
	private void createFacePanels() {
		HumanSwipeListener list = new HumanSwipeListener();
		FacePanel whiteFacePanel = new FacePanel(Color.WHITE, 1, 1, list, DIM);
		cubeFacePanels[0] = whiteFacePanel;
		FacePanel redFacePanel = new FacePanel(Color.RED, 0, 1, list, DIM);
		cubeFacePanels[1] = redFacePanel;
		FacePanel blueFacePanel = new FacePanel(Color.BLUE, 1, 2, list, DIM);
		cubeFacePanels[2] = blueFacePanel;
		FacePanel orangeFacePanel = new FacePanel(Color.ORANGE, 2, 1, list, DIM);
		cubeFacePanels[3] = orangeFacePanel;
		FacePanel greenFacePanel = new FacePanel(Color.GREEN, 1, 0, list, DIM);
		cubeFacePanels[4] = greenFacePanel;
		FacePanel yellowFacePanel = new FacePanel(Color.YELLOW, 3, 1, list, DIM);
		cubeFacePanels[5] = yellowFacePanel;

		for(FacePanel pan : cubeFacePanels) {
			pan.setPreferredSize(new Dimension(200, 200));     //Set the panel's preferred size.
			GridBagConstraints c = createFacePanelConstraints(pan); //Make personalized constraints for the panel
			this.add(pan, c);                                  //Add the panel and the constraints.
		}
	}

	/**
	 * This method simply sets the references of each of the 6 face panels to each other, so they
	 * can refer to their appropriate neighbor when changing the color of any particular face of the
	 * cube.
	 */
	private void setReferences() {
		cubeFacePanels[0].setReferences(cubeFacePanels[4], cubeFacePanels[2], cubeFacePanels[3], 
				cubeFacePanels[1], cubeFacePanels[5]);
		cubeFacePanels[1].setReferences(cubeFacePanels[4], cubeFacePanels[2], cubeFacePanels[0], 
				cubeFacePanels[5], cubeFacePanels[3]);
		cubeFacePanels[2].setReferences(cubeFacePanels[0], cubeFacePanels[5], cubeFacePanels[3], 
				cubeFacePanels[1], cubeFacePanels[4]);
		cubeFacePanels[3].setReferences(cubeFacePanels[4], cubeFacePanels[2], cubeFacePanels[5], 
				cubeFacePanels[0], cubeFacePanels[1]);
		cubeFacePanels[4].setReferences(cubeFacePanels[5], cubeFacePanels[0], cubeFacePanels[3], 
				cubeFacePanels[1], cubeFacePanels[2]);
		cubeFacePanels[5].setReferences(cubeFacePanels[4], cubeFacePanels[2], cubeFacePanels[1], 
				cubeFacePanels[3], cubeFacePanels[0]);
	}

	/**
	 * This method creates the 4 surrounding panels of the facePanels.
	 */
	private void createOtherPanels() {

		GridBagConstraints c;

		titlePanel = new JPanel();
		titlePanel.setPreferredSize(new Dimension(200, 200));
		titlePanel.setBackground(Color.BLACK);
		c = createShortPanelConstraints(titlePanel, false);
		this.add(titlePanel, c);

		buttonPanel = new ButtonPanel(new ShuffleClickListener(), new ModeClickListener());
		c = createShortPanelConstraints(buttonPanel, true);
		this.add(buttonPanel, c);

		infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(400, 200));
		infoPanel.setBackground(Color.GREEN);
		c = createLongPanelConstraints(infoPanel, false); //false for infoPanel
		
		this.add(infoPanel, c);

		optionsPanel = new JPanel();
		JLabel resizeLabel = new JLabel("Resize Cube Dimensions:");
		JSlider dimensionSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, DIM);
		dimensionSlider.setMajorTickSpacing(1);
		dimensionSlider.setPaintTicks(true);
		dimensionSlider.setPaintLabels(true);
		dimensionSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		dimensionSlider.addChangeListener(new ResizeListener());
		optionsPanel.add(resizeLabel);
		optionsPanel.add(dimensionSlider);
		optionsPanel.setPreferredSize(new Dimension(400, 200));
		optionsPanel.setLayout(new GridBagLayout());
		c = createLongPanelConstraints(optionsPanel, true); //true for optionsPanel
		this.add(optionsPanel, c);  
	}
	
	/**
	 * This method removes all panels currently on the Cuber Panel. This is used when changing the
	 * mode from 2D mode to 3D mode.
	 * 
	 * @param mode - The current mode of the CuberPanel.
	 */
	private void removeAllPanels(String mode) {
		if(mode == "2D") {
			//buttonPanel.setVisible(false); Use this, move it to a new location. Top corner.
			this.remove(buttonPanel);
			this.remove(titlePanel);
			this.remove(infoPanel);
			this.remove(optionsPanel);
			removeAllFacePanels();
			create3DModeLayout();
			this.revalidate();
			this.repaint();
		} else if(mode == "3D") {
			//buttonPanel.setVisible(true);
			this.remove(threeDPanel);
			this.remove(buttonPanel);
			create2DModeLayout();
			this.revalidate();
			this.repaint();
		}
	}
	
	/**
	 * A method that simply removes all the face panels from the cuberPanel.
	 */
	private void removeAllFacePanels() {
		for(int i = 0; i < cubeFacePanels.length; i++) {
			this.remove(cubeFacePanels[i]);
		}
	}

	/**
	 * A method called to return the cuberPanel to a 2D layout after the mode has been changed to
	 * 3D mode.
	 */
	private void create2DModeLayout() {
		GridBagConstraints c;
		
		for(FacePanel pan : cubeFacePanels) {
			c = createFacePanelConstraints(pan); //Make personalized constraints for the panel
			this.add(pan, c);                                  //Add the panel and the constraints.
		}
		
		c = createShortPanelConstraints(titlePanel, false);
		this.add(titlePanel, c);
		
		c = createShortPanelConstraints(buttonPanel, true);
		this.add(buttonPanel, c);
		
		c = createLongPanelConstraints(infoPanel, false); //false for infoPanel
		this.add(infoPanel, c);

		c = createLongPanelConstraints(optionsPanel, true); //true for optionsPanel
		this.add(optionsPanel, c); 
		
	}
	
	/**
	 * A method for creating a visual layout for 3D mode.
	 * 
	 * Completely WIP.
	 */
	private void create3DModeLayout() {
		threeDPanel = new JPanel();
		threeDPanel.setPreferredSize(new Dimension(600, 600));
		threeDPanel.setBackground(Color.CYAN);
		GridBagConstraints c = create3DModeConstraints(threeDPanel);
		this.add(threeDPanel, c);
		this.add(buttonPanel);
	}

	//The next 4 methods are redundant and should be refactored so constraints
	//are all generated in a single method with parameters.
	
	/** 
	 * A helper method for the creation of appropriate GridBagConstraints for the 
	 * creation of the 3D mode's main panel.
	 * 
	 * @param panelReference - The panel that needs the constraints
	 * @return - the finished constraints for the panel.
	 */
	private GridBagConstraints create3DModeConstraints(JPanel panelReference) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = 3;
		constraints.gridheight = 3;
		constraints.gridx = 0;
		constraints.gridy = 0;
		return constraints;
	}
	
	private GridBagConstraints createShortPanelConstraints(JPanel panelReference, boolean flag) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		if(flag) {
			constraints.gridy = 2;
		} else {
			constraints.gridy = 0;
		}
		return constraints;
	}

	private GridBagConstraints createLongPanelConstraints(JPanel panelReference, boolean flag) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.gridx = 2;
		if(flag) {
			constraints.gridy = 2;
		} else {
			constraints.gridy = 0;
		}
		return constraints;
	}

	private GridBagConstraints createFacePanelConstraints(FacePanel panelReference) {

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.gridx = panelReference.getXValue();
		constraints.gridy = panelReference.getYValue();

		return constraints;

	}

	/**
	 * First rendition of a rotation method, used to make a left rotation of the cube.
	 * 
	 * @param square - The square being used to spurn the rotation.
	 */
	public void leftRotate(FaceSquare square) {
		int squarePanelIndex = square.getIntendedPanel();
		int row = square.getRow();
		FacePanel front = cubeFacePanels[squarePanelIndex];
		FaceSquare[][] frontSquares = front.getSquareArray();
		FacePanel left = front.getLeftRef();
		FaceSquare[][] leftSquares = left.getSquareArray();
		FacePanel back = front.getBackRef();
		FaceSquare[][] backSquares = back.getSquareArray();
		FacePanel right = front.getRightRef();
		FaceSquare[][] rightSquares = right.getSquareArray();
		Color[] frontColors = new Color[DIM];
		Color[] leftColors = new Color[DIM];
		Color[] backColors = new Color[DIM];
		Color[] rightColors = new Color[DIM];
		if(squarePanelIndex == 2) {
			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[i][row].getCurrentColor();
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftColors[i] = leftSquares[(DIM - 1) - row][i].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[i][(DIM - 1) - row].getCurrentColor();
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightColors[i] = rightSquares[row][i].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[i][row].setSquareColor(rightColors[i]);
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftSquares[(DIM - 1) - row][i].setSquareColor(frontColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[i][(DIM - 1) - row].setSquareColor(leftColors[i]);
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightSquares[row][i].setSquareColor(backColors[i]);
			}
		} else if(squarePanelIndex == 4) {
			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[i][row].getCurrentColor();
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftColors[i] = leftSquares[row][i].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[i][(DIM - 1) - row].getCurrentColor();
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightColors[i] = rightSquares[(DIM - 1) - row][i].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[i][row].setSquareColor(rightColors[i]);
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftSquares[row][i].setSquareColor(frontColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[i][(DIM - 1) - row].setSquareColor(leftColors[i]);
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightSquares[(DIM - 1) - row][i].setSquareColor(backColors[i]);
			}
		} else {
			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[i][row].getCurrentColor();
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftColors[i] = leftSquares[i][row].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[i][row].getCurrentColor();
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightColors[i] = rightSquares[i][row].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[i][row].setSquareColor(rightColors[i]);
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftSquares[i][row].setSquareColor(frontColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[i][row].setSquareColor(leftColors[i]);
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightSquares[i][row].setSquareColor(backColors[i]);
			}
		}
		if(row == 0 ) {
			front.getUpRef().rotateSquaresRight();
		} else if(row == (DIM - 1)) {
			front.getDownRef().rotateSquaresLeft();
		}
	}

	/**
	 * First rendition of a rotation method, used to make a right rotation of the cube.
	 * 
	 * @param square - The square being used to spurn the rotation.
	 */
	public void rightRotate(FaceSquare square) {
		int squarePanelIndex = square.getIntendedPanel();
		int row = square.getRow();
		FacePanel front = cubeFacePanels[squarePanelIndex];
		FaceSquare[][] frontSquares = front.getSquareArray();
		FacePanel left = front.getLeftRef();
		FaceSquare[][] leftSquares = left.getSquareArray();
		FacePanel back = front.getBackRef();
		FaceSquare[][] backSquares = back.getSquareArray();
		FacePanel right = front.getRightRef();
		FaceSquare[][] rightSquares = right.getSquareArray();
		Color[] frontColors = new Color[DIM];
		Color[] leftColors = new Color[DIM];
		Color[] backColors = new Color[DIM];
		Color[] rightColors = new Color[DIM];
		if(squarePanelIndex == 2) { //special case
			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[i][row].getCurrentColor();
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftColors[i] = leftSquares[(DIM - 1) - row][i].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[i][(DIM - 1) - row].getCurrentColor();
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightColors[i] = rightSquares[row][i].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[i][row].setSquareColor(leftColors[i]);
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftSquares[(DIM - 1) - row][i].setSquareColor(backColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[i][(DIM - 1) - row].setSquareColor(rightColors[i]);
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightSquares[row][i].setSquareColor(frontColors[i]);
			}
		} else if(squarePanelIndex == 4) {
			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[i][row].getCurrentColor();
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftColors[i] = leftSquares[row][i].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[i][(DIM - 1) - row].getCurrentColor();
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightColors[i] = rightSquares[(DIM - 1) - row][i].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[i][row].setSquareColor(leftColors[i]);
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftSquares[row][i].setSquareColor(backColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[i][(DIM - 1) - row].setSquareColor(rightColors[i]);
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightSquares[(DIM - 1) - row][i].setSquareColor(frontColors[i]);
			}
		} else {
			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[i][row].getCurrentColor();
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftColors[i] = leftSquares[i][row].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[i][row].getCurrentColor();
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightColors[i] = rightSquares[i][row].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[i][row].setSquareColor(leftColors[i]);
			}
			for(int i = 0; i < leftSquares.length; i++) {
				leftSquares[i][row].setSquareColor(backColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[i][row].setSquareColor(rightColors[i]);
			}
			for(int i = 0; i < rightSquares.length; i++) {
				rightSquares[i][row].setSquareColor(frontColors[i]);
			}
		}
		if(row == 0) {
			front.getUpRef().rotateSquaresLeft();
		} else if(row == (DIM - 1)) {
			front.getDownRef().rotateSquaresRight();
		}
	}

	/**
	 * First rendition of a rotation method, used to make an upwards rotation of the cube.
	 * 
	 * @param square - The square being used to spurn the rotation.
	 */
	public void upRotate(FaceSquare square) {
		int squarePanelIndex = square.getIntendedPanel();
		int col = square.getCol();
		FacePanel front = cubeFacePanels[squarePanelIndex];
		FaceSquare[][] frontSquares = front.getSquareArray();

		FacePanel up = front.getUpRef();
		FaceSquare[][] upSquares = up.getSquareArray();
		FacePanel back = front.getBackRef();
		FaceSquare[][] backSquares = back.getSquareArray();
		FacePanel down = front.getDownRef();
		FaceSquare[][] downSquares = down.getSquareArray();
		Color[] frontColors = new Color[DIM];
		Color[] upColors = new Color[DIM];
		Color[] backColors = new Color[DIM];
		Color[] downColors = new Color[DIM];

		if(squarePanelIndex == 1) { //special case
			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[col][i].getCurrentColor();
			}
			for(int i = 0; i < upSquares.length; i++) {
				upColors[i] = upSquares[i][col].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[(DIM - 1) - col][i].getCurrentColor();
			}
			for(int i = 0; i < downSquares.length; i++) {
				downColors[i] = downSquares[i][(DIM - 1) - col].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[col][i].setSquareColor(downColors[i]);
			}
			for(int i = 0; i < upSquares.length; i++) {
				upSquares[i][col].setSquareColor(frontColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[(DIM - 1) - col][i].setSquareColor(upColors[i]);
			}
			for(int i = 0; i < downSquares.length; i++) {
				downSquares[i][(DIM - 1) - col].setSquareColor(backColors[i]);
			}
		} else if(squarePanelIndex == 3) {
			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[col][i].getCurrentColor();
			}
			for(int i = 0; i < upSquares.length; i++) {
				upColors[i] = upSquares[i][(DIM - 1) - col].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[(DIM - 1) - col][i].getCurrentColor();
			}
			for(int i = 0; i < downSquares.length; i++) {
				downColors[i] = downSquares[i][col].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[col][i].setSquareColor(downColors[i]);
			}
			for(int i = 0; i < upSquares.length; i++) {
				upSquares[i][(DIM - 1) - col].setSquareColor(frontColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[(DIM - 1) - col][i].setSquareColor(upColors[i]);
			}
			for(int i = 0; i < downSquares.length; i++) {
				downSquares[i][col].setSquareColor(backColors[i]);
			}
		} else {
			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[col][i].getCurrentColor();
			}
			for(int i = 0; i < upSquares.length; i++) {
				upColors[i] = upSquares[col][i].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[col][i].getCurrentColor();
			}
			for(int i = 0; i < downSquares.length; i++) {
				downColors[i] = downSquares[col][i].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[col][i].setSquareColor(downColors[i]);
			}
			for(int i = 0; i < upSquares.length; i++) {
				upSquares[col][i].setSquareColor(frontColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[col][i].setSquareColor(upColors[i]);
			}
			for(int i = 0; i < downSquares.length; i++) {
				downSquares[col][i].setSquareColor(backColors[i]);
			}

		}
		if(col == 0 ) {
			front.getLeftRef().rotateSquaresLeft();
		} else if(col == (DIM - 1)) {
			front.getRightRef().rotateSquaresRight();
		}
	}

	/**
	 * First rendition of a rotation method, used to make a downwards rotation of the cube.
	 * 
	 * @param square - The square being used to spurn the rotation.
	 */
	public void downRotate(FaceSquare square) {
		int squarePanelIndex = square.getIntendedPanel();
		int col = square.getCol();

		FacePanel front = cubeFacePanels[squarePanelIndex];
		FaceSquare[][] frontSquares = front.getSquareArray();
		FacePanel up = front.getUpRef();
		FaceSquare[][] upSquares = up.getSquareArray();
		FacePanel back = front.getBackRef();
		FaceSquare[][] backSquares = back.getSquareArray();
		FacePanel down = front.getDownRef();
		FaceSquare[][] downSquares = down.getSquareArray();
		Color[] frontColors = new Color[DIM];
		Color[] upColors = new Color[DIM];
		Color[] backColors = new Color[DIM];
		Color[] downColors = new Color[DIM];

		if(squarePanelIndex == 1) { //special case

			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[col][i].getCurrentColor();
			}
			for(int i = 0; i < upSquares.length; i++) {
				upColors[i] = upSquares[i][col].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[(DIM - 1) - col][i].getCurrentColor();
			}
			for(int i = 0; i < downSquares.length; i++) {
				downColors[i] = downSquares[i][(DIM - 1) - col].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[col][i].setSquareColor(upColors[i]);
			}
			for(int i = 0; i < upSquares.length; i++) {
				upSquares[i][col].setSquareColor(backColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[(DIM - 1) - col][i].setSquareColor(downColors[i]);
			}
			for(int i = 0; i < downSquares.length; i++) {
				downSquares[i][(DIM - 1) - col].setSquareColor(frontColors[i]);
			}
		} else if(squarePanelIndex == 3) {
			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[col][i].getCurrentColor();
			}
			for(int i = 0; i < upSquares.length; i++) {
				upColors[i] = upSquares[i][(DIM - 1) - col].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[(DIM - 1) - col][i].getCurrentColor();
			}
			for(int i = 0; i < downSquares.length; i++) {
				downColors[i] = downSquares[i][col].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[col][i].setSquareColor(upColors[i]);
			}
			for(int i = 0; i < upSquares.length; i++) {
				upSquares[i][(DIM - 1) - col].setSquareColor(backColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[(DIM - 1) - col][i].setSquareColor(downColors[i]);
			}
			for(int i = 0; i < downSquares.length; i++) {
				downSquares[i][col].setSquareColor(frontColors[i]);
			}
		} else {

			for(int i = 0; i < frontSquares.length; i++) {
				frontColors[i] = frontSquares[col][i].getCurrentColor();
			}
			for(int i = 0; i < upSquares.length; i++) {
				upColors[i] = upSquares[col][i].getCurrentColor();
			}
			for(int i = 0; i < backSquares.length; i++) {
				backColors[i] = backSquares[col][i].getCurrentColor();
			}
			for(int i = 0; i < downSquares.length; i++) {
				downColors[i] = downSquares[col][i].getCurrentColor();
			}

			for(int i = 0; i < frontSquares.length; i++) {
				frontSquares[col][i].setSquareColor(upColors[i]);
			}
			for(int i = 0; i < upSquares.length; i++) {
				upSquares[col][i].setSquareColor(backColors[i]);
			}
			for(int i = 0; i < backSquares.length; i++) {
				backSquares[col][i].setSquareColor(downColors[i]);
			}
			for(int i = 0; i < downSquares.length; i++) {
				downSquares[col][i].setSquareColor(frontColors[i]);
			}
		}
		if(col == 0 ) {
			front.getLeftRef().rotateSquaresRight();
		} else if(col == (DIM - 1)) {
			front.getRightRef().rotateSquaresLeft();
		}
	}

	/**
	 * Simple method used to change the Dimensions of the Rubix Cube.
	 * 
	 * @param newDIM - an integer representing the new X by X dimension for the cube.
	 */
	public void changeDimensions(int newDIM) {
		DIM = newDIM;
		removeAllFacePanels();
		createFacePanels();
		setReferences();
		this.revalidate();
	}
	
	/** 
	 * A simple method to shuffle the current cube.
	 */
	public void shuffle() {
		Random rand = new Random();
		for(int i = 0; i < DIM * 50; i++) {
			
			//Pick a random face.
			int faceNumber = Math.abs(rand.nextInt() % 6);
			
			//Pick a random square.
			int squareRow = Math.abs(rand.nextInt() % DIM);
			int squareCol = Math.abs(rand.nextInt() % DIM);
			
			//Pick a random direction.
			int direction = Math.abs(rand.nextInt() % 4);
			
			FaceSquare[][] sArray = cubeFacePanels[faceNumber].getSquareArray();
			FaceSquare theSquare = sArray[squareRow][squareCol];
			
			if(direction == 1) {
				rightRotate(theSquare);
			} else if(direction == 2) {
				leftRotate(theSquare);
			} else if(direction == 3) {
				downRotate(theSquare);
			} else {
				upRotate(theSquare);
			}
		}
	}
	
	/**
	 * An ActionListener that listens to the movement of the JSlider that changes the size of the
	 * Rubix Cube.
	 * 
	 * @author Carson Smith
	 */
	private class ResizeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			//if(!source.getValueIsAdjusting()) {
			int newDIM = source.getValue();
			changeDimensions(newDIM);
		}
	}
	
	/**
	 * An ActionListener that listens to any clicks that happen to the changeMode button.
	 * 
	 * @author Carson Smith
	 */
	private class ModeClickListener extends MouseInputAdapter {
		
		public void mousePressed(MouseEvent e) {
			if(mode == "2D") {
				removeAllPanels(mode);
				mode = "3D";
			} else if(mode == "3D") {
				removeAllPanels(mode);
				mode = "2D";
			}
		}
	}
	
	/**
	 * An ActionListener that listens to any clicks that happen to the shuffleButton.
	 * 
	 * @author Carson Smith
	 */
	private class ShuffleClickListener extends MouseInputAdapter {

		public void mousePressed(MouseEvent e) {//
			ImageIcon clickedImage = new ImageIcon("src/assets/clicked_shuffle.png");
			buttonPanel.changeImage(clickedImage);
		}

		public void mouseReleased(MouseEvent e) {
			ImageIcon shuffleImage = new ImageIcon("src/assets/button_shuffle.png");
			buttonPanel.changeImage(shuffleImage);
			shuffle();
		}
	}

	/**
	 * An ActionListener that listens to any human-made moves that are made to the 2D rubix cubes.
	 * 
	 * @author Carson Smith
	 */
	private class HumanSwipeListener extends MouseInputAdapter {

		Point clickStart;
		Point clickEnd;

		public void mousePressed(MouseEvent e) {
			clickStart = e.getPoint();
		}

		public void mouseReleased(MouseEvent e) {
			FaceSquare clickedThing = (FaceSquare) e.getSource();
			clickEnd = e.getPoint();
			figureOutRotation(clickedThing);
		}

		private void figureOutRotation(FaceSquare square) {
			double sX = clickStart.getX();
			double sY = clickStart.getY();
			double eX = clickEnd.getX();
			double eY = clickEnd.getY();
			int xDiff = (int) (sX - eX);
			int yDiff = (int) (sY - eY);
			int absX = Math.abs(xDiff);
			int absY = Math.abs(yDiff);
			if(absX > absY) {
				if(xDiff > sX) {
					leftRotate(square);
				} else if(xDiff < sX){
					rightRotate(square);
				}
			} else if(absY > absX) {
				if(yDiff > sY) {
					upRotate(square);
				} else if(yDiff < sY) {
					downRotate(square);
				}
			}
		}
	}
}
