import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class TechAdventure implements ConnectionListener, Serializable {

    Player player1; //Creates player 1

    AdventureServer adventureServer = null; //Creates the server

    //Creates all the rooms and the list that holds them
    public static LinkedList<Room> roomList = null;
    Room dormRoom;
    Room hallway1;
    Room buddyRoom;
    Room elevator;
    Room hallway2;
    Room stairs;
    Room welcomeArea;
    Room bathroom;
    Room diningHall;

    int count; //Creates a count that moves a monster

    ArrayList<Player> playerList = new ArrayList<>(); //Holds all the players connected


    public TechAdventure() {
        adventureServer = new AdventureServer(); //creates a new server
        adventureServer.setOnTransmission(this);
        roomList = new LinkedList<>();

        count = 0; //sets the count at 0

        //Instantiates all the rooms and links them to each other
        hallway1 = new Room("Hallway 1", "The light is flickering, and a fog seems to permeate the air. You find it " +
                "difficult to breath. You feel like something bad is about to happen. Maybe you should pop " +
                "by your buddy’s room...", new String[]{""});
        dormRoom = new Room("Dormroom", "There are 4 pairs of pants on the ground, papers on a desk, posters on the " +
                "wall, and your favorite sweatshirt on the hook. You can't seem to remember where your ID was... " +
                "what would be the most logical place to put it?",
                new String[]{"SWEATSHIRT", "PANTS", "POSTERS", "DESK"});
        buddyRoom = new Room("Buddy's Room", "Your friend, Frank, is an avid clock collector. His half of the dorm room is entirely " +
                "plastered with clocks from all eras, all ticking exactly in sync, which kind of freaks you " +
                "out. He tells you that the Computer Science majors have gone feral, and getting off at any " +
                "floor other than Floor 2 is a death wish. He hands you a piece of paper with the number TWO on it.",
                new String[]{});
        elevator = new Room("Elevator", "The elevator is as musty as ever, and no-one is in there. The walls have tomato sauce and " +
                "mustard stains on them. There is a panel of buttons on the left side of the door. Someone " +
                "appears to have left a flashlight with a note on it.", new String[]{"FLOOR1_BUTTON", "FLOOR2_BUTTON", "FLOOR3_BUTTON",
                "Flashlight"});
        hallway2 = new Room("Hallway 2", "The lights are on in this area, but periodically flicker off every 6 seconds or so. You really " +
                "hate being in this room. It reminds you of a horror movie kind of hallway. ", new String[]{""});
        hallway2.setMonster(true, 0);
        stairs = new Room("Stairs", "You arrive at the stairs, only to find a CS Major with the body of a lion. It calls itself the " +
                "Sphinx, and says you do not need to answer their riddle in order to pass. However, should " +
                "you choose to engage in a battle of wits, a victory means you obtain an item of high " +
                "value. However, a loss means certain death. In order to play, you must drop a rock onto " +
                "the ground to initiate the battle of wits.", new String[]{""});
        welcomeArea = new Room("Welcome Area", "You arrive at the welcome area. Everyone there seems to be acting perfectly normal, as if " +
                "they are not seeing the feral CS Major infestation that seems to have overrun the building. " +
                "There are windows that display the raging storm outside, and the smell of fresh lunch " +
                "wafts in from the dining hall.", new String[]{});
        welcomeArea.setMonster(true, 3);
        bathroom = new Room("Bathroom", "You walk into the bathroom, because you realize the importance of personal hygiene, " +
                "especially before a meal. The floor is caked with black goo in between the white tiles." +
                "The fans are running, and an extremely high pitched ringing coming from somewhere in " +
                "the room.", new String[]{"HAND_SANITIZER"});
        diningHall = new Room("Dining Hall", "Home at last! The hall seems to be packed with eager lunch-goers, who do not seem to be " +
                "traumatized at all by the apparent CS Major uprising. The food being served today " +
                "appears to be Marbled Rye Ruebens and Berry Pie. You can hardly contain your " +
                "anticipation for the meal.", new String[]{"LUNCH"});
        dormRoom.linkRoom(hallway1);
        hallway1.linkRoom(dormRoom);
        hallway1.linkRoom(buddyRoom);
        hallway1.linkRoom(elevator);
        buddyRoom.linkRoom(hallway1);
        hallway2.linkRoom(elevator);
        hallway2.linkRoom(stairs);
        stairs.linkRoom(welcomeArea);
        stairs.linkRoom(hallway2);
        welcomeArea.linkRoom(bathroom);
        welcomeArea.linkRoom(diningHall);
        bathroom.linkRoom(welcomeArea);
        diningHall.linkRoom(welcomeArea);
        roomList.add(dormRoom);
        roomList.add(hallway1);
        roomList.add(buddyRoom);
        roomList.add(elevator);
        roomList.add(hallway2);
        roomList.add(stairs);
        roomList.add(welcomeArea);
        roomList.add(bathroom);
        roomList.add(diningHall);
        //Instantiates the player and adds them to the playerlist
        player1 = new Player();
        playerList.add(player1);
    }

    /**
     * Moves the player to a new room if there is a room there
     *
     * @param direction - the direction of the room they want to go to
     * @param player    - The player that wants to move
     * @throws UnknownConnectionException - thrown when there are no players with the playerID connected
     */
    public void goRoom(String direction, Player player) throws UnknownConnectionException {
        Room testRoom = Room.noRoom; // A room that is null
        if (direction == null) { //Ensures they give a direction when using the go command
            adventureServer.sendMessage(player.playerId(), "You must specify a direction!");
            return;
        } else if (player.getRoom().isMonster() && player.getRoom().getMonster().isAlive()) { //kills the player if they try to flee the room with a monster
            adventureServer.sendMessage(player.playerId(), "You tried to flee the room with the monster, but you are killed before you leave.");
            gameOver();
        } else if (direction.equalsIgnoreCase("south")) { //moves the player south if it was an option
            testRoom = player.getRoom().getSouthRoom();
        } else if (direction.equalsIgnoreCase("north")) //moves the player north if it was an option
            testRoom = player.getRoom().getNorthRoom();
        else if (direction.equalsIgnoreCase("east")) //Moves the player east if it was an option
            testRoom = player.getRoom().getEastRoom();
        else if (direction.equalsIgnoreCase("west")) //Moves the player west if it was an option
            testRoom = player.getRoom().getWestRoom();
        if (testRoom == Room.noRoom && player.isAlive()) //Tells them they tried to move where there was no room
            adventureServer.sendMessage(player.playerId(), "No room there!");
        else
            player.setRoom(testRoom);
        if (player.getRoom() == diningHall && !player.getTtems().contains("STUDENT_ID")) {
            adventureServer.sendMessage(player.playerId(), "You attempted to get into the dining hall without your student ID. You perish.");
            gameOver();
        }
    }

    /**
     * Stops the server if the player died
     */
    public void gameOver() throws UnknownConnectionException {
        player1.setAlive(false);
        adventureServer.sendMessage(player1.playerId(), "Please Reconnect and try again!");
        try {
            adventureServer.disconnect(player1.playerId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gives the description of an item
     *
     * @param item   - the item the player wants to look at
     * @param player - The player that wants to look at the item
     * @throws UnknownConnectionException - thrown when the player with the playerID doesn't exist
     */
    public void lookItem(String item, Player player) throws UnknownConnectionException {
        item.toUpperCase();
        if (!(player.getTtems().contains(item))) //Tells the player they dont have that item
            adventureServer.sendMessage(player.playerId(), "You either do not have this item, or it does not exist.");
        if (item.equalsIgnoreCase("pants")) //Gives the description of item "pants"
            adventureServer.sendMessage(player.playerId(), "These stink.");
        else if (item.equalsIgnoreCase("posters")) //Gives the description of item "posters"
            adventureServer.sendMessage(player.playerId(), "Blade Runner, Twin Peaks, etc. Pretty good taste in posters.");
        else if (item.equalsIgnoreCase("desk")) //Gives the description of item "desk"
            adventureServer.sendMessage(player.playerId(), "How are you carrying this?");
        else if (item.equalsIgnoreCase("student_id")) //Gives the description of item "student_id"
            adventureServer.sendMessage(player.playerId(), "Need this to get into the Dining hall.");
        else if (item.equalsIgnoreCase("flashlight")) //Gives the description of item "flashlight"
            adventureServer.sendMessage(player.playerId(), "An ordinary flashlight. The note on it reads \"Press this to scare away CS Majors.\" ");
        else if (item.equalsIgnoreCase("rock")) //Gives the description of item "rock"
            adventureServer.sendMessage(player.playerId(), "A rock with a note on it that reads: 'MOUNTAIN'");
        else if (item.equalsIgnoreCase("TRACKER")) //Gives the description of item "tracker"
            adventureServer.sendMessage(player.playerId(), "A more powerful flashlight TRACKER. Might need this for some of the bigger CS Majors.");
        else if (item.equalsIgnoreCase("hand_sanitizer")) //Gives the description of item "hand_sanitizer"
            adventureServer.sendMessage(player.playerId(), "Gotta PRESS this to stay fresh and clean for lunch.");
        else if (item.equalsIgnoreCase("cleanliness")) //Gives the description of item "cleanliness"
            adventureServer.sendMessage(player.playerId(), "So fresh and so clean.");

    }

    /**
     * Will pick up the item from the room if in the room
     *
     * @param item   - the item they want to pick up
     * @param player - the player that wants to pick the item up
     * @throws UnknownConnectionException - thrown when the playerID doesn't have a player connected with it
     */
    public void getItem(String item, Player player) throws UnknownConnectionException {
        boolean exists = false;
        for (String item1 : player.getRoom().getRoomItems()) { //checks if the item is in the room
            if (item1.equalsIgnoreCase(item))
                exists = true;
        }
        if (!exists) //tells the user the item is not in the room
            adventureServer.sendMessage(player.playerId(), "Item is not in room!");
        else if (item.contains("BUTTON")) { //tells the user they died for taking the elevator buttons
            adventureServer.sendMessage(player.playerId(), "You rip the buttons out of the elevator, causing a short circuit, and you plummet to your death.");
            gameOver();
        } else if (item.equalsIgnoreCase("Sweatshirt")) { //tells the user the sweatshirt had the id
            adventureServer.sendMessage(player.playerId(), "The sweatshirt had your STUDENT_ID in it!");
            player.addItem("STUDENT_ID");
        } else { //adds the item to the inventory if it was in the room
            player.addItem(item);
            player.getRoom().addRemoveItem("remove", item);
            adventureServer.sendMessage(player.playerId(), item + " was added to your inventory.");
        }

    }

    /**
     * This will let the user answer the question from the sphinx
     *
     * @param player - the player that wants to answer the question
     * @param answer - the answer the player gave to the sphinx
     * @throws UnknownConnectionException - thrown if the playerID doesn't exist
     */
    public void answer(Player player, String answer) throws UnknownConnectionException {
        if (player.getRoom() != stairs) //Makes it so the answer command only works in the stairs with the spinx
            adventureServer.sendMessage(player.playerId(), "Your answer is lost in the empty room.");
        else if (answer.equalsIgnoreCase("mountain")) { //tells the player they answered correctly and gives an item
            adventureServer.sendMessage(player.playerId(), "The Sphinx congratulates your wit, and gives you a flashlight TRACKER.");
            player.addItem("TRACKER");
        } else { //tells the user you answered wrong and kills you
            adventureServer.sendMessage(player.playerId(), "The Sphinx sighs, and obliterates you.");
            gameOver();
        }
    }

    /**
     * Lets the user press buttons or interact with certain items
     *
     * @param player - the player that is trying to use PRESS
     * @param item   - the item the player wants to press
     * @throws UnknownConnectionException - thrown when no player has that connection ID
     */
    public void pressItem(Player player, String item) throws UnknownConnectionException {
        if (player.getRoom() == elevator && item.equalsIgnoreCase("Floor2_button")) { //moves the player to hallway 2
            player.setRoom(hallway2);
            adventureServer.sendMessage(player1.playerId(), player1.getRoom().getName() + player1.getRoom().getDescription() + player1.getRoom().getRoomItems().toString() + " South:" + player1.getRoom().getSouthRoom().getName()
                    + "     North: " + player1.getRoom().getNorthRoom().getName() + "     East: " + player1.getRoom().getEastRoom().getName() + "     West:" + player1.getRoom().getWestRoom().getName());
        } else if (player.getRoom() == elevator && item.contains("BUTTON")) { //kills the player if they choose the wrong floor
            adventureServer.sendMessage(player.playerId(), "You travel to that floor, only to be greeted by a massive horde of CS Majors, tearing you to shreds.");
            gameOver();
        } else if (item.equalsIgnoreCase("TRACKER") && player.getTtems().contains("TRACKER")) { //Tells the player if the monster is dead or alive and which room its in
            String monsterRooms = "";
            for (Room room : roomList) {
                if (room.isMonster()) {
                    monsterRooms = monsterRooms + room.getName() + ": MONSTER. MONSTER IS " + (room.getMonster().isAlive() ? "Alive     " : "Dead     ");
                }
            }
            adventureServer.sendMessage(player1.playerId(), monsterRooms);
        } else if (item.equalsIgnoreCase("HAND_SANITIZER") && player.getTtems().contains("HAND_SANITIZER")) { //tells the user that they can now eat their lunch safely
            adventureServer.sendMessage(player.playerId(), "Your hands are mighty clean now, ready to eat lunch.");
            player.addItem("CLEANLINESS");
        } else if (item.equalsIgnoreCase("FLASHLIGHT") && player.getTtems().contains("FLASHLIGHT") && player.getRoom().isMonster() && player.getRoom().getMonster().isAlive()) { // lets the user kill the second monster
            adventureServer.sendMessage(player.playerId(), player.getRoom().getMonster().attack(player, true));
        } else { //tells the user that the thing they tried to press can't be pressed or it doesn't exist
            adventureServer.sendMessage(player.playerId(), "You either pressed a nonexistent object, or the object does nothing when pressed.");
        }
    }

    /**
     * Lets the user drop any items they have in their inventory
     *
     * @param player - the player trying to drop the item
     * @param item   - the item trying to be dropped
     * @throws UnknownConnectionException - thrown if the playerID isn't valid
     */
    public void dropItem(Player player, String item) throws UnknownConnectionException {
        if (!(player.getTtems().contains(item))) //Tells the user they dont have an item with tht name
            adventureServer.sendMessage(player.playerId(), "Item is not in inventory!");
        else if (player.getRoom() == stairs && item.equalsIgnoreCase("rock")) { // lets the user talk to the sphinx and answer the riddle
            adventureServer.sendMessage(player.playerId(), "You drop the rock, and the Sphinx eats it, then asks its riddle." +
                    "What has roots as nobody sees," +
                    "Is taller than trees," +
                    "Up, up it goes," +
                    "And yet never grows?");
            adventureServer.sendMessage(player.playerId(), "Type in ANSWER, then a space, then your answer to the riddle.");
        } else { //drops the item the user had with no extra effects
            adventureServer.sendMessage(player1.playerId(), player.dropItem(item));
            player.getRoom().addRemoveItem("add", item);
        }
    }

    public void win(String item) throws UnknownConnectionException {
        if (item.equalsIgnoreCase("LUNCH")) {
            if (player1.heldItems.contains("CLEANLINESS")) {
                adventureServer.sendMessage(player1.playerId(), "YOU WIN!!!!!");
                try {
                    adventureServer.disconnect(player1.playerId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                adventureServer.sendMessage(player1.playerId(), "You ate lunch without cleaning your hands! You get rapid-onset salmonella and die.");
                gameOver();
            }
        }
        else {
            adventureServer.sendMessage(player1.playerId(), "You cannot eat that item");
        }
    }
    /**
     * starts the server with the desired port
     *
     * @param port - the port we want the server on
     */
    public void start(int port) {
        adventureServer.startServer(port);
    }

    /**
     * This method will take care of any inputs to the server and calls the methods needed to handle the inputs or will manage the server when needed
     *
     * @param e - the event that lets us communicate and get info from the server
     */
    @Override
    public void handle(ConnectionEvent e) {
        try {
            switch (e.getCode()) {
                case CONNECTION_ESTABLISHED: //Happens whenever a player first joins the server
                    player1.setConnectionId(e.getConnectionID()); // Stores the players ID
                    //Gives the story of the game to the player
                    adventureServer.sendMessage(player1.playerId(), "You are a freshman student at Michigan Technological University. " +
                            "The University has placed you into the Wadsworth dormitory halls. One day, you decide to get lunch at the Wadsworth dining " +
                            "hall. However, there are vicious Computer Science majors that are out for your blood. You must navigate the treacherous halls " +
                            "of Wadsworth in order to get your lunch. If you win, you will get a Rueben Sandwich on marbled rye, and a slice of berry " +
                            "pie to go along. If you lose, however, you will perish. ");
                    adventureServer.sendMessage(player1.playerId(), player1.getRoom().getName());
                    adventureServer.sendMessage(player1.playerId(), player1.getRoom().getDescription());
                    adventureServer.sendMessage(player1.playerId(), player1.getRoom().getRoomItems().toString());
                    adventureServer.sendMessage(player1.playerId(), "South:" + player1.getRoom().getSouthRoom().getName()
                            + "     North: " + player1.getRoom().getNorthRoom().getName() + "     East: " + player1.getRoom().getEastRoom().getName() + "     West:" + player1.getRoom().getWestRoom().getName());

                    break;
                case TRANSMISSION_RECEIVED: //happens whenever text is put into the server
                    adventureServer.sendMessage(player1.playerId(), ""); //Used to breakup text in the console
                    String input = e.getData(); //Gets the input from the server
                    String[] inputArray = input.split(" "); //splits the input based on spaces for separating commands
                    String command = inputArray[0];
                    String command2 = "";
                    if (inputArray.length > 1) { //sets command2 to the second part of the commands if it was given
                        command2 = inputArray[1];
                    }
                    switch (command) {
                        case "GO": //calls the goRoom method if the player inputted GO
                            goRoom(command2, player1);
                            if (player1.isAlive()) {
                                adventureServer.sendMessage(player1.playerId(), player1.getRoom().getName() + " " + player1.getRoom().getDescription() + " " + player1.getRoom().getRoomItems().toString() + " South: " + player1.getRoom().getSouthRoom().getName()
                                        + "     North: " + player1.getRoom().getNorthRoom().getName() + "     East: " + player1.getRoom().getEastRoom().getName() + "     West: " + player1.getRoom().getWestRoom().getName());
                            }
                            break;
                        case "GET": //Calls the getItem method if the player inputted GET
                            getItem(command2, player1);
                            break;
                        case "ANSWER": //Calls the answer method if the player inputted ANSWER
                            answer(player1, command2);
                            break;
                        case "DROP": //calls the dropItem method if the player inputted DROP
                            dropItem(player1, command2);
                            break;
                        case "LOOK":
                            //Prints the current room and its properties when the player input LOOK
                            if (command2 == "")
                                adventureServer.sendMessage(player1.playerId(), player1.getRoom().getName() + " " + player1.getRoom().getDescription() + " " + player1.getRoom().getRoomItems().toString() + " South: " + player1.getRoom().getSouthRoom().getName()
                                        + "     North: " + player1.getRoom().getNorthRoom().getName() + "     East: " + player1.getRoom().getEastRoom().getName() + "     West: " + player1.getRoom().getWestRoom().getName());
                            else
                                lookItem(command2, player1);
                            break;
                        case "INVENTORY": //Prints the players held items if they input INVENTORY
                            adventureServer.sendMessage(player1.playerId(), player1.getTtems().toString());
                            break;
                        case "PRESS": //calls the pressItem method if PRESS is inputted
                            pressItem(player1, command2);
                            break;
                        case "EAT":
                            win(command2);
                            break;
                        case "SAVE": //Saves the current player specific properties and room specific properties if SAVE is inputted
                            try {
                                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save.data"));
                                out.writeObject(roomList);
                                out.writeObject(player1.heldItems);
                                out.writeObject(player1.currentRoom);
                                out.writeObject(playerList);
                                adventureServer.sendMessage(e.getConnectionID(), "Saved");
                                out.flush();
                                out.close();
                            } catch (IOException ex) {
                                try {
                                    adventureServer.sendMessage(e.getConnectionID(), "Can't Save");
                                } catch (UnknownConnectionException exc) {
                                    exc.printStackTrace();
                                }
                            }
                            break;
                        case "RESTORE": //Loads the file holding the items and rooms specific to an instance of the game
                            try {
                                ObjectInputStream in = new ObjectInputStream(new FileInputStream("save.data"));
                                roomList = (LinkedList<Room>) in.readObject();
                                player1.heldItems = (ArrayList<String>) in.readObject();
                                player1.currentRoom = (Room) in.readObject();
                                playerList = (ArrayList<Player>) in.readObject();
                                adventureServer.sendMessage(player1.playerId(), "Data Restored");
                                adventureServer.sendMessage(player1.playerId(), "");
                                adventureServer.sendMessage(player1.playerId(), "Held Items: " + player1.heldItems + ", Current Location: ");
                                adventureServer.sendMessage(player1.playerId(), player1.getRoom().getName() + " " + player1.getRoom().getDescription() + " " + player1.getRoom().getRoomItems().toString() + " South: " + player1.getRoom().getSouthRoom().getName()
                                        + "     North: " + player1.getRoom().getNorthRoom().getName() + "     East: " + player1.getRoom().getEastRoom().getName() + "     West: " + player1.getRoom().getWestRoom().getName());
                                in.close();
                            } catch (Exception ez) {
                                try {
                                    adventureServer.sendMessage(player1.playerId(), "Cant Restore");
                                    ez.printStackTrace();
                                } catch (UnknownConnectionException exc) {
                                    exc.printStackTrace();
                                }
                            }
                            break;
                        case "QUIT": //Disconnects the player from the sever
                            try {
                                adventureServer.disconnect(player1.playerId());
                            } catch (IOException ex) {
                                adventureServer.sendMessage(player1.playerId(), "IOException");
                            }
                            break;
                        default: //sends the user invalid input if none of the other cases are inputted
                            adventureServer.sendMessage(player1.playerId(), "Invalid Input");
                            break;
                    }
                    count++; //increases count for when the monster needs to move
                    if (count == 3 && welcomeArea.isMonster() && welcomeArea.getMonster().isAlive()) { //moves the monster to a new room
                        welcomeArea.setMonster(false, 3);
                        bathroom.setMonster(true, 3);
                        count = 0;
                        if (player1.getTtems().contains("TRACKER"))
                            adventureServer.sendMessage(player1.playerId(), "The roaming CS major moved from the Welcome Area to the Bathroom.");
                    } else if (count == 3 && bathroom.isMonster() && bathroom.getMonster().isAlive()) { //Moves the monster to a new room
                        bathroom.setMonster(false, 3);
                        welcomeArea.setMonster(true, 3);
                        count = 0;
                        if (player1.getTtems().contains("TRACKER"))
                            adventureServer.sendMessage(player1.playerId(), "The roaming CS major moved from the Bathroom to the Welcome Area.");
                    }
                    break;
                case CONNECTION_TERMINATED: //occurs whenever a player leaves the server
                    TechAdventure restart = new TechAdventure();
                    adventureServer.stopServer();
                    restart.start(2112);
                    break;
                default: //Prints to the server that something was not accounted for in the cases
                    System.out.println("Did not fall into the cases");
            }
        } catch (UnknownConnectionException unknownConnectionException) {
            unknownConnectionException.printStackTrace();
        }
    }

    /**
     * This method gets the port from the command line arguments if needed and calls the server class and starts it
     *
     * @param args - param for main methods
     */
    public static void main(String[] args) {
        int port; //the port that we want the server on
        //Gets the desired port from the command line arguments for sets it to 2112 as default
        try {
            port = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            port = 2112;
        }
        //Starts the server with the correct port
        TechAdventure adventureServer = new TechAdventure();
        adventureServer.start(port);
    }
}


//EDGE CASES:

//All commands work when dead except the go command - minor
