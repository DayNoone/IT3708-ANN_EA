import enums.BoardElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class Board {
    BoardElement[][] board = new BoardElement[Values.FLATLAND_BOARD_SIZE][Values.FLATLAND_BOARD_SIZE];

    public Board(){
        for(int y = 0; y < Values.FLATLAND_BOARD_SIZE; y++){
            for(int x = 0; x < Values.FLATLAND_BOARD_SIZE; x++){
                board[y][x] = BoardElement.EMPTY;
            }
        }
        initBoard();
    }

    private void initBoard() {
        board[0][3] = BoardElement.FOOD;
        board[1][2] = BoardElement.FOOD;
        board[1][3] = BoardElement.FOOD;
        board[1][4] = BoardElement.FOOD;
        board[3][2] = BoardElement.AGENT;
        board[3][3] = BoardElement.POISON;
        board[4][5] = BoardElement.POISON;
        board[5][6] = BoardElement.POISON;

    }

    public GridPane drawGrid() {
        GridPane boardGrid = new GridPane();
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
        return boardGrid;
    }
}
