/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.fxwindows.tutorial;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jfxtras.scene.control.window.Window;

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
        
        Window w = new Window("Chart Sample 01");
        
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Grapefruit", 13),
                new PieChart.Data("Oranges", 25),
                new PieChart.Data("Plums", 10),
                new PieChart.Data("Pears", 22),
                new PieChart.Data("Apples", 30));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Imported Fruits");

        w.getContentPane().getChildren().add(chart);
        
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
