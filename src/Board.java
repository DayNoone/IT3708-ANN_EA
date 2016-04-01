import enums.BoardElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Board {
    private final Image agentImage, foodImage, poisonImage, emptyImage;
    BoardElement[][] board = new BoardElement[Values.FLATLAND_BOARD_SIZE][Values.FLATLAND_BOARD_SIZE];
    Pair<Integer, Integer> agentCoordinate;
    int direction; // 0: North, 1: East, 2: South, 3: West
    int[] colDirection = {0, 1, 0, -1};
    int[] rowDirection = {-1, 0, 1, 0};
    static ArrayList<Pair<Integer, Integer>> coordinates = new ArrayList<>();
    static Random random = new Random();

    private int foodEaten = 0;
    private int poisonEaten = 0;

    private BoardElement[][] initialBoardState;
    private Pair<Integer, Integer> intialAgentCoordinate;

    public Board(){

        for(int y = 0; y < Values.FLATLAND_BOARD_SIZE; y++){
            for(int x = 0; x < Values.FLATLAND_BOARD_SIZE; x++){
                board[y][x] = BoardElement.EMPTY;
                coordinates.add(new Pair<>(x, y));
            }
        }
        initBoard();
        agentImage = new Image("res/agent.PNG");
        foodImage = new Image("res/food.PNG");
        poisonImage = new Image("res/poison.PNG");
        emptyImage = new Image("res/empty.PNG");
    }

    private void initBoard() {
        fillFood();
        fillPoison();
        fillAgent();

        initialBoardState = deepCopyBoardElementMatrix(board);

        intialAgentCoordinate = new Pair<>(agentCoordinate.getElement1(), agentCoordinate.getElement2());
        getSensors();
    }

    public void resetBoard(){
        foodEaten = 0;
        poisonEaten = 0;
        board = deepCopyBoardElementMatrix(initialBoardState);
        agentCoordinate = new Pair<>(intialAgentCoordinate.getElement1(), intialAgentCoordinate.getElement2());
    }

    public void moveForeward(){
        board[agentCoordinate.getElement2()][agentCoordinate.getElement1()] = BoardElement.EMPTY;
        int newXPos = (agentCoordinate.getElement1() + colDirection[direction] + Values.FLATLAND_BOARD_SIZE) % Values.FLATLAND_BOARD_SIZE;
        int newYPos = (agentCoordinate.getElement2() + rowDirection[direction] + Values.FLATLAND_BOARD_SIZE) % Values.FLATLAND_BOARD_SIZE;
        BoardElement boardElement = board[newYPos][newXPos];
        agentCoordinate.setElement1(newXPos);
        agentCoordinate.setElement2(newYPos);
        if(boardElement == BoardElement.FOOD) {
            foodEaten += 1;
        } else if (boardElement == BoardElement.POISON){
            poisonEaten++;
        }
        board[newYPos][newXPos] = BoardElement.AGENT;
    }
    public void moveRight(){
        direction++;
        direction = (direction + 4) % 4;
        moveForeward();
    }
    public void moveLeft(){
        direction--;
        direction = (direction + 4) % 4;
        moveForeward();
    }

    private void fillFood() {
        int foodCount = Values.FLATLAND_MAX_FOOD_COUNT;
        while (foodCount > 0) {
            fillElement(BoardElement.FOOD);
            foodCount--;
        }
    }

    private void fillPoison() {
        int poisonCount = (Values.FLATLAND_BOARD_SIZE*Values.FLATLAND_BOARD_SIZE*2) / 9;
        while(poisonCount > 0) {
            fillElement(BoardElement.POISON);
            poisonCount--;
        }
    }

    private void fillAgent() {
        agentCoordinate = fillElement(BoardElement.AGENT);
    }

    private Pair<Integer, Integer> fillElement(BoardElement element) {
        Pair<Integer, Integer> coordinate = coordinates.remove(random.nextInt(coordinates.size()));
        board[coordinate.getElement2()][coordinate.getElement1()] = element;
        return coordinate;
    }

    public ArrayList<BoardElement> getSensors(){
        ArrayList<BoardElement> elements = new ArrayList<>();
        for(int i = -1; i < 2; i++){
            int sensorDirection = (direction + i + 4) % 4;
            elements.add(board[(agentCoordinate.getElement2() + rowDirection[sensorDirection] + Values.FLATLAND_BOARD_SIZE) % Values.FLATLAND_BOARD_SIZE][(agentCoordinate.getElement1() + colDirection[sensorDirection] + Values.FLATLAND_BOARD_SIZE) % Values.FLATLAND_BOARD_SIZE]);
        }
        Collections.swap(elements, 0, 1);
        ArrayList<BoardElement> sensors = new ArrayList<>();
        for (int j = 0; j < elements.size()*2; j++){
            if(j < elements.size()){
                if(elements.get(j) == BoardElement.FOOD){
                    sensors.add(elements.get(j));
                } else {
                    sensors.add(BoardElement.EMPTY);
                }
            } else {
                if(elements.get(j-elements.size()) == BoardElement.POISON){
                    sensors.add(elements.get(j-elements.size()));
                } else {
                    sensors.add(BoardElement.EMPTY);
                }
            }
        }
        return sensors;
    }


    public int getFoodEaten() {
        return foodEaten;
    }

    public int getPoisonEaten() {
        return poisonEaten;
    }

    public GridPane getBoardGridPane() {
        GridPane gridPane = new GridPane();

        for(int y = 0; y < Values.FLATLAND_BOARD_SIZE; y++){
            for(int x = 0; x < Values.FLATLAND_BOARD_SIZE; x++){
                HBox box = new HBox();
                box.setStyle("-fx-border-color: black;");
                Image img;
                switch (board[y][x]){
                    case AGENT:
                        img = agentImage;
                        break;
                    case FOOD:
                        img = foodImage;
                        break;
                    case POISON:
                        img = poisonImage;
                        break;
                    case EMPTY:
                        img = emptyImage;
                        break;
                    default:
                        img = emptyImage;
                        break;
                }
                ImageView imgView = new ImageView(img);
                box.getChildren().add(imgView);
                gridPane.add(box, x, y);
            }
        }
        return gridPane;
    }

    public void printBoard(){
        for (int j = 0; j < board.length; j++) {
            System.out.println(Arrays.toString(board[j]));
        }
        System.out.println("");
    }

    private static BoardElement[][] deepCopyBoardElementMatrix(BoardElement[][] input) {
        if (input == null)
            return null;
        BoardElement[][] result = new BoardElement[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }
}
