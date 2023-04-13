package com.svalero.steaminfo.service;

import com.svalero.steaminfo.model.GameInfoSteamID;
import com.svalero.steaminfo.model.PlayerInfo;
import com.svalero.steaminfo.model.VanityURL;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface SteamAPI {
    @GET("ISteamUser/ResolveVanityURL/v1/")
    Observable<VanityURL> getSteamID(@Query("key") String key,
                                           @Query("vanityurl") String url);

    @GET("IPlayerService/GetOwnedGames/v1/")
    Observable<GameInfoSteamID> getOwnedGames(@Query("key") String key,
                                              @Query("steamid") Long steamID,
                                              @Query("include_appinfo") boolean includeAppInfo,
                                              @Query("include_played_free_games") boolean includeFreeGames);


    @GET("ISteamUser/GetPlayerSummaries/v2/")
    Observable<PlayerInfo> getPlayerInfo(@Query("key") String key,
                                         @Query("steamids") Long steamID);

}
