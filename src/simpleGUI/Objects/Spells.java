package simpleGUI.Objects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import exceptions.NotEnoughManaException;
import exceptions.NotYourTurnException;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.Spell;

@SuppressWarnings({ "unused", "serial" })
public class Spells extends JPanel {

	private Spell spellCard;
	private int cardIndexInHand;
	private Player owner;
	private JPanel retainSize;

	private JPanel extraDetails;

	private JPanel mCRAttributeP;

	private int maxGap = 20;

	private JPanel properties;

	private JFrame extraDetailsFrame;

	private JButton Use;
	private JButton ownHero;
	private JPanel extraDetailsProtectSizeUse;

	public Spells(Spell x, int cardIndexHand, Player owner) {
		this.spellCard = x;
		this.owner = owner;
		this.cardIndexInHand = cardIndexHand;
		retainSize = new JPanel(new GridLayout(2, 1));
		setLayout(new GridLayout());

		JButton b = new JButton("Just fake button");
		Dimension buttonSize = b.getPreferredSize();
//		retainSize.setPreferredSize(new Dimension((int) (buttonSize.getWidth() * 2.5) + maxGap,
//				(int) (buttonSize.getHeight() * 3.5) + maxGap * 2));

		if (x.getName().contains("Shadow")) {
			setToolTipText("Must choose a minion with 5 or more attack");
		}

		JLabel temp = new JLabel("");
		retainSize.setBorder(new TitledBorder(new LineBorder(Color.black),
				(x.getName().contains("Seal of") ? x.getName().replaceAll(" ", "") : x.getName()), 2, 2,
				new Font("", Font.BOLD, (temp.getFont().getSize()) - 2), Color.BLACK));

		JLabel mCRAttribute = new JLabel("<html><div style='text-align: center;'>MC " + x.getManaCost() + " | "
				+ x.getRarity().toString().charAt(0));
//		JLabel rarityAttribute = new JLabel(
//				"<html><div style='text-align: center;'>" + x.getRarity().toString().charAt(0));

		mCRAttribute.setFont(new Font(mCRAttribute.getFont().getFamily(), mCRAttribute.getFont().getStyle(),
				mCRAttribute.getFont().getSize() - 2));
//		rarityAttribute.setFont(
//				new Font(rarityAttribute.getFont().getFamily(), Font.BOLD, rarityAttribute.getFont().getSize() + 1));

		Use = new JButton("U");
		Use.setMargin(new Insets(2, 4, 2, 4));
		Use.setActionCommand(Integer.toString(cardIndexHand) + "  " + "UseSpell");
		Use.addActionListener(owner);
		Use.setFont(new Font(Use.getFont().getFamily(), Use.getFont().getStyle(), Use.getFont().getSize() - 2));
		Use.setAlignmentX(Component.CENTER_ALIGNMENT);
		extraDetailsProtectSizeUse = new JPanel(new GridLayout());
		extraDetailsProtectSizeUse.add(Use);

		if (owner.getPlayersHero().getCurrentManaCrystals() < x.getManaCost()) {
			Use.setEnabled(false);
			Use.setToolTipText("Can't Use me, Not Enough Mana Crystals");
		}
		if (spellCard.getName().contains("Seal of Champions") && (owner.getPlayersHero().getField().isEmpty()
				&& owner.getPlayerOpponent().getPlayersHero().getField().isEmpty())) {
			Use.setEnabled(false);
			Use.setToolTipText("There are no minions on the field");
		}

		mCRAttribute.setAlignmentX(Component.CENTER_ALIGNMENT);
		// rarityAttribute.setAlignmentX(Component.CENTER_ALIGNMENT);

		Use.setFont(new Font(Use.getFont().getFamily(), Use.getFont().getStyle(), Use.getFont().getSize() - 2));
		// rarityAttribute.setFont(new Font(rarityAttribute.getFont().getFamily(),
		// rarityAttribute.getFont().getStyle(),
		// rarityAttribute.getFont().getSize() - 2));
		mCRAttribute.setFont(new Font(mCRAttribute.getFont().getFamily(), mCRAttribute.getFont().getStyle(),
				mCRAttribute.getFont().getSize()));

		mCRAttributeP = new JPanel(new GridLayout());
		mCRAttributeP.add(mCRAttribute, SwingConstants.CENTER);

		if (spellCard instanceof HeroTargetSpell) {

			ownHero = new JButton("<html><div style='text-align: center;'>SELF");
			ownHero.setFont(new Font("", Font.PLAIN, 10));
			ownHero.setMargin(new Insets(2, 4, 2, 4));
			ownHero.setFont(new Font(ownHero.getFont().getFamily(), ownHero.getFont().getStyle(),
					ownHero.getFont().getSize() - 2));
			ownHero.setAlignmentX(Component.CENTER_ALIGNMENT);
			JPanel extraDetailsProtectSizeOwnHero = new JPanel(new GridLayout());
			extraDetailsProtectSizeOwnHero.add(ownHero);
			ownHero.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						owner.getPlayersHero().castSpell((HeroTargetSpell) spellCard, owner.getPlayersHero());
						owner.getGamePlayWindow().rememberAction(
								owner.getPlayersName() + " Used " + spellCard.getName() + " on himself ");
						owner.getGamePlayWindow().refresh();
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			});

