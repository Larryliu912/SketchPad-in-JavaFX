package application.view;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;


public class SketchPadController {
	//import FXML element 
    @FXML
    private Pane Pad;
    @FXML
    private Pane Pad2;
    @FXML
    private Line LineSetting;
    @FXML
    private Slider WidthSetting;
    @FXML
    private ColorPicker ColorSetting;
    @FXML
    private Text TextSetting;
    @FXML
    private TextField Dashwidth;
    @FXML
    private Button SetDash;
    @FXML
    private ToggleButton LinearGradient;
    @FXML
    private ToggleButton RadialGradient;
    @FXML
    private Slider FontSize;
    @FXML
    private ChoiceBox<String> FontStyle;
    
    //import the local font library from local environment
    GraphicsEnvironment GetEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();  
    //Create a font list
    ObservableList<String> FontList = FXCollections.observableArrayList(GetEnv.getAvailableFontFamilyNames());
    
    //intialize all the variable
    Stage PrimaryStage;
    Stage aboutStage;
    Circle circle = new Circle();
    Rectangle rectangle = new Rectangle();
    Rectangle square = new Rectangle();
    Line line = new Line();
    Ellipse ellipse = new Ellipse();
    TextField textfield = new TextField();
    Rectangle rubber = new Rectangle();
    Group undogroup = new Group();
    Path path = new Path();
    Group TextGroup = new Group();
    Group EllipseGroup = new Group();
    Group PencilGroup = new Group();
    Group LineGroup = new Group();
    Group RectangleGroup = new Group();
    Group RubberGroup = new Group();
    Group CircleGroup = new Group();
    Line rubberw = new Line();
    String fontname;
    double orginx;
    double orginy;
    double orgSceneX;
    double orgSceneY;
    double orgTranslateX;
    double orgTranslateY;
    double EndX;
    double EndY;
    double size=16;
    int imgno = 0;
    int db = 1;
    int i = 0;
    int n = 0;
    MoveTo StartPoint = new MoveTo();
    
    //Two sets of canvas and graph group for two pads
    Rectangle[] canvas = new Rectangle[2];
    Group[] TabGroup = new Group[2];
    
