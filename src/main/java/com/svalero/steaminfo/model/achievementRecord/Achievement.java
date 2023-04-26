package com.svalero.steaminfo.model.achievementRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Achievement {
    public String apiname;
    public Integer achieved;
    public Integer unlocktime;
}