			if (owner.getPlayersHero().getCurrentManaCrystals() < spellCard.getManaCost()) {
				ownHero.setToolTipText("You don't have enough Mana Crystals");
				ownHero.setEnabled(false);
			}

			JPanel tempR1 = new JPanel(new GridLayout(1, 2));
			tempR1.add(extraDetailsProtectSizeOwnHero, SwingConstants.CENTER);
			tempR1.add(mCRAttribute, SwingConstants.CENTER);
			mCRAttribute.setHorizontalAlignment(JLabel.CENTER);
			retainSize.add(tempR1);

			JPanel details = createDetailsLabel();
			JPanel tempR2 = new JPanel(new GridLayout(1, 2));
			tempR2.add(extraDetailsProtectSizeUse);
			tempR2.add(details);
			retainSize.add(tempR2);

		} else {
			JPanel tempR1 = new JPanel(new GridLayout());
			tempR1.add(mCRAttribute, SwingConstants.CENTER);
			mCRAttribute.setHorizontalAlignment(JLabel.CENTER);
			retainSize.add(tempR1);

			JPanel details = createDetailsLabel();
			JPanel tempR2 = new JPanel(new GridLayout(1, 2));
			tempR2.add(extraDetailsProtectSizeUse);
			tempR2.add(details);
			retainSize.add(tempR2);
		}

		add(retainSize);

	}

	private JPanel createDetailsLabel() {

		JButton extraDetailsPrompt = new JButton("?");
		extraDetailsPrompt.setMargin(new Insets(2, 4, 2, 4));
		JPanel extraDetailsProtectSize = new JPanel(new GridLayout());
		extraDetailsProtectSize.add(extraDetailsPrompt);
		extraDetailsPrompt.setFont(new Font(extraDetailsPrompt.getFont().getFamily(),
				extraDetailsPrompt.getFont().getStyle(), extraDetailsPrompt.getFont().getSize() - 2));
		extraDetailsPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
		extraDetailsPrompt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame detailsFrameML = createDetailsFrame();

			}
		});
		return extraDetailsProtectSize;
	}

	public JFrame createDetailsFrame() {
		this.extraDetails = new JPanel();
		extraDetails.setLayout(new BoxLayout(extraDetails, BoxLayout.Y_AXIS));
		JLabel spellName = new JLabel(
				"<html><div style='text-align: center;'>" + spellCard.getName() + "<br/>Mana Cost : "
						+ spellCard.getManaCost() + " <br/>" + "Rarity : " + spellCard.getRarity().toString() + "<br/>",
				SwingConstants.CENTER);
		JPanel spellNameP = new JPanel(new GridLayout());
		spellNameP.add(spellName);

		JLabel spellDetails = createSpellDetails();
		JPanel spellDetailsP = new JPanel(new GridLayout(1, 1));
		spellDetailsP.add(spellDetails);

		extraDetails.add(new JLabel(""));// for look
		extraDetails.add(spellNameP);
//		extraDetails.add(spellMCP);
//		extraDetails.add(spellRarityP);
		extraDetails.add(spellDetailsP);
		extraDetails.add(new JLabel(""));// for look

		this.extraDetailsFrame = new JFrame();
		extraDetailsFrame.setResizable(false);
		extraDetailsFrame.setLocation(MouseInfo.getPointerInfo().getLocation());
		extraDetailsFrame.setUndecorated(false);
		extraDetailsFrame.setVisible(true);
		extraDetailsFrame.setLayout(new GridLayout());
		extraDetailsFrame.add(extraDetails);
		extraDetailsFrame.setAlwaysOnTop(true);

//		if (spellCard.getName().contains("polymorph")) {
//			extraDetailsFrame.setSize(250, 350);
//		} else {
//			
//
//		}
		extraDetailsFrame.setSize(220, 280);
		return extraDetailsFrame;
	}

	public JLabel createSpellDetails() {
		JLabel spellDetails = new JLabel();

		switch (spellCard.getName()) {
		case "Curse of Weakness":
			spellDetails = new JLabel("<html><div style='text-align: center;'>Spell Details :"
					+ " Decreases the attack of all enemy minions by 2.<br/>", SwingConstants.CENTER);
			break;
		case "Divine Spirit":
			spellDetails = new JLabel("<html><div style='text-align: center;'>Spell Details :"
					+ " Doubles the current and max HP of a minion.<br/>", SwingConstants.CENTER);
			break;
		case "Flamestrike":
			spellDetails = new JLabel("<html><div style='text-align: center;'>Spell Details : "
					+ "Deals 4 damage to all enemy minions.<br/>", SwingConstants.CENTER);
			break;

		case "Holy Nova":
			spellDetails = new JLabel("<html><div style='text-align: center;'>Spell Details :"
					+ " Deals 2 damage to all enemy minions. Restores 2 health " + "to all friendly minions.<br/>",
					SwingConstants.CENTER);
			break;

		case "Kill Command":
			spellDetails = new JLabel("<html><div style='text-align: center;'>Spell Details :"
					+ " Deals 5 damage to a minion or 3 damage to a hero.<br/>", SwingConstants.CENTER);
			break;

		case "Level Up!":
			spellDetails = new JLabel(
					"<html><div style='text-align: center;'>Spell Details : "
							+ "Increase the attack, current, and max HP of all silver hand recruits by 1.<br/>",
					SwingConstants.CENTER);
			break;

		case "Multi-Shot":
			spellDetails = new JLabel("<html><div style='text-align: center;'>Spell Details : "
					+ "Deals 3 damage to two random enemy minions. If the opponent has only one minion, it deals 3 damage once to this minion. If the opponent's field is empty then nothing happens.<br/>",
					SwingConstants.CENTER);
			break;
		case "Polymorph":
			spellDetails = new JLabel(
					"<html><div style='text-align: center;'>Spell Details :<br/> Transforms a minion into a minion with the following attributes:"
							+ "<br/> CurrentHP, maxHp and attack value (all with a value of 1)."
							+ "<br/> Name is Sheep." + "<br/> A non-taunt, non-divine and non-charge minion."
							+ "<br/> Mana cost is 1 mana crystal.<br/>",
					SwingConstants.CENTER);
			break;

		case "Pyroblast":
			spellDetails = new JLabel("<html><div style='text-align: center;'>Spell Details : "
					+ "Deals 10 damage to a chosen target (a hero or a minion).<br/>", SwingConstants.CENTER);
			break;

		case "Seal of Champions":
			spellDetails = new JLabel(
					"<html><div style='text-align: center;'>Spell Details : "
							+ "Increases the attack of a minion by 3 and gives it divine shield.<br/>",
					SwingConstants.CENTER);
			break;

		case "Shadow Word: Death":
			spellDetails = new JLabel(
					"<html><div style='text-align: center;'>Spell Details : "
							+ "Destroys a minion that his attack is 5 or more even if it has a divine shield.<br/>",
					SwingConstants.CENTER);
			break;

		case "Siphon Soul":
			spellDetails = new JLabel("<html><div style='text-align: center;'>Spell Details :"
					+ " Destroys a minion even if it has a divine shield and restores 3 health points to the hero .<br/>",
					SwingConstants.CENTER);
			break;

		case "Twisting Nether":
			spellDetails = new JLabel(
					"<html><div style='text-align: center;'>Spell Details :"
							+ " Destroys all minions of both heroes even if any of them has a divine shield.<br/>",
					SwingConstants.CENTER);
			break;

		default:
			spellDetails = new JLabel();
			break;
		}
		return spellDetails;

	}

	public JButton getUse() {
		return Use;
	}

	public void setUse(JButton use) {
		Use = use;
	}

	public JButton getOwnHero() {
		return ownHero;
	}

	public JPanel getExtraDetailsProtectSizeUse() {
		return extraDetailsProtectSizeUse;
	}

	public void setExtraDetailsProtectSizeUse(JPanel extraDetailsProtectSizeUse) {
		this.extraDetailsProtectSizeUse = extraDetailsProtectSizeUse;
	}

	public void setOwnHero(JButton ownHero) {
		this.ownHero = ownHero;
	}

}
