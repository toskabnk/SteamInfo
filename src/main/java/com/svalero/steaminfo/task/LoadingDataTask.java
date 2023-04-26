package com.svalero.steaminfo.task;

import com.svalero.steaminfo.controller.UserInfoController;
import com.svalero.steaminfo.model.AllData;
import com.svalero.steaminfo.model.Game;
import com.svalero.steaminfo.model.Player;
import com.svalero.steaminfo.model.ResponseVanityURL;
import com.svalero.steaminfo.util.R;
import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LoadingDataTask extends Task<Integer> {
    private JSONArray message = new JSONArray();;
    private String id;
    private String API_KEY = loadApiKey();
    private GameListTask gameListTask;
    private UserInfoTask userInfoTask;
    private VanityConverterTask vanityConverterTask;
    private String idSteam;
    private boolean correct;
    private Player player;
    private List<Game> games = new ArrayList<>();


    public LoadingDataTask(String id){
        this.id = id;
    }


    @Override
    protected Integer call() throws Exception {
        message.put(0, "");
        message.put(1, "");
        String url = "https://steamcommunity.com/id/";


        //Si contiene la URL
        if(id.contains(url)){
            //Actualizamos el mensaje
            message.put(0, "Checking URL...");
            updateMessage(String.valueOf(message));

            //Eliminamos toda la parte de la URL y nos quedamos el ID
            int index = id.indexOf(url) + url.length();
            String idFormatted = id.substring(index);

            //Si el ID acaba en / lo quitamos
            if(idFormatted.endsWith("/")){
                idFormatted = idFormatted.substring(0, idFormatted.length() -1);
            }

            //Comprobamos que sea correcto
            checkVanityURL(idFormatted);
            updateProgress(10, 100);
        } else {
            //Actualizamos el mensaje
            message.put(0, "Checking ID...");
            updateMessage(String.valueOf(message));
            boolean isIdNumber;
            Long userID = null;

            //Comprobamos que es un Long
            try{
                userID = Long.parseLong(id);
                isIdNumber = true;
                idSteam = id;

            }
            catch (NumberFormatException nfe){
                isIdNumber = false;
            }

            //Si es un numero, comprobamos que corresponde a algun usuario de Steam, si no, comprobamos si tiene un ID personalizado
            if(isIdNumber){
                message.put(0, "Getting player info...");
                updateMessage(String.valueOf(message));
                Consumer<List<Player>> playerConsumer = (userAPI) -> {
                    if(userAPI.isEmpty()){
                        correct = false;
                    } else {
                        correct = true;
                        for (Player p:userAPI){
                            player = p;
                        }
                    }
                    updateProgress(10, 100);
                };

                this.userInfoTask = new UserInfoTask(playerConsumer, API_KEY, userID);
                Thread userInfo = new Thread(userInfoTask);
                userInfo.start();
                message.put(1, "Searching...");
                while (userInfo.isAlive()){
                }
            } else {
                message.put(0, "Checking Vanity..");
                updateMessage(String.valueOf(message));
                checkVanityURL(id);
                updateProgress(20, 100);
            }
        }

        //Bucle hasta comprobar que el ID corresponde a un jugador
        while (correct && idSteam.length()==0){
            Thread.sleep(300);
            System.out.print(correct);
            System.out.println(idSteam.length());
        }

        //Si no es correcto mostramos un mensaje de error
        if(!correct){
            updateProgress(100, 100);
            message.put(0, "SteamID not found");
            message.put(1, "Error");
            updateMessage(String.valueOf(message));

            //Si es correcto conseguimos los datos del jugador
        } else {
            //Actualizamos el mensaje
            message.put(0, "Getting player info...");
            updateMessage(String.valueOf(message));

            //Si no tenemos info del jugador, intentamos obtenerla
            if (player == null) {
                Consumer<List<Player>> playerConsumer = (userAPI) -> {
                    if(userAPI.isEmpty()){
                        correct = false;
                    } else {
                        updateProgress(20, 100);
                        correct = true;
                        for (Player p:userAPI){
                            player = p;
                        }
                    }
                };

                this.userInfoTask = new UserInfoTask(playerConsumer, API_KEY, Long.parseLong(idSteam));
                Thread userThread = new Thread(userInfoTask);
                userThread.start();
                while (userThread.isAlive()){

                }
            }

            //Actualizamos el progreso y el mensaje
            updateProgress(30, 100);
            message.put(0, "Getting your games...");
            updateMessage(String.valueOf(message));

            //Obtenemos la lista de juegos
            Consumer<Game> gameConsumer = (gameApi) -> {
                games.add(gameApi);
                Thread.sleep(20);
            };

            this.gameListTask = new GameListTask(API_KEY, Long.parseLong(idSteam), gameConsumer, true);
            Thread thread = new Thread(gameListTask);
            thread.start();

            Double update = 30.;
            while (thread.isAlive()){
                Thread.sleep(100);
                String textString = games.size() + " games loaded";
                message.put(1, textString);
                updateMessage(String.valueOf(message));
                if(update < 90){
                    update = update + 0.1;
                    update++;
                    updateProgress(update, 100);
                }
            }

            updateProgress(100, 1);
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
        return null;
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
        Thread thread = new Thread(vanityConverterTask);
        thread.start();
        while (thread.isAlive()){

        }
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
