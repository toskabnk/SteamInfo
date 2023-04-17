package com.svalero.steaminfo.controller;

import com.svalero.steaminfo.model.AllData;
import com.svalero.steaminfo.model.Game;
import com.svalero.steaminfo.model.Player;
import com.svalero.steaminfo.model.ResponseVanityURL;
import com.svalero.steaminfo.model.appDetails.IDApp;
import com.svalero.steaminfo.task.GameInfoTask;
import com.svalero.steaminfo.task.GameListTask;
import com.svalero.steaminfo.task.UserInfoTask;
import com.svalero.steaminfo.task.VanityConverterTask;
import com.svalero.steaminfo.util.R;
import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AppController {
    @FXML
    public TextField idField;
    @FXML
    public Button btSend;
    @FXML
    public ProgressBar pgBar;
    @FXML
    public Text txtBar;

    private String API_KEY = loadApiKey();
    public GameInfoTask gameInfoTask;
    public GameListTask gameListTask;
    public UserInfoTask userInfoTask;
    public VanityConverterTask vanityConverterTask;

    private String idSteam;
    private boolean correct;
    private Player player;
    private List<Game> games;


    @FXML
    public void calculate(ActionEvent actionEvent) throws InterruptedException {
        correct = true;
        player = null;
        games = new ArrayList<>();
        idSteam = "";
        String id = idField.getText();
        String url = "https://steamcommunity.com/id/";
        pgBar.setVisible(true);
        txtBar.setText("Getting your info...");

        if(id.contains(url)){
            int index = id.indexOf(url) + url.length();
            String idFormatted = id.substring(index);

            if(idFormatted.endsWith("/")){
                idFormatted = idFormatted.substring(0, idFormatted.length() -1);
            }

            checkVanityURL(idFormatted);
            pgBar.setProgress(10.);
        } else {
            boolean isIdNumber;
            Long userID = null;

            try{
                userID = Long.parseLong(id);
                isIdNumber = true;
                idSteam = id;

            }
            catch (NumberFormatException nfe){
                isIdNumber = false;
            }

            if(isIdNumber){
                Consumer<List<Player>> playerConsumer = (userAPI) -> {
                    if(userAPI.isEmpty()){
                        correct = false;
                    } else {
                        correct = true;
                        for (Player p:userAPI){
                            player = p;
                        }
                    }
                    pgBar.setProgress(30.);
                };

                this.userInfoTask = new UserInfoTask(playerConsumer, API_KEY, userID);
                new Thread(userInfoTask).start();
            } else {
                checkVanityURL(id);
                pgBar.setProgress(30.);
            }
        }

        while (correct && idSteam.length()==0){
            Thread.sleep(1000);
            System.out.print(correct);
            System.out.println(idSteam.length());
        }

        Alert alert;
        if(!correct){
            pgBar.setProgress(100.);
            txtBar.setText("SteamID not found");
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("SteamID not found");
            alert.setContentText("SteamID not found, please, check the ID or VanityURL");

            alert.showAndWait();

        } else {
            if (player == null) {
                Consumer<List<Player>> playerConsumer = (userAPI) -> {
                    if(userAPI.isEmpty()){
                        correct = false;
                    } else {
                        correct = true;
                        for (Player p:userAPI){
                            player = p;
                        }
                    }
                };

                this.userInfoTask = new UserInfoTask(playerConsumer, API_KEY, Long.parseLong(idSteam));
                new Thread(userInfoTask).start();
            }

            Consumer<List<Game>> gameConsumer = (gameApi) -> {
                games.addAll(gameApi);
            };

            this.gameListTask = new GameListTask(API_KEY, Long.parseLong(idSteam), gameConsumer, true);
            new Thread(gameListTask).start();
            txtBar.setText("Getting your games...");
            pgBar.setProgress(40.);

            while (games.isEmpty() || player == null){
                if(pgBar.getProgress() <= 99.){
                    pgBar.setProgress(pgBar.getProgress() + 1.);
                    Thread.sleep(100);
                }
            }
            pgBar.setProgress(100.);
            AllData allData = new AllData(player, games);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(R.getUI("userInfo.fxml"));
                    loader.setController(new UserInfoController(allData));
                    VBox vbox = null;
                    try {
                        vbox = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Scene scene = new Scene(vbox);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("UserInfo");
                    stage.show();
                }
            });
        }
    }

    private void checkVanityURL(String idFormatted) {
        Consumer<ResponseVanityURL> vanityConsumer = (vanityAPI) -> {
            if(vanityAPI.getSuccess() == 1){
                correct = true;
                idSteam = vanityAPI.getSteamid();
            } else {
                correct = false;
            }
        };

        this.vanityConverterTask = new VanityConverterTask(API_KEY, idFormatted, vanityConsumer);
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
