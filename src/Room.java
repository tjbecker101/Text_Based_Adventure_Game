import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Room implements Serializable {
    private String name; //Name of the room
    private String description; //The room description
    private List<String> roomItems = new ArrayList<String>(); //Items in the room
    private List<Room> connectedRooms = new ArrayList<Room>(); //The adjacent rooms to the room.
    public static Room noRoom = new Room("", "", new String[]{""}); //A room that represents nothing, used for when no room is present in a direction.
    private Room northRoom = noRoom; //Room to the north
    private Room eastRoom = noRoom; //Room to the east
    private Room westRoom = noRoom; //Room to the west
    private Room southRoom = noRoom; //Room to the south
    private boolean monster; //The status of monster occupation in the room.
    private Monster m; //Monster in room, if applicable

    public Room(String name1, String description1, String[] roomItems1) {
        name = name1;
        description = description1;
        for (String s : roomItems1) {
            roomItems.add(s);
        }
        monster = false;
    }

    /**
     *
     * @return m - The monster in the room, if applicable
     */
    public Monster getMonster() {
        return m;
    }

    /**
     * Links a room to another room
     * @param r - the room to be linked.
     */
    public void linkRoom(Room r) {
        if (southRoom == noRoom)
            southRoom = r;
        else if (northRoom == noRoom)
            northRoom = r;
        else if (eastRoom == noRoom)
            eastRoom = r;
        else
            westRoom = r;
    }

    /**
     * Creates or destroys a monster in a room.
     * @param input - if the monster is applicable to the room
     * @param powerLevel - the power level of the potential monster.
     */
    public void setMonster(boolean input, int powerLevel) {
        monster = input;
        m = new Monster(powerLevel);
    }

    /**
     * Returns monster status.
     * @return monster - false if no monster, true if there is a monster.
     */
    public boolean isMonster() {
        return monster;
    }

    /**
     * Gets room name
     * @return name - name of the room
     */
    public String getName() {
        return name;
    }

    /**
     * Gets room description, adds if there is a monster or not.
     * @return description - the room description.
     */
    public String getDescription() {
        String tempDesc = "" + description;
        if (monster)
            tempDesc = tempDesc + m.monsterStatus();
        return tempDesc;
    }

    /**
     * Gets the items in the room
     * @return roomItems - the list of items in the room.
     */
    public List<String> getRoomItems() {
        return roomItems;
    }

    /**
     * Returns the room to the north
     * @return northRoom - the linked room to the North, returns noRoom if there is no room there.
     */
    public Room getNorthRoom() {
        return northRoom;
    }
    /**
     * Returns the linked room to the east
     * @return eastEoom - the linked room to the East, returns noRoom if there is no room there.
     */
    public Room getEastRoom() {
        return eastRoom;
    }
    /**
     * Returns the linked room to the west
     * @return northRoom - the linked room to the West, returns noRoom if there is no room there.
     */
    public Room getWestRoom() {
        return westRoom;
    }
    /**
     * Returns the linked room to the South
     * @return northRoom - the linked room to the South, returns noRoom if there is no room there.
     */
    public Room getSouthRoom() {
        return southRoom;
    }

    /**
     * Adds or removes an item to the roomItem list.
     * @param addOrRemove to determine if an item is going to be added or removed from the room.
     * @param item the item to be added or removed.
     */
    public void addRemoveItem(String addOrRemove, String item) {
        item.toUpperCase();
        if (addOrRemove.equals("add"))
            roomItems.add(item);
        else if (addOrRemove.equals("remove")) {
            int count = 0;
            for (count = 0; count < roomItems.size(); count++) {
                if (roomItems.get(count).equalsIgnoreCase(item)) break;
            }
            if (roomItems.contains(item))
                roomItems.remove(count);
        } else
            System.out.println("Wrong argument, first argument: 'add' or 'remove', second argument: itemName");
    }


}
