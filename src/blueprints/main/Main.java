package blueprints.main;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import static com.sun.jna.platform.win32.WinUser.GWL_STYLE;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	
    public static void main(String[] args) { 
    	launch(args); 
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("BlueprintsGUI.fxml"));
        primaryStage.setScene(new Scene(root, 555, 555));
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());
		primaryStage.setWidth(primaryScreenBounds.getWidth());
		primaryStage.setHeight(primaryScreenBounds.getHeight());
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        //Borrowed code below from https://stackoverflow.com/questions/26972683/javafx-minimizing-undecorated-stage
        long lhwnd = com.sun.glass.ui.Window.getWindows().get(0).getNativeWindow();
        Pointer lpVoid = new Pointer(lhwnd);
        HWND hwnd = new HWND(lpVoid);
        final User32 user32 = User32.INSTANCE;
        int oldStyle = user32.GetWindowLong(hwnd, GWL_STYLE);
        int newStyle = oldStyle | 0x00020000;
        user32.SetWindowLong(hwnd, GWL_STYLE, newStyle);
    }
}