package com.svalero.steaminfo.controller;

import com.svalero.steaminfo.model.AllData;
import com.svalero.steaminfo.model.Game;
import com.svalero.steaminfo.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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



    public UserInfoController(AllData allData){
        player = allData.getPlayer();
        ownedGames.addAll(allData.getGames());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
                            imageView.setFitWidth(20);
                            imageView.setPreserveRatio(true);

                            Text titleText = new Text(game.getName());
                            String hoursCell = "Hours played: " + game.getPlaytime_forever();
                            Text subtitleText = new Text(hoursCell);

                            HBox hbox = new HBox();
                            hbox.getChildren().addAll(imageView, titleText, subtitleText);

                            // Establece la celda de la lista como el HBox
                            setGraphic(hbox);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        ObservableList<Game> gamesListObservable = FXCollections.observableArrayList();
        gamesListObservable.addAll(ownedGames);
        gameList.setItems(gamesListObservable);
        /*
        gameList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    gameList.getSelectionModel().getSelectedItems()
                }
            }
        });

         */

        for(Game game: ownedGames){
            hoursSpent = hoursSpent + game.playtime_forever;
        }
        hoursSpent = hoursSpent / 60;
        String text = tHours.getText();
        String textFormatted = text.replace("x", String.valueOf(hoursSpent));
        tHours.setText(textFormatted);
    }

    @FXML
    public void searchGame(ActionEvent actionEvent){

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

    /*
    private class CustomGameCell extends ListCell<Game>{
        private HBox content;
        private Text gameName;
        private Text hoursPlayed;
        private ImageView gameImage;

        public CustomGameCell() {
            super();
            gameName = new Text();
            hoursPlayed = new Text();
            gameImage = new ImageView();
            VBox vBox = new VBox(gameName, hoursPlayed);
            content = new HBox(gameImage, vBox);
            content.setSpacing(10);
        }
    }

     */


}
