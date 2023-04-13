package com.svalero.steaminfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Player {
    public String steamid;
    public Integer communityvisibilitystate;
    public Integer profilestate;
    public String personaname;
    public String profileurl;
    public String avatar;
    public String avatarmedium;
    public String avatarfull;
    public String avatarhash;
    public Integer lastlogoff;
    public Integer personastate;
    public String realname;
    public String primaryclanid;
    public Integer timecreated;
    public Integer personastateflags;
    public String loccountrycode;
    public String locstatecode;
    public Integer loccityid;
}
