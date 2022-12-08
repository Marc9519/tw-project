package org.tw.client.component;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tw.client.controller.MapController;
import org.tw.client.model.ViewPort;
import org.tw.data.service.model.Village;

/**
 * The map Component
 */
public class MapPanel extends JPanel {

	/*
	 * The serial verion UID
	 */
	private static final long serialVersionUID = 6961886340433132128L;

	private Point dragStartPoint;

	/*
	 * The controller
	 */
	protected MapController ctrl;

	private JLabel imageLabel;

	public MapPanel(MapController ctrl) {
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
				Village vill = ctrl.handleMoveEventData(x,y);
				if (vill != null) {
					setCursor(new Cursor(Cursor.HAND_CURSOR));
					String name = vill.getName();
				}else {
					setCursor(new Cursor(Cursor.MOVE_CURSOR));
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				int dragX = e.getX();
				int dragY = e.getY();
				Map<String, Object> data = ctrl.handleDragEventData(dragX, dragY, getDragStartPoint());
				Integer locX = (Integer) data.get("LOC_X");
				Integer locY = (Integer) data.get("LOC_Y");
				BufferedImage img = (BufferedImage) data.get("IMAGE");
				if (img != null) {
					getImageLabel().setIcon(new ImageIcon(img));
				}
				getImageLabel().setLocation(locX, locY);
				Point targetPoint = e.getPoint();
				setDragStartPoint(targetPoint);
			}
		});
	}

	private void init() {
		BufferedImage mapExcerpt = ctrl.createCurrentMapExcerpt();
		imageLabel = new JLabel(new ImageIcon(mapExcerpt));
		ViewPort viewPort = ctrl.getViewPort();
		imageLabel.setLocation(-1*viewPort.getInnerFrameLocationXPx(), -1*viewPort.getInnerFrameLocationYPx());
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