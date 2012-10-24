/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.fxwindows.tutorial;

import eu.mihosoft.vrl.fxwindows.Window;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.RotateEvent;
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

        // create a scene that displays the scrollpane (resolution 1200, 1200)
        Scene scene = new Scene(scrollPane, 1200, 800);

        BrowserWindow.createAndAddWindow(canvas, "http://www.google.com");
        
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
