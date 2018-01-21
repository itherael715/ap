package modules;

import modules.Route;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by 鄧鄧蟲 on 2018/1/20.
 */

public interface FindpathListener {
    void onFindpathStart();
    void onFindpathSuccess( List <Route> route);
}
