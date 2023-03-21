package simpleGUI.Objects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import engine.GameListener;
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;
import model.cards.Card;
import model.cards.minions.Icehowl;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.Spell;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;
import simpleGUI.Notifications.EndGameNotification;
import simpleGUI.Notifications.FullHandExceptionNotification;
import simpleGUI.Notifications.TurnNotification;
import simpleGUI.Windows.GamePlayWindow;

@SuppressWarnings("unused")
public class Player implements ActionListener, GameListener {

	@SuppressWarnings("serial")
	private static class MyPanel extends JPanel {

		public MyPanel() {
			this.setOpaque(false);
			this.setPreferredSize(new Dimension(9 * TILE, 9 * TILE));
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.lightGray);
			int w = this.getWidth() / TILE + 1;
			int h = this.getHeight() / TILE + 1;
			for (int row = 0; row < h; row++) {
				for (int col = 0; col < w; col++) {
					if ((row + col) % 2 == 0) {
						g.fillRect(col * TILE, row * TILE, TILE, TILE);
					}
				}
			}
		}
	}

	private static final int TILE = 64;

	private Player opponent;
	private Player thisPlayer;

	private GameObject gameObjectInstance;

	private boolean heroHasBeenSelected;

	private Hero playersHero;
	private int playerNumber;
	private String playersName;

	private JScrollPane scrollPanels;
	private JLabel heroSelected;
	private JTextField nameInput;

	private String heroSelectedName;

	private JPanel playingSide;

	private JPanel details;
	private JPanel fieldPanel;

	private JButton endTurn;
	private JButton heroPower;

	private GamePlayWindow gamePlayWindow;

	private ArrayList<String> chargeAttribute;

	private boolean attackInitialized;
	private Minion attacker;
	private int attackerIndex;

	private Minion target;
	private int targetIndex;

	private Hero targetHero;
	private boolean heroPowerInitialized;
	private JButton heroPowerMagePriest;

	private boolean spellInitialized;
	private Spell spellObject;

	private Border loweredbevel;
	private Border raisedbevel;

	private boolean opponentHasTaunt;

	private JLabel temp;

	private EndGameNotification endGame;

	public Player() {
		this.thisPlayer = this;
		heroHasBeenSelected = false;
		chargeAttribute = new ArrayList<String>();
		attackInitialized = false;
		heroPowerInitialized = false;
		spellInitialized = false;

		loweredbevel = BorderFactory.createLoweredBevelBorder();
		raisedbevel = BorderFactory.createRaisedBevelBorder();

		temp = new JLabel("");

	}

	public void checkOpponentTaunt() {
		opponentHasTaunt = false;
		for (int i = 0; i < getPlayerOpponent().getPlayersHero().getField().size(); i++) {
			if (getPlayerOpponent().getPlayersHero().getField().get(i).isTaunt()) {
				opponentHasTaunt = true;
			}
		}
	}

	@SuppressWarnings("resource")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contains("Player")) {
			this.heroHasBeenSelected = true;
			JButton button = (JButton) e.getSource();
			this.heroSelected
					.setText("<html><div style='text-align: center;'>You selected : " + "<br/>" + button.getText());
			heroSelected.setFont(new Font("", Font.BOLD, 15));
			heroSelected.setForeground(Color.white);
			String buttonText = button.getText();
			this.heroSelectedName = button.getText();
			if (buttonText.contains("Hunter")) {
				try {
					this.playersHero = new Hunter();
				} catch (IOException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (buttonText.contains("Mage")) {
				try {
					this.playersHero = new Mage();
				} catch (IOException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (buttonText.contains("Paladin")) {
				try {
					this.playersHero = new Paladin();
				} catch (IOException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (buttonText.contains("Warlock")) {
				try {
					this.playersHero = new Warlock();
				} catch (IOException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (buttonText.contains("Priest")) {
				try {
					this.playersHero = new Priest();
				} catch (IOException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if (heroHasBeenSelected) {

			if (playersHero.getCurrentHP() == 0 || opponent.getPlayersHero().getCurrentHP() == 0) {
				onGameOver();
			} else {
				if (e.getActionCommand().contains("UseSpell")) {
					String actionCommand = e.getActionCommand();
					int spellCardIndex = Integer.parseInt(actionCommand.substring(0, 3).trim());
					spellObject = (Spell) playersHero.getHand().get(spellCardIndex);
					if (spellObject instanceof FieldSpell) {
						try {
							playersHero.castSpell((FieldSpell) spellObject);
							gamePlayWindow.rememberAction(playersName + " cast the spell " + spellObject.getName());
							spellInitialized = false;
							heroPowerInitialized = false;
							attackInitialized = false;
						} catch (NotYourTurnException | NotEnoughManaException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					if (spellObject instanceof AOESpell) {
						try {
							playersHero.castSpell((AOESpell) spellObject, opponent.getPlayersHero().getField());
							gamePlayWindow.rememberAction(playersName + " cast the spell " + spellObject.getName());
							spellInitialized = false;
							heroPowerInitialized = false;
							attackInitialized = false;
						} catch (NotYourTurnException | NotEnoughManaException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (spellObject instanceof MinionTargetSpell) {
						spellInitialized = true;
						heroPowerInitialized = false;
						attackInitialized = false;

					} else if (spellObject instanceof HeroTargetSpell) {
						spellInitialized = true;
						heroPowerInitialized = false;
						attackInitialized = false;

					} else if (spellObject instanceof LeechingSpell) {
						spellInitialized = true;
						heroPowerInitialized = false;
						attackInitialized = false;

					}
					gamePlayWindow.refresh();
				}

				if (e.getActionCommand().contains("Hero Power")) {

					if (heroSelectedName.contains("Hunter")) {
						try {
							((Hunter) playersHero).useHeroPower();

						} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
								| FullHandException | FullFieldException | CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (heroSelectedName.contains("Warlock")) {
						try {
							((Warlock) playersHero).useHeroPower();
						} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
								| FullHandException | FullFieldException | CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (heroSelectedName.contains("Paladin")) {
						try {
							((Paladin) playersHero).useHeroPower();
						} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
								| FullHandException | FullFieldException | CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						spellInitialized = false;
						heroPowerInitialized = false;
						attackInitialized = false;
					} else {
						heroPowerInitialized = true;
						spellInitialized = false;
						attackInitialized = false;
					}

					gamePlayWindow.refresh();
					gamePlayWindow.rememberAction(playersName + " Used his Hero Power");

				}

				if (e.getActionCommand().contains("AttackMinionCard")) {
					attackInitialized = true;
					spellInitialized = false;
					heroPowerInitialized = false;
					String actionCommand = e.getActionCommand();
					Scanner s = new Scanner(actionCommand).useDelimiter("");
					attackerIndex = s.nextInt();
					attacker = playersHero.getField().get(attackerIndex);
					gamePlayWindow.refresh();
				}

				if ((heroPowerInitialized || attackInitialized || spellInitialized)
						&& e.getActionCommand().contains("heroTarget")) {
					targetHero = opponent.getPlayersHero();

					if (heroPowerInitialized) {
						try {
							if (heroSelectedName.contains("Mage")) {
								((Mage) playersHero).useHeroPower(targetHero);
							}
							if (heroSelectedName.contains("Priest")) {
								((Priest) playersHero).useHeroPower(targetHero);
							}
						} catch (NotEnoughManaException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (HeroPowerAlreadyUsedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NotYourTurnException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (FullHandException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (FullFieldException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						gamePlayWindow.rememberAction(playersName + " Used his Hero Power on the Opponent Hero");

					} else {
						if (attackInitialized) {
							try {
								playersHero.attackWithMinion(attacker, targetHero);
							} catch (CannotAttackException e1) {
								e1.printStackTrace();
							} catch (NotYourTurnException e1) {
								e1.printStackTrace();
							} catch (TauntBypassException e1) {
								e1.printStackTrace();
							} catch (InvalidTargetException e1) {
								e1.printStackTrace();
							} catch (NotSummonedException e1) {
								e1.printStackTrace();
							}
							gamePlayWindow.rememberAction(
									playersName + " attacked the Opponent Hero using " + attacker.getName());

						} else {
							if (spellInitialized) {
								if (spellObject instanceof HeroTargetSpell) {
									try {
										playersHero.castSpell((HeroTargetSpell) spellObject, targetHero);
										gamePlayWindow.rememberAction(playersName + " cast the spell "
												+ spellObject.getName() + "on the Opponent Hero");
										spellInitialized = false;
										heroPowerInitialized = false;
										attackInitialized = false;
										gamePlayWindow.refresh();
									} catch (NotYourTurnException | NotEnoughManaException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								} else {
									spellInitialized = false;
									heroPowerInitialized = false;
									attackInitialized = false;
									gamePlayWindow.refresh();
								}
							}
						}
					}
					spellInitialized = false;
					heroPowerInitialized = false;
					attackInitialized = false;
					gamePlayWindow.refresh();
				}

				if ((heroPowerInitialized || attackInitialized || spellInitialized)
						&& e.getActionCommand().contains("minionTarget")) {
					String actionCommand = e.getActionCommand();
					targetIndex = Integer.parseInt(actionCommand.substring(0, 3).trim());
					if (actionCommand.contains(" ownerisenemy" + false)) {
						target = getPlayersHero().getField().get(targetIndex);
					} else {
						target = opponent.getPlayersHero().getField().get(targetIndex);
					}
					if (heroPowerInitialized) {
						try {
							if (heroSelectedName.contains("Mage")) {
								((Mage) playersHero).useHeroPower(target);
								gamePlayWindow
										.rememberAction(playersName + " used his hero power on " + target.getName());

							}
							if (heroSelectedName.contains("Priest")) {
								((Priest) playersHero).useHeroPower(target);
								gamePlayWindow
										.rememberAction(playersName + " used his hero power on " + target.getName());

							}
						} catch (NotEnoughManaException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (HeroPowerAlreadyUsedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NotYourTurnException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (FullHandException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (FullFieldException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						if (attackInitialized) {
							try {
								playersHero.attackWithMinion(attacker, target);
								gamePlayWindow.rememberAction(
										playersName + " attacked " + target.getName() + " using " + attacker.getName());

							} catch (CannotAttackException e1) {
								e1.printStackTrace();
							} catch (NotYourTurnException e1) {
								e1.printStackTrace();
							} catch (TauntBypassException e1) {
								e1.printStackTrace();
							} catch (InvalidTargetException e1) {
								e1.printStackTrace();
							} catch (NotSummonedException e1) {
								e1.printStackTrace();
							}
						} else {
							if (spellInitialized) {

								if (spellObject instanceof MinionTargetSpell) {
									try {
										gamePlayWindow.rememberAction(playersName + " cast the spell "
												+ spellObject.getName() + " on " + target.getName());
										spellInitialized = false;
										heroPowerInitialized = false;
										attackInitialized = false;
										gamePlayWindow.refresh();
										playersHero.castSpell((MinionTargetSpell) spellObject, target);
									} catch (NotYourTurnException | NotEnoughManaException
											| InvalidTargetException e1) {

										e1.printStackTrace();
									}
								}
								if (spellObject instanceof LeechingSpell) {
									try {
										playersHero.castSpell((LeechingSpell) spellObject, target);
										gamePlayWindow.rememberAction(playersName + " cast the spell "
												+ spellObject.getName() + " on " + target.getName());
										spellInitialized = false;
										heroPowerInitialized = false;
										attackInitialized = false;
										gamePlayWindow.refresh();
									} catch (NotYourTurnException | NotEnoughManaException e1) {
										e1.printStackTrace();
									}
								}
							}
						}
					}
					spellInitialized = false;
					heroPowerInitialized = false;
					attackInitialized = false;
					gamePlayWindow.refresh();
				}

				if (e.getActionCommand().contains("PlayMinionCard")) {
					String actionCommand = e.getActionCommand();
					Scanner sc = new Scanner(actionCommand);
					sc.useDelimiter("  ");
					int index = Integer.parseInt(sc.next());
					Minion toBePlayed = (Minion) playersHero.getHand().get(index);
					try {
						playersHero.playMinion(toBePlayed);
						gamePlayWindow.rememberAction(playersName + " played the minion " + toBePlayed.getName());
						gamePlayWindow.refresh();
					} catch (NotYourTurnException e1) {
						// TODO Auto-generated catch block
						// e1.printStackTrace();
					} catch (NotEnoughManaException e1) {
						// TODO Auto-generated catch block
						// e1.printStackTrace();
					} catch (FullFieldException e1) {
						// TODO Auto-generated catch block
						// e1.printStackTrace();
					}
					spellInitialized = false;
					heroPowerInitialized = false;
					attackInitialized = false;
				}

				if (e.getActionCommand().contains("End Turn")) {
					spellInitialized = false;
					heroPowerInitialized = false;
					attackInitialized = false;
					try {
						gameObjectInstance.getGameInstance().getCurrentHero().endTurn();
						gameObjectInstance.setTurnCounter(gameObjectInstance.getTurnCounter() + 1);
						spellInitialized = false;
						heroPowerInitialized = false;
						attackInitialized = false;
						gamePlayWindow.refresh();
						spellInitialized = false;
						heroPowerInitialized = false;
						attackInitialized = false;
					} catch (FullHandException e1) {
						FullHandExceptionNotification exception = new FullHandExceptionNotification(this,
								e1.getBurned());
						gameObjectInstance.setTurnCounter(gameObjectInstance.getTurnCounter() + 1);
						spellInitialized = false;
						heroPowerInitialized = false;
						attackInitialized = false;
						gamePlayWindow.refresh();
						spellInitialized = false;
						heroPowerInitialized = false;
						attackInitialized = false;
					} catch (CloneNotSupportedException e1) {
						e1.printStackTrace();
					}
					gamePlayWindow.setNewTurn(new TurnNotification(this));

					spellInitialized = false;
					heroPowerInitialized = false;
					attackInitialized = false;
					ActionListener taskPerformer = new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							gamePlayWindow.getNewTurn().dispose();
						}
					};
					Timer timer = new Timer(5000, taskPerformer);
					timer.setRepeats(false);
					timer.start();
				}

			}
		}

	}

	public JPanel createDetailsPanel() {
		details = new JPanel(new GridLayout(1, 2));
		JPanel firstColumn = new JPanel(new GridLayout(2, 1));
		JPanel secondColumn = new JPanel(new GridLayout(2, 1));
		JPanel firstRow = new JPanel();
		firstRow.add(newLabel(playersName + " |"));
		firstRow.add(newLabel(heroSelectedName));
		if (!getGameObjectInstance().getGameInstance().getCurrentHero().equals(playersHero)) {
			firstRow.add(newLabel(" | #HandCards " + getPlayersHero().getHand().size()));
		}

		JPanel secondRow = new JPanel();
		secondRow.add(newLabel("HP " + playersHero.getCurrentHP() + " |"));
		secondRow.add(newLabel("Mana Crystals " + playersHero.getCurrentManaCrystals() + "/"
				+ playersHero.getTotalManaCrystals() + " |"));
		secondRow.add(newLabel("Cards Left " + Integer.toString(playersHero.getDeck().size()) + " |"));

		firstColumn.add(firstRow);
		firstColumn.add(secondRow);

		heroPower = new JButton("Hero Power");
		heroPower.addActionListener(this);
		heroPower.setActionCommand("Hero Power");

		endTurn = new JButton("End Turn");
		endTurn.addActionListener(this);
		endTurn.setActionCommand("End Turn");
		if (!gameObjectInstance.getGameInstance().getCurrentHero().equals(playersHero)) {
			endTurn.setEnabled(false);
			heroPower.setEnabled(false);
		}
		if (playersHero.isHeroPowerUsed()) {
			heroPower.setEnabled(false);
		}
		secondColumn.add(endTurn);
		heroPowerMagePriest = new JButton("Hero Power on own");
		if (playersHero instanceof Mage || playersHero instanceof Priest) {
			heroPowerMagePriest.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if (heroSelectedName.contains("Mage")) {
							((Mage) playersHero).useHeroPower(playersHero);
							gamePlayWindow.rememberAction(playersName + " Used his hero power on himself ");
						}
						if (heroSelectedName.contains("Priest")) {
							((Priest) playersHero).useHeroPower(playersHero);
							gamePlayWindow.rememberAction(playersName + " Used his hero power on himself ");
						}
						gamePlayWindow.refresh();
					} catch (NotEnoughManaException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (HeroPowerAlreadyUsedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotYourTurnException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FullHandException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FullFieldException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (CloneNotSupportedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			});
			JPanel twoHeroPower = new JPanel(new GridLayout(1, 2));
			twoHeroPower.add(heroPower);
			twoHeroPower.add(heroPowerMagePriest);
			secondColumn.add(twoHeroPower);
			if (playersHero.isHeroPowerUsed()) {
				heroPowerMagePriest.setEnabled(false);
			}
			if (!gameObjectInstance.getGameInstance().getCurrentHero().equals(playersHero)) {
				heroPowerMagePriest.setEnabled(false);
			}
			if (playersHero.getCurrentManaCrystals() < 2) {
				heroPowerMagePriest.setEnabled(false);
				heroPowerMagePriest.setToolTipText("You don't have enough Mana");
				heroPower.setEnabled(false);
				heroPower.setToolTipText("You don't have enough Mana");
			}

		} else {
			secondColumn.add(heroPower);
		}
		if (playersHero.getCurrentManaCrystals() < 2) {
			heroPowerMagePriest.setEnabled(false);
			heroPowerMagePriest.setToolTipText("You don't have enough Mana");
			heroPower.setEnabled(false);
			heroPower.setToolTipText("You don't have enough Mana");
		}
		details.add(firstColumn);
		details.add(secondColumn);

		details.setBorder(new TitledBorder("Control Panel"));
		return details;
	}

	public void makePlayingSide() {
		JPanel PlayingSide = new JPanel();
		TitledBorder playerNameBorder = new TitledBorder(new LineBorder(Color.black),
				"Player " + Integer.toString(playerNumber) + " " + playersName, 2, 2,
				new Font("", Font.BOLD, ((new JLabel()).getFont().getSize()) + 5), Color.RED);
		PlayingSide.setBorder(playerNameBorder);

		if (gameObjectInstance.getGameInstance().getCurrentHero().equals(playersHero)) {
			playerNameBorder.setTitleColor(Color.GREEN);
		}
		PlayingSide.setLayout(new BorderLayout());

		JPanel Field = createFieldPanel();
		PlayingSide.add(Field);

		JPanel Details = createDetailsPanel();
		PlayingSide.add(Details, BorderLayout.SOUTH);

		this.playingSide = PlayingSide;

	}

	public JPanel createFieldPanel() {
		fieldPanel = new JPanel(new BorderLayout());
		JPanel oppTarget = new JPanel();
		JLabel oppHeroTagForAttacking = newLabel(
				"<html><div style='text-align: center;'>" + "Click Here to Attack your Opponent's Hero" + "<br/>"

						+ " HP  " + opponent.getPlayersHero().getCurrentHP());
		if (attackInitialized || heroPowerInitialized || (spellInitialized && spellObject instanceof HeroTargetSpell)) {
			if (opponentHasTaunt || attackInitialized) {
				if (attackInitialized) {
					if (attacker.getName().contains("Ice")) {
						oppTarget.setToolTipText("You can't attack me, an Ice Howl can't target the opponent Hero");
						oppTarget.setBorder(new TitledBorder(loweredbevel, "", 2, 2,
								new Font("", Font.ITALIC, (temp.getFont().getSize())), Color.RED));
					} else if (opponentHasTaunt) {
						oppTarget.setToolTipText("You can't attack me, there is a taunt minion on the Field");
						oppTarget.setBorder(new TitledBorder(loweredbevel, "", 2, 2,
								new Font("", Font.ITALIC, (temp.getFont().getSize())), Color.RED));
					} else {
						oppTarget.addMouseListener(new MouseListener() {

							@Override
							public void mouseReleased(MouseEvent e) {
								// TODO Auto-generated method stub

							}

							@Override
							public void mousePressed(MouseEvent e) {
								JButton targetHero = new JButton();
								targetHero.setActionCommand("heroTarget");
								targetHero.addActionListener(thisPlayer);
								targetHero.doClick();
							}

							@Override
							public void mouseExited(MouseEvent e) {
								// TODO Auto-generated method stub

							}

							@Override
							public void mouseEntered(MouseEvent e) {
								// TODO Auto-generated method stub

							}

							@Override
							public void mouseClicked(MouseEvent e) {
								// TODO Auto-generated method stub

							}
						});
						oppTarget.setBorder(new TitledBorder(raisedbevel, "", 2, 2,
								new Font("", Font.ITALIC, (temp.getFont().getSize())), Color.RED));

					}
				} else {
					oppTarget.addMouseListener(new MouseListener() {

						@Override
						public void mouseReleased(MouseEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void mousePressed(MouseEvent e) {
							JButton targetHero = new JButton();
							targetHero.setActionCommand("heroTarget");
							targetHero.addActionListener(thisPlayer);
							targetHero.doClick();
						}

						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void mouseClicked(MouseEvent e) {
							// TODO Auto-generated method stub

						}
					});
					oppTarget.setBorder(new TitledBorder(raisedbevel, "", 2, 2,
							new Font("", Font.ITALIC, (temp.getFont().getSize())), Color.RED));
				}
			} else {
				oppTarget.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mousePressed(MouseEvent e) {
						JButton targetHero = new JButton();
						targetHero.setActionCommand("heroTarget");
						targetHero.addActionListener(thisPlayer);
						targetHero.doClick();
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub

					}
				});
				oppTarget.setBorder(new TitledBorder(raisedbevel, "", 2, 2,
						new Font("", Font.ITALIC, (temp.getFont().getSize())), Color.RED));
			}
		}

		oppHeroTagForAttacking.setForeground(Color.red);
		oppHeroTagForAttacking.setFont(new Font("", Font.BOLD, 15));
		oppTarget.add(oppHeroTagForAttacking);// need to enable clicking so that can be targeted
		if (playersHero.equals(gameObjectInstance.getGameInstance().getCurrentHero())) {
			fieldPanel.add(oppTarget, BorderLayout.NORTH);
		}
		JPanel playingField = createPlayingField();
		fieldPanel.add(playingField);

		if (playersHero.equals(gameObjectInstance.getGameInstance().getCurrentHero())) {
			JPanel handCards = createHandPanel();
			fieldPanel.add(handCards, BorderLayout.SOUTH);
		}
		// 3ndena minions, spells, sheep el hwa byegy bel hero power, minions wel sheep
		// bas el mmkn ytl3bo on field, spells l2 tb3n,
		// e7na 3yzen 7aga tupdate el field, teshof el player lw 3ndo 7aga, 3ndo yb2a
		// t3mlha add
		// nfs el 7aga lel hand,
		// el e7na kna bnfkr fe zyada eno n3ml el hand wel field arrays of 10 w 7 we
		// ymlo, wnm3l
		// kol element jpanel 3shan yt7t feha el card lw fe 7aga;

		return fieldPanel;
	}

	public JPanel createHandPanel() {
		JPanel HandCards = new JPanel(new GridLayout(2, 1));
		JPanel firstRow = new JPanel(new GridLayout(1, 5));
		JPanel secondRow = new JPanel(new GridLayout(1, 5));
		ArrayList<Card> handCards = playersHero.getHand();
		ArrayList<JPanel> handCardsObjects = new ArrayList<JPanel>();

		for (int i = 0; i < handCards.size(); i++) {
			if (handCards.get(i) instanceof Minion || handCards.get(i) instanceof Icehowl) {
				Minion x = (Minion) handCards.get(i);
				Minions minionCardObject = new Minions(x, i, this, false, false, attackInitialized,
						heroPowerInitialized, spellInitialized);
				handCardsObjects.add(minionCardObject);
			} else {

				Spell x = (Spell) handCards.get(i);
				Spells spellCardObject = new Spells(x, i, this);
				handCardsObjects.add(spellCardObject);
			}

		}

		JPanel[] availableHandCells = new JPanel[10];

		for (int i = 0; i < handCardsObjects.size(); i++) {
			availableHandCells[i] = handCardsObjects.get(i);
		}

		for (int i = 0; i < handCardsObjects.size(); i++) {
			if (handCardsObjects.size() > 5) {
				if (i < handCardsObjects.size() / 2) {
					firstRow.setLayout(new GridLayout(1, i));
					firstRow.add(availableHandCells[i]);
				} else {
					secondRow.setLayout(new GridLayout(1, i));
					secondRow.add(availableHandCells[i]);
				}
			} else {
				if (i < 5) {
					firstRow.setLayout(new GridLayout(1, i));
					firstRow.add(availableHandCells[i]);
				}
			}
		}

		if (handCardsObjects.size() > 5) {
			HandCards.add(firstRow);
			HandCards.add(secondRow);
		} else {
			HandCards.setLayout(new GridLayout(1, 1));
			HandCards.add(firstRow);

		}

		HandCards.setBorder(new TitledBorder(new LineBorder(Color.black), "Your Cards", 2, 2,
				new Font("", Font.ITALIC, HandCards.getFont().getSize()), Color.gray));

		return HandCards;
	}

	public JPanel createPlayingField() {

		JPanel wholeField = new JPanel(new GridLayout(2, 1));
		JPanel enemysField = new JPanel(new GridLayout(2, 1));
		JPanel playersField = new JPanel(new GridLayout(2, 1));

		ArrayList<Minion> fieldMinions = playersHero.getField();
		// ArrayList<Minions> fieldCardsObjects =
		// Minions.createFieldPanels(fieldMinions, this);

		ArrayList<Minion> enemyFieldMinions = opponent.getPlayersHero().getField();
		// ArrayList<Minions> enemyFieldCardsObjects =
		// Minions.createFieldPanels(enemyFieldMinions, this);

		ArrayList<Minions> fieldCardsObjects = new ArrayList<Minions>();
		ArrayList<Minions> enemyFieldCardsObjects = new ArrayList<Minions>();

		for (int i = 0; i < fieldMinions.size(); i++) {
			if (fieldMinions.get(i) instanceof Minion || fieldMinions.get(i) instanceof Icehowl) {
				Minion x = (Minion) fieldMinions.get(i);
				Minions minionCardObject = new Minions(x, i, this, true, false, attackInitialized, heroPowerInitialized,
						spellInitialized);
				fieldCardsObjects.add(minionCardObject);
			}
		}
		for (int i = 0; i < enemyFieldMinions.size(); i++) {
			if (enemyFieldMinions.get(i) instanceof Minion || enemyFieldMinions.get(i) instanceof Icehowl) {
				Minion x = (Minion) enemyFieldMinions.get(i);
				Minions minionCardObject = new Minions(x, i, this, true, true, attackInitialized, heroPowerInitialized,
						spellInitialized);
				enemyFieldCardsObjects.add(minionCardObject);
			}
		}

		JPanel[] enemysFieldCells = new JPanel[7];
		JPanel[] playersFieldCells = new JPanel[7];

		for (int i = 0; i < 7; i++) {
			JLabel emptyCell = newLabel("");
			JPanel emptyCellPanel = new JPanel();

			emptyCellPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "Empty Cell", 2, 2,
					new Font("", Font.ITALIC, emptyCell.getFont().getSize()), Color.RED));
			emptyCellPanel.add(emptyCell);
			enemysFieldCells[i] = emptyCellPanel;
			JLabel emptyCell1 = newLabel("");
			JPanel emptyCellPanel1 = new JPanel();
			emptyCellPanel1.setBorder(new TitledBorder(new LineBorder(Color.black), "Empty Cell", 2, 2,
					new Font("", Font.ITALIC, emptyCell1.getFont().getSize()), Color.green));
			emptyCellPanel1.add(emptyCell1);
			playersFieldCells[i] = emptyCellPanel1;
		}

		for (int i = 0; i < enemyFieldCardsObjects.size(); i++) {
			enemysFieldCells[i] = ((JPanel) enemyFieldCardsObjects.get(i));
		}

		for (int i = 0; i < fieldCardsObjects.size(); i++) {
			playersFieldCells[i] = ((JPanel) fieldCardsObjects.get(i));
		}

		JPanel firstRowPlayersField = new JPanel(new GridLayout(1, 4));
		JPanel secondRowPlayersField = new JPanel(new GridLayout(1, 3));

		JPanel firstRowOppField = new JPanel(new GridLayout(1, 3));
		JPanel secondRowOppField = new JPanel(new GridLayout(1, 4));

		for (int i = 0; i < 7; i++) {
			if (i < 3) {
				firstRowPlayersField.add(playersFieldCells[i]);
				firstRowOppField.add(enemysFieldCells[i]);
			}
			if (i >= 3) {
				secondRowPlayersField.add(playersFieldCells[i]);
				secondRowOppField.add(enemysFieldCells[i]);
			}
		}
		playersField.add(firstRowPlayersField);
		playersField.add(secondRowPlayersField);

		enemysField.add(secondRowOppField);
		enemysField.add(firstRowOppField);

		wholeField.add(enemysField);
		wholeField.add(playersField);

		return wholeField;

	}

	public JPanel createSelectedHeroesLabels() {
		JPanel ChosenHeroName = new JPanel();
		JLabel heroSelected = new JLabel(
				"<html><div style='text-align: center;'> You haven't selected <br/>a hero yet. <br/>" + "  ",
				SwingConstants.CENTER);
		heroSelected.setFont(new Font("", Font.BOLD, 15));
		heroSelected.setForeground(Color.white);

		this.heroSelected = heroSelected;
		ChosenHeroName.add(heroSelected);
		ChosenHeroName.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));

		return ChosenHeroName;
	}

	public JTextField createJTextField() {
		JTextField nameInput = new JTextField("Enter your Name", SwingConstants.CENTER);
		nameInput.setForeground(Color.white);
		nameInput.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		nameInput.setSize(nameInput.getWidth() + 5, nameInput.getHeight() + 4);
		nameInput.setFont(new Font(nameInput.getFont().getFamily(), Font.PLAIN, 15));
		nameInput.setMargin(new Insets(2, 2, 2, 2));
		nameInput.setHorizontalAlignment(JTextField.CENTER);
		nameInput.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (nameInput.getText().compareTo("You haven't selected a hero yet.") == 0
						|| nameInput.getText().compareTo("Enter your Name") == 0
						|| nameInput.getText().compareTo("You forgot to input here!") == 0) {
					nameInput.setText("");
				}
				nameInput.setForeground(Color.white);
			}
		});

		nameInput.addActionListener(this);
		nameInput.setActionCommand("nameInput");
		this.nameInput = nameInput;
		return nameInput;
	}

	public JPanel createJScrollPanel() {
		JPanel HeroChoices = new JPanel();
		HeroChoices.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		HeroChoices.setLayout(new GridLayout(5, 1, 0, 5));

		JButton Hunter = new JButton("Hunter - Rexxar");
		Hunter.setFont(new Font("", Font.BOLD, 13));
		Hunter.setBorder(new LineBorder(Color.black));
		Hunter.setForeground(Color.white);
		Hunter.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		Hunter.setContentAreaFilled(false);
		Hunter.setActionCommand(" Player Is Hunter");
		Hunter.addActionListener(this);
		HeroChoices.add(Hunter);

		JButton Mage = new JButton("Mage - Jaina Proudmoore");
		Mage.setFont(new Font("", Font.BOLD, 13));
		Mage.setBorder(new LineBorder(Color.black));
		Mage.setForeground(Color.white);
		Mage.setOpaque(false);
		Mage.setContentAreaFilled(false);
		Mage.addActionListener(this);
		Mage.setActionCommand(" Player Is Mage");
		HeroChoices.add(Mage);

		JButton Paladin = new JButton("Paladin - Uther Lightbringer");
		Paladin.setFont(new Font("", Font.BOLD, 13));
		Paladin.setBorder(new LineBorder(Color.black));
		Paladin.setForeground(Color.white);
		Paladin.setOpaque(false);
		Paladin.setContentAreaFilled(false);
		Paladin.addActionListener(this);
		Paladin.setActionCommand("Player Is Paladin");
		HeroChoices.add(Paladin);

		JButton Priest = new JButton("Priest - Anduin Wrynn");
		Priest.setFont(new Font("", Font.BOLD, 13));
		Priest.setBorder(new LineBorder(Color.black));
		Priest.setForeground(Color.white);
		Priest.setOpaque(false);
		Priest.setContentAreaFilled(false);
		Priest.addActionListener(this);
		Priest.setActionCommand("Player Is Priest");
		HeroChoices.add(Priest);

		JButton Warlock = new JButton("Warlock - Gul'dan");
		Warlock.setFont(new Font("", Font.BOLD, 13));
		Warlock.setBorder(new LineBorder(Color.black));
		Warlock.setForeground(Color.white);
		Warlock.setOpaque(false);
		Warlock.setContentAreaFilled(false);
		Warlock.addActionListener(this);
		Warlock.addActionListener(this);
		Warlock.setActionCommand("Player Is Warlock");
		HeroChoices.add(Warlock);

		JScrollPane PlayerScrollPane = new JScrollPane(HeroChoices, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		PlayerScrollPane.setOpaque(false);
		PlayerScrollPane.setPreferredSize(new Dimension(210, 100));
		PlayerScrollPane.setMinimumSize(PlayerScrollPane.getPreferredSize());
		PlayerScrollPane.setMaximumSize(PlayerScrollPane.getPreferredSize());
		PlayerScrollPane.setSize(PlayerScrollPane.getPreferredSize());

		JViewport transparent = new JViewport();
		transparent.setOpaque(false);
		transparent.setView(HeroChoices);

		PlayerScrollPane.setViewport(transparent);

		this.scrollPanels = PlayerScrollPane;
		JPanel returnPanel = new JPanel();
		returnPanel.setOpaque(false);
		returnPanel.add(PlayerScrollPane);
		return returnPanel;
	}

	public boolean isHeroHasBeenSelected() {
		return heroHasBeenSelected;
	}

	public JTextField getNameInput() {
		return nameInput;
	}

	public Hero getPlayersHero() {
		return playersHero;
	}

	public String getPlayersName() {
		return playersName;
	}

	public GameObject getGameObjectInstance() {
		return gameObjectInstance;
	}

	public void setGameObjectInstance(GameObject gameObjectInstance) {
		this.gameObjectInstance = gameObjectInstance;
	}

	public Player getPlayerOpponent() {
		return opponent;
	}

	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}

	public GamePlayWindow getGamePlayWindow() {
		return gamePlayWindow;
	}

	public void setGamePlayWindow(GamePlayWindow gamePlayWindow) {
		this.gamePlayWindow = gamePlayWindow;
	}

	public ArrayList<String> getChargeAttribute() {
		return chargeAttribute;
	}

	public void setChargeCheck(ArrayList<String> chargeAttribute) {
		this.chargeAttribute = chargeAttribute;
	}

	public JPanel getPlayingSide() {
		return playingSide;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public void setPlayersName(String playersName) {
		this.playersName = playersName;
	}

	public String getHeroSelectedName() {
		return heroSelectedName;
	}

	public void setHeroSelectedName(String heroSelectedName) {
		this.heroSelectedName = heroSelectedName;
	}

	public Spell getSpellObject() {
		return spellObject;
	}

	public void setSpellObject(Spell spellObject) {
		this.spellObject = spellObject;
	}

	public JLabel newLabel(String text) {
		JLabel label = new JLabel("<html><div style='text-align: center;'>" + text, SwingConstants.CENTER);
		return label;
	}

	@Override
	public void onGameOver() {
		endGame = new EndGameNotification(thisPlayer);
	}
}
