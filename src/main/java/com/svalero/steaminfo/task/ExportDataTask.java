package com.svalero.steaminfo.task;

import com.svalero.steaminfo.controller.GameInfoController;
import com.svalero.steaminfo.model.Game;
import com.svalero.steaminfo.model.appDetails.IDApp;
import com.svalero.steaminfo.util.R;
import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.Map;

public class ExportDataTask extends Task<Integer> {
    private List<Game> gameList;
    private File file;
    private GameInfoTask gameInfoTask;
    private IDApp idApp = null;
    private static final String CSV_SEPARATOR = ";";
    Integer currentIter = 0;

    public ExportDataTask(List<Game> gameList, File file){
        this.gameList = gameList;
        this.file = file;
    }

    @Override
    protected Integer call() throws Exception {
        double progress = 0.0;

        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            StringBuilder line = new StringBuilder();
            line.append("Number" + CSV_SEPARATOR + "Game" + CSV_SEPARATOR + "Hours Played" + CSV_SEPARATOR + "Number of Achivements" + CSV_SEPARATOR + "Game ID" + CSV_SEPARATOR + "Game Website" + CSV_SEPARATOR + "Metacritic Score");
            bufferedWriter.write(line.toString());
            bufferedWriter.newLine();
            for (Game game: gameList){
                line.delete(0, line.length());

                //Number
                line.append(currentIter);
                line.append(CSV_SEPARATOR);

                currentIter++;
                progress = ((double) currentIter / gameList.size());
                updateProgress(progress, 1);

                //Game
                line.append(game.getName());
                line.append(CSV_SEPARATOR);

                //Hours
                line.append(game.getPlaytime_forever());
                line.append(CSV_SEPARATOR);

                Consumer<Map<String, IDApp>> gameInfoConsumer = (gameInfo) -> {
                    for (Map.Entry<String, IDApp> entry : gameInfo.entrySet()) {
                        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                        idApp = entry.getValue();
                    }
                };

                gameInfoTask = new GameInfoTask(game.getAppid(), gameInfoConsumer);
                new Thread(gameInfoTask).start();

                while(idApp == null){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                if(idApp.getSuccess()){
                    //Number of Achievements
                    if(idApp.getData().getAchievements() != null){
                        line.append(idApp.getData().getAchievements().getTotal());
                    } else {
                        line.append("0");
                    }
                    line.append(CSV_SEPARATOR);

                    //GameID
                    line.append(idApp.getData().getSteam_appid());
                    line.append(CSV_SEPARATOR);

                    //Game Website
                    if(idApp.getData().getWebsite() != null){
                        line.append(idApp.getData().getWebsite());
                    } else {
                        line.append(" ");
                    }
                    line.append(CSV_SEPARATOR);

                    //Metacritic
                    if(idApp.getData().getMetacritic() != null){
                        line.append(idApp.getData().getMetacritic().getScore());
                    } else {
                        line.append("No data");
                    }
                    line.append(CSV_SEPARATOR);
                } else {
                    //Number of Achievements
                    line.append("No data");
                    line.append(CSV_SEPARATOR);

                    //GameID
                    line.append(game.getAppid());
                    line.append(CSV_SEPARATOR);

                    //Game Website
                    line.append("No data");
                    line.append(CSV_SEPARATOR);

                    //Metacritic
                    line.append("No data");
                    line.append(CSV_SEPARATOR);
                }
                idApp = null;
                bufferedWriter.write(line.toString());
                bufferedWriter.newLine();
                Thread.sleep(50);
            }
            updateProgress(1,1);
            updateMessage("Ok");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException ignored){
        }
        return null;
    }
}
