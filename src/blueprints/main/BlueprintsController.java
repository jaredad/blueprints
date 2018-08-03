package blueprints.main;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class BlueprintsController {

	@FXML
	private Tab fileTab, objectsTab, connectorsTab;

	@FXML
	private TabPane tabPane;

	@FXML
	private Rectangle header, rect2;

	@FXML
	private AnchorPane pane, headerPane;

	@FXML
	private Text headerText;

	@FXML
	private VBox textVbox;

	@FXML
	private Button minimize, exit, restore_and_maximize, save;

	private boolean maximized = true;

	private double x;
	private double y;

	private Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

	public void initialize() {
		restore_and_maximize.setText("🗗");
		save.setText("💾");
		initializeBind();
		initializeFocus();
		initializeDrag();
	}

	public void initializeBind() {
		pane.widthProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue o, Object oldVal, Object newVal) {
				headerPane.setPrefWidth((double) newVal);
				header.setWidth((double) newVal);
				tabPane.setMinWidth((double) newVal + 2.0);
				textVbox.setLayoutX((double) newVal / 2.0 - textVbox.getWidth() / 2);
			}
		});
	}

	public void initializeFocus() {
		minimize.focusedProperty().addListener(ChangeListener -> tabPane.requestFocus());
		restore_and_maximize.focusedProperty().addListener(ChangeListener -> tabPane.requestFocus());
	}

	public void initializeDrag() {
		header.setOnMousePressed(event -> beginDrag(event));
		header.setOnMouseDragged(event -> dragWindow(event));
		header.setOnMouseReleased(event -> boundReleaseFunctions(event));
		headerText.setOnMousePressed(event -> beginDrag(event));
		headerText.setOnMouseDragged(event -> dragWindow(event));
		headerText.setOnMouseReleased(event -> boundReleaseFunctions(event));
	}

	public void exit() {
		Platform.exit();
	}

	public void minimize() {
		Stage stage = (Stage) pane.getScene().getWindow();
		stage.setIconified(true);
	}

	public void restoreAndMaximize() {
		if (maximized) {
			restore();
		} else {
			maximize();
		}
	}

	public void maximize() {
		rect2.setFill(Color.rgb(7, 102, 79));
		rect2.setWidth(257.0);
		restore_and_maximize.setText("🗗");
		Stage stage = (Stage) pane.getScene().getWindow();
		stage.setX(bounds.getMinX());
		stage.setY(bounds.getMinY());
		stage.setWidth(bounds.getWidth());
		stage.setHeight(bounds.getHeight());
		maximized = true;
	}

	public void restore() {
		rect2.setFill(Color.TRANSPARENT);
		rect2.setWidth(0.0);
		restore_and_maximize.setText("🗖");
		Stage stage = (Stage) pane.getScene().getWindow();
		stage.setX(bounds.getWidth() / 2.0);
		stage.setY(bounds.getMinY());
		stage.setWidth(bounds.getWidth() / 2.0);
		stage.setHeight(bounds.getHeight());
		maximized = false;
	}

	public void dragRestore() {
		rect2.setFill(Color.TRANSPARENT);
		rect2.setWidth(0.0);
		maximized = false;
		restore_and_maximize.setText("🗖");
		Stage primaryStage = (Stage) pane.getScene().getWindow();
		primaryStage.setWidth(1000);
		primaryStage.setHeight(600);
	}

	public void boundReleaseFunctions(MouseEvent event) {
		pane.getScene().setCursor(Cursor.DEFAULT);
		Stage stage = (Stage) pane.getScene().getWindow();
		if (event.getScreenX() < 1.0) {
			stage.setX(bounds.getMinX());
			stage.setY(bounds.getMinY());
			stage.setWidth(bounds.getWidth() / 2.0);
			stage.setHeight(bounds.getHeight());
		} else if (event.getScreenX() > bounds.getWidth() - 1.0) {
			restore();
		} else {
			if (event.getScreenY() < 1.0) {
				maximize();
			}
		}
	}

	public void dragWindow(MouseEvent event) {
		pane.getScene().setCursor(Cursor.HAND);
		Stage primaryStage = (Stage) pane.getScene().getWindow();
		primaryStage.setX(event.getScreenX() - x);
		if (event.getScreenY() < bounds.getHeight()-50.0) {
			primaryStage.setY(event.getScreenY() - y);
		}
	}

	public void beginDrag(MouseEvent event) {
		x = event.getSceneX();
		y = event.getSceneY();
		if (maximized) {
			dragRestore();
		}
	}
}
