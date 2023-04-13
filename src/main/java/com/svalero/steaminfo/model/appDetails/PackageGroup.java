
package com.svalero.steaminfo.model.appDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageGroup {

    public String name;
    public String title;
    public String description;
    public String selection_text;
    public String save_text;
    public Integer display_type;
    public String is_recurring_subscription;
    public List<Sub> subs;

}
