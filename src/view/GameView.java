package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GameView extends JFrame{
	private JPanel rescue;
	private JPanel units;
	private JPanel info;
	public GameView() {
		this.validate();
		setTitle("Command Center");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(0,0,1920,1040);
		getContentPane().setLayout(new BorderLayout());
		
		
		info=new JPanel();
		//JScrollPane scrInfo = new JScrollPane(info);
		info.setPreferredSize(new Dimension(290,1040));
		add(info,BorderLayout.WEST);
		info.setLayout(new BorderLayout());
		info.setBackground(Color.LIGHT_GRAY);
		this.pack();
		rescue=new JPanel();
		rescue.setBackground(Color.LIGHT_GRAY);
		rescue.setBackground(Color.black);
		add(new JLabel("  "
				+ "        "
				+ "        "
				+ "                "
				+ "                    "
				+ "                      "
				+ "                         "
				+ "                             "
				+ "                                 "
				+ "                      "
				+ "                                       Rescue Simulation"),BorderLayout.NORTH);
		
		rescue.setLayout(new GridLayout(10,10,1,1));
		rescue.setPreferredSize(new Dimension(1240,1040));
		add(rescue,BorderLayout.CENTER);
		
		
		units=new JPanel();
		units.setPreferredSize(new Dimension(390,1040));
		add(units,BorderLayout.EAST);
		units.setLayout(new BorderLayout());
		units.setBackground(Color.LIGHT_GRAY);
	}
	public GameView(int x) {
		setTitle("3aaa4");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(600,500,600,400);
		info=new JPanel(); 
		add(info);
		info.setLayout(new BorderLayout());
		setVisible(true);
	}
	public static void main(String[] args) {
		GameView o=new GameView();
	}
	public void exit() {
		System.exit(0);
	}
	public JPanel getRescue() {
		return rescue;
	}
	public JPanel getUnits() {
		return units;
	}

	public JPanel getInfo() {
		return info;
	}

}
