package arad.amir.ac.snlje.windowed.controllers;

import arad.amir.ac.snlje.game.bl.GameSession;
import arad.amir.ac.snlje.game.model.Player;
import arad.amir.ac.snlje.windowed.PlayerColor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author amira
 * @since 16/08/2014
 */
public class NewPlayerController extends AbsController{
    private static final Logger log = LoggerFactory.getLogger(NewPlayerController.class);

    static enum Type{DISABLE(null), HUMAN(Player.Type.HUMAN), COMPUTER(Player.Type.COMPUTER);
        private final Player.Type blType;
        Type(Player.Type blType) {
            this.blType = blType;
        }
    }

    private NewGameController newGameController;
    private PlayerColor color;
    private Player player;

    @FXML
    private Label title;

    @FXML
    private ImageView image;

    @FXML
    private ToggleGroup playerType;

    @FXML
    private TextField name;

    @FXML
    private Pane container;

    public NewPlayerController(GameSession<ControllersManager> session) {
        super(session);
    }

    public void init(NewGameController newGameController, PlayerColor color) {
        container.setVisible(false);
        this.newGameController = newGameController;
        this.color = color;
        title.setText(color.getTitle());
        image.setImage(color.getImage());
        player = new Player(name.getText(), getType((RadioButton) playerType.getSelectedToggle()).blType);
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                setName(newValue);
            }
        });
    }


    public void setEnabled(boolean enabled) {
        container.setVisible(enabled);
        reportActivePlayer();
    }

    public void onTypeSelect(ActionEvent actionEvent) {
        player.setType(getType((RadioButton) actionEvent.getSource()).blType);
        reportActivePlayer();
    }

    private void reportActivePlayer() {
        newGameController.handlePlayerActive(color, container.isVisible() && player.getType() != null);
    }

    private void setName(String name) {
        player.setName(name);
    }

    public Player getPlayer() {
        if (container.isVisible() && player.getName() != null && !player.getName().trim().isEmpty() && player.getType() != null) {
            return player;
        } else {
            return null;
        }
    }

    public Type getType(){
        return getType((RadioButton) playerType.getSelectedToggle());
    }

    private Type getType(RadioButton selected) {
        return Type.valueOf(selected.getId().toUpperCase());
    }

    public String getName(){
        return name.getText();
    }

}
