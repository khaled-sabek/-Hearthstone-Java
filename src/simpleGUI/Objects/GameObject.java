package simpleGUI.Objects;

import engine.Game;
import exceptions.FullHandException;

@SuppressWarnings({ "unused" })

public class GameObject {
	private Game gameInstance;

	private int turnCounter;

	public int getTurnCounter() {
		return turnCounter;
	}

	public void setTurnCounter(int turnCounter) {
		this.turnCounter = turnCounter;
	}

	public Game getGameInstance() {
		return gameInstance;
	}

	public void setGameInstance(Game gameInstance) {
		this.gameInstance = gameInstance;
	}

	private Player currentPlayer;
	private Player opponent;
	private Player firstPlayer;
	private Player secondPlayer;

	public GameObject(Player firstPlayer, Player secondPlayer) throws FullHandException, CloneNotSupportedException {
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
		this.gameInstance = new Game(firstPlayer.getPlayersHero(), secondPlayer.getPlayersHero());
		gameInstance.setListener(firstPlayer);
		gameInstance.setListener(secondPlayer);
		this.currentPlayer = firstPlayer.getPlayersHero().equals(gameInstance.getCurrentHero()) ? firstPlayer
				: secondPlayer;
		firstPlayer.setOpponent(secondPlayer);
		secondPlayer.setOpponent(firstPlayer);
		this.opponent = currentPlayer.equals(firstPlayer) ? secondPlayer : firstPlayer;
		firstPlayer.setGameObjectInstance(this);
		secondPlayer.setGameObjectInstance(this);
		this.turnCounter = 1;
	}

}
