package com.svalero.steaminfo.model.achievementSchema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    public String gameName;
    public String gameVersion;
    public AvailableGameStats availableGameStats;
}
