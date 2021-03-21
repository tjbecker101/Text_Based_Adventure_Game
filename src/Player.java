import com.sun.jndi.ldap.Connection;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class creates the player and holds specific information that relates to the player
 */
public class Player implements Serializable {
    public ArrayList<String> heldItems;
    public Room currentRoom = null; //The current room the player occupies.
    private long connectionId; //The connection ID for the player
    private boolean alive; //True if player is alive, false if not
    //Instantiates the connection ID of the player.
    public void setConnectionId(long connectionId) {
        this.connectionId = connectionId;
    }

    public Player() {
        heldItems = new ArrayList<String>();
        currentRoom = TechAdventure.roomList.get(0);
        alive = true;
    }
    /** Sets the player's alive status.
     *  @param input - what alive will be set to
     */
    public void setAlive(boolean input) {
        alive = input;
    }

    /**
     * Returns the player's alive status.
     * @return alive - The player's mortality.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Returns the connection ID of the player.
     * @return connectionId - the Connection ID of the player.
     */
    public long playerId() {
        return connectionId;
    }

    public void setRoom(Room room) {
        currentRoom = room;
    }

    /**
     * Returns the current room of the player.
     * @return currentRoom -  the current room the player is in.
     */
    public Room getRoom() {
        return currentRoom;
    }

    /**
     * Adds an item to player's inventory
     * @param item - the item to be added
     */
    public void addItem(String item) {
        heldItems.add(item);
    }
    /**
     * Drops an item from a player's inventory
     * @param item - the item to be dropped
     */
    public String dropItem(String item) {
        if (!heldItems.contains(item))
            return item + " is not in your inventory!";
        else {
            heldItems.remove(item);
            return item + " was dropped on the floor";
        }
    }

    /**
     * Returns an ArrayList of player items.
     * @return heldItems - The items the player is carrying
     */
    public ArrayList<String> getTtems() {
        return heldItems;
    }

}