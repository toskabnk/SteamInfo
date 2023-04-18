package com.svalero.steaminfo.model.achievementSchema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Achievement {
    public String name;
    public Integer defaultvalue;
    public String displayName;
    public Integer hidden;
    public String description;
    public String icon;
    public String icongray;

}
