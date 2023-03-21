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

import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.Spell;
import simpleGUI.Objects.Minions;
import simpleGUI.Objects.Player;
import simpleGUI.Objects.Spells;

@SuppressWarnings({ "serial" })
public class FullHandExceptionNotification extends JFrame {

	public FullHandExceptionNotification(Player player, Card burned) {
		getContentPane().setLayout(new BorderLayout());
		JPanel fullHandP = new JPanel(new GridLayout(3, 1));
		JLabel fullHand = new JLabel(
				(player.getGameObjectInstance().getGameInstance().getCurrentHero().equals(player.getPlayersHero())
						? ("<html><div style='text-align: center;'>" + player.getPlayersName()
								+ " , Your Hand was full,<br/> and this card was burned<br/>")
						: ("<html><div style='text-align: center;'>" + player.getPlayerOpponent().getPlayersName()
								+ " , Your Hand was full,<br/> and this card was burned<br/>")),
				SwingConstants.CENTER);
		fullHand.setFont(new Font("Serif", Font.PLAIN, 20));
		fullHandP.add(fullHand);
		JPanel burnedSize = new JPanel(new GridLayout());
		if (burned instanceof Minion) {
			Minions burnedMinion = new Minions((Minion) burned, 0, player, false, false, false, false, false);
			burnedMinion.getButtonP().remove(burnedMinion.getAbilityButton());
			burnedMinion.getRarityAttribute().setFont(new Font(burnedMinion.getRarityAttribute().getFont().getFamily(),
					Font.PLAIN, burnedMinion.getRarityAttribute().getFont().getSize()));
			burnedSize.add(burnedMinion);
		}
		if (burned instanceof Spell) {
			Spells burnedSpell = new Spells((Spell) burned, 0, player);
			if (burnedSpell instanceof HeroTargetSpell) {
				burnedSpell.getExtraDetailsProtectSizeUse().remove(burnedSpell.getOwnHero());
			}
			burnedSpell.getExtraDetailsProtectSizeUse().remove(burnedSpell.getUse());
			burnedSize.add(burnedSpell);

		}
		JButton okay = new JButton("Ok");
		okay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JPanel okaySize = new JPanel();
		okaySize.add(okay);

		fullHandP.add(burnedSize);
		fullHandP.add(okaySize, BorderLayout.SOUTH);

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
