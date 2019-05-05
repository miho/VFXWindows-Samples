/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.fxwindows.tutorial;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import jfxtras.scene.control.window.CloseIcon;
import jfxtras.scene.control.window.MinimizeIcon;
import jfxtras.scene.control.window.Window;
import jfxtras.scene.control.window.WindowIcon;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class BrowserWindow extends Window {

    private WebController webController;
    private int historyIndex = -1;

    public BrowserWindow(final Pane canvas, final String url) {

        // add window icons:

        // either to the left
        getLeftIcons().add(new CloseIcon(this));

        // .. or to the right
        getRightIcons().add(new MinimizeIcon(this));

        MenuItem newWindowItem = new MenuItem("New Window");

        newWindowItem.onActionProperty().set(event -> {
            Window newW = createAndAddWindow(canvas, "http://www.google.com");

            newW.setPrefSize(getPrefWidth(), getPrefHeight());
        });

        final ContextMenu m = new ContextMenu(newWindowItem);

        // you can also add custom icons
        final WindowIcon customIcon = new WindowIcon();
        customIcon.setOnAction(event -> {
            // we add a nice scale transition
            // (it doesn't do anything useful but it is cool!)

            m.show(customIcon, Side.BOTTOM, 0, 0);
        });

        // finally, we add our custom icon
        getRightIcons().add(customIcon);

        // note: we actually could style the icon via css
        //       see the javafx documentation on how to do that

        // create a webview and define the webpage to load
        final WebView view = new WebView();

        webController = new WebController(view.getEngine());
        webController.load(url);

        view.getEngine().setCreatePopupHandler(p -> {
            BrowserWindow newW = createAndAddWindow(canvas, "http://www.google.com");

            newW.setPrefSize(getPrefWidth(), getPrefHeight());

            return newW.webController.engine;
        });

        // create a webview with address field and add the content pane
        setContentPane(createWindowContent(canvas, view, DEFAULT_STYLE_CLASS));


        setOnRotate(event -> {

            setRotate(getRotate() + event.getAngle());
            event.consume();
        });

        setOnZoom(event -> {

            setScaleX(getScaleX() * event.getZoomFactor());
            setScaleY(getScaleY() * event.getZoomFactor());

            event.consume();
        });

        DropShadow shadow = new DropShadow(18, Color.BLACK);

        setEffect(shadow);

    }

    public static BrowserWindow createAndAddWindow(final Pane canvas, String url) {
        BrowserWindow w = new BrowserWindow(canvas, url);

        // set the window position to 10,10 (coordinates inside canvas)
        w.setLayoutX(10);
        w.setLayoutY(10);

        // define the initial window size
        w.setPrefSize(600, 400);

        // add the window to the canvas
        canvas.getChildren().add(w);

        w.setOnCloseAction(t -> {

            List<BrowserWindow> browserWindows = new ArrayList<>();

            for (Node n : canvas.getChildrenUnmodifiable()) {
                if (n instanceof BrowserWindow) {
                    browserWindows.add((BrowserWindow) n);
                }
            }

            if (browserWindows.size() == 1) {
                createAndAddWindow(canvas, "http://www.google.com");
            }
        });

        return w;
    }

    /**
     * Here we create a webview with address field.
     *
     * <p>Based on ensemble code</b>
     *
     * @param url url to load
     * @return pane that contains the webview and the address field
     */
    private Pane createWindowContent(final Pane canvas, final WebView view, String url) {

        StackPane content = new StackPane();
        content.setPadding(new Insets(2));

        final TextField locationField = new TextField(url);
        view.getEngine().locationProperty().addListener(
                (observable, oldValue, newValue) -> locationField.setText(newValue));

        EventHandler<ActionEvent> goAction = event -> {

            String location = locationField.getText();

            if (!location.startsWith("http://") && !location.startsWith("file://")) {
                location = "http://" + location;
            }

            webController.load(location);
        };

        locationField.setOnAction(goAction);

        Button goButton = new Button("Go");
        goButton.setDefaultButton(true);
        goButton.setOnAction(goAction);


        EventHandler<ActionEvent> backAction = event -> webController.backward();

        EventHandler<ActionEvent> forAction = event -> webController.forward();

        final Button backButton = new Button("<-");
        backButton.setOnAction(backAction);
        backButton.setDisable(true);

        final Button forButton = new Button("->");
        forButton.setOnAction(forAction);
        forButton.setDisable(true);

        backButton.disableProperty().bind(webController.backwardIsEmptyProperty);
        forButton.disableProperty().bind(webController.forwardIsEmptyProperty);

        titleProperty().bind(view.getEngine().titleProperty());

        // Layout logic
        HBox hBox = new HBox(5);
        hBox.getChildren().setAll(backButton, forButton, locationField, goButton);
        HBox.setHgrow(locationField, Priority.ALWAYS);

        VBox vBox = new VBox(5);
        vBox.getChildren().setAll(hBox, view);
        VBox.setVgrow(view, Priority.ALWAYS);

        content.getChildren().add(vBox);

        return content;
    }

    static class WebController {

        enum Direction {
            FORWARD,
            BACKWARD,
            NONE
        }
        private Stack<String> oldEntries = new Stack<>();
        private Stack<String> newEntries = new Stack<>();
        private WebEngine engine;
        private BooleanProperty backwardIsEmptyProperty = new SimpleBooleanProperty();
        private BooleanProperty forwardIsEmptyProperty = new SimpleBooleanProperty();
        private Direction direction = Direction.NONE;

        public WebController(WebEngine engine) {
            this.engine = engine;

            engine.locationProperty().addListener((ov, t, t1) -> {

                if (t == null || t.isEmpty()) {
                    backwardIsEmptyProperty.set(oldEntries.isEmpty());
                    forwardIsEmptyProperty.set(newEntries.isEmpty());
                    return;
                }

                if (direction == Direction.BACKWARD) {
                    newEntries.push(t);
                } else if (direction == Direction.FORWARD) {
                    oldEntries.push(t);
                }

                backwardIsEmptyProperty.set(oldEntries.isEmpty());
                forwardIsEmptyProperty.set(newEntries.isEmpty());
            });
        }

        public void backward() {

            direction = Direction.BACKWARD;

            if (oldEntries.isEmpty()) {
                return;
            }

            String e = oldEntries.pop();

            engine.load(e);
        }

        public void forward() {

            direction = Direction.FORWARD;

            if (newEntries.isEmpty()) {
                return;
            }

            String e = newEntries.pop();

            engine.load(e);
        }

        public void load(String url) {

            direction = Direction.FORWARD;

            engine.load(url);

            newEntries.clear();
        }

        /**
         * @return the backwardIsEmptyProperty
         */
        public ReadOnlyBooleanProperty getBackwardIsEmptyProperty() {
            return backwardIsEmptyProperty;
        }

        /**
         * @return the forwardIsEmptyProperty
         */
        public ReadOnlyBooleanProperty getForwardIsEmptyProperty() {
            return forwardIsEmptyProperty;
        }
    }
}
