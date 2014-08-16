package arad.amir.ac.snlje.windowed;

import javafx.scene.image.Image;

/**
 * @author amira
 * @since 16/08/2014
 */
public enum PlayerColor {
    BLUE,
    PURPLE,
    GREEN,
    RED;

    public Image getImage(){
        String imageFile = name().toLowerCase() + ".png";
        return new Image(getClass().getClassLoader().getResourceAsStream(imageFile));
    }

    public PlayerColor next(){
        return ordinal() + 1 == values().length ? null : values()[ordinal() + 1];
    }

    public String getTitle() {
        return name().toLowerCase() + " player";
    }
}
