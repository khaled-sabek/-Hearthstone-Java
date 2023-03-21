package simpleGUI.Notifications;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
public class InvalidTargetExceptionNotification extends JFrame {

	public InvalidTargetExceptionNotification(Player player) {
		getContentPane().setLayout(new BorderLayout());
		JPanel fullHandP = new JPanel();
		JLabel fullHand = new JLabel("You can't choose that minion, Must choose a minion with 5 or more attack",
				SwingConstants.CENTER);
		fullHand.setFont(new Font("Serif", Font.PLAIN, 20));
		// fullHandP.setLayout(new GridLayout(3, 1));
		fullHandP.add(fullHand);
		// fullHandP.add(new JLabel());
		JButton okay = new JButton("Ok");
		okay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		fullHandP.add(okay, BorderLayout.SOUTH);

		fullHandP.setBorder(new TitledBorder(new LineBorder(Color.black), "Notice", 2, 2,
				new Font("", Font.BOLD, fullHand.getFont().getSize()), Color.RED));
		add(fullHandP);

		setSize(400, 200);
		setUndecorated(true);
		setVisible(true);
		setResizable(false);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

	}

}
