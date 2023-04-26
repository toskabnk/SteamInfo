package com.svalero.steaminfo.task;

import com.svalero.steaminfo.model.appDetails.IDApp;
import com.svalero.steaminfo.service.SteamService;
import javafx.concurrent.Task;

import java.util.Map;
import io.reactivex.functions.Consumer;

public class GameInfoTask extends Task<Integer>  {
    private Consumer<Map<String, IDApp>> mapConsumer;
    private Integer steamApp;

    public GameInfoTask(Integer steamApp, Consumer<Map<String, IDApp>> mapConsumer){
        this.steamApp = steamApp;
        this.mapConsumer = mapConsumer;
    }

    protected Integer call() throws Exception{
        SteamService steamService = new SteamService();
        steamService.getGameInfo(steamApp).subscribe(mapConsumer);

        return null;
    }
}
