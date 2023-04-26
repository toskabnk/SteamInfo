package com.svalero.steaminfo.model.achievementRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Playerstats {
    public String steamID;
    public String gameName;
    public List<Achievement> achievements;
    public Boolean success;
}
