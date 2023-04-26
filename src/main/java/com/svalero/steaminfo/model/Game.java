package com.svalero.steaminfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    public Integer appid;
    public String name;
    public Integer playtime_forever;
    public String img_icon_url;
    public Integer rtime_last_played;


    @Override
    public String toString() {
        return "appid=" + appid;
    }
}
