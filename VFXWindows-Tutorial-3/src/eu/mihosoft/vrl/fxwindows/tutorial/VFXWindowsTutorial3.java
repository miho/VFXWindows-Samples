/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.fxwindows.tutorial;

import eu.mihosoft.vrl.fxwindows.CloseIcon;
import eu.mihosoft.vrl.fxwindows.MinimizeIcon;
import eu.mihosoft.vrl.fxwindows.Window;
import eu.mihosoft.vrl.fxwindows.WindowIcon;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        Scene scene = new Scene(scrollPane, 600, 600);

        // create a window with title "My Window"
        final Window w = new Window("My Window");

        // add window icons:

        // either to the left
        w.getLeftIcons().add(new CloseIcon(w));

        // .. or to the right
        w.getRightIcons().add(new MinimizeIcon(w));

        // you can also add custom icons
        WindowIcon customIcon = new WindowIcon();
        customIcon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                // we add a nice scale transition
                // (it doesn't do anything useful but it is cool!)
                ScaleTransition st = new ScaleTransition(Duration.seconds(2), w);
                st.setFromX(w.getScaleX());
                st.setFromY(w.getScaleY());
                st.setToX(0.1);
                st.setToY(0.1);
                st.setAutoReverse(true);
                st.setCycleCount(2);
                st.play();
            }
        });

        // finally, we add our custom icon
        w.getRightIcons().add(customIcon);

        // note: we actually could style the icon via css
        //       see the javafx documentation on how to do that

        
        // create a custom content pane
        StackPane content = new StackPane();
        content.setPadding(new Insets(2));

        // create a webview and define the webpage to load
        WebView view = new WebView();
        view.getEngine().load("http://www.google.com");
        
        // add the webview to the content pane
        content.getChildren().add(view);
        w.setContentPane(content);
        
        // set the window position to 10,10 (coordinates inside canvas)
        w.setLayoutX(10);
        w.setLayoutY(10);

        // define the initial window size
        w.setPrefSize(300, 200);

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
