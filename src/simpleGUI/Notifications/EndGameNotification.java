package simpleGUI.Notifications;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import simpleGUI.Objects.Player;

@SuppressWarnings({ "serial" })
public class EndGameNotification extends JFrame {

	public EndGameNotification(Player player) {
		getContentPane().setLayout(new BorderLayout());
		JPanel endGameP = new JPanel();
		JLabel endGame = new JLabel((player.getPlayersHero().getCurrentHP() == 0
				? ("<html><div style='text-align: center;'>" + player.getPlayersName() + " , You sir, have lost...<br/>"
						+ (player.getPlayerOpponent().getPlayersName() + " , Unlike you sir, you WON!!!"))
				: ("<html><div style='text-align: center;'>" + player.getPlayerOpponent().getPlayersName())
						+ " , You sir, have lost...<br/>"
						+ (player.getPlayersName() + " , Unlike you sir, you WON!!!")),
				SwingConstants.CENTER);
		endGame.setFont(new Font("Serif", Font.PLAIN, 20));
		endGameP.setLayout(new GridLayout(3, 1));
		endGameP.add(endGame);
		endGameP.add(new JLabel(), BorderLayout.CENTER);
		JButton okay = new JButton("Ok");
		okay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		endGameP.add(okay, BorderLayout.SOUTH);

		endGameP.setBorder(new TitledBorder(new LineBorder(Color.black), "Notice", 2, 2,
				new Font("", Font.BOLD, endGame.getFont().getSize()), Color.RED));
		add(endGameP);

		setSize(400, 200);
		setUndecorated(true);
		setVisible(true);
		setResizable(false);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

	}

}
