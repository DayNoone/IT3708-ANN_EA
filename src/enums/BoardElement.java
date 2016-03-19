package enums;

/**
 * Created by DagInge on 12/03/2016.
 */
public enum BoardElement {
    FOOD, POISON, AGENT, EMPTY;


    @Override
    public String toString() {
        switch (this){
            case AGENT:
                return "A";
            case EMPTY:
                return " ";
            case FOOD:
                return "F";
            case POISON:
                return "P";
            default:
                return "O";

        }
    }
}
