package modules;

import android.os.AsyncTask;

import com.example.embulanceapp.MapsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鄧鄧蟲 on 2018/1/20.
 */

public class Findpath {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI";
    private FindpathListener listener;
    private String start;
    private String ending;



    public Findpath(FindpathListener listener, String start, String ending) {
        this.listener = listener;
        this.start = start;
        this.ending = ending;
    }

    public void execute() throws UnsupportedEncodingException {
        listener.onFindpathStart();
        new DownloadRawData().execute(createUrl());
    }

    //使用 direction api 需要將起點跟終點的(地址or經緯)放在url裡面，才能執行顯示
    //所以我們需要把值都放在url裡面
    private String createUrl() throws UnsupportedEncodingException{
        String urlStart = URLEncoder.encode(start,"utf-8");
        String urlEnd = URLEncoder.encode(ending, "utf-8");
        return DIRECTION_URL_API + "origin=" +urlStart +"&destination=" +urlEnd+"&language='zh-TW'&key="+ GOOGLE_API_KEY;
        // return使用direction api
    }
    //將url跑出的結果下載放在 string 裡面
    private class DownloadRawData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                //將url裡面的內容讀進stringbuffer
                URL url = new URL(link);
                InputStream urlIn = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlIn));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();
            } catch (MalformedURLException a) {
                a.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //direction api 會回傳 JSon code, 需要把json code解讀出來
    //參考之程式碼 https://github.com/hiepxuan2008/GoogleMapDirectionSimple/blob/master/app/src/main/java/Modules/DirectionFinder.java
        private void parseJSon(String data) throws JSONException{
            if(data== null){
                return;
            }
            List<Route> routes = new ArrayList<Route>();
            JSONObject jsonData = new JSONObject(data);
            JSONArray jsonRoutes = jsonData.getJSONArray("routes");
            for(int i =0; i<jsonRoutes.length();i++){
                JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
                Route route = new Route();
                JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
                JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
                JSONObject jsonLeg = jsonLegs.getJSONObject(0);
                JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
                JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
                JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

                route.distance = new Distance(jsonDistance.getString("text"),jsonDistance.getInt("value"));
                route.duration = new Duration(jsonDuration.getString("text"),jsonDuration.getInt("value"));
                route.endAddress = jsonLeg.getString("end_address");
                route.startAddress = jsonLeg.getString("start_address");
                route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"),jsonStartLocation.getDouble("lng"));
                route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
                route.points = decodePolyLine(overview_polylineJson.getString("points"));

                routes.add(route);

            }

            listener.onFindpathSuccess(routes);
        }
        //json code 會有路徑規劃，需要將之解讀回傳
        //參考之程式碼 https://github.com/hiepxuan2008/GoogleMapDirectionSimple/blob/master/app/src/main/java/Modules/DirectionFinder.java
        private List<LatLng> decodePolyLine(final String poly){
            int len = poly.length();
            int index = 0;
            List<LatLng> decoded = new ArrayList<LatLng>();
            int lat = 0;
            int lng = 0;

            while (index <len){
                int b;
                int shift = 0;
                int result = 0;
                do{
                    b = poly.charAt(index++) - 63;
                    result |= (b & 0x1f)<<shift;
                    shift+=5;
                }while(b>=0x20);
                int dlat = ((result & 1)!= 0 ? ~(result>>1):(result>>1));
                lat += dlat;

                shift =0;
                result =0;

                do{
                    b = poly.charAt(index++) - 63;
                    result |= (b & 0x1f)<<shift;
                    shift+=5;
                }while(b>=0x20);
                int dlng = ((result & 1)!= 0 ? ~(result>>1):(result>>1));
                lng += dlng;

                decoded.add(new LatLng(
                        lat/100000d, lng/100000d
                ));
            }
            return decoded;
        }

}
