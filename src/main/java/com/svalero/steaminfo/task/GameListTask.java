package com.svalero.steaminfo.task;

import com.svalero.steaminfo.model.Game;
import com.svalero.steaminfo.service.SteamService;
import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

import java.util.List;

public class GameListTask extends Task<Integer> {

    private Consumer<List<Game>> gameConsumer;
    private String apiKey;
    private Long userId;
    private boolean includeFreeGames;

    public GameListTask(String apiKey, Long userId, Consumer<List<Game>> gameConsumer, boolean includeFreeGames){
        this.gameConsumer = gameConsumer;
        this.apiKey = apiKey;
        this.userId = userId;
        this.includeFreeGames = includeFreeGames;
    }

    protected Integer call() throws Exception {
        SteamService steamService = new SteamService();
        steamService.getOwnedGames(apiKey, userId, true, includeFreeGames).subscribe(gameConsumer);

        return null;
    }
}
