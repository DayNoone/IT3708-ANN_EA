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
    BoardElement[][] board = new BoardElement[Values.FLATLAND_BOARD_SIZE][Values.FLATLAND_BOARD_SIZE];
    Pair<Integer, Integer> agentCoordinate;
    int direction; // 0: North, 1: East, 2: South, 3: West
    int[] colDirection = {0, 1, 0, -1};
    int[] rowDirection = {-1, 0, 1, 0};
    static ArrayList<Pair<Integer, Integer>> coordinates = new ArrayList<>();
    static Random random = new Random();

    private int foodEaten, poisonEaten;

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
    }

    private void initBoard() {
        fillFood();
        fillPoison();
        fillAgent();
        initialBoardState = new BoardElement[board.length][];
        System.arraycopy( board, 0, initialBoardState, 0, board.length );
        intialAgentCoordinate = new Pair<>(agentCoordinate.getElement1(), agentCoordinate.getElement2());
        getSensors();
    }

    public void resetBoard(){
        foodEaten = 0;
        poisonEaten = 0;
        for (int i = 0; i < initialBoardState.length; i++) {
            System.arraycopy(initialBoardState[i], 0, board[i], 0, initialBoardState[i].length);
        }
        printBoard();
        agentCoordinate = new Pair<>(intialAgentCoordinate.getElement1(), intialAgentCoordinate.getElement2());
    }

    public void moveForeward(){
        board[agentCoordinate.getElement2()][agentCoordinate.getElement1()] = BoardElement.EMPTY;
        int newXPos = (agentCoordinate.getElement1() + colDirection[direction] + Values.FLATLAND_BOARD_SIZE) % Values.FLATLAND_BOARD_SIZE;
        int newYPos = (agentCoordinate.getElement2() + rowDirection[direction] + Values.FLATLAND_BOARD_SIZE) % Values.FLATLAND_BOARD_SIZE;
        BoardElement boardElement = board[newXPos][newYPos];
        agentCoordinate.setElement1(newXPos);
        agentCoordinate.setElement2(newYPos);
        if(boardElement == BoardElement.FOOD) {
            foodEaten++;
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
        fillElement(BoardElement.AGENT);
    }

    private void fillElement(BoardElement element) {
        Pair<Integer, Integer> coordinate = coordinates.remove(random.nextInt(coordinates.size()));
        agentCoordinate = coordinate;
        board[coordinate.getElement2()][coordinate.getElement1()] = element;
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

    public void setFoodEaten(int foodEaten) {
        this.foodEaten = foodEaten;
    }

    public void setPoisonEaten(int poisonEaten) {
        this.poisonEaten = poisonEaten;
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
                String imgPath;
                switch (board[y][x]){
                    case AGENT:
                        imgPath = "res/agent.PNG";
                        break;
                    case FOOD:
                        imgPath = "res/food.PNG";
                        break;
                    case POISON:
                        imgPath = "res/poison.PNG";
                        break;
                    case EMPTY:
                        imgPath = "res/empty.PNG";
                        break;
                    default:
                        imgPath = "res/empty.PNG";
                        break;
                }
                ImageView img = new ImageView(new Image(imgPath));
                box.getChildren().add(img);
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
}
