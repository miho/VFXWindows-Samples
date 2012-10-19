/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.fxwindows.tutorial;

import eu.mihosoft.vrl.fxwindows.CloseIcon;
import eu.mihosoft.vrl.fxwindows.MinimizeIcon;
import eu.mihosoft.vrl.fxwindows.RotateIcon;
import eu.mihosoft.vrl.fxwindows.Window;
import eu.mihosoft.vrl.fxwindows.WindowIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class BrowserWindow extends Window {

    private WebEngine engine;
    private int historyIndex = -1;

    public BrowserWindow(final Pane canvas, final String url) {

        // add window icons:

        // either to the left
        getLeftIcons().add(new CloseIcon(this));

        // .. or to the right
        getRightIcons().add(new MinimizeIcon(this));


        MenuItem newWindowItem = new MenuItem("New Window");

        newWindowItem.onActionProperty().set(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Window newW = new BrowserWindow(canvas, url);

                newW.setPrefSize(getPrefWidth(), getPrefHeight());
                canvas.getChildren().add(newW);
            }
        });

        final ContextMenu m = new ContextMenu(newWindowItem);

        // you can also add custom icons
        final WindowIcon customIcon = new WindowIcon();
        customIcon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                // we add a nice scale transition
                // (it doesn't do anything useful but it is cool!)

                m.show(customIcon, Side.BOTTOM, 0, 0);
            }
        });

        // finally, we add our custom icon
        getRightIcons().add(customIcon);

        // note: we actually could style the icon via css
        //       see the javafx documentation on how to do that

        // create a webview and define the webpage to load
        final WebView view = new WebView();
        engine = view.getEngine();
        engine.load(url);

        view.getEngine().setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() {
            @Override
            public WebEngine call(PopupFeatures p) {
                BrowserWindow newW = new BrowserWindow(canvas, url);

                newW.setPrefSize(getPrefWidth(), getPrefHeight());
                canvas.getChildren().add(newW);

                return newW.engine;
            }
        });

        // create a webview with address field and add the content pane
        setContentPane(createWindowContent(canvas, view, DEFAULT_STYLE_CLASS));

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
                new ChangeListener<String>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends String> observable,
                            String oldValue, String newValue) {
                        locationField.setText(newValue);
                    }
                });
        EventHandler<ActionEvent> goAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.getEngine().load(
                        locationField.getText().startsWith("http://")
                        ? locationField.getText()
                        : "http://" + locationField.getText());
            }
        };

        locationField.setOnAction(goAction);

        Button goButton = new Button("Go");
        goButton.setDefaultButton(true);
        goButton.setOnAction(goAction);

        final Button backButton = new Button("<-");
        backButton.setOnAction(goAction);
        backButton.setDisable(true);

        titleProperty().bind(view.getEngine().titleProperty());
        
        view.getEngine().locationProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                historyIndex = -1;
            }
        });

        view.getEngine().getHistory().currentIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                backButton.setDisable(t1.intValue() < 1);
            }
        });

        EventHandler<ActionEvent> backAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (historyIndex < 0) {
                    historyIndex = view.getEngine().getHistory().getCurrentIndex();
                }

                int newIndex = historyIndex - 1;

                String url =
                        view.getEngine().getHistory().getEntries().get(newIndex).getUrl();

                view.getEngine().load(url);
            }
        };

        backButton.setOnAction(backAction);

        // Layout logic
        HBox hBox = new HBox(5);
        hBox.getChildren().setAll(backButton, locationField, goButton);
        HBox.setHgrow(locationField, Priority.ALWAYS);

        VBox vBox = new VBox(5);
        vBox.getChildren().setAll(hBox, view);
        VBox.setVgrow(view, Priority.ALWAYS);

        content.getChildren().add(vBox);

        return content;
    }
}
