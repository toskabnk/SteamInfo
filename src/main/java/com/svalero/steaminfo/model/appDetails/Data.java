
package com.svalero.steaminfo.model.appDetails;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {

    public String type;
    public String name;
    public Integer steam_appid;
    public Integer requiredAge;
    public Boolean is_free;
    public String detailed_description;
    public String about_ghe_game;
    public String short_description;
    public String supported_languages;
    public String header_image;
    public String website;
    public PcRequirements pc_requirements;
    public List<String> developers;
    public List<String> publishers;
    public PriceOverview price_overview;
    public List<Integer> packages;
    public List<PackageGroup> package_groups;
    public Platforms platforms;
    public Metacritic metacritic;
    public List<Category> categories;
    public List<Genre> genres;
    public List<Screenshot> screenshots;
    public Recommendations recommendations;
    public Achievements achievements;
    public ReleaseDate release_date;
    public SupportInfo support_info;
    public String background;
    public String background_raw;
    public ContentDescriptors content_descriptors;

}
