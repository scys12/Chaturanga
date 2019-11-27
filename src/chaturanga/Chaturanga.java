package chaturanga;

import chaturanga.gui.Table;

//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import javafx.animation.TranslateTransition;
//import javafx.application.Application;
//import javafx.geometry.*;
//import javafx.scene.*;
//import javafx.scene.image.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.*;
//import javafx.scene.shape.*;
//import javafx.scene.text.*;
//import javafx.scene.input.KeyCode;
//import javafx.scene.effect.DropShadow;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
public class Chaturanga {
//
//    private static Font font;
//    private MenuBox menu;
//
//    private Parent createContent() {
//        Pane root = new Pane();
//        root.setPrefSize(800, 600);
//
//        try (InputStream is = Files.newInputStream(Paths.get("src/art/chess.jpg"));
//             InputStream fontStream = Files.newInputStream(Paths.get("src/art/chess.ttf"))) {
//            ImageView img = new ImageView(new Image(is));
//            img.setFitWidth(1066);
//            img.setFitHeight(600);
//
//            root.getChildren().add(img);
//
//            font = Font.loadFont(fontStream, 30);
//        }
//        catch (IOException e) {
//            System.out.println("Couldn't load image or font");
//        }
//
//        MenuItem itemQuit = new MenuItem("QUIT");
//        itemQuit.setOnMouseClicked(event -> System.exit(0));
//
//        menu = new MenuBox("CAMPAIGN",
//                new MenuItem("NEW GAME"),
//                itemQuit);
//
//        root.getChildren().add(menu);
//        return root;
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        Scene scene = new Scene(createContent());
//        scene.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ESCAPE) {
//                if (menu.isVisible()) {
//                    menu.hide();
//                }
//                else {
//                    menu.show();
//                }
//            }
//        });
//        primaryStage.setTitle("Chaturanga");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    private static class MenuBox extends StackPane {
//        public MenuBox(String title, MenuItem... items) {
//        }
//
//        public void show() {
//            setVisible(true);
//
//        }
//
//        public void hide() {
//        }
//    }
//
//    private static class MenuItem extends StackPane {
//        public MenuItem(String name) {
//            Rectangle bg = new Rectangle(300, 24);
//        }
//    }

    public static void main(String[] args) {
        Table table = new Table();

    }
}