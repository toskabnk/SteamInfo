package com.svalero.steaminfo.controller;

import com.svalero.steaminfo.model.appDetails.Data;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class GameInfoController implements Initializable {
    Data data;
    @FXML
    ImageView ivHeaderImage;
    @FXML
    ImageView gameImage;
    @FXML
    Text tGameName;
    @FXML
    Text tShortDescription;
    @FXML
    Text tWebsite;
    @FXML
    Text tNOfAchievements;
    @FXML
    Text tRecommendations;
    @FXML
    Text tMetacritc;
    @FXML
    ListView lvAchievements;
    public GameInfoController(Data data){
        this.data = data;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ivHeaderImage.setImage(getImage(data.getBackground(), false));
            gameImage.setImage(getImage(data.getHeader_image(), true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tGameName.setText(data.getName());
        tShortDescription.setText(data.getShort_description());
        tWebsite.setText(data.getWebsite());
        if(data.getAchievements() == null){
            tNOfAchievements.setText("0");
        } else {
            tNOfAchievements.setText(String.valueOf(data.getAchievements().getTotal()));
        }
        tRecommendations.setText(String.valueOf(data.getRecommendations().getTotal()));
        if(data.getMetacritic() == null){
            tMetacritc.setText("No data");
        } else {
            tMetacritc.setText(String.valueOf(data.getMetacritic().getScore()));
        }
    }

    private Image getImage(String imgURL, boolean header) throws IOException {
        if(header){
            imgURL = "https://cdn.akamai.steamstatic.com/steam/apps/" + data.getSteam_appid() + "/header.jpg";
        }
        URL url = new URL(imgURL);
        InputStream inputStream = url.openStream();
        Image image = new Image(inputStream);
        return image;
    }
}
