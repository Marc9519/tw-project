package org.tw.client.gui;

import javax.swing.JFrame;

import org.tw.client.component.MapPanel;
import org.tw.client.component.MiniMapPanel;
import org.tw.client.controller.MapController;
import org.tw.client.controller.MiniMapController;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainFrame() {
		super();
	}

	public void init() {
		setSize(950, 650);
		setTitle("TW-Client  V1.0.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		//Create the content
		setLayout(null);
		int mapComponentWidth = 9*53;
		int mapComponentHeight = 9*38;
		MapController mapController = new MapController();
		MapPanel mapPanel = new MapPanel(mapController);
		mapPanel.setLocation(10, 10);
		mapPanel.setSize(mapComponentWidth, mapComponentHeight);
		this.add(mapPanel);

		MiniMapPanel miniMapPanel = new MiniMapPanel(new MiniMapController(mapController.getMapDataCache()));
		miniMapPanel.setSize(250, 250);
		miniMapPanel.setLocation(520, 10);
		this.add(miniMapPanel);

		setVisible(true);
	}

}