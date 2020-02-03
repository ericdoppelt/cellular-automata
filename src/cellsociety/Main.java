package cellsociety;

import cellsociety.Visuals.Footer;
import cellsociety.Visuals.GridView;
import cellsociety.Visuals.Header;
import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.Random;
import xml.XMLException;
import xml.XMLParser;


public class Main extends Application {

  private static final String DEFAULT_RESOURCE_FOLDER = "/Resources/";
  private static final String STYLESHEET = "default.css";
  private static final String RESOURCE_LANGUAGE = "Standard";

  private static final double FRAMES_PER_SECOND = 5;
  private static final double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
  private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
  private double SCENE_WIDTH = 500;
  private double SCENE_HEIGHT = 500;

  private static final String DATA_FILE_EXTENSION = "*.xml";
  private static final FileChooser FILE_CHOOSER = makeChooser(DATA_FILE_EXTENSION);

  private Grid grid;
  private GridView gridView;
  private BorderPane root;
  private Header inputHeader;
  private Footer inputFooter;

  private Stage myStage;

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

    root = new BorderPane();
    root.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    root.setMaxHeight(SCENE_HEIGHT);
    root.setMaxWidth(SCENE_WIDTH);

    inputHeader = new Header(SCENE_HEIGHT, SCENE_WIDTH, RESOURCE_LANGUAGE);
    root.setTop(inputHeader.renderHeader());

    inputFooter = new Footer(SCENE_HEIGHT, SCENE_WIDTH, RESOURCE_LANGUAGE);
    root.setBottom(inputFooter.renderFooter());

    gridView = new GridView(SCENE_WIDTH, SCENE_HEIGHT);
    root.setCenter(gridView.getGridPane());

    Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
    myStage = primaryStage;
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void startAnimationLoop() {
    KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
        step(SECOND_DELAY);
    });
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  private void step(double elapsedTime){
    if (inputHeader.getLoadStatus()) xmlToGrid();
    else if (inputHeader.getSkipStatus()) skipAhead();
    else if (inputHeader.getSpeedStatus()) updateSpeed();
    else if (inputHeader.getPlayStatus()) updateState();
    }


  private void skipAhead() {
    for (int i = 0; i < inputFooter.getJumpValue(); i++) {
      grid.updateGrid();
    }
    gridView.updateGrid(grid);
    inputHeader.setSkipOff();
  }

//  private void uploadFile() {
//    FileChooser fc = new FileChooser();
//    File file = fc.showOpenDialog(myStage);
//    if (file == null) System.out.println("Please pick a file!");
//    else if (!file.getName().substring(file.getName().length() - 4).equals(".xml")) System.out.println("Please pick an XML File!");
//    else {
//      myXMLFile = file;
//    }
//
//    inputHeader.setLoadOff();
//  }

  private static FileChooser makeChooser (String extensionAccepted) {
    FileChooser result = new FileChooser();
    result.setTitle("Open Data File");
    // pick a reasonable place to start searching for files
    result.setInitialDirectory(new File(System.getProperty("user.dir")));
    result.getExtensionFilters().setAll(new ExtensionFilter("Text Files", extensionAccepted));
    return result;
  }

  private void xmlToGrid(){
    File dataFile = FILE_CHOOSER.showOpenDialog(myStage);
    try {
      grid = new XMLParser("grid").getGrid(dataFile);
      gridView.updateGrid(grid);
      }
    catch (XMLException e) {
      System.out.println(e.getMessage());
    }
    inputHeader.setLoadOff();
  }

  private void updateSpeed() {
    inputHeader.setSpeedOff();
  }

  private void updateState() {
    grid.updateGrid();
    gridView.updateGrid(grid);
  }
}
