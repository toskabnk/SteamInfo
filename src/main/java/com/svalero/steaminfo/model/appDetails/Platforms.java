
package com.svalero.steaminfo.model.appDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Platforms {

    public Boolean windows;
    public Boolean mac;
    public Boolean linux;

}
