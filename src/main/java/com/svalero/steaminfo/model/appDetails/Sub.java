
package com.svalero.steaminfo.model.appDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sub {

    public Integer packageid;
    public String percent_savings_text;
    public Integer percent_savings;
    public String option_text;
    public String option_description;
    public String can_get_free_license;
    public Boolean is_free_license;
    public Integer price_in_cents_with_discount;

}
