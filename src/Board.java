import enums.BoardElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
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

    private BoardElement[][] intialBoardState;
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
        //TODO CHECK IF COPIED
        intialBoardState = board;
        intialAgentCoordinate = agentCoordinate;
        getSensors();
    }

    public void resetBoard(){
        foodEaten = 0;
        poisonEaten = 0;
        board = intialBoardState;
        agentCoordinate = intialAgentCoordinate;
    }

    public void moveForeward(){
        agentCoordinate.setElement1((agentCoordinate.getElement1() + colDirection[direction] + Values.FLATLAND_BOARD_SIZE) % Values.FLATLAND_BOARD_SIZE);
        agentCoordinate.setElement2((agentCoordinate.getElement2() + rowDirection[direction] + Values.FLATLAND_BOARD_SIZE) % Values.FLATLAND_BOARD_SIZE);
        BoardElement boardElement = board[agentCoordinate.getElement2()][agentCoordinate.getElement1()];
        if(boardElement == BoardElement.FOOD) {
            foodEaten++;
        } else if (boardElement == BoardElement.POISON){
            poisonEaten++;
        }
        board[agentCoordinate.getElement2()][agentCoordinate.getElement1()] = BoardElement.AGENT;
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
        int foodCount = (Values.FLATLAND_BOARD_SIZE*Values.FLATLAND_BOARD_SIZE) / 3;
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
        ArrayList<BoardElement> sensors = new ArrayList<>();
        for(int i = -1; i < 2; i++){
            int sensorDirection = (direction + i + 4) % 4;
            sensors.add(board[(agentCoordinate.getElement2() + rowDirection[sensorDirection] + Values.FLATLAND_BOARD_SIZE) % Values.FLATLAND_BOARD_SIZE][(agentCoordinate.getElement1() + colDirection[sensorDirection] + Values.FLATLAND_BOARD_SIZE) % Values.FLATLAND_BOARD_SIZE]);
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

    public void drawGrid(GridPane boardGrid) {
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
                boardGrid.add(box, x, y);
            }
        }
    }
}
