package com.svalero.steaminfo.controller;

import com.svalero.steaminfo.model.AllData;
import com.svalero.steaminfo.model.Game;
import com.svalero.steaminfo.model.Player;
import com.svalero.steaminfo.model.appDetails.IDApp;
import com.svalero.steaminfo.task.GameInfoTask;
import com.svalero.steaminfo.util.R;
import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class UserInfoController implements Initializable {
    private Player player;
    private List<Game> ownedGames = new ArrayList<>();

    @FXML
    private Text username;
    @FXML
    private Text tIdUser;
    @FXML
    private Text tOwnedGames;
    @FXML
    private Text tHours;
    @FXML
    private TextField tfSearch;
    @FXML
    private ImageView userAvatar;
    @FXML
    private ListView<Game> gameList;

    private GameInfoTask gameInfoTask;
    private IDApp idApp = null;
    ObservableList<Game> gamesListObservable;
    ObservableList<Game> gamesListObservableCopy;




    public UserInfoController(AllData allData){
        player = allData.getPlayer();
        ownedGames.addAll(allData.getGames());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gamesListObservable = FXCollections.observableArrayList();
        gamesListObservableCopy = FXCollections.observableArrayList();
        try {
            setImage(player.getAvatarfull());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        username.setText(player.getPersonaname());
        tIdUser.setText(player.getSteamid());
        tOwnedGames.setText("Number of owned games: " + ownedGames.size());
        Integer hoursSpent = 0;

        gameList.setCellFactory(new Callback<ListView<Game>, ListCell<Game>>() {
            @Override
            public ListCell<Game> call(ListView<Game> listView) {
                return new ListCell<>(){
                    protected void updateItem(Game game, boolean empty){
                        super.updateItem(game, empty);
                        if(game != null && !empty){
                            ImageView imageView = new ImageView();
                            try {
                                imageView.setImage(setImageListCell(game.getAppid(), game.getImg_icon_url()));
                            } catch (Exception e) {
                                File file = new File("src/main/resources/ui/icons/missing.jpg");
                                Image image = new Image(file.toURI().toString());
                                imageView.setImage(image);
                            }

                            imageView.setFitWidth(32);
                            imageView.setPreserveRatio(true);

                            Text titleText = new Text(game.getName());
                            String hoursCell = "Hours played: " + (game.getPlaytime_forever()/60);
                            Text subtitleText = new Text(hoursCell);

                            VBox vBox = new VBox(titleText, subtitleText);
                            HBox hbox = new HBox();
                            hbox.getChildren().addAll(imageView,vBox);
                            hbox.setSpacing(10);

                            setGraphic(hbox);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        gamesListObservable.addAll(ownedGames);
        gamesListObservableCopy.addAll(ownedGames);
        gameList.setItems(gamesListObservable);

        gameList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    int index = gameList.getSelectionModel().getSelectedIndex();
                    Game game = gamesListObservable.get(index);

                    Consumer<Map<String, IDApp>> gameInfoConsumer = (gameInfo) -> {
                        for (Map.Entry<String, IDApp> entry : gameInfo.entrySet()) {
                            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                            idApp = entry.getValue();
                        }
                    };

                    gameInfoTask = new GameInfoTask(game.appid, gameInfoConsumer);
                    new Thread(gameInfoTask).start();

                    while(idApp == null){
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if(idApp.getSuccess()){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(R.getUI("gameInfo.fxml"));
                                System.out.println(idApp);
                                loader.setController(new GameInfoController(idApp.getData()));
                                VBox vbox;
                                try {
                                    vbox = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene scene = new Scene(vbox);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.setTitle(idApp.getData().getName());
                                stage.show();
                                idApp = null;
                            }
                        });
                    } else {
                        Alert alert;
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Game details not found");
                        alert.setContentText("Game details not found in Steam Database. Sorry.");
                        alert.showAndWait();
                        idApp = null;
                    }
                }
            }
        });

        /*
        //No es necesario
        gamesListObservable.addListener(new ListChangeListener<Game>() {
            @Override
            public void onChanged(Change<? extends Game> change) {
                if(change.wasAdded()){
                    gameList.getItems().addAll(change.getAddedSubList());
                } else if(change.wasRemoved()){
                    gameList.getItems().removeAll(change.getRemoved());
                }
            }
        });
         */

        tfSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.length() != 0) {
                    Predicate<Game> nameFilter = game -> game.getName().contains(t1);
                    gamesListObservable = gamesListObservableCopy;
                    gamesListObservable = gamesListObservable.filtered(nameFilter);
                } else {
                    gamesListObservable = gamesListObservableCopy;
                }
                gameList.setItems(gamesListObservable);
                gameList.refresh();
            }
        });

        for(Game game: ownedGames){
            hoursSpent = hoursSpent + game.playtime_forever;
        }
        hoursSpent = hoursSpent / 60;
        String text = tHours.getText();
        String textFormatted = text.replace("x", String.valueOf(hoursSpent));
        tHours.setText(textFormatted);
    }

    private void setImage(String urlAvatar) throws IOException {
        URL url = new URL(urlAvatar);
        InputStream inputStream = url.openStream();
        Image image = new Image(inputStream);
        userAvatar.setImage(image);
    }

    private Image setImageListCell(Integer appID, String imgURL) throws IOException {
        String urlGame = "https://media.steampowered.com/steamcommunity/public/images/apps/";
        urlGame = urlGame + appID + "/" + imgURL + ".jpg";

        URL url = new URL(urlGame);
        InputStream inputStream = url.openStream();
        Image image = new Image(inputStream);
        return image;
    }
}
