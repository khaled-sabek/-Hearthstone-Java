package simpleGUI.Windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import exceptions.FullHandException;
import simpleGUI.Objects.Player;

@SuppressWarnings("serial")
class contentPaneOverride extends JPanel {
	public contentPaneOverride() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenHeight = dim.height;
		int screenWidth = dim.width;
		Dimension wanted = new Dimension((int) (screenWidth / 1.2), (int) (screenHeight / 1.2) - 100);
		// setLayout(new GridLayout());
		setSize(wanted);
		setPreferredSize(wanted);
		setMinimumSize(wanted);

	}

	@Override
	public boolean isOptimizedDrawingEnabled() {
		return false;
	}
}

@SuppressWarnings({ "serial", "unused" })
public class HeroSelectionWindow extends JFrame implements ItemListener, ActionListener, MouseListener {

	int screenHeight;
	int screenWidth;
	Dimension labelDimensions;
	Player firstPlayer;
	Player secondPlayer;

	JPanel firstPlayersScrollPane;
	JPanel secondPlayersScrollPane;

	JPanel firstPlayerSelectedHero;
	JPanel secondPlayerSelectedHero;

	JTextField firstPlayerTextField;
	JTextField secondPlayerTextField;

	JCheckBox fullScreenChoice;
	JButton nextWindow;

	GamePlayWindow gamePlayWindow;

	contentPaneOverride content;
	contentPaneOverride Main;

	ImageIcon gifImage;

	Dimension wanted;

	HeroSelectionWindow thisWindow;

	// int randomInteger = 1;

	Image img;
	Clip clip;
	AudioInputStream audioInputStream;

