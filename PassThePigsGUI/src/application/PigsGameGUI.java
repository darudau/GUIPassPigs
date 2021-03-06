package application;

import java.util.Observable;
import java.util.Observer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * PassPigsGUI class creates a GUI for the Pass the Pigs Game This GUI also
 * Contains the controller for the PassThePigsGame
 * 
 * @author Douglas Rudau
 * @version May 17, 2019
 *
 */
public class PigsGameGUI extends BorderPane
		implements Observer, EventHandler<ActionEvent>
{
	private static final int HUMAN_TURN = 0;

	/** Constant to state when it is the AI's turn */
	private final int AI_TURN = 1;

	/** Pass Pigs Player */
	private PassPigsPlayer aiPlayer;

	/** Pass Pigs Game Object */
	private PassThePigsGame game;

	/** Menu Bar for the game */
	private MenuBar menuBar;

	/** Game Menu */
	private Menu gameMenu;

	/** New Game Option */
	private MenuItem newGameOption;

	/** Quit Option */
	private MenuItem quitOption;

	/** About Menu */
	private Menu aboutMenu;

	/** Rules Option */
	private MenuItem rulesOption;

	/** Roll Stats Option */
	private MenuItem rollStatsOption;

	/** About the Game option */
	private MenuItem aboutGameOption;

	/** Status of the game VBox */
	private VBox statusVBox;

	/** ScoreBoard */
	private Label scoreboardText;

	/** Current Roll Status */
	private TextArea rollStatusText;

	/** Shows whose turn it is */
	private Label turnStatus;

	/** HBox for Buttons */
	private HBox buttons;

	/** Roll Pigs Button */
	private Button rollPigsButton;

	/** Pass Pigs Button */
	private Button passPigsButton;

	/** AI's Turn Button */
	private Button aiTurnButton;

	/**
	 * Constructor
	 */
	public PigsGameGUI()
	{
		game = new PassThePigsGame(2);
		aiPlayer = new PassPigsPlayer();

		// makes the GUI an observer of the model
		game.addObserver(this);

		this.menuBar = new MenuBar();
		this.gameMenu = new Menu("Game");
		this.newGameOption = new MenuItem("New Game");
		this.newGameOption.setOnAction(this);

		this.gameMenu.getItems().add(newGameOption);
		this.quitOption = new MenuItem("Quit Game");
		this.quitOption.setOnAction(this);

		this.gameMenu.getItems().add(quitOption);

		this.aboutMenu = new Menu("About");
		this.aboutGameOption = new MenuItem("About Pass the Pigs");
		this.aboutGameOption.setOnAction(this);
		this.aboutMenu.getItems().add(aboutGameOption);

		this.rulesOption = new MenuItem("Rules");
		this.aboutMenu.getItems().add(rulesOption);
		this.rulesOption.setOnAction(this);

		this.rollStatsOption = new MenuItem("Statistics");
		this.rollStatsOption.setOnAction(this);
		this.aboutMenu.getItems().add(rollStatsOption);

		this.menuBar.getMenus().add(gameMenu);
		this.menuBar.getMenus().add(aboutMenu);

		this.statusVBox = new VBox();
		this.scoreboardText = new Label();
		this.rollStatusText = new TextArea();
		this.rollStatusText.setEditable(false);
		this.turnStatus = new Label();
		this.statusVBox.getChildren().add(turnStatus);
		this.setTurnStatus();
		game.getPlayerTurn();
		this.statusVBox.getChildren().add(new Separator());
		this.statusVBox.getChildren().add(rollStatusText);
		this.statusVBox.getChildren().add(new Separator());
		this.statusVBox.getChildren().add(this.scoreboardText);

		this.buttons = new HBox();
		this.rollPigsButton = new Button("Roll the Pigs");
		this.rollPigsButton.setOnAction(this);
		this.passPigsButton = new Button("Pass the Pigs");
		this.passPigsButton.setOnAction(this);
		this.aiTurnButton = new Button("View AI's Roll");
		this.aiTurnButton.setOnAction(this);
		this.buttons.getChildren().add(rollPigsButton);
		this.buttons.getChildren().add(passPigsButton);
		this.buttons.getChildren().add(this.aiTurnButton);

		this.setTop(menuBar);
		this.setBottom(buttons);
		this.setCenter(statusVBox);

		this.scoreboardText.setText(this.generateScoreboard());

	}

	/**
	 * Event handling method for the PigGameGUI.
	 * 
	 * @param event
	 *            the event that has taken place
	 */
	@Override
	public void handle(ActionEvent event)
	{
		if (event.getSource().equals(newGameOption))
		{
			game.newGame();
			this.scoreboardText.setText(game.scoreboardToString());
		}

		if (event.getSource().equals(quitOption))
		{
			System.exit(0);
		}

		if (event.getSource().equals(aboutGameOption))
		{
			displayAboutGame();
		}

		if (event.getSource().equals(rulesOption))
		{
			displayRules();
		}

		if (event.getSource().equals(rollStatsOption))
		{
			displayRollStats();
		}

		// user has selected the roll pigs button
		if (event.getSource().equals(rollPigsButton)
				&& game.getPlayerTurn() == HUMAN_TURN)
		{
			// roll the pigs
			if (game.isGameOver())
			{
				// redisplays the winner alert until the user starts a new game.
				displayWinner();
			}
			else
			{
				this.rollStatusText.setText(game.playerAction(1)
						+ " score for your turn is: " + game.getTurnScore());
			}
		}

		// User has selected the pass pigs button
		if (event.getSource().equals(passPigsButton)
				&& game.getPlayerTurn() == HUMAN_TURN)
		{
			game.playerAction(2);
		}

		if (event.getSource().equals(this.aiTurnButton)
				&& game.getPlayerTurn() == AI_TURN)
		{
			if (game.isGameOver())
			{
				displayWinner();
			}
			else
			{
				aiTurn();
			}
		}
	}

	/**
	 * Displays the statistics for each type of roll
	 */
	private void displayRollStats()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Roll Statistics");
		alert.setHeaderText("Statistics for each type of roll");
		alert.setContentText(
				"Outcome\t\tProbabiltiy\nPlain Side\t\t35%\nDottedSide\t30%"
						+ "\nRazorback\t22%\nTrotter\t\t 9%\nSnouter\t\t 3"
						+ "%\nLeaning Jowler\t 1%");
		alert.show();
	}

	/**
	 * Displays the rules
	 */
	private void displayRules()
	{
		String rules = "The first player to score 100 or more points wins the game. Each player will\n"
				+ "roll the pigs to attempt to gain points. The player may roll the pigs as many\n"
				+ "times as they would like, however with each roll the points you lose\n"
				+ "increases. The player�s turn is done if they roll a Pig Out (where the pigs\n"
				+ "land on opposite side), an Oinker (where the pigs land touching), or the\n"
				+ "player decides to pass the pigs to the next player and record their total\n"
				+ "number of points. If the player rolls an Oinker, their score for the game is\n"
				+ "reset to zero. Players can pass the pigs without rolling them, though this is\n"
				+ "not recommended as no points will be gained. ";

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Pass the Pigs Rules");
		alert.setHeaderText("Pass the Pigs Rules");
		alert.setContentText(rules);
		alert.show();
	}

	/**
	 * Displays Information about the game
	 */
	private void displayAboutGame()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About Pass the Pigs");
		alert.setHeaderText("Game created by: Douglas Rudau");
		alert.setContentText(
				"Created for COS 389 at Bethel University\nFor the final Project\nMay 10, 2019");
		alert.show();
	}

	/**
	 * Function to display the winner of the game, an alert is displayed
	 * explaining who won the game. The alter is shown until the user selects
	 * the Ok button within the alert.
	 */
	private void displayWinner()
	{
		this.scoreboardText.setText(game.scoreboardToString());

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game Over");

		if (game.getWinner() == HUMAN_TURN)
		{
			alert.setHeaderText("Congratulation: You won the game");
			alert.setContentText(
					"You have defeated the AI at Pass the Pigs\nThanks for playing");
		}
		else
		{
			alert.setHeaderText("Sorry, You lost to the AI");
			alert.setContentText("Better luck next time\nThanks for playing");
		}

		alert.showAndWait();
	}

	/**
	 * Update Function for the observer pattern.
	 */
	@Override
	public void update(Observable arg0, Object arg1)
	{
		setTurnStatus();

		if (game.isGameOver())
		{
			displayWinner();
		}
		else
		{
			this.rollStatusText.setText("");
			this.scoreboardText.setText(generateScoreboard());
		}
	}

	/**
	 * Method that generates a more human readable scoreboard for the GUI.
	 * 
	 * @return string representation of the scoreboard.
	 */
	private String generateScoreboard()
	{
		String s = "Human Player: " + game.getPlayerScore(HUMAN_TURN) + "\n";
		s += "AI Player: " + game.getPlayerScore(AI_TURN);
		return s;
	}

	/**
	 * Method to make the Turn Status more human readable. It converts from
	 * Player 0 and Player 1 to AI and Human
	 */
	private void setTurnStatus()
	{
		if (game.getPlayerTurn() == AI_TURN)
		{
			this.turnStatus.setText("AI's Turn: Press View AI's Roll to Continue");
		}
		else
		{
			this.turnStatus
					.setText("Your Turn, Select either Roll or Pass the pigs");
		}
	}

	/**
	 * Method to make the AI's Turn. Uses the PassPigsPlayer instance to figure
	 * out what to do for the turn. The actions of the AI are displayed in the
	 * Roll Status area for the human player to observer.
	 * 
	 * The PassThePigs Player is allowed to continue to roll as long as it is
	 * the AI's turn. When the Model of the game changes states to the Human
	 * player's turn, the AI's turn will end. The AI can end its turn early by
	 * passing the pigs the human player, just like the human player could.
	 */
	private void aiTurn()
	{
		String turnString = "Base Expected Value: " + aiPlayer.getBaseExpectedValue()
				+ "\n";
		int numMoves = 0;

		while (game.getPlayerTurn() == AI_TURN)
		{

			if (numMoves == 0)
			{
				turnString += "AI's First Roll:\n";
			}
			else
			{
				turnString += "AI's Reasoning\n" + aiPlayer.getAIReasoning() + "\n";
			}

			if (game.getPlayerTurn() != AI_TURN)
			{
				turnString += "AI's Reasoning\n" + aiPlayer.getAIReasoning();
			}

			int aiMove = aiPlayer.makeMove(game.getPlayerScore(AI_TURN),
					game.getPlayerScore(HUMAN_TURN), game.getTurnScore());

			if (aiMove == game.ROLL)
			{
				turnString += game.playerAction(aiMove) + " score for AI's turn is: "
						+ game.getTurnScore() + "\n";
			}
			else
			{
				turnString += "AI's reasoning " + aiPlayer.getAIReasoning() + "\n"
						+ game.playerAction(aiMove);
			}
			numMoves++;

		}

		this.rollStatusText.setText(turnString);
		this.aiPlayer.resetNumMoves();
	}
}
