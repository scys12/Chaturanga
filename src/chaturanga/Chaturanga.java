package chaturanga;

import chaturanga.gui.Menu;
import chaturanga.gui.Table;
import chaturanga.sound.Sound;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Chaturanga extends Application{
    private static Font font;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        Pane root = new Pane();
        root.setPrefSize(860,  700);

        try (InputStream is = Files.newInputStream(Paths.get("src/art/chess.jpg"));
             InputStream fontStream = Files.newInputStream(Paths.get("src/art/chess.ttf"))) {
            ImageView img = new ImageView(new Image(is));
            img.setFitWidth(860);
            img.setFitHeight(700);
            root.getChildren().add(img);
            font = Font.loadFont(fontStream, 40);
        }
        catch (IOException e) {
            System.out.println("Couldn't load image");
        }

        MediaPlayer a =new MediaPlayer(new Media(new File("src/art/menu-back-sound.wav").toURI().toString()));
        a.setOnEndOfMedia(new Runnable() {
            public void run() {
                a.seek(Duration.ZERO);
            }
        });
        a.play();

        Title title = new Title("C H A T U R A N G A");
        title.setTranslateX(75);
        title.setTranslateY(200);

        MenuItem itemExit = new MenuItem("EXIT");
        itemExit.setOnMouseClicked(event -> System.exit(0));

        MenuItem vsHuman = new MenuItem("NEW GAME[VS HUMAN]");
        vsHuman.setOnMouseClicked(event -> {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Table.get(1).show();
                }
            });
            a.stop();
            primaryStage.hide();
        });

        MenuItem vsComp = new MenuItem("NEW GAME[VS COMPUTER]");
        vsComp.setOnMouseClicked(event -> {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Table.get(2).show();
                }
            });
            a.stop();
            primaryStage.hide();
        });

        MenuBox menu = new MenuBox(
                vsHuman,
                vsComp,
                itemExit);
        menu.setTranslateX(100);
        menu.setTranslateY(300);

        root.getChildren().addAll(title, menu);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Chaturanga");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static class Title extends StackPane {
        public Title(String name) {
            Rectangle bg = new Rectangle(500, 60);
            bg.setStroke(Color.WHITE);
            bg.setStrokeWidth(2);
            bg.setFill(null);

            Text text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(font);

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);
        }
    }

    public static class MenuBox extends VBox {
        public MenuBox(MenuItem... items) {
            getChildren().add(createSeparator());

            for (MenuItem item : items) {
                getChildren().addAll(item, createSeparator());
            }
        }

        private Line createSeparator() {
            Line sep = new Line();
            sep.setEndX(200);
            sep.setStroke(Color.DARKGREY);
            return sep;
        }
    }

    public static class MenuItem extends StackPane {
        public MenuItem(String name) {
            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop[] {
                    new Stop(0, Color.DARKVIOLET),
                    new Stop(0.1, Color.BLACK),
                    new Stop(0.9, Color.BLACK),
                    new Stop(1, Color.DARKVIOLET)
            });

            Rectangle bg = new Rectangle(200, 30);
            bg.setOpacity(0.4);

            Text text = new Text(name);
            text.setFill(Color.DARKGREY);
            text.setFont(Font.font("Tw Cen MT Condensed", FontWeight.SEMI_BOLD, 22));

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
                Sound.playSound("src/art/hover.wav");
                bg.setFill(gradient);
                text.setFill(Color.WHITE);
            });


            setOnMouseExited(event -> {
                bg.setFill(Color.BLACK);
                text.setFill(Color.DARKGREY);
            });

            setOnMousePressed(event -> {
                Sound.playSound("src/art/clickmenu.wav");
                bg.setFill(Color.DARKVIOLET);
            });

            setOnMouseReleased(event -> {
                bg.setFill(gradient);
            });
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}