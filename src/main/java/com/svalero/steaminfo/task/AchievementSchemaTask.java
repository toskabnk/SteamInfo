package com.svalero.steaminfo.task;

import com.svalero.steaminfo.model.achievementSchema.Achievement;
import com.svalero.steaminfo.service.SteamService;
import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;


public class AchievementSchemaTask extends Task<Integer> {
    private Consumer<Achievement> consumer;
    private String idApp;
    private String key;

    public AchievementSchemaTask(String key, String idApp, Consumer<Achievement> consumer){
        this.consumer = consumer;
        this.idApp = idApp;
        this.key = key;
    }
    @Override
    protected Integer call() throws Exception {
        SteamService steamService = new SteamService();
        steamService.getAchievementSchema(key, idApp).subscribe(consumer);
        return null;
    }
}
