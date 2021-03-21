import java.io.Serializable;

public class Monster implements Serializable {
    private int powerLevel; //The power level of the monster, 0 if it is stationary, greater than 0 if it roams.
    private boolean alive; //The mortality status of the monster.

    public Monster(int power) {
        powerLevel = power;
        alive = true;
    }

    /**
     * Returns the monster's mortality status
     * @return alive- True if monster is alive, false if otherwise
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * The attack command, returns the status of the monster after the player strikes.
     * @param player the player attacking.
     * @param hasTracker if the player has a TRACKER or not.
     * @return the outcome of the player's attack.
     */
    public String attack(Player player, boolean hasTracker) {
        if (powerLevel > 0 ) {
            alive = false;
            return "The light made the CS Major stop dead in his tracks, and he falls over and dies.";
        } else if (powerLevel == 0) {
            player.addItem("ROCK");
            alive = false;
            return "The monster perishes! He drops a rock with a note on the ground. You pick it up.";
        } else {
            return "You failed to subjugate the monster, and died.";
        }
    }

    /**
     * Returns the status of the monster in the room.
     * @return status - A string that represents the status of the monster.
     */
    public String monsterStatus() {
        String status = "";
        if (isAlive()) {
            if (powerLevel > 1) {
                status = "There is a super huge CS Major in this room! Obliterate it!";
            } else
                status = "There is a CS Major with his vertebrae poking out of his back... he appears to be holding a rock with a note on it.";
        } else {
            status = "There is a CS Major corpse in this room. It smells pretty bad.";
        }
        return status;
    }
}
