
package com.svalero.steaminfo.model.appDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Screenshot {

    public Integer id;
    public String path_thumbnail;
    public String path_full;

}
