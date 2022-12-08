package org.tw.client.start;

import javax.swing.SwingUtilities;

import org.tw.client.gui.MainFrame;

/**
 * The application launcher
 */
public class ApplicationLauncher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainFrame mainFrame = new MainFrame();
			mainFrame.init();
		});
	}

}