
package arad.amir.ac.snlje.xml.model;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="players" type="{}players"/>
 *         &lt;element name="board" type="{}board"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="number_of_soldiers" use="required" type="{}number_of_soldiers" />
 *       &lt;attribute name="current_player" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "players",
    "board"
})
@XmlRootElement(name = "snakesandladders")
public class Snakesandladders {

    @XmlElement(required = true)
    protected Players players;
    @XmlElement(required = true)
    protected Board board;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "number_of_soldiers", required = true)
    protected int numberOfSoldiers;
    @XmlAttribute(name = "current_player", required = true)
    protected String currentPlayer;

    /**
     * Gets the value of the players property.
     * 
     * @return
     *     possible object is
     *     {@link Players }
     *     
     */
    public Players getPlayers() {
        return players;
    }

    /**
     * Sets the value of the players property.
     * 
     * @param value
     *     allowed object is
     *     {@link Players }
     *     
     */
    public void setPlayers(Players value) {
        this.players = value;
    }

    /**
     * Gets the value of the board property.
     * 
     * @return
     *     possible object is
     *     {@link Board }
     *     
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the value of the board property.
     * 
     * @param value
     *     allowed object is
     *     {@link Board }
     *     
     */
    public void setBoard(Board value) {
        this.board = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the numberOfSoldiers property.
     * 
     */
    public int getNumberOfSoldiers() {
        return numberOfSoldiers;
    }

    /**
     * Sets the value of the numberOfSoldiers property.
     * 
     */
    public void setNumberOfSoldiers(int value) {
        this.numberOfSoldiers = value;
    }

    /**
     * Gets the value of the currentPlayer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the value of the currentPlayer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentPlayer(String value) {
        this.currentPlayer = value;
    }

}
