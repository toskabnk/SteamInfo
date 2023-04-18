package com.svalero.steaminfo.model.achievementSchema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableGameStats {
    public List<Achievement> achievements;
}
