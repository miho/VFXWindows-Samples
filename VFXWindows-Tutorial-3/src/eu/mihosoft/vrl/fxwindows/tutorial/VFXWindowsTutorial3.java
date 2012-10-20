/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.fxwindows.tutorial;

import eu.mihosoft.vrl.fxwindows.Window;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Third VFXWindows tutorial.
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class VFXWindowsTutorial3 extends Application {

    @Override
    public void start(Stage primaryStage) {

        // create the canvas where the windows will be added to
        Pane canvas = new Pane();

        // create a scrollpane
        ScrollPane scrollPane = new ScrollPane();

        // define the scrollpane content
        scrollPane.setContent(canvas);

        // create a scene that displays the scrollpane (resolution 600,600)
        Scene scene = new Scene(scrollPane, 800, 800);

        Window w = new BrowserWindow(canvas, "http://www.google.com");

        // set the window position to 10,10 (coordinates inside canvas)
        w.setLayoutX(10);
        w.setLayoutY(10);

        // define the initial window size
        w.setPrefSize(400, 300);

        // add the window to the canvas
        canvas.getChildren().add(w);

        // init and show the stage
        primaryStage.setTitle("VFXWindows Tutorial 03");
        primaryStage.setScene(scene);
        primaryStage.show();
    }    

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
