package com.svalero.steaminfo.service;

import com.svalero.steaminfo.model.appDetails.IDApp;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Map;

public interface SteamStoreAPI {
    @GET("api/appdetails")
    Observable<Map<String, IDApp>> getAppInfo(@Query("appids") Integer steamApp);
}