    //Pencil Move
    EventHandler<MouseEvent> shapeOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                orgSceneX = t.getSceneX();
                orgSceneY = t.getSceneY();
                orgTranslateX = ((Shape) (t.getSource())).getTranslateX();
                orgTranslateY = ((Shape) (t.getSource())).getTranslateY();
            }
        };

    EventHandler<MouseEvent> shapeOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                double offsetX = t.getSceneX() - orgSceneX;
                double offsetY = t.getSceneY() - orgSceneY;
                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;
                ((Shape) (t.getSource())).setTranslateX(newTranslateX);
                ((Shape) (t.getSource())).setTranslateY(newTranslateY);
            }
        };

    //Line Resize and move
    EventHandler<MouseEvent> LineResizePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                	//Resize
                } else {
                	//Move
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Shape) (t.getSource())).getTranslateX();
                    orgTranslateY = ((Shape) (t.getSource())).getTranslateY();
                }
            }
        };

    EventHandler<MouseEvent> LineResizeDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                	//Resize
                    orginx = ((Line) t.getSource()).getStartX();
                    orginy = ((Line) t.getSource()).getStartY();
                    EndX = t.getX();
                    EndY = t.getY();
                    ((Line) t.getSource()).setEndX(EndX);
                    ((Line) t.getSource()).setEndY(EndY);
                } else {
                	//Move
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((Shape) (t.getSource())).setTranslateX(newTranslateX);
                    ((Shape) (t.getSource())).setTranslateY(newTranslateY);
                }
            }
        };

    //Circle Resize and move
    EventHandler<MouseEvent> CircleResizePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                } else {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Shape) (t.getSource())).getTranslateX();
                    orgTranslateY = ((Shape) (t.getSource())).getTranslateY();
                }
            }
        };

    EventHandler<MouseEvent> CircleResizeDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                    EndX = t.getX();
                    EndY = t.getY();
                    ((Circle) t.getSource()).setRadius(((EndX - orginx) > 0)
                        ? ((EndX - orginx) / 2) : ((orginx - EndX) / 2));
                } else {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((Shape) (t.getSource())).setTranslateX(newTranslateX);
                    ((Shape) (t.getSource())).setTranslateY(newTranslateY);
                }
            }
        };

    //Ellipse Resize and move
    EventHandler<MouseEvent> ElliResizePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                } else {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Shape) (t.getSource())).getTranslateX();
                    orgTranslateY = ((Shape) (t.getSource())).getTranslateY();
                }
            }
        };

    EventHandler<MouseEvent> ElliResizeDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                    EndX = t.getX();
                    EndY = t.getY();
                    ((Ellipse) t.getSource()).setRadiusX(((EndX - orginx) > 0)
                        ? ((EndX - orginx) / 2) : ((orginx - EndX) / 2));
                    ((Ellipse) t.getSource()).setRadiusY(((EndY - orginy) > 0)
                        ? ((EndY - orginy) / 2) : ((orginy - EndY) / 2));
                } else {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((Shape) (t.getSource())).setTranslateX(newTranslateX);
                    ((Shape) (t.getSource())).setTranslateY(newTranslateY);
                }
            }
        };

    //Square Resize and move
    EventHandler<MouseEvent> SquaResizePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                } else {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Shape) (t.getSource())).getTranslateX();
                    orgTranslateY = ((Shape) (t.getSource())).getTranslateY();
                }
            }
        };

    EventHandler<MouseEvent> SquaResizeDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                    orginx = ((Rectangle) t.getSource()).getX();
                    orginy = ((Rectangle) t.getSource()).getY();
                    EndX = t.getX();
                    EndY = t.getY();
                    ((Rectangle) t.getSource()).setWidth(((EndX - orginx) > 0)
                    		? (EndX - orginx) : (orginx - EndX));
                    ((Rectangle) t.getSource()).setHeight(((EndX - orginx) > 0)
                    		? (EndX - orginx) : (orginx - EndX));
                } else {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((Shape) (t.getSource())).setTranslateX(newTranslateX);
                    ((Shape) (t.getSource())).setTranslateY(newTranslateY);
                }
            }
        };

    //Rectangle Resize and move
    EventHandler<MouseEvent> RectResizePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                } else {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Shape) (t.getSource())).getTranslateX();
                    orgTranslateY = ((Shape) (t.getSource())).getTranslateY();
                }
            }
        };

    EventHandler<MouseEvent> RectResizeDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                    orginx = ((Rectangle) t.getSource()).getX();
                    orginy = ((Rectangle) t.getSource()).getY();
                    EndX = t.getX();
                    EndY = t.getY();
                    ((Rectangle) t.getSource()).setWidth(((EndX - orginx) > 0)
                        ? (EndX - orginx) : (orginx - EndX));
                    ((Rectangle) t.getSource()).setHeight(((EndY - orginy) > 0)
                        ? (EndY - orginy) : (orginy - EndY));
                } else {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((Shape) (t.getSource())).setTranslateX(newTranslateX);
                    ((Shape) (t.getSource())).setTranslateY(newTranslateY);
                }
            }
        };

    EventHandler<MouseEvent> textResizePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                } else {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((TextField) (t.getSource())).getTranslateX();
                    orgTranslateY = ((TextField) (t.getSource())).getTranslateY();
                }
            }
        };

    EventHandler<MouseEvent> textResizeDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.MIDDLE)) {
                    EndX = t.getX();
                    EndY = t.getY();
                    ((TextField) (t.getSource())).setPrefHeight(((EndY -
                        orginy) > 0) ? (EndY - orginy) : (orginy - EndY));
                    ((TextField) (t.getSource())).setPrefWidth(((EndX - orginx) > 0)
                        ? (EndX - orginx) : (orginx - EndX));
                } else {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((TextField) (t.getSource())).setTranslateX(newTranslateX);
                    ((TextField) (t.getSource())).setTranslateY(newTranslateY);
                }
            }
        };

    //Paint
    EventHandler<MouseEvent> paintOnMouseEventHander = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.PRIMARY)) {
                    if (t.getClickCount() == 2) {
                    	if (LinearGradient.selectedProperty().getValue().equals(true)){
                    		System.out.println(LinearGradient.selectedProperty());
                      	Stop[] stops = new Stop[] { new Stop(0,GradientPickerController.getfirst()), new Stop(1,GradientPickerController.getsecond())};
                    	LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
                         ((Shape) t.getSource()).setFill(lg);
                    	}
                    	else if (RadialGradient.selectedProperty().getValue().equals(true)){
                    		System.out.println(RadialGradient.selectedProperty());
                      	Stop[] stops = new Stop[] { new Stop(0,GradientPickerController.getfirst()), new Stop(1,GradientPickerController.getsecond())};
                      	RadialGradient rg = new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, stops);
                        ((Shape) t.getSource()).setFill(rg);

                    	}
                    	else{
                             ((Shape) t.getSource()).setFill(LineSetting.getStroke());
                    	}
                    }
                }
            }
        };

        
    //Gradient color picker page
    @FXML
    private void Gradient() throws IOException{
    	if(LinearGradient.selectedProperty().getValue().equals(true)||RadialGradient.selectedProperty().getValue().equals(true)){
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GradientPickerview.fxml"));
    	Parent root = fxmlLoader.load();
    	Stage stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setTitle("Gradient Colors Picker");
    	stage.setScene(new Scene(root, 500, 320));
    	stage.showAndWait();
    	}
    };
        
        
    public SketchPadController() {
    }
    
    //intialize
    @FXML
    private void initialize() {   
    	
    	//set the listener of font style bar(size slier and font name choice box)
    	FontStyle.setValue(GetEnv.getAvailableFontFamilyNames()[0]);
    	FontStyle.setItems(FontList);
    	FontStyle.getSelectionModel().selectedIndexProperty().addListener(
    	new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				fontname=FontList.get(newValue.intValue());
				TextSetting.setFont(new Font(FontList.get(newValue.intValue()),size));
				textfield.setFont(new Font(FontList.get(newValue.intValue()),size));
			}
    	});
    	FontSize.valueProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					size=newValue.doubleValue();
					TextSetting.setFont(new Font(fontname,size));
					textfield.setFont(new Font(fontname,size));
			}
    	});
    	
    	//intialize the two pads
        canvas[0] = new Rectangle(2 * Pad.getPrefWidth(),
                2 * Pad.getPrefHeight());
        canvas[1] = new Rectangle(2 * Pad2.getPrefWidth(),
                2 * Pad2.getPrefHeight());
    	LineSetting.getStrokeDashArray().add(1d);
        LineSetting.strokeWidthProperty().bind(WidthSetting.valueProperty());
        rubberw.strokeWidthProperty().bind(WidthSetting.valueProperty());
        canvas[0].setCursor(Cursor.CROSSHAIR);
        canvas[0].setFill(Color.WHITE);
        canvas[1].setCursor(Cursor.CROSSHAIR);
        canvas[1].setFill(Color.WHITE);
        TabGroup[0] = new Group();
        TabGroup[1] = new Group();
        TabGroup[0].getChildren().add(canvas[0]);
        Pad.getChildren().add(TabGroup[0]);
        TabGroup[1].getChildren().add(canvas[1]);
        Pad2.getChildren().add(TabGroup[1]);
    }

    @FXML
    private void FirstPad() {
    	//Switch the pad
        i = 0;
    }

    @FXML
    private void SecondPad() {
        i = 1;
    }
    
    //Dash Line setting
    @FXML
    public void setDash(){
    	double dashwid=Double.parseDouble(Dashwidth.getText());
    	if(dashwid>1){
    	LineSetting.getStrokeDashArray().clear();
    	LineSetting.getStrokeDashArray().addAll(dashwid,dashwid,dashwid,dashwid);
    	}
    	else{
        	LineSetting.getStrokeDashArray().clear();
        	LineSetting.getStrokeDashArray().addAll(1d,1d,1d,1d);
    	}
    };
    //Color Picker
    @FXML
    public void ColorSett() {
        Color c = ColorSetting.getValue();
        LineSetting.setStroke(Color.color(c.getRed(), c.getGreen(), c.getBlue()));
    }
    
    
    //Font Size funtion
    @FXML
    private void FontSize(){
    	textfield.setFont(new Font(FontSize.getValue()));

    }
    
    //Draw Pencil
    @FXML
    private void DrawPenc() {
        Group PencilGroup = new Group();
        System.out.println("Pencil");
        //Start to listen the mouse event
        canvas[i].setMouseTransparent(false);
        canvas[i].setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    path = new Path();
                    Context(PencilGroup);
                    path.setOnMousePressed(shapeOnMousePressedEventHandler);
                    path.setOnMouseDragged(shapeOnMouseDraggedEventHandler);
                    path.setOnMouseClicked(paintOnMouseEventHander);
                    //Check the dash value
                    if(LineSetting.getStrokeDashArray().get(0)!=0){
                    path.getStrokeDashArray().add(LineSetting.getStrokeDashArray().get(0));
                    }                    
                    path.setStrokeWidth(LineSetting.getStrokeWidth());
                    path.setStroke(LineSetting.getStroke());
                    PencilGroup.getChildren().add(path);
                    path.getElements().add(new MoveTo(me.getX(), me.getY()));
                }
            });

        canvas[i].setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    //Stop to listen the mouse event
                    canvas[i].setMouseTransparent(true);
                }
            });
        canvas[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    // keep shapes within canvas
                    if (canvas[i].getBoundsInLocal()
                                     .contains(me.getX(), me.getY())) {
                        path.getElements().add(new LineTo(me.getX(), me.getY()));
                    }
                }
            });
        //add pencil graph to graph group
        TabGroup[i].getChildren().add(PencilGroup);
    }

    //Draw Line
    @FXML
    private void DrawLine() {
        Group LineGroup = new Group();
        System.out.println("Line");
        canvas[i].setMouseTransparent(false);
        canvas[i].setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                	//create new line
                    line = new Line();
                    //set invisible
                    line.setVisible(false);
                    //add context menu
                    Context(LineGroup);
                    //add mouse event
                    line.setOnMouseClicked(paintOnMouseEventHander);
                    line.setOnMousePressed(LineResizePressedEventHandler);
                    line.setOnMouseDragged(LineResizeDraggedEventHandler);
                    if(LineSetting.getStrokeDashArray().get(0)!=0){
                    line.getStrokeDashArray().add(LineSetting.getStrokeDashArray().get(0));
                    }
                    //get start point
                    orginx = me.getX();
                    orginy = me.getY();
                    //gain the color and width of outline
                    line.setStrokeWidth(LineSetting.getStrokeWidth());
                    line.setStroke(LineSetting.getStroke());
                    //add line in group
                    LineGroup.getChildren().add(line);
                }
            });

        canvas[i].setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    canvas[i].setMouseTransparent(true);
                }
            });
        canvas[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    // keep shapes within canvas
                    if (canvas[i].getBoundsInLocal()
                                     .contains(me.getX(), me.getY())) {
                    	//set visible
                        line.setVisible(true);
                        //gain end point
                        EndX = me.getX();
                        EndY = me.getY();
                        //draw line
                        line.setStartX(orginx);
                        line.setStartY(orginy);
                        line.setEndX(EndX);
                        line.setEndY(EndY);
                    }
                }
            });
        //add linegroup in graph group
        TabGroup[i].getChildren().add(LineGroup);
    }

    //Draw Square the structure of the below code is similar to the code of Draw line
    @FXML
    private void DrawSqua() {
        Group SquareGroup = new Group();
        System.out.println("Square");
        canvas[i].setMouseTransparent(false);
        canvas[i].setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    orginx = me.getX();
                    orginy = me.getY();
                    Context(SquareGroup);
                    square = new Rectangle();
                    square.setVisible(false);
                    square.setOnMousePressed(SquaResizePressedEventHandler);
                    square.setOnMouseDragged(SquaResizeDraggedEventHandler);
                    square.setOnMouseClicked(paintOnMouseEventHander);
                    if(LineSetting.getStrokeDashArray().get(0)!=0){
                    square.getStrokeDashArray().add(LineSetting.getStrokeDashArray().get(0));
                    }                    
                    square.setStrokeWidth(LineSetting.getStrokeWidth());
                    square.setStroke(LineSetting.getStroke());
                    SquareGroup.getChildren().add(square);
                }
            });

        canvas[i].setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    canvas[i].setMouseTransparent(true);
                }
            });
        canvas[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    // keep lines within rectangle
                    if (canvas[i].getBoundsInLocal()
                                     .contains(me.getX(), me.getY())) {
                        square.setVisible(true);
                        EndX = me.getX();
                        EndY = me.getY();
                        square.setX((orginx < EndX) ? orginx : EndX);
                        square.setY((orginy < EndY) ? orginy : EndY);
                        square.setWidth(((EndX - orginx) > 0) ? (EndX - orginx)
                                                              : (orginx - EndX));
                        square.setHeight(((EndX - orginx) > 0) ? (EndX -
                            orginx) : (orginx - EndX));
                        square.setFill(null);
                    }
                }
            });
        TabGroup[i].getChildren().add(SquareGroup);
    }

    //Draw Rectangle
    @FXML
    private void DrawRect() {
        Group RectangleGroup = new Group();
        System.out.println("Rectangle");
        canvas[i].setMouseTransparent(false);
        canvas[i].setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    orginx = me.getX();
                    orginy = me.getY();
                    rectangle = new Rectangle();
                    rectangle.setVisible(false);
                    Context(RectangleGroup);
                    rectangle.setOnMousePressed(RectResizePressedEventHandler);
                    rectangle.setOnMouseDragged(RectResizeDraggedEventHandler);
                    rectangle.setOnMouseClicked(paintOnMouseEventHander);
                    if(LineSetting.getStrokeDashArray().get(0)!=0){
                    rectangle.getStrokeDashArray().add(LineSetting.getStrokeDashArray().get(0));
                    }                    
                    rectangle.setStrokeWidth(LineSetting.getStrokeWidth());
                    rectangle.setStroke(LineSetting.getStroke());
                    RectangleGroup.getChildren().add(rectangle);
                }
            });

        canvas[i].setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    canvas[i].setMouseTransparent(true);
                }
            });
        canvas[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    // keep lines within rectangle
                    if (canvas[i].getBoundsInLocal()
                                     .contains(me.getX(), me.getY())) {
                        EndX = me.getX();
                        EndY = me.getY();
                        rectangle.setVisible(true);
                        rectangle.setX((orginx < EndX) ? orginx : EndX);
                        rectangle.setY((orginy < EndY) ? orginy : EndY);
                        rectangle.setWidth(((EndX - orginx) > 0)
                            ? (EndX - orginx) : (orginx - EndX));
                        rectangle.setHeight(((EndY - orginy) > 0)
                            ? (EndY - orginy) : (orginy - EndY));
                        rectangle.setFill(null);
                    }
                }
            });
        TabGroup[i].getChildren().add(RectangleGroup);
    }

    //Draw Circle
    @FXML
    private void DrawCirc() {
        Group CircleGroup = new Group();
        System.out.println("Circle");
        canvas[i].setMouseTransparent(false);
        canvas[i].setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    circle = new Circle();
                    circle.setVisible(false);
                    Context(CircleGroup);
                    circle.setOnMouseClicked(paintOnMouseEventHander);
                    circle.setOnMousePressed(CircleResizePressedEventHandler);
                    circle.setOnMouseDragged(CircleResizeDraggedEventHandler);
                    if(LineSetting.getStrokeDashArray().get(0)!=0){
                    circle.getStrokeDashArray().add(LineSetting.getStrokeDashArray().get(0));
                    }                    
                    orginx = me.getX();
                    orginy = me.getY();
                    circle.setStrokeWidth(LineSetting.getStrokeWidth());
                    circle.setStroke(LineSetting.getStroke());
                    CircleGroup.getChildren().add(circle);
                }
            });

        canvas[i].setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    canvas[i].setMouseTransparent(true);
                }
            });
        canvas[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    // keep lines within rectangle
                    if (canvas[i].getBoundsInLocal()
                                     .contains(me.getX(), me.getY())) {
                        circle.setVisible(true);
                        EndX = me.getX();
                        EndY = me.getY();
                        circle.setCenterX((orginx + EndX) / 2);
                        circle.setCenterY((orginy + EndY) / 2);
                        circle.setRadius(((EndX - orginx) > 0)
                            ? ((EndX - orginx) / 2) : ((orginx - EndX) / 2));
                        circle.setFill(null);
                    }
                }
            });
        TabGroup[i].getChildren().add(CircleGroup);
    }

    //Draw Ellipse
    @FXML
    private void DrawElli() {
        Group EllipseGroup = new Group();
        System.out.println("Ellipse");
        canvas[i].setMouseTransparent(false);
        canvas[i].setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    ellipse = new Ellipse();
                    Context(EllipseGroup);
                    ellipse.setOnMousePressed(ElliResizePressedEventHandler);
                    ellipse.setOnMouseDragged(ElliResizeDraggedEventHandler);
                    ellipse.setOnMouseClicked(paintOnMouseEventHander);
                    if(LineSetting.getStrokeDashArray().get(0)!=0){
                    ellipse.getStrokeDashArray().add(LineSetting.getStrokeDashArray().get(0));
                    }                    
                    orginx = me.getX();
                    orginy = me.getY();
                    ellipse.setVisible(false);

                    ellipse.setStrokeWidth(LineSetting.getStrokeWidth());
                    ellipse.setStroke(LineSetting.getStroke());
                    EllipseGroup.getChildren().add(ellipse);
                }
            });

        canvas[i].setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    canvas[i].setMouseTransparent(true);
                }
            });
        canvas[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    // keep lines within rectangle
                    if (canvas[i].getBoundsInLocal()
                                     .contains(me.getX(), me.getY())) {
                        EndX = me.getX();
                        EndY = me.getY();
                        ellipse.setVisible(true);
                        ellipse.setCenterX((orginx + EndX) / 2);
                        ellipse.setCenterY((orginy + EndY) / 2);
                        ellipse.setRadiusX(((EndX - orginx) > 0)
                            ? ((EndX - orginx) / 2) : ((orginx - EndX) / 2));
                        ellipse.setRadiusY(((EndY - orginy) > 0)
                            ? ((EndY - orginy) / 2) : ((orginy - EndY) / 2));
                        ellipse.setFill(null);
                    }
                }
            });
        TabGroup[i].getChildren().add(EllipseGroup);
    }

    //TextField
    @FXML
    private void Textfield() {
        Group TextGroup = new Group();
        System.out.println("Textfield");
        canvas[i].setMouseTransparent(false);
        canvas[i].setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    textfield = new TextField();
                    textfield.setOnMousePressed(textResizePressedEventHandler);
                    textfield.setOnMouseDragged(textResizeDraggedEventHandler);
                    orginx = me.getX();
                    orginy = me.getY();
                    textfield.setPrefSize(0, 0);
                    textfield.setVisible(false);
                    textfield.setPromptText("Enter Text");
                    TextGroup.getChildren().add(textfield);
                }
            });

        canvas[i].setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    canvas[i].setMouseTransparent(true);
                }
            });
        canvas[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    // keep lines within rectangle
                    if (canvas[i].getBoundsInLocal()
                                     .contains(me.getX(), me.getY())) {
                        EndX = me.getX();
                        EndY = me.getY();
                        textfield.setVisible(true);
                        textfield.setFont(TextSetting.getFont());
                        textfield.setLayoutX((orginx < EndX) ? orginx : EndX);
                        textfield.setLayoutY((orginy < EndY) ? orginy : EndY);
                        textfield.setPrefHeight(((EndY - orginy) > 0)
                            ? (EndY - orginy) : (orginy - EndY));
                        textfield.setPrefWidth(((EndX - orginx) > 0)
                            ? (EndX - orginx) : (orginx - EndX));
                    }
                }
            });
        TabGroup[i].getChildren().add(TextGroup);
    }

    //Rubber similar to pencil
    @FXML
    private void Rubber() {
        rubberw.setStroke(Color.WHITE);

        Group RubberGroup = new Group();
        System.out.println("Rubber");
        canvas[i].setMouseTransparent(false);
        canvas[i].setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    path = new Path();
                    path.setStrokeWidth(rubberw.getStrokeWidth());
                    path.setStroke(rubberw.getStroke());
                    RubberGroup.getChildren().add(path);
                    path.getElements().add(new MoveTo(me.getX(), me.getY()));
                }
            });

        canvas[i].setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    path = new Path();
                }
            });
        canvas[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    // keep lines within rectangle
                    if (canvas[i].getBoundsInLocal()
                                     .contains(me.getX(), me.getY())) {
                        path.getElements().add(new LineTo(me.getX(), me.getY()));
                    }
                }
            });
        TabGroup[i].getChildren().add(RubberGroup);
    }

    //Clear
    @FXML
    private void Clear() {
        System.out.println("Clear");
        //clear the graph group
        TabGroup[i].getChildren().clear();
        //add white canvas into graph group
        canvas[i].setFill(Color.WHITE);
        TabGroup[i].getChildren().add(canvas[i]);
    }

    //About
    @FXML
    private void about() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("About Move Resize and Fill");
        alert.setContentText(
            "You can move the shapes and textfields by pressing their sides and dragging mouse,if you click shapes by middle mouse, you can resize them, and if you double click them, they will be filled by the color you chose.");
        alert.showAndWait();
    }

    //Save
    @FXML
    private void save() {
        imgno++;
        //snap shot the graph group for writing in .png file
        WritableImage image = TabGroup[i].snapshot(new SnapshotParameters(),
                null);
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("\u4fdd\u5b58\u56fe\u7247");
            fileChooser.setInitialFileName("IMG_" + imgno + ".png");

            File file = fileChooser.showSaveDialog(PrimaryStage);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Load
    @FXML
    private void load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("\u4fdd\u5b58\u56fe\u7247");
        fileChooser.getExtensionFilters()
                   .addAll(new ExtensionFilter("Image", "*.png", "*.jpg",
                "*.jpeg"));
        File file = fileChooser.showOpenDialog(PrimaryStage);
        if (file != null) {
            try {
                Image image = new Image(new FileInputStream(file));
                ImageView imageread = new ImageView(image);
                TabGroup[i].getChildren().clear();
                canvas[i].setFill(Color.TRANSPARENT);
                TabGroup[i].getChildren().addAll(imageread, canvas[i]);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    //Mouse stop all listener
    @FXML
    private void move() {
        canvas[i].setMouseTransparent(true);
    }

    //Context Menu
    private void Context(Node node) {
        final ContextMenu contextmenu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    undogroup.getChildren().add(node);
                    TabGroup[i].getChildren().remove(node);
                }
            });
        contextmenu.getItems().add(delete);
        node.addEventHandler(MouseEvent.MOUSE_CLICKED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                	//right button
                    if (t.getButton().equals(MouseButton.SECONDARY)) {
                        contextmenu.show(node, t.getScreenX(), t.getScreenY());
                    }
                }
            });
    }

    //Undo
    @FXML
    private void Undo() {
        int size = TabGroup[i].getChildren().size() - 1;
        if (size > 0) {
            try {
                undogroup.getChildren().add(TabGroup[i].getChildren().get(size));
                TabGroup[i].getChildren().remove(size);
            } catch (RuntimeException e) {
            }
        }
    }

    //Redo
    @FXML
    private void Redo() {
        int undocount = undogroup.getChildren().size() - 1;
        if (undocount >= 0) {
            TabGroup[i].getChildren().add(undogroup.getChildren().get(undocount));
        }
    }
}
