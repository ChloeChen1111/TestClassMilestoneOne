public class ShipMutant
{
    private int row;
    private int col;
    private int length;
    private int direction;

    public static final int UNSET = -1;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    // Constructor
    public ShipMutant(int length) {
        this.length = length;
        this.row = -1;
        this.col = -1;
        this.direction = UNSET;
    }

    // Introduced Mutant: Returning row + 1 instead of row
    public int getRow() {
        return row + 1; // Mutant
    }

    // Introduced Mutant: Returning col - 1 instead of col
    public int getCol() {
        return col - 1; // Mutant
    }

    // Introduced Mutant: Returning length * 2 instead of length
    public int getLength() {
        return length * 2; // Mutant
    }

    // Introduced Mutant: Returning direction + 1 instead of direction
    public int getDirection() {
        return direction + 1; // Mutant
    }

    // Set location of the ship
    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }


    // Set the direction of the ship
    public void setDirection(int direction) {
        if (direction != UNSET && direction != HORIZONTAL && direction != VERTICAL) {
            throw new IllegalArgumentException("Invalid direction. It must be -1, 0, or 1");
        }
        this.direction = direction;
    }

    // Helper method to get a string value from the direction
    private String directionToString()
    {
        if (direction == UNSET)
            return "UNSET";
        else if (direction == HORIZONTAL)
            return "HORIZONTAL";
        else
            return "VERTICAL";
    }

    // toString value for this Ship
    public String toString()
    {
        return "Ship: " + getRow() + ", " + getCol() + " with length " + getLength() + " and direction " + directionToString();
    }
}