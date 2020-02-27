import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel {

	private JLabel shuffleButton;
	private JLabel modeButton;
	
	/**
	 * CONSTRUCTOR
	 * A panel with an image that changes when the image is clicked.
	 * 
	 */
	/**
	 * CONSTRUCTOR
	 * 
	 * This method constructs a new ButtonPanel Object, which is simply a JPanel with two custom-built
	 * JLabels-turned-JButtons with custom images for them, as I didn't like the default appearance of
	 * a JButton.
	 * 
	 * @param shuffleClickListener - Action listener to link to the shuffleButton
	 * @param modeClickListener - Action listener to link to the modeButton
	 */
	public ButtonPanel(MouseInputAdapter shuffleClickListener, MouseInputAdapter modeClickListener) {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(200, 200));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.insets = new Insets(10, 10, 10, 10);
		c.fill = GridBagConstraints.BOTH;
		ImageIcon shuffleImage = new ImageIcon("src/assets/button_shuffle.png");
		ImageIcon modeImage = new ImageIcon("src/assets/button_d-mode.png");
		shuffleButton = new JLabel(shuffleImage);
		shuffleButton.addMouseListener(shuffleClickListener);
		this.add(shuffleButton, c);
		modeButton = new JLabel(modeImage);
		c.gridx = 1;
		c.gridy = 2;
		modeButton.addMouseListener(modeClickListener);
		this.add(modeButton, c);
	}
	
	/**
	 * A simple method to change the image currently associated with the shuffleButton
	 * @param image
	 */
	public void changeImage(ImageIcon image) {
		shuffleButton.setIcon(image);
	}
}
