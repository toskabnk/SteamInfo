package com.svalero.steaminfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class AllData {
    private Player player;
    private List<Game> games;
}
