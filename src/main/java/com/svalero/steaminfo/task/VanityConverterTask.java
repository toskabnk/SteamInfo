package com.svalero.steaminfo.task;

import com.svalero.steaminfo.model.Player;
import com.svalero.steaminfo.model.ResponseVanityURL;
import com.svalero.steaminfo.service.SteamService;
import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

import java.util.List;

public class VanityConverterTask extends Task<Integer> {
    private String apiKey;
    private String vanityURL;
    private Consumer<ResponseVanityURL> playerList;

    public VanityConverterTask(String apiKey, String vanityURL, Consumer<ResponseVanityURL> playerList) {
        this.apiKey = apiKey;
        this.vanityURL = vanityURL;
        this.playerList = playerList;
    }

    protected Integer call() throws Exception {
        SteamService steamService = new SteamService();
        steamService.getSteamID(apiKey, vanityURL).subscribe(playerList);
        return null;
    }
}