	public HeroSelectionWindow()
			throws UnsupportedAudioFileException, IOException, LineUnavailableException, FontFormatException {

		thisWindow = this;

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		screenHeight = dim.height;
		screenWidth = dim.width;
		wanted = new Dimension((int) (screenWidth / 1.2), (int) (screenHeight / 1.2));
		setSize(wanted);

		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		firstPlayer = new Player();
		firstPlayer.setPlayerNumber(1);
		secondPlayer = new Player();
		secondPlayer.setPlayerNumber(2);

		Main = new contentPaneOverride();
		Main.setOpaque(false);
		Main.setLayout(new BoxLayout(Main, BoxLayout.PAGE_AXIS));

		labelDimensions = new Dimension((int) (screenWidth / 1.2), (int) ((screenHeight / 1.2) / 4));

		JPanel title = new JPanel();
		title.setOpaque(false);
		JLabel TitleSelectYourHero = new JLabel("<html><div style='text-align: center;'><br/>Select your Hero<br/>",
				SwingConstants.CENTER);
		TitleSelectYourHero.setForeground(Color.white);

		TitleSelectYourHero.setFont(new Font("Serif", Font.PLAIN, 60));
		title.add(TitleSelectYourHero);
		Main.add(title);

		JPanel players = new JPanel();
		players.setOpaque(false);
		players.setLayout(new GridLayout(1, 5));
		JLabel firstPlayerNameTag = newLabel("First Player");
		firstPlayerNameTag.setForeground(Color.white);
		JLabel secondPlayerNameTag = newLabel("Second Player");
		secondPlayerNameTag.setForeground(Color.white);
		firstPlayerNameTag.setFont(new Font("Serif", Font.PLAIN, 40));
		secondPlayerNameTag.setFont(new Font("Serif", Font.PLAIN, 40));
		players.add(newPanel());
		players.add(firstPlayerNameTag);
		players.add(newPanel());
		players.add(secondPlayerNameTag);
		players.add(newPanel());

		Main.add(players);

		// delw2ty e7na 3mlna scrollpanels lel etnen we syvhom hnak, el fkra en e7na
		// lazem nst5dm dool brdo
		// 3shan lw 8yrna fehom y8yr hna, w3mlna el listeners el players 3shan mn3odsh
		// neshof player 1 wala 2
		// bas hal f3ln hn7tag nefr2? hngrb

		for (int i = 0; i < 4; i++) {
			Main.add(newPanel());
		}

		JPanel heroChoices = new JPanel();
		heroChoices.setOpaque(false);
		heroChoices.setLayout(new GridLayout(1, 5));
		heroChoices.add(newPanel());
		firstPlayersScrollPane = firstPlayer.createJScrollPanel();
		firstPlayersScrollPane.setOpaque(false);
		heroChoices.add(firstPlayersScrollPane);
		heroChoices.add(newPanel());
		secondPlayersScrollPane = secondPlayer.createJScrollPanel();
		secondPlayersScrollPane.setOpaque(false);
		heroChoices.add(secondPlayersScrollPane);
		heroChoices.add(newPanel());
		// heroChoices.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
		Main.add(heroChoices);

		for (int i = 0; i < 2; i++) {
			Main.add(newPanel());
		}

		JPanel playerHeroSelection = new JPanel();
		playerHeroSelection.setOpaque(false);
		playerHeroSelection.setLayout(new GridLayout(1, 5));
		JPanel settingSize = new JPanel();
		settingSize.setOpaque(false);
		JLabel asIfSelected = new JLabel(
				"<html><div style='text-align: center;'>You selected : " + "<br/>" + "Paladin - Uther Lightbringer");
		asIfSelected.setFont(new Font("", Font.PLAIN, 15));
		asIfSelected.setForeground(Color.white);
		settingSize.add(asIfSelected);
		settingSize.setVisible(false);
		playerHeroSelection.add(settingSize);
		firstPlayerSelectedHero = firstPlayer.createSelectedHeroesLabels();
		playerHeroSelection.add(firstPlayerSelectedHero);
		playerHeroSelection.add(newPanel());
		secondPlayerSelectedHero = secondPlayer.createSelectedHeroesLabels();
		playerHeroSelection.add(secondPlayerSelectedHero);
		playerHeroSelection.add(newPanel());
		Main.add(playerHeroSelection);

		for (int i = 0; i < 2; i++) {
			Main.add(newPanel());
		}

		JPanel playerTextFields = new JPanel();
		playerTextFields.setOpaque(false);
		playerTextFields.setLayout(new GridLayout(1, 5));
		playerTextFields.add(newPanel());
		JPanel firstPlayerTextFieldPanel = new JPanel(new GridLayout());
		firstPlayerTextFieldPanel.setOpaque(false);
		firstPlayerTextField = firstPlayer.createJTextField();
		firstPlayerTextFieldPanel.add(firstPlayerTextField);
		playerTextFields.add(firstPlayerTextFieldPanel);
		playerTextFields.add(newPanel());
		JPanel secondPlayerTextFieldPanel = new JPanel(new GridLayout());
		secondPlayerTextFieldPanel.setOpaque(false);
		secondPlayerTextField = secondPlayer.createJTextField();
		secondPlayerTextFieldPanel.add(secondPlayerTextField);
		playerTextFields.add(secondPlayerTextFieldPanel);
		playerTextFields.add(newPanel());
		playerTextFields.setOpaque(false);
		Main.add(playerTextFields);

		JPanel buttonRow = new JPanel(new GridLayout(1, 8));
		buttonRow.setOpaque(false);
		for (int i = 0; i < 9; i++) {
			buttonRow.add(newPanel());
		}
		JPanel buttons = new JPanel(new GridLayout(3, 1));
		buttons.setOpaque(false);
		fullScreenChoice = new JCheckBox("<html>Full Screen</html>");
		fullScreenChoice.setForeground(Color.white);
		fullScreenChoice.setHorizontalAlignment(JTextField.CENTER);
		fullScreenChoice.setFont(new Font(fullScreenChoice.getFont().getFamily(), Font.PLAIN, 10));
		fullScreenChoice.setSelected(false);
		fullScreenChoice.setOpaque(false);
		fullScreenChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					thisWindow.dispose();
					thisWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
					thisWindow.setUndecorated(true);
					thisWindow.setVisible(true);
					thisWindow.revalidate();
					thisWindow.repaint();
				} else {
					thisWindow.dispose();
					thisWindow.setExtendedState(JFrame.NORMAL);
					thisWindow.setUndecorated(false);
					thisWindow.setSize(wanted);
					thisWindow.setPreferredSize(wanted);
					thisWindow.setLocation(dim.width / 2 - thisWindow.getSize().width / 2,
							dim.height / 2 - thisWindow.getSize().height / 2);
					thisWindow.setVisible(true);
					thisWindow.revalidate();
					thisWindow.repaint();
				}
			}
		});

		nextWindow = new JButton("Play!");
		nextWindow.setMargin(new Insets(0, 0, 0, 0));
		nextWindow.setFont(new Font("", Font.BOLD, 13));
		nextWindow.setBorder(new LineBorder(Color.black));
		nextWindow.setForeground(Color.white);
		nextWindow.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		nextWindow.setContentAreaFilled(false);
		nextWindow.addActionListener(this);
		nextWindow.addMouseListener((MouseListener) this);
		nextWindow.setActionCommand("NextWindowAfterHeroSelection");
		buttons.add(nextWindow);
		buttons.add(fullScreenChoice);

		buttonRow.add(buttons);
		for (int i = 0; i < 1; i++) {
			buttonRow.add(newPanel());
		}
		buttonRow.setOpaque(false);
		Main.setOpaque(false);
		Main.add(buttonRow);

		for (int i = 0; i < 15; i++) {
			Main.add(newPanel());
		}

		background(0);
		revalidate();
		repaint();

		setFont(new Font("System", Font.PLAIN, 14));
		Font f = getFont();
		FontMetrics fm = getFontMetrics(f);
		int x = fm.stringWidth("HearthStone");
		int y = fm.stringWidth(" ");
		int z = getWidth() / 2 - (x / 2);
		int w = z / y;
		String pad = "";
		pad = String.format("%" + w + "s", pad);
		setTitle(pad + "                               HearthStone ");

		addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent e) {
				getContentPane().removeAll();
				background(0);

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});

		audioInputStream = AudioSystem.getAudioInputStream(new File("bettermusic.wav").getAbsoluteFile());
		clip = AudioSystem.getClip();
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();

	}

	public void background(int random) {
		content = new contentPaneOverride();
		content.setLayout(new OverlayLayout(content));

		if (random == 1) {

			img = Toolkit.getDefaultToolkit().createImage("2.gif");
			getContentPane().removeAll();

			gifImage = new ImageIcon(img);
			Image image = gifImage.getImage();
			Image newimg = image.getScaledInstance((int) thisWindow.getWidth(), (int) thisWindow.getHeight(),
					java.awt.Image.SCALE_FAST);
			gifImage = new ImageIcon(newimg);

			JLabel yourLabel = new JLabel(gifImage);
			yourLabel.setLayout(new GridLayout());

			JPanel setsizeyarab = new JPanel(new GridLayout());
			setsizeyarab.setPreferredSize(wanted);
			setsizeyarab.add(yourLabel);
			setsizeyarab.setMinimumSize(wanted);

			thisWindow.setContentPane(setsizeyarab);

		} else {
			img = Toolkit.getDefaultToolkit().createImage("finalcompressed.gif");

			gifImage = new ImageIcon(img);
			Image image = gifImage.getImage();
			Image newimg = image.getScaledInstance(getWidth(), getHeight(), java.awt.Image.SCALE_DEFAULT);
			gifImage = new ImageIcon(newimg);

			JLabel yourLabel = new JLabel(gifImage);
			yourLabel.setLayout(new GridLayout());

			JPanel setsizeyarab = new JPanel(new GridLayout());
			setsizeyarab.setPreferredSize(wanted);
			setsizeyarab.add(yourLabel);
			setsizeyarab.setMinimumSize(wanted);

			JPanel helpVision = new JPanel();
			helpVision.setOpaque(false);

			JPanel setsizeyarabmain = new JPanel(new GridLayout());
			setsizeyarabmain.add(Main);
			setsizeyarabmain.setOpaque(false);

			content.add(setsizeyarabmain);
			content.add(helpVision);
			content.add(setsizeyarab);

			content.revalidate();
			content.repaint();

			thisWindow.add(content);
		}
		thisWindow.revalidate();
		thisWindow.repaint();
	}

	private JPanel newPanel(JLabel label) {
		JPanel returnPanel = new JPanel();
		returnPanel.add(label);
		return returnPanel;
	}

	private JPanel newPanel() {
		JPanel returnPanel = new JPanel();
		returnPanel.setOpaque(false);
		return returnPanel;
	}

	public JLabel newLabel(String text) {
		JLabel label = new JLabel("<html><div style='text-align: center;'>" + text, SwingConstants.CENTER);
		label.setPreferredSize(labelDimensions);
		return label;

	}

	public void runGamePlayWindow() throws FullHandException, CloneNotSupportedException, InterruptedException {
		gamePlayWindow = new GamePlayWindow(firstPlayer, secondPlayer, thisWindow);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contains("NextWindowAfterHeroSelection")) {
			if (!firstPlayer.isHeroHasBeenSelected() || !secondPlayer.isHeroHasBeenSelected()
					|| firstPlayer.getNameInput().getText().compareTo("You haven't selected a hero yet.") == 0
					|| firstPlayer.getNameInput().getText().compareTo("Enter your Name") == 0
					|| firstPlayer.getNameInput().getText().compareTo("You forgot to input here!") == 0
					|| firstPlayer.getNameInput().getText().compareTo("") == 0
					|| secondPlayer.getNameInput().getText().compareTo("You haven't selected a hero yet.") == 0
					|| secondPlayer.getNameInput().getText().compareTo("Enter your Name") == 0
					|| secondPlayer.getNameInput().getText().compareTo("") == 0
					|| secondPlayer.getNameInput().getText().compareTo("You forgot to input here!") == 0) {

				if (!firstPlayer.isHeroHasBeenSelected() || !secondPlayer.isHeroHasBeenSelected()) {
					JFrame Error = new JFrame();
					JLabel Message = new JLabel("Make sure you chose your Heroes", JLabel.CENTER);
					Message.setForeground(Color.black);
					Message.setFont(new Font(Message.getFont().getFamily(), Font.PLAIN, 20));
					Error.setSize(500, 200);
					Error.add(Message);
					Error.setVisible(true);
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					Error.setLocation(dim.width / 2 - Error.getSize().width / 2,
							dim.height / 2 - Error.getSize().height / 2);

				}

				if (firstPlayer.getNameInput().getText().compareTo("You haven't selected a hero yet.") == 0
						|| firstPlayer.getNameInput().getText().compareTo("Enter your Name") == 0
						|| firstPlayer.getNameInput().getText().compareTo("You forgot to input here!") == 0
						|| firstPlayer.getNameInput().getText().compareTo("") == 0) {
					firstPlayer.getNameInput().setText("You forgot to input here!");
					firstPlayer.getNameInput().setForeground(Color.red);
					JFrame Error = new JFrame();
					JLabel Message = new JLabel("The first player is missing a name", JLabel.CENTER);
					Message.setForeground(Color.black);
					Message.setFont(new Font(Message.getFont().getFamily(), Font.PLAIN, 20));
					Error.setSize(500, 200);
					Error.add(Message);
					Error.setVisible(true);
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					Error.setLocation(dim.width / 2 - Error.getSize().width / 2,
							dim.height / 2 - Error.getSize().height / 2);
				}
				if (secondPlayer.getNameInput().getText().compareTo("You haven't selected a hero yet.") == 0
						|| secondPlayer.getNameInput().getText().compareTo("Enter your Name") == 0
						|| secondPlayer.getNameInput().getText().compareTo("") == 0
						|| secondPlayer.getNameInput().getText().compareTo("You forgot to input here!") == 0) {
					secondPlayer.getNameInput().setText("You forgot to input here!");
					secondPlayer.getNameInput().setForeground(Color.red);
					JFrame Error = new JFrame();
					JLabel Message = new JLabel("The second player is missing a name", JLabel.CENTER);
					Message.setForeground(Color.black);
					Message.setFont(new Font(Message.getFont().getFamily(), Font.PLAIN, 20));
					Error.setSize(500, 200);
					Error.add(Message);
					Error.setVisible(true);
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					Error.setLocation(dim.width / 2 - Error.getSize().width / 2,
							dim.height / 2 - Error.getSize().height / 2);
				}

			} else {
				firstPlayer.setPlayersName(firstPlayer.getNameInput().getText());
				secondPlayer.setPlayersName(secondPlayer.getNameInput().getText());
				thisWindow.getContentPane().removeAll();
				clip.stop();

				try {
					audioInputStream = AudioSystem.getAudioInputStream(new File("bgmusic.wav").getAbsoluteFile());
				} catch (UnsupportedAudioFileException | IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				try {
					clip = AudioSystem.getClip();
				} catch (LineUnavailableException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					clip.open(audioInputStream);
				} catch (LineUnavailableException | IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				clip.start();

				Timer swingTimer2 = new Timer(1500, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						background(1);
					}
				});
				swingTimer2.start();
				swingTimer2.setRepeats(false);

				Timer swingTimer = new Timer(6200, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							dispose();
							runGamePlayWindow();
						} catch (FullHandException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				});
				swingTimer.start();
				swingTimer.setRepeats(false);

			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
