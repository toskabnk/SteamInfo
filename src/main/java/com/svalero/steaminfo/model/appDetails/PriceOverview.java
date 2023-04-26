
package com.svalero.steaminfo.model.appDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceOverview {

    public String currency;
    public Integer initial;
    public Integer _final;
    public Integer discount_percent;
    public String initial_formatted;
    public String final_formatted;

}
