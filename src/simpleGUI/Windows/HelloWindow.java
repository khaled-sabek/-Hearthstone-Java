package simpleGUI.Windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import exceptions.FullHandException;

@SuppressWarnings({ "serial", "unused" })
public class HelloWindow extends JFrame {

	JLabel Message;
	JProgressBar ProgressBar;
	int HelloWindowFontSize;
	Timer Timer;
	Timer Timer2;
	int Temp1 = 0;
	int yyy = 0;
	boolean yf = false;

	int i = 0;

	public HelloWindow() throws InterruptedException, FullHandException, CloneNotSupportedException, IOException,
			UnsupportedAudioFileException, LineUnavailableException, FontFormatException {

		Message = new JLabel("Hearthstone", JLabel.CENTER);
		Message.setForeground(Color.white);
		Message.setFont(new Font("", Font.PLAIN, 2));
		HelloWindowFontSize = Message.getFont().getSize();
		setSize(200, 200);

		ProgressBar = new JProgressBar();
		ProgressBar.setSize(getWidth(), 2);
		ProgressBar.setValue(0);

		ProgressBar.setStringPainted(true);

		add(Message);
		add(ProgressBar, BorderLayout.SOUTH);
		setUndecorated(true);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

		StartAction();

	}

	public void FadeIn() throws InterruptedException {

		Timer = new Timer(6, new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				if (Temp1 < 255) {
					if ((Temp1 % 6.5 == 0) && HelloWindowFontSize <= 30 && Temp1 != 250) {
						yf = true;
						ProgressBar.setValue(i);
						if (i <= 100) {
							i += 6;
						}
					}

					if (yf) {
						Message.setFont(new Font(Message.getName(), Font.PLAIN, HelloWindowFontSize++));
						yf = false;
					}

					getContentPane().setBackground(new Color(0, 0, 0, Temp1++));
				}
			}

		});

		Timer.start();

	}

	public void StartAction() throws InterruptedException, FullHandException, CloneNotSupportedException, IOException,
			UnsupportedAudioFileException, LineUnavailableException, FontFormatException {
		FadeIn();
		TimeUnit.SECONDS.sleep(3);
		dispose();
		HeroSelectionWindow simpleGUIfirstWindow = new HeroSelectionWindow();

	}

	public static void main(String[] args) throws FullHandException, InterruptedException, CloneNotSupportedException,
			IOException, UnsupportedAudioFileException, LineUnavailableException, FontFormatException {
		HelloWindow x = new HelloWindow();
	}

}
