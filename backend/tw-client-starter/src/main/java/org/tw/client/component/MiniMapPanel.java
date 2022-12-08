package org.tw.client.component;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tw.client.controller.MiniMapController;

/**
 * The map Component
 */
public class MiniMapPanel extends JPanel {

	/*
	 * The serial verion UID
	 */
	private static final long serialVersionUID = 6961886340433132128L;

	private Point dragStartPoint;

	/*
	 * The controller
	 */
	protected MiniMapController ctrl;

	private JLabel imageLabel;

	public MiniMapPanel(MiniMapController ctrl) {
		this.ctrl = ctrl;
		setLayout(null);
		setBorder(BorderFactory.createEtchedBorder());
		init();
		addComponentListeners();
	}

	private void addComponentListeners() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				setDragStartPoint(e.getPoint());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				setDragStartPoint(e.getPoint());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getDefaultCursor());
			}

		});

		this.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				int dragX = e.getX();
				int dragY = e.getY();
				Point startPoint = getDragStartPoint();
			}
		});
	}

	private void init() {
		BufferedImage mapExcerpt = ctrl.createCurrentMiniMapExcerpt();
		imageLabel = new JLabel(new ImageIcon(mapExcerpt));
		imageLabel.setLocation(0, 0);
		imageLabel.setSize(mapExcerpt.getWidth(), mapExcerpt.getHeight());
		this.add(imageLabel);
	}

	public void setDragStartPoint(Point currentPoint) {
		this.dragStartPoint = currentPoint;
	}

	public Point getDragStartPoint() {
		return dragStartPoint;
	}

	public JLabel getImageLabel() {
		return imageLabel;
	}

}