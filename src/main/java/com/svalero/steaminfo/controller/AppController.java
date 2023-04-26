package com.svalero.steaminfo.controller;

import com.svalero.steaminfo.model.AllData;
import com.svalero.steaminfo.model.Game;
import com.svalero.steaminfo.model.Player;
import com.svalero.steaminfo.model.ResponseVanityURL;
import com.svalero.steaminfo.model.appDetails.IDApp;
import com.svalero.steaminfo.task.*;
import com.svalero.steaminfo.util.R;
import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;

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
    public Button btSend = new Button();
    @FXML
    public ProgressBar pgBar;
    @FXML
    public Text txtBar;
    @FXML
    public Text txtBar1;
    private LoadingDataTask loadingDataTask;
    private JSONArray message;
    private boolean first = true;
    @FXML
    public void calculate(ActionEvent actionEvent) throws InterruptedException {
        loadingDataTask = new LoadingDataTask(idField.getText());
        pgBar.progressProperty().unbind();
        pgBar.progressProperty().bind(loadingDataTask.progressProperty());
        btSend.setDisable(true);

        loadingDataTask.stateProperty().addListener((observableValue, oldState, newState) -> {
            if(newState == Worker.State.SUCCEEDED){
                btSend.setDisable(false);
            } else if(newState == Worker.State.FAILED){
                btSend.setDisable(false);
            } else if(newState == Worker.State.CANCELLED){
                btSend.setDisable(false);
            }
        });

        loadingDataTask.messageProperty().addListener((observableValue, oldValue, newValue) -> {
            if(first){
                first = false;
            } else {
                message = new JSONArray(newValue);
                txtBar.setText(message.get(0).toString());
                txtBar1.setText(message.get(1).toString());
            }

            if(message.get(1).toString().equals("Error")){
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("SteamID not found");
                alert.setContentText("SteamID not found, please, check the ID or VanityURL");

                alert.showAndWait();
            }
        });

        new Thread(loadingDataTask).start();
    }
}
