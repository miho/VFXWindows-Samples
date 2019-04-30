module vfx_windows_tutorial_2 {
    requires javafx.controls;
    requires jfxtras.window;

    opens eu.mihosoft.vrl.fxwindows.tutorial to javafx.fxml;
    exports eu.mihosoft.vrl.fxwindows.tutorial;
}