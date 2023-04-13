package com.svalero.steaminfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVanityURL {
    //Succes 1 OK 42 NoMatch
    public String steamid;
    public Integer success;
}
