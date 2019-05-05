module vfx_windows_tutorial_3_1 {
    requires javafx.controls;
    requires javafx.web;
    requires jfxtras.window;

    opens eu.mihosoft.vrl.fxwindows.tutorial to javafx.fxml;
    exports eu.mihosoft.vrl.fxwindows.tutorial;
}