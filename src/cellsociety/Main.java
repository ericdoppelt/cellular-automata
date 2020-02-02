package cellsociety;

import cellsociety.Visuals.Footer;
import cellsociety.Visuals.GridView;
import cellsociety.Visuals.Header;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import java.util.Random;


public class Main extends Application {

  private static final String RESOURCE_LANGUAGE = "Standard";

  private static final double FRAMES_PER_SECOND = 30;
  private static final double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
  private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
  private static double SCENE_WIDTH = 400;
  private static double SCENE_HEIGHT = 500;

  private Grid grid;
  private GridView gridView;
  private BorderPane root;
  private Header inputHeader;

  private Random r = new Random();

  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Begins our JavaFX application Gets the current grid and sets the stage to a scene with that
   * grid
   */
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Simulation");
    startAnimationLoop();

    grid = new FireGrid(10, 10, 0.6);
    grid.getGrid().get(grid.getColumns() / 2).get(grid.getColumns() / 2).update(Color.RED, "burning");
    //random generation of Percolation blocked bricks (33% blocked)
//    for (int i = 0; i < grid.getRows(); i++) {
//      for (int j = 0; j < grid.getColumns(); j++) {
//        int rr = r.nextInt(3);
//        if (rr == 1){
//          int ran_x = r.nextInt(grid.getColumns());
//          int ran_y = r.nextInt(grid.getRows());
//          grid.getGrid().get(ran_x).get(ran_y).update(Color.BLACK, "blocked");
//        }
//      }
//    }

    root = new BorderPane();

    gridView = new GridView(grid, SCENE_WIDTH, SCENE_HEIGHT - 100);
    root.setCenter(gridView.getGridPane());

    inputHeader = new Header(SCENE_WIDTH, RESOURCE_LANGUAGE);
    root.setTop(inputHeader.renderHeader());

    Footer footerInput = new Footer(SCENE_WIDTH, RESOURCE_LANGUAGE);
    root.setBottom(footerInput.renderFooter());
    Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void startAnimationLoop() {
    KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
      try {
        step(SECOND_DELAY);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    });
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  private void step(double elapsedTime) throws InterruptedException {
    if (inputHeader.getPlayStatus()) {
      grid.updateGrid();
      gridView.updateGrid(grid);
    }
  }
}
