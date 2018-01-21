package modules;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
/**
 * Created by 鄧鄧蟲 on 2018/1/20.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
