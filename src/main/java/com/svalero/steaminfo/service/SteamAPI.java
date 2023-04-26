package com.svalero.steaminfo.service;

import com.svalero.steaminfo.model.GameInfoSteamID;
import com.svalero.steaminfo.model.PlayerInfo;
import com.svalero.steaminfo.model.VanityURL;
import com.svalero.steaminfo.model.achievementRecord.AchievementRecord;
import com.svalero.steaminfo.model.achievementSchema.AchievementSchema;
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

    @GET("ISteamUserStats/GetPlayerAchievements/v1/")
    Observable<AchievementRecord> getPlayerAchievements(@Query("key") String key,
                                                        @Query("steamid") Long steamID,
                                                        @Query("appid") String appId);

    @GET("ISteamUserStats/GetSchemaForGame/v2/")
    Observable<AchievementSchema> getAchievementSchema(@Query("key") String key,
                                                       @Query("appid") String appId);
}
