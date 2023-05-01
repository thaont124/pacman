package maze.pacman;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class MazeGame extends Application {
    private static final int TILE_SIZE = 20;
    private static final int[] START = {1, 0};

    private Group mazeGroup;
    private Entity entity;

    public void start(Stage primaryStage) throws Exception {
        // Đọc ma trận mê cung từ file
        Scanner scanner = new Scanner(new File("map.txt"));
        MazeSolver mazeSolver = new MazeSolver();
        mazeSolver.setMaze(scanner);
        ArrayList<int[]> path = mazeSolver.dfs();

        // Tạo đối tượng thực thể và đặt vị trí ban đầu
        entity = new Entity(START);

        // Tạo màn hình chơi game
        mazeGroup = new Group();
        for (int i = 0; i < mazeSolver.getMaze().size(); i++) {
            ArrayList<Integer> row = mazeSolver.getMaze().get(i);
            for (int j = 0; j < row.size(); j++) {
                int type = row.get(j);
                Rectangle tile = new Rectangle(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                if (type == 1) {
                    tile.setFill(Color.BLACK);
                } else if (type == 3) {
                    tile.setFill(Color.GREEN);
                } else {
                    tile.setFill(Color.WHITE);
                }
                mazeGroup.getChildren().add(tile);
            }
        }

        // Thêm thực thể vào màn hình chơi game
        Rectangle entityRect = new Rectangle(START[1] * TILE_SIZE, START[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        entityRect.setFill(Color.BLUE);
        mazeGroup.getChildren().add(entityRect);

        // Tạo scene và hiển thị
        Scene scene = new Scene(mazeGroup, mazeSolver.getMaze().get(0).size() * TILE_SIZE, mazeSolver.getMaze().size() * TILE_SIZE);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Di chuyển thực thể
        Timeline timeline = new Timeline();
        for (int i = 1; i < path.size(); i++) {
            int[] current = path.get(i);
            int x = current[0];
            int y = current[1];

            KeyValue xKeyValue = new KeyValue(entityRect.xProperty(), y * TILE_SIZE);
            KeyValue yKeyValue = new KeyValue(entityRect.yProperty(), x * TILE_SIZE);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(500 * i), xKeyValue, yKeyValue);

            timeline.getKeyFrames().add(keyFrame);

            entity.setPosition(new int[]{x, y});
        }
        timeline.play();
    }
    public static void main(String[] args) {
        launch();
    }
}
