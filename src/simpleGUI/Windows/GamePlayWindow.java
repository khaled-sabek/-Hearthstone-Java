package simpleGUI.Windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import exceptions.FullHandException;
import simpleGUI.Notifications.TurnNotification;
import simpleGUI.Objects.GameObject;
import simpleGUI.Objects.Player;

@SuppressWarnings({ "serial" })
public class GamePlayWindow extends JFrame implements ActionListener, ItemListener {

	int screenHeight;
	int screenWidth;

	private JPanel Toolbar;
	private JPanel MainPanel;

	private Player playerOne;
	private Player playerTwo;

	private JPanel playingSideOne;
	private JPanel playingSideTwo;

	private JPanel GameField;

	@SuppressWarnings("unused")
	private GameObject gameObjectInstance;
	private TurnNotification beginning;
	private TurnNotification newTurn;

	private JPanel ToolbarDone;

	@SuppressWarnings("rawtypes")
	private JComboBox ComboBox;
	private ArrayList<String> actionList;
	private JPanel ToolbarPanel;

	private JCheckBox fullScreenChoice;
	private JCheckBox music;
	GamePlayWindow thisWindow;
	private Dimension wanted;
	private Dimension dim;

	HeroSelectionWindow hsW;

	public GamePlayWindow(Player playerOne, Player playerTwo, HeroSelectionWindow hsW)
			throws FullHandException, CloneNotSupportedException, InterruptedException {

		this.hsW = hsW;
		thisWindow = this;

		dim = Toolkit.getDefaultToolkit().getScreenSize();
		screenHeight = dim.height;
		screenWidth = dim.width;
		wanted = new Dimension((int) (screenWidth / 1.1), (int) (screenHeight / 1.1));

		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
		this.gameObjectInstance = new GameObject(playerOne, playerTwo);

		actionList = new ArrayList<String>();
		actionList.add("No Actions done yet");

		playerOne.setGamePlayWindow(this);
		playerTwo.setGamePlayWindow(this);

		setSize((int) (screenWidth / 1.1), (int) (screenHeight / 1.1));
		setPreferredSize(getSize());

		this.setLocation(dim.width / 2 - this.getSize().width / 2, (dim.height / 2 - this.getSize().height / 2) - 10);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// plan borderlayout, to put toolbar on the bottom, then inside have box
		setLayout(new BorderLayout());

		ToolbarDone = createToolbar();
		this.Toolbar = ToolbarDone;
		add(Toolbar, BorderLayout.SOUTH);

		this.GameField = createGameField();
		add(GameField, BorderLayout.CENTER);

		this.beginning = new TurnNotification(playerOne);

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				getBeginning().dispose();

			}
		};
		Timer timer = new Timer(5000, taskPerformer);
		timer.setRepeats(false);
		timer.start();

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

	}

	public JPanel createMainPanel() {
		JPanel MainPanel = new JPanel();
		MainPanel.setBorder(new LineBorder(Color.black));
		MainPanel.setLayout(new GridLayout(1, 2));
		playerOne.makePlayingSide();
		playerTwo.makePlayingSide();
		playingSideOne = playerOne.getPlayingSide();
		playingSideTwo = playerTwo.getPlayingSide();
		MainPanel.add(playingSideOne);
		MainPanel.add(playingSideTwo);
		return MainPanel;
	}

	public JPanel createGameField() {
		JPanel GameField = new JPanel();
		GameField.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white));
		this.MainPanel = createMainPanel();
		GameField.setLayout(new BorderLayout());
		GameField.add(MainPanel, BorderLayout.CENTER);
		return GameField;
	}

	public void refresh() {
		GameField.remove(MainPanel);
		repaint();
		revalidate();

		playerOne.checkOpponentTaunt();
		playerTwo.checkOpponentTaunt();

		this.MainPanel = createMainPanel();
		GameField.add(MainPanel, BorderLayout.CENTER);
		revalidate();
		repaint();

		remove(Toolbar);
		ToolbarDone = createToolbar();
		this.Toolbar = ToolbarDone;
		add(Toolbar, BorderLayout.SOUTH);
		revalidate();
		repaint();
	}

	public void rememberAction(String action) {
		if (getActionList().get(0).contains("No Actions done yet")) {
			getActionList().remove(0);
		}
		getActionList().add(0, action);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JPanel createToolbar() {
		this.ToolbarPanel = new JPanel();
		TitledBorder x = new TitledBorder("Toolbar");
		ToolbarPanel.setBorder(x);
		ToolbarPanel.setLayout(new GridLayout(1, 4));

		ComboBox = new JComboBox(actionList.toArray());
		JPanel comboBoxP = new JPanel(new GridLayout());
		comboBoxP.add(ComboBox);

		JButton Exit = new JButton("Exit");
		Exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				dispose();
			}
		});

		fullScreenChoice = new JCheckBox("<html>Full Screen</html>");
		fullScreenChoice.setForeground(Color.black);
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

		music = new JCheckBox("<html>Mute</html>");
		music.setForeground(Color.black);
		music.setHorizontalAlignment(JTextField.CENTER);
		music.setFont(new Font(fullScreenChoice.getFont().getFamily(), Font.PLAIN, 10));
		music.setSelected(false);
		music.setOpaque(false);
		music.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					hsW.clip.stop();

				} else {
					hsW.clip.start();
					hsW.clip.loop(Clip.LOOP_CONTINUOUSLY);
				}
			}
		});

		JPanel exitEnvelope = new JPanel(new FlowLayout());
		Exit.setMargin(new Insets(0, 0, 0, 0));
		exitEnvelope.add(Exit);

		ToolbarPanel.add(comboBoxP);

		JPanel fullscrEnv = new JPanel(new FlowLayout());
		fullScreenChoice.setMargin(new Insets(0, 0, 0, 0));
		fullscrEnv.add(fullScreenChoice);

		JPanel musicEnv = new JPanel(new FlowLayout());
		music.setMargin(new Insets(0, 0, 0, 0));
		musicEnv.add(music);

		ToolbarPanel.add(fullscrEnv);
		ToolbarPanel.add(musicEnv);
		ToolbarPanel.add(exitEnvelope);

		return ToolbarPanel;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public ArrayList<String> getActionList() {
		return actionList;
	}

	public void setActionList(ArrayList<String> actionList) {
		this.actionList = actionList;
	}

	public TurnNotification getBeginning() {
		return beginning;
	}

	public void setBeginning(TurnNotification beginning) {
		this.beginning = beginning;
	}

	public TurnNotification getNewTurn() {
		return newTurn;
	}

	public void setNewTurn(TurnNotification newTurn) {
		this.newTurn = newTurn;
	}

}
