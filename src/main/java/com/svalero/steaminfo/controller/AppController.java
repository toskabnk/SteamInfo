package com.svalero.steaminfo.controller;

import com.svalero.steaminfo.model.Game;
import com.svalero.steaminfo.model.Player;
import com.svalero.steaminfo.model.ResponseVanityURL;
import com.svalero.steaminfo.model.appDetails.IDApp;
import com.svalero.steaminfo.task.GameInfoTask;
import com.svalero.steaminfo.task.GameListTask;
import com.svalero.steaminfo.task.UserInfoTask;
import com.svalero.steaminfo.task.VanityConverterTask;
import io.reactivex.functions.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Value;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AppController {
    @FXML
    public TextField idField;
    @FXML
    public TextField idUser;
    @FXML
    public TextField vanityURL;
    @FXML
    public TextField steamID;
    @FXML
    public Button btSend;
    @FXML
    public Button btSend1;
    @FXML
    public Button btConvert;
    @FXML
    public TextArea textArea;
    @FXML
    public ListView textArea1;
    @FXML
    public TextArea textArea2;
    private String API_KEY = loadApiKey();
    public GameInfoTask gameInfoTask;
    public GameListTask gameListTask;
    public UserInfoTask userInfoTask;
    public VanityConverterTask vanityConverterTask;

    @FXML
    public void searchsteamapp(ActionEvent actionEvent){

        Integer idgame = Integer.parseInt(idField.getText());
        System.out.println(idgame);
        this.idField.setText("");
        this.textArea.setText("");

        Consumer<Map<String, IDApp>> mapConsumer = (test) -> {
            String previousText;
            previousText = textArea.getText() + "\n";
            Thread.sleep(10);
            for (Map.Entry<String, IDApp> entry : test.entrySet()) {
                textArea.setText(previousText + "Key = " + entry.getKey() + ", Value = " + entry.getValue());
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

            }
        };

        this.gameInfoTask = new GameInfoTask(idgame, mapConsumer);
        new Thread(gameInfoTask).start();
    }

    @FXML
    public void searchSteamID(ActionEvent actionEvent){
        Long userid = Long.parseLong(idUser.getText());
        System.out.println(userid);
        this.idUser.setText("");
        this.textArea2.setText("");


        Consumer<List<Game>> gameConsumer = (test1) -> {
            ObservableList<Game> games = FXCollections.observableArrayList();
            games.addAll(test1);
            textArea1.setFixedCellSize(40);
            textArea1.setItems(games);
        };

        this.gameListTask = new GameListTask(API_KEY, userid, gameConsumer, true);
        new Thread(gameListTask).start();


        Consumer<List<Player>> playerConsumer = (test2) -> {
            Thread.sleep(10);
            for (Player player:test2){
                textArea2.setText(textArea2.getText() + player.toString() + "\n");
            }

        };

        this.userInfoTask = new UserInfoTask(playerConsumer, API_KEY, userid);
        new Thread(userInfoTask).start();


    }
    @FXML
    public void convertVanity(ActionEvent actionEvent){
        String vanityURL = this.vanityURL.getText();
        System.out.println(vanityURL);
        this.vanityURL.setText("");
        this.steamID.setText("");

        Consumer<ResponseVanityURL> vanityConsumer = (test3) -> {
            System.out.println(test3.getSteamid());
            this.steamID.setText(String.valueOf(test3.getSteamid()));
        };

        this.vanityConverterTask = new VanityConverterTask(API_KEY, vanityURL, vanityConsumer);
        new Thread(vanityConverterTask).start();
    }

    public String loadApiKey(){
        Properties props = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                props.load(inputStream);
            } else {
                throw new FileNotFoundException("Archivo de propiedades 'config.properties' no encontrado en el classpath");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de propiedades 'config.properties'", e);
        }
        String apiKey = props.getProperty("api.key");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new RuntimeException("La propiedad 'api.key' no se encuentra definida en el archivo de propiedades 'config.properties'");
        }
        return apiKey;
    }
}
