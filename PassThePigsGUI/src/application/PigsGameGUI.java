package application;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PigsGameGUI extends BorderPane
{
	/** HBox that holds menu */
	private HBox topMenu;

	/** Menu Bar for the game */
	private MenuBar menuBar;

	/** Game Menu */
	private Menu gameMenu;

	/** About Menu */
	private Menu aboutMenu;

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
		this.topMenu = new HBox();
		this.menuBar = new MenuBar();
		this.gameMenu = new Menu("Game");
		this.aboutMenu = new Menu("About");
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
		this.passPigsButton = new Button("Pass the Pigs");
		this.buttons.getChildren().add(rollPigsButton);
		this.buttons.getChildren().add(passPigsButton);

		this.setTop(topMenu);
		this.setBottom(buttons);
		this.setCenter(statusVBox);

	}

}
