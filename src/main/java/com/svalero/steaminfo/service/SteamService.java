package com.svalero.steaminfo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svalero.steaminfo.model.*;
import com.svalero.steaminfo.model.appDetails.IDApp;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import java.util.Map;

public class SteamService {
    private String STEAM_API = "https://api.steampowered.com";
    private String STORE_API = "https://store.steampowered.com";
    private SteamAPI steamAPI;
    private SteamStoreAPI storeAPI;

    public SteamService(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gsonParser = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit steamAPI = new Retrofit.Builder()
                .baseUrl(STEAM_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonParser))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        Retrofit storeAPI = new Retrofit.Builder()
                .baseUrl(STORE_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonParser))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.steamAPI = steamAPI.create(SteamAPI.class);
        this.storeAPI = storeAPI.create(SteamStoreAPI.class);
    }

    public Observable<ResponseVanityURL> getSteamID(String key, String url){
        return this.steamAPI.getSteamID(key, url).map(vanityURL -> vanityURL.getResponse());
    }
    public Observable<List<Game>> getOwnedGames(String key, Long steamID, boolean includeAppInfo, boolean includeFreeGames){

        return this.steamAPI.getOwnedGames(key, steamID, includeAppInfo, includeFreeGames).map(gameInfoSteamID -> gameInfoSteamID.getResponse().getGames());
    }

    public Observable<List<Player>> getPlayerInfo(String key, Long steamID){
        return this.steamAPI.getPlayerInfo(key, steamID).map(playerInfo -> playerInfo.getResponse().getPlayers());
    }

    public Observable<Map<String, IDApp>> getGameInfo(Integer steamApp){
        return this.storeAPI.getAppInfo(steamApp);
    }


}
