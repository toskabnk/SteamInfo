package com.svalero.steaminfo.task;

import com.svalero.steaminfo.model.Player;
import com.svalero.steaminfo.service.SteamService;
import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

import java.util.List;

public class UserInfoTask extends Task<Integer> {
    private Consumer<List<Player>> playerConsumer;
    private String apiKey;
    private Long userId;

    public UserInfoTask(Consumer<List<Player>> playerConsumer, String apiKey, Long userId) {
        this.playerConsumer = playerConsumer;
        this.apiKey = apiKey;
        this.userId = userId;
    }

    @Override
    protected Integer call() throws Exception {
        SteamService steamService = new SteamService();
        steamService.getPlayerInfo(apiKey, userId).subscribe(playerConsumer);

        return null;
    }
}
