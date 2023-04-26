package com.svalero.steaminfo.controller;

import com.svalero.steaminfo.model.Player;
import com.svalero.steaminfo.model.achievementSchema.Achievement;
import com.svalero.steaminfo.model.appDetails.Data;
import com.svalero.steaminfo.task.AchievementSchemaTask;
import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class GameInfoController implements Initializable {
    Data data;
    String apiKey;
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
    ListView<Achievement> lvAchievements;
    ObservableList<Achievement> achievementObservableListchievements;
    AchievementSchemaTask achievementSchemaTask;
    public GameInfoController(Data data){
        this.data = data;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apiKey = loadApiKey();
        achievementObservableListchievements = FXCollections.observableArrayList();
        try {
            ivHeaderImage.setImage(getImage(data.getBackground(), false));
            gameImage.setImage(getImage(data.getHeader_image(), true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tGameName.setText(data.getName());

        tShortDescription.setText(data.getShort_description());
        tShortDescription.setWrappingWidth(440);

        if(data.getWebsite() == null){
            tWebsite.setText("No website data");
        } else {
            tWebsite.setText(data.getWebsite());
        }

        String achievements = "Number of achievements: ";
        if(data.getAchievements() == null){
            achievements = achievements + "0";
            tNOfAchievements.setText(achievements);
        } else {
            achievements = achievements + String.valueOf(data.getAchievements().getTotal());
            tNOfAchievements.setText(achievements);
        }

        if(data.getRecommendations() == null){
            tRecommendations.setText("No recommendations data");
        } else {
            String recomendations = "Number of recommendations: " + String.valueOf(data.getRecommendations().getTotal());
            tRecommendations.setText(recomendations);
        }

        String metracritic = "Metacritic score: ";
        if(data.getMetacritic() == null){
            metracritic = metracritic + "No data";
            tMetacritc.setText(metracritic);
        } else {
            metracritic = metracritic + String.valueOf(data.getMetacritic().getScore());
            tMetacritc.setText(metracritic);
        }

        lvAchievements.setCellFactory(new Callback<ListView<Achievement>, ListCell<Achievement>>() {
            @Override
            public ListCell<Achievement> call(ListView<Achievement> achievementListView) {
                return new ListCell<>(){
                    protected void updateItem(Achievement achievement, boolean empty){
                        super.updateItem(achievement, empty);
                        if(achievement != null && !empty){
                            ImageView imageView = new ImageView();
                            try{
                                imageView.setImage(getImage(achievement.getIcon(), false));
                            } catch (Exception e){
                                File file = new File("src/main/resources/ui/icons/missing.jpg");
                                Image image = new Image(file.toURI().toString());
                                imageView.setImage(image);
                            }
                            imageView.setFitWidth(32);
                            imageView.setPreserveRatio(true);

                            Text titleName = new Text(achievement.getDisplayName());
                            Text description;

                            if(achievement.getHidden() == 0){
                                description = new Text(achievement.getDescription());
                            } else {
                                description = new Text("Hidden");
                            }

                            VBox vBox = new VBox(titleName, description);
                            HBox hBox = new HBox();
                            hBox.getChildren().addAll(imageView,vBox);
                            hBox.setSpacing(10);
                            setGraphic(hBox);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        if(data.getAchievements() != null){
            if(data.getAchievements().getTotal() != 0){
                Consumer<Achievement> achievementConsumer = achievement -> {
                    achievementObservableListchievements.add(achievement);
                    lvAchievements.setItems(achievementObservableListchievements);
                    lvAchievements.refresh();
                    Thread.sleep(500);
                };
                achievementSchemaTask = new AchievementSchemaTask(apiKey, String.valueOf(data.getSteam_appid()), achievementConsumer);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(achievementSchemaTask).start();
                    }
                });
            }
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
