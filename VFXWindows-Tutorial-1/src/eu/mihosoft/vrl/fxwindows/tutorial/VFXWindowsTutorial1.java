/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.fxwindows.tutorial;

import eu.mihosoft.vrl.fxwindows.Window;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * First VFXWindows tutorial.
 * 
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class VFXWindowsTutorial1 extends Application {

    @Override
    public void start(Stage primaryStage) {

        // create the canvas where the windows will be added to
        Pane canvas = new Pane();
        
        // create a scene that displays the canvas (resolution 600,600)
        Scene scene = new Scene(canvas, 600, 600);

        // create a window with title "My Window"
        Window w = new Window("My Window");
        
        // set the window position to 10,10 (coordinates inside canvas)
        w.setLayoutX(10);
        w.setLayoutY(10);
        
        // define the initial window size
        w.setPrefSize(300, 200);

        // add the window to the canvas
        canvas.getChildren().add(w);

        // init and show the stage
        primaryStage.setTitle("VFXWindows Tutorial 01");
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
