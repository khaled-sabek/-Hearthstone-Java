package simpleGUI.Objects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import model.cards.minions.Minion;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.ShadowWordDeath;

@SuppressWarnings({ "unused", "serial" })
public class Minions extends JPanel implements ActionListener {

	private boolean onField;
	private int cardIndexOnField;

	private boolean inHand;
	private int cardIndex;

	private Minion minion;

	private Player owner;

	private String ID;

	private JButton abilityButton;
	private JPanel extraDetails;

	private JLabel chargeAttribute;
	private JLabel tauntAttribute;
	private JLabel divineAttribute;
	private JLabel currentHPAttribute;
	private JLabel attackAttribute;
	private JLabel rarityAttribute;
	private JLabel manaCostAttribute;

	private boolean minionIsCharge;

	private JLabel minionCharge;

	private JFrame detailsFrame;
	private JFrame extraDetailsFrame;
	private boolean attackInitialized;

	private Border loweredbevel;
	private Border raisedbevel;

	private boolean opponentHasTaunt;

	private JLabel temp;
	private JPanel ButtonP;
	private JPanel minionObject;
	private JPanel minionObjectRowTwo;

	public Minions(Minion x, int cardIndex, Player owner, boolean onField, boolean enemyField,
			boolean attackInitialized, boolean heroPowerInitialized, boolean spellInitialized) {

		temp = new JLabel("");

		this.minion = x;
		minionObject = new JPanel();

		loweredbevel = BorderFactory.createLoweredBevelBorder();
		raisedbevel = BorderFactory.createRaisedBevelBorder();

		if (x.getName().equals("Icehowl") || x.getName().equals("Argent Commander") || x.getName().equals("Wolfrider")
				|| x.getName().equals("Stonetusk Boar")) {
			this.minionIsCharge = true;
		}

		this.onField = onField;
		this.cardIndex = cardIndex;
		this.owner = owner;
		this.attackInitialized = attackInitialized;
		opponentHasTaunt = false;
		for (int i = 0; i < owner.getPlayerOpponent().getPlayersHero().getField().size(); i++) {
			if (owner.getPlayerOpponent().getPlayersHero().getField().get(i).isTaunt()) {
				opponentHasTaunt = true;
			}
		}

		minionObject.setLayout(new GridLayout(2, 1));
		JPanel minionObjectRowOne = new JPanel(new GridLayout(1, 3));
		minionObjectRowTwo = new JPanel(new GridLayout(1, 3));
		int width = 0;
		if (!onField) {
			minionObject.setBorder(new TitledBorder(new LineBorder(Color.black),
					("<html><div style='text-align: center;'>" + (x.getName().contains("Colossus")
							? x.getName().replaceAll("Colossus of the Moon", "ColossusOfTheMoon")
							: x.getName())),
					2, 2,
					new Font("", Font.ITALIC, (temp.getFont().getSize()) - (x.getName().contains("Colossus") ? 3 : 2)),
					Color.BLACK));
		}
		if (onField && enemyField) {
			minionObject.setBorder(new TitledBorder(new LineBorder(Color.black),
					("<html><div style='text-align: center;'>" + (x.getName().contains("Colossus")
							? x.getName().replaceAll("Colossus of the Moon", "ColossusOfTheMoon")
							: x.getName())),
					2, 2, new Font("", Font.ITALIC, (temp.getFont().getSize())), Color.RED));
		}
		if (onField && !enemyField) {
			minionObject.setBorder(new TitledBorder(new LineBorder(Color.black),
					("<html><div style='text-align: center;'>" + (x.getName().contains("Colossus")
							? x.getName().replaceAll("Colossus of the Moon", "ColossusOfTheMoon")
							: x.getName())),
					2, 2, new Font("", Font.ITALIC, (temp.getFont().getSize())), Color.GREEN));
		}

		manaCostAttribute = new JLabel("<html><div style='text-align: center;'>MC " + x.getManaCost() + "</html>");
		rarityAttribute = new JLabel("<html><div style='text-align: center;'>" + x.getRarity().toString().charAt(0));
		attackAttribute = new JLabel("<html><div style='text-align: center;'>ATT " + x.getAttack());
		currentHPAttribute = new JLabel("<html><div style='text-align: center;'>HP " + x.getCurrentHP());
		divineAttribute = new JLabel(x.isDivine() ? "<html><div style='text-align: center;'>D" : "");
		tauntAttribute = new JLabel(x.isTaunt() ? "<html><div style='text-align: center;'>T" : "");

		JPanel manaCostAttributeP = new JPanel(new FlowLayout());
		manaCostAttributeP.add(manaCostAttribute, SwingConstants.CENTER);
		JPanel rarityAttributeP = new JPanel(new FlowLayout());
		rarityAttributeP.add(rarityAttribute, SwingConstants.CENTER);
		JPanel attackAttributeP = new JPanel(new FlowLayout());
		attackAttributeP.add(attackAttribute, SwingConstants.CENTER);
		JPanel currentHPAttributeP = new JPanel(new FlowLayout());
		currentHPAttributeP.add(currentHPAttribute, SwingConstants.CENTER);
		JPanel divineAttributeP = new JPanel(new FlowLayout());
		divineAttributeP.add(divineAttribute, SwingConstants.CENTER);
		JPanel tauntAttributeP = new JPanel(new FlowLayout());
		tauntAttributeP.add(tauntAttribute, SwingConstants.CENTER);

		currentHPAttribute.setFont(new Font(currentHPAttribute.getFont().getFamily(),
				currentHPAttribute.getFont().getStyle(), currentHPAttribute.getFont().getSize() - 2));
		attackAttribute.setFont(new Font(attackAttribute.getFont().getFamily(), attackAttribute.getFont().getStyle(),
				attackAttribute.getFont().getSize() - 2));
		rarityAttribute.setFont(
				new Font(rarityAttribute.getFont().getFamily(), Font.BOLD, rarityAttribute.getFont().getSize()));
		manaCostAttribute.setFont(new Font(manaCostAttribute.getFont().getFamily(),
				manaCostAttribute.getFont().getStyle(), manaCostAttribute.getFont().getSize() - 2));
		divineAttribute.setFont(new Font(divineAttribute.getFont().getFamily(), divineAttribute.getFont().getStyle(),
				divineAttribute.getFont().getSize() - 2));
		tauntAttribute.setFont(new Font(tauntAttribute.getFont().getFamily(), tauntAttribute.getFont().getStyle(),
				tauntAttribute.getFont().getSize() - 2));

		if (!onField) {
			this.abilityButton = new JButton("P");
			abilityButton.setMargin(new Insets(0, 0, 0, 0));
			abilityButton.setActionCommand(Integer.toString(cardIndex) + "  " + "PlayMinionCard");
			if (owner.getPlayersHero().getCurrentManaCrystals() < x.getManaCost()) {
				abilityButton.setEnabled(false);
				abilityButton.setToolTipText("You don't have enough Mana Crystals");
			}
			if (owner.getPlayersHero().getField().size() == 7) {
				abilityButton.setEnabled(false);
				abilityButton.setToolTipText("You don't have space on your field");
			}
		} else {
			this.abilityButton = new JButton("A");
			abilityButton.setMargin(new Insets(0, 0, 0, 0));
			abilityButton.setActionCommand(Integer.toString(cardIndex) + "  " + "AttackMinionCard");
			if (x.isSleeping()) {
				abilityButton.setEnabled(false);
				abilityButton.setToolTipText("The minion is Sleeping");
			}
			if (x.isAttacked()) {
				abilityButton.setEnabled(false);
				abilityButton.setToolTipText("This minion has already attacked");
			}
			if (!owner.getPlayersHero().equals(owner.getGameObjectInstance().getGameInstance().getCurrentHero())) {
				abilityButton.setVisible(false);
			}
		}

		if (((owner.getHeroSelectedName().contains("Mage") || owner.getHeroSelectedName().contains("Priest"))
				|| spellInitialized) && onField) {
			if (spellInitialized) {
				if (owner.getSpellObject() instanceof ShadowWordDeath) {
					if (x.getAttack() < 5) {
						setToolTipText("You can't choose that minion, Must choose a minion with 5 or more attack");
						if (onField) {
							minionObject.setBorder(new TitledBorder(loweredbevel,
									("<html><div style='text-align: center;'>" + (x.getName().contains("Colossus")
											? x.getName().replaceAll("Colossus of the Moon", "ColossusOfTheMoon")
											: x.getName())),
									2, 2, new Font("", Font.ITALIC, (temp.getFont().getSize())),
									enemyField ? Color.red : Color.green));
						}
					} else {
						minionObject.setBorder(new TitledBorder(raisedbevel,
								("<html><div style='text-align: center;'>" + (x.getName().contains("Colossus")
										? x.getName().replaceAll("Colossus of the Moon", "ColossusOfTheMoon")
										: x.getName())),
								2, 2, new Font("", Font.ITALIC, (temp.getFont().getSize())),
								enemyField ? Color.red : Color.green));
						setToolTipText("Click here to target me");

						addMouseListener(new MouseListener() {
							@Override
							public void mouseReleased(MouseEvent e) {
							}

							@Override
							public void mousePressed(MouseEvent e) {
							}

							@Override
							public void mouseExited(MouseEvent e) {
							}

							@Override
							public void mouseEntered(MouseEvent e) {
							}

							@Override
							public void mouseClicked(MouseEvent e) {
								JButton target = new JButton();
								target.setActionCommand(
										cardIndex + "  " + "minionTarget" + " " + " ownerisenemy" + enemyField);
								target.addActionListener(owner);
								target.doClick();
							}
						});
					}
				} else {
					minionObject.setBorder(new TitledBorder(raisedbevel,
							("<html><div style='text-align: center;'>" + (x.getName().contains("Colossus")
									? x.getName().replaceAll("Colossus of the Moon", "ColossusOfTheMoon")
									: x.getName())),
							2, 2, new Font("", Font.ITALIC, (temp.getFont().getSize())),
							enemyField ? Color.red : Color.green));
					setToolTipText("Click here to target me");

					addMouseListener(new MouseListener() {
						@Override
						public void mouseReleased(MouseEvent e) {
						}

						@Override
						public void mousePressed(MouseEvent e) {
						}

						@Override
						public void mouseExited(MouseEvent e) {
						}

						@Override
						public void mouseEntered(MouseEvent e) {
						}

						@Override
						public void mouseClicked(MouseEvent e) {
							JButton target = new JButton();
							target.setActionCommand(
									cardIndex + "  " + "minionTarget" + " " + " ownerisenemy" + enemyField);
							target.addActionListener(owner);
							target.doClick();
						}
					});
				}
			}

		}
		if ((((owner.getSpellObject() instanceof MinionTargetSpell || owner.getSpellObject() instanceof LeechingSpell)
				&& (spellInitialized)) || (heroPowerInitialized && onField))) {

			if (onField && ((!(spellInitialized) && owner.getSpellObject() instanceof ShadowWordDeath))) {
				minionObject.setBorder(new TitledBorder(raisedbevel,
						("<html><div style='text-align: center;'>" + (x.getName().contains("Colossus")
								? x.getName().replaceAll("Colossus of the Moon", "ColossusOfTheMoon")
								: x.getName())),
						2, 2, new Font("", Font.ITALIC, (temp.getFont().getSize())),
						enemyField ? Color.red : Color.green));
				setToolTipText("Click here to target me");

				addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {
					}

					@Override
					public void mousePressed(MouseEvent e) {
					}

					@Override
					public void mouseExited(MouseEvent e) {
					}

					@Override
					public void mouseEntered(MouseEvent e) {
					}

					@Override
					public void mouseClicked(MouseEvent e) {
						JButton target = new JButton();
						target.setActionCommand(cardIndex + "  " + "minionTarget" + " " + " ownerisenemy" + enemyField);
						target.addActionListener(owner);
						target.doClick();
					}
				});
			}
		}

		if (attackInitialized && onField && enemyField) {

			if (x.isTaunt()) {
				setToolTipText("Click on me to attack me");
				addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
					}

					@Override
					public void mousePressed(MouseEvent e) {
					}

					@Override
					public void mouseExited(MouseEvent e) {
					}

					@Override
					public void mouseEntered(MouseEvent e) {
					}

					@Override
					public void mouseClicked(MouseEvent e) {
						JButton target = new JButton();
						target.setActionCommand(cardIndex + "  " + "minionTarget" + " " + " ownerisenemy" + enemyField);
						target.addActionListener(owner);
						target.doClick();
					}
				});

				if (onField && enemyField) {
					minionObject.setBorder(new TitledBorder(raisedbevel,
							("<html><div style='text-align: center;'>" + (x.getName().contains("Colossus")
									? x.getName().replaceAll("Colossus of the Moon", "ColossusOfTheMoon")
									: x.getName())),
							2, 2, new Font("", Font.ITALIC, (temp.getFont().getSize())), Color.RED));
				}

			} else {
				if (opponentHasTaunt) {
					setToolTipText("You can't attack me, there is a taunt minion on the Field");
					if (onField && enemyField) {
						minionObject.setBorder(new TitledBorder(loweredbevel,
								("<html><div style='text-align: center;'>" + (x.getName().contains("Colossus")
										? x.getName().replaceAll("Colossus of the Moon", "ColossusOfTheMoon")
										: x.getName())),
								2, 2, new Font("", Font.ITALIC, (temp.getFont().getSize())), Color.RED));
					}
				} else {
					setToolTipText("Click on me to attack me");
					if (onField && enemyField) {
						minionObject.setBorder(new TitledBorder(raisedbevel,
								("<html><div style='text-align: center;'>" + (x.getName().contains("Colossus")
										? x.getName().replaceAll("Colossus of the Moon", "ColossusOfTheMoon")
										: x.getName())),
								2, 2, new Font("", Font.ITALIC, (temp.getFont().getSize())), Color.RED));
					}
					addMouseListener(new MouseListener() {
						@Override
						public void mouseReleased(MouseEvent e) {
						}

						@Override
						public void mousePressed(MouseEvent e) {
							JButton target = new JButton();
							target.setActionCommand(
									cardIndex + "  " + "minionTarget" + " " + " ownerisenemy" + enemyField);
							target.addActionListener(owner);
							target.doClick();
						}

						@Override
						public void mouseExited(MouseEvent e) {
						}

						@Override
						public void mouseEntered(MouseEvent e) {
						}

						@Override
						public void mouseClicked(MouseEvent e) {
						}
					});
				}
			}
		}

		if (enemyField) {
			abilityButton.setVisible(false);
		}
		abilityButton.addActionListener(owner);

		ButtonP = new JPanel(new GridLayout());
		JPanel extraDetails = createDetailsLabel();

		ButtonP.add(abilityButton);
		ButtonP.add(extraDetails);

		abilityButton.setFont(new Font(abilityButton.getFont().getFamily(), abilityButton.getFont().getStyle(),
				abilityButton.getFont().getSize() - 2));

		minionObjectRowOne.add(manaCostAttributeP);
		manaCostAttribute.setAlignmentX(Component.CENTER_ALIGNMENT);

		minionObjectRowOne.add(rarityAttributeP);
		rarityAttribute.setAlignmentX(Component.CENTER_ALIGNMENT);

		minionObjectRowOne.add(attackAttributeP);
		attackAttribute.setAlignmentX(Component.CENTER_ALIGNMENT);

		minionObjectRowTwo.add(currentHPAttributeP);
		minionObjectRowTwo.setLayout(new GridLayout(1, width++));
		currentHPAttribute.setAlignmentX(Component.CENTER_ALIGNMENT);

		if (minionIsCharge && onField) {
			chargeAttribute = new JLabel("<html><div style='text-align: center;'>C");
			chargeAttribute.setFont(new Font(chargeAttribute.getFont().getFamily(),
					chargeAttribute.getFont().getStyle(), chargeAttribute.getFont().getSize() - 2));
			JPanel chargeAttributeP = new JPanel(new FlowLayout());
			chargeAttributeP.add(chargeAttribute, SwingConstants.CENTER);
			minionObjectRowTwo.add(chargeAttributeP);
			minionObjectRowTwo.setLayout(new GridLayout(1, width++));
			chargeAttribute.setAlignmentX(Component.CENTER_ALIGNMENT);
		} else {
			if (!minionIsCharge && onField) {

			} else if (minionIsCharge && !onField) {

				chargeAttribute = new JLabel("<html><div style='text-align: center;'>C");
				chargeAttribute.setFont(new Font(chargeAttribute.getFont().getFamily(),
						chargeAttribute.getFont().getStyle(), chargeAttribute.getFont().getSize() - 2));
				JPanel chargeAttributeP = new JPanel(new FlowLayout());
				chargeAttributeP.add(chargeAttribute, SwingConstants.CENTER);
				minionObjectRowTwo.add(chargeAttributeP);
				minionObjectRowTwo.setLayout(new GridLayout(1, width++));
				chargeAttribute.setAlignmentX(Component.CENTER_ALIGNMENT);
			} else if (!minionIsCharge && !onField) {
			}
		}
		if (x.isSleeping() && onField) {
			chargeAttribute = new JLabel("<html><div style='text-align: center;'>zZz");
			chargeAttribute.setFont(new Font(chargeAttribute.getFont().getFamily(),
					chargeAttribute.getFont().getStyle(), chargeAttribute.getFont().getSize() - 2));
			JPanel chargeAttributeP = new JPanel(new FlowLayout());
			chargeAttributeP.add(chargeAttribute, SwingConstants.CENTER);
			minionObjectRowTwo.add(chargeAttributeP);
			minionObjectRowTwo.setLayout(new GridLayout(1, width++));
			chargeAttribute.setAlignmentX(Component.CENTER_ALIGNMENT);
		}

		if (x.isDivine()) {
			minionObjectRowTwo.add(divineAttributeP);
			minionObjectRowTwo.setLayout(new GridLayout(1, width++));
			divineAttributeP.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		if (x.isTaunt()) {
			minionObjectRowTwo.add(tauntAttributeP);
			minionObjectRowTwo.setLayout(new GridLayout(1, width++));
			tauntAttributeP.setAlignmentX(Component.CENTER_ALIGNMENT);
		}

		minionObjectRowTwo.add(ButtonP);
		minionObjectRowTwo.setLayout(new GridLayout(1, width++));
		abilityButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		minionObject.add(minionObjectRowOne);
		minionObject.add(minionObjectRowTwo);

		this.setLayout(new GridLayout());
		this.add(minionObject);

	}

	private JPanel createDetailsLabel() {

		JButton extraDetailsPrompt = new JButton("?");
		extraDetailsPrompt.setMargin(new Insets(0, 0, 0, 0));
		JPanel extraDetailsProtectSize = new JPanel();
		extraDetailsProtectSize.setLayout(new GridLayout());
		extraDetailsProtectSize.add(extraDetailsPrompt);
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
		extraDetails.setLayout(new GridLayout(11, 1));
		JLabel minionName = new JLabel("<html><div style='text-align: center;'>" + minion.getName() + "<br/>",
				SwingConstants.CENTER);

		JLabel minionMC = new JLabel(
				"<html><div style='text-align: center;'>Mana Cost : " + minion.getManaCost() + "<br/>",
				SwingConstants.CENTER);

		JLabel minionRarity = new JLabel(
				"<html><div style='text-align: center;'>Rarity : " + minion.getRarity().toString() + "<br/>",
				SwingConstants.CENTER);

		JLabel minionAttack = new JLabel(
				"<html><div style='text-align: center;'>Attack Points : " + minion.getAttack() + "<br/>",
				SwingConstants.CENTER);

		JLabel minionCurrentHP = new JLabel(
				"<html><div style='text-align: center;'>Health Points : " + minion.getMaxHP() + "<br/>",
				SwingConstants.CENTER);

		JLabel minionDivine = new JLabel(
				"<html><div style='text-align: center;'>Divine : " + minion.isDivine() + "<br/>",
				SwingConstants.CENTER);

		JLabel minionTaunt = new JLabel("<html><div style='text-align: center;'>Taunt : " + minion.isTaunt() + "<br/>",
				SwingConstants.CENTER);
		if (minionIsCharge) {
			minionCharge = new JLabel("<html><div style='text-align: center;'>Charge : " + "true" + "<br/>",
					SwingConstants.CENTER);
		} else {
			minionCharge = new JLabel("<html><div style='text-align: center;'>Charge : " + "false" + "<br/>",
					SwingConstants.CENTER);
		}
		JLabel minionSleeping = new JLabel(
				"<html><div style='text-align: center;'>Sleeping : " + minion.isSleeping() + "<br/>",
				SwingConstants.CENTER);

		extraDetails.add(new JLabel(""));// for look
		extraDetails.add(minionName);
		extraDetails.add(minionMC);
		extraDetails.add(minionRarity);
		extraDetails.add(minionAttack);
		extraDetails.add(minionCurrentHP);
		extraDetails.add(minionDivine);
		extraDetails.add(minionTaunt);
		extraDetails.add(minionCharge);
		extraDetails.add(minionSleeping);
		extraDetails.add(new JLabel(""));// for look

		this.extraDetailsFrame = new JFrame();
		extraDetailsFrame.setResizable(false);
		extraDetailsFrame.setLocation(MouseInfo.getPointerInfo().getLocation());
		extraDetailsFrame.setSize(200, 240);
		extraDetailsFrame.setUndecorated(false);
		extraDetailsFrame.setVisible(true);
		extraDetailsFrame.setLayout(new BorderLayout());
		extraDetailsFrame.add(extraDetails, BorderLayout.CENTER);
		extraDetailsFrame.setAlwaysOnTop(true);

		return extraDetailsFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void setChargeAttribute(JLabel chargeAttribute) {
		this.chargeAttribute = chargeAttribute;
	}

	public JLabel getTauntAttribute() {
		return tauntAttribute;
	}

	public void setTauntAttribute(JLabel tauntAttribute) {
		this.tauntAttribute = tauntAttribute;
	}

	public JLabel getDivineAttribute() {
		return divineAttribute;
	}

	public void setDivineAttribute(JLabel divineAttribute) {
		this.divineAttribute = divineAttribute;
	}

	public JLabel getCurrentHPAttribute() {
		return currentHPAttribute;
	}

	public void setCurrentHPAttribute(JLabel currentHPAttribute) {
		this.currentHPAttribute = currentHPAttribute;
	}

	public JLabel getAttackAttribute() {
		return attackAttribute;
	}

	public void setAttackAttribute(JLabel attackAttribute) {
		this.attackAttribute = attackAttribute;
	}

	public JLabel getRarityAttribute() {
		return rarityAttribute;
	}

	public void setRarityAttribute(JLabel rarityAttribute) {
		this.rarityAttribute = rarityAttribute;
	}

	public JLabel getManaCostAttribute() {
		return manaCostAttribute;
	}

	public void setManaCostAttribute(JLabel manaCostAttribute) {
		this.manaCostAttribute = manaCostAttribute;
	}

	public boolean isAttackInitialized() {
		return attackInitialized;
	}

	public JButton getAbilityButton() {
		return abilityButton;
	}

	public void setAbilityButton(JButton abilityButton) {
		this.abilityButton = abilityButton;
	}

	public void setAttackInitialized(boolean attackInitialized) {
		this.attackInitialized = attackInitialized;
	}

	public JPanel getMinionObjectRowTwo() {
		return minionObjectRowTwo;
	}

	public void setMinionObjectRowTwo(JPanel minionObjectRowTwo) {
		this.minionObjectRowTwo = minionObjectRowTwo;
	}

	public JPanel getMinionObject() {
		return minionObject;
	}

	public void setMinionObject(JPanel minionObject) {
		this.minionObject = minionObject;
	}

	public JPanel getButtonP() {
		return ButtonP;
	}

	public void setButtonP(JPanel buttonP) {
		ButtonP = buttonP;
	}
}
