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
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import simpleGUI.Objects.Player;

@SuppressWarnings({ "serial" })
public class TurnNotification extends JFrame {

	public TurnNotification(Player player) {
		getContentPane().setLayout(new BorderLayout());
		JPanel whosTurnP = new JPanel();
		JLabel whosTurn = new JLabel(
				(player.getGameObjectInstance().getGameInstance().getCurrentHero().equals(player.getPlayersHero())
						? (player.getPlayersName() + " , it's your turn")
						: (player.getPlayerOpponent().getPlayersName() + " , it's your turn")),
				SwingConstants.CENTER);
		whosTurn.setFont(new Font("Serif", Font.PLAIN, 40));
		whosTurnP.setLayout(new GridLayout(3, 1));
		whosTurnP.add(whosTurn);
		whosTurnP.add(new JLabel(), BorderLayout.CENTER);
		JButton okay = new JButton("Ok");
		okay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		whosTurnP.add(okay, BorderLayout.SOUTH);

		whosTurnP.setBorder(new TitledBorder(new LineBorder(Color.black), "Notice", 2, 2,
				new Font("", Font.BOLD, whosTurn.getFont().getSize()), Color.RED));
		add(whosTurnP);

		setSize(400, 200);
		setUndecorated(true);
		setVisible(true);
		setResizable(false);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dispose();
			}
		};
		Timer timer = new Timer(5000, taskPerformer);
		timer.setRepeats(false);
		timer.start();

	}

}
