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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
	private Label rollStatusText;

	/** Shows whose turn it is */
	private Label turnStatus;

	/** HBox for Buttons */
	private HBox buttons;

	/** Roll Pigs Button */
	private Button rollPigsButton;

	/** Pass Pigs Button */
	private Button passPigsButton;

	/** stores the current player on display in the GUI */
	private int currentPlayer;

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
		this.rollStatusText = new Label();
		this.turnStatus = new Label();
		this.statusVBox.getChildren().add(turnStatus);
		this.turnStatus.setText("Player " + game.getPlayerTurn() + "\'s Turn");
		this.currentPlayer = game.getPlayerTurn();
		this.statusVBox.getChildren().add(new Separator());
		this.statusVBox.getChildren().add(rollStatusText);
		this.statusVBox.getChildren().add(new Separator());
		this.statusVBox.getChildren().add(this.scoreboardText);

		this.buttons = new HBox();
		this.rollPigsButton = new Button("Roll the Pigs");
		this.rollPigsButton.setOnAction(this);
		this.passPigsButton = new Button("Pass the Pigs");
		this.passPigsButton.setOnAction(this);
		this.buttons.getChildren().add(rollPigsButton);
		this.buttons.getChildren().add(passPigsButton);

		this.setTop(menuBar);
		this.setBottom(buttons);
		this.setCenter(statusVBox);

		this.scoreboardText.setText(game.scoreboardToString());

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
			// print out information about the game on another window
		}

		if (event.getSource().equals(rulesOption))
		{
			// display rules in another window
		}

		if (event.getSource().equals(rollStatsOption))
		{
			// display roll stats in another window
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
				this.rollStatusText.setText(
						this.rollStatusText.getText() + "\n" + game.playerAction(1)
								+ "score for your turn is: " + game.getTurnScore());

				if (game.getPlayerTurn() != currentPlayer)
				{
					this.turnStatus
							.setText("Player " + game.getPlayerTurn() + "\'s Turn");
					currentPlayer = game.getPlayerTurn();
				}
			}
		}

		// User has selected the pass pigs button
		if (event.getSource().equals(passPigsButton)
				&& game.getPlayerTurn() == HUMAN_TURN)
		{
			game.playerAction(2);
		}
		// {
		// if (game.isGameOver())
		// {
		// // redisplays the winner alert until the user starts a new game
		// displayWinner();
		// }
		// else if (game.getPlayerTurn() == AI_TURN)
		// {
		// while (game.getPlayerTurn() == AI_TURN)
		// {
		// // AI's turn
		// System.out.println("AI's Turn");
		// game.playerAction(aiPlayer.makeMove(game.getPlayerScore(0),
		// game.getPlayerScore(1)));
		//
		// this.rollStatusText.setText(game.playerAction(1)
		// + "score for your turn is: " + game.getTurnScore());
		// System.out.println(rollStatusText.getText());
		//
		// }
		// this.turnStatus
		// .setText("Player " + game.getPlayerTurn() + "\'s Turn");
		// currentPlayer = game.getPlayerTurn();
		// }
		// else
		// {
		// this.rollStatusText.setText(game.playerAction(2));
		//
		// if (game.isGameOver())
		// {
		// displayWinner();
		// }
		//
		// this.turnStatus
		// .setText("Player " + game.getPlayerTurn() + "\'s Turn");
		// this.scoreboardText.setText(game.scoreboardToString());
		// }
		//
		// }
	}

	private void displayWinner()
	{
		Alert winner = new Alert(AlertType.INFORMATION);
		winner.setTitle("Congratulations Player " + game.getWinner());
		winner.setHeaderText("You won the game");
		winner.setContentText("Thank you for playing");
		winner.showAndWait();
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		if (game.isGameOver())
		{
			displayWinner();
		}

		else if (game.getPlayerTurn() == AI_TURN)
		{

			this.rollStatusText.setText(game.playerAction(aiPlayer.makeMove(
					game.getPlayerScore(AI_TURN), game.getPlayerScore(HUMAN_TURN))));
			displayAITurnOverMessage();
		}

		this.scoreboardText.setText(game.scoreboardToString());
	}

	private void displayAITurnOverMessage()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AI's Turn is Over");
		alert.setHeaderText("Press Ok to begin your turn, or view the AI's Actions");
		alert.showAndWait();
	}

}
