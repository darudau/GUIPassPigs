package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PigsGameGUI extends BorderPane implements EventHandler<ActionEvent>
{
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

	/** HBox for Buttons */
	private HBox buttons;

	/** Roll Pigs Button */
	private Button rollPigsButton;

	/** Pass Pigs Button */
	private Button passPigsButton;

	public PigsGameGUI()
	{
		game = new PassThePigsGame(2);

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

	}

	@Override
	public void handle(ActionEvent event)
	{
		if (event.getSource().equals(newGameOption))
		{
			game.newGame();
		}

		if (event.getSource().equals(quitOption))
		{
			System.exit(0);
		}

		if (event.getSource().equals(aboutGameOption))
		{
			// print out information about the game on anohter window
		}

		if (event.getSource().equals(rulesOption))
		{
			// display rules in another window
		}

		if (event.getSource().equals(rollStatsOption))
		{
			// display roll stats in another window
		}

		if (event.getSource().equals(rollPigsButton))
		{
			// roll the pigs
			this.rollStatusText.setText(game.playerAction(1));
			this.scoreboardText.setText(game.scoreboardToString());
		}

		if (event.getSource().equals(passPigsButton))
		{
			this.rollStatusText.setText(game.playerAction(2));
			// pass the pigs
		}
	}

}
