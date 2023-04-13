package com.svalero.steaminfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponeGameInfoSteamID {
    public Integer game_count;
    public List<Game> games;
}
