package org.tw.client.component;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MapPanelTest {

	protected static JPanel panel;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(850, 650);
		frame.setTitle("BL - Frame");
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		execFrameBl(frame);

	}

	private static void execFrameBl(JFrame frame) {
		frame.setLayout(null);
		panel = new JPanel();
		panel.setBackground(Color.BLUE);
		panel.setLocation(10, 10);
		panel.setSize(350, 110);
		panel.setVisible(true);
		frame.add(panel);


		frame.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX() + 10;
				int y = e.getY() - 10;
				panel.setLocation(new Point(x, y));
			}
		});

		JButton blBtn = new JButton("BL!");
		blBtn.setLocation(450, 10);
		blBtn.setSize(50, 20);

		blBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Point location = panel.getLocation();
				panel.setLocation(location.x + 1, location.y);
				frame.repaint();
			}
		});

		frame.add(blBtn);




	}

}
