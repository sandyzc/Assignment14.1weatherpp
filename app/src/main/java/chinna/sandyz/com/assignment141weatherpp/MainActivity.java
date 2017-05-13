package chinna.sandyz.com.assignment141weatherpp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.okhttp.FormEncodingBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnWebServiceResult{

    TextView country_name, min_temp, max_temp, description,status;
    String url="http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=d7b900681c37193223281142bd919019";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        country_name= (TextView)findViewById(R.id.country_name);
        min_temp= (TextView)findViewById(R.id.min_temp);
        max_temp= (TextView)findViewById(R.id.max_temp);
        status= (TextView)findViewById(R.id.status);
        description=(TextView)findViewById(R.id.description);
        getTempInfo();
    }

    private void getTempInfo(){
        FormEncodingBuilder parameters= new FormEncodingBuilder();
        parameters.add("page" , "1");
        if(NetworkStatus.getInstance(this).isConnectedToInternet()) {
            CallAddr call = new CallAddr(this, url,parameters,CommonUtilities.SERVICE_TYPE.GET_DATA, this);
            call.execute();
        }else {
            Toast.makeText(this, "No Network!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getWebResponse(String result, CommonUtilities.SERVICE_TYPE type) {
        switch (type){
            case GET_DATA:
                try {
                    JSONObject obj= new JSONObject(result);
                    JSONArray arr= obj.getJSONArray("weather");
                    JSONObject object= arr.getJSONObject(0);
                    status.setText("Status: "+object.getString("main"));
                    description.setText("Description: "+object.getString("description"));
                    country_name.setText("Country: "+obj.getString("name"));
                    min_temp.setText("Min Temperature: "+obj.getJSONObject("main").getDouble("temp_min")+"");
                    max_temp.setText("Max Temperature: "+obj.getJSONObject("main").getDouble("temp_max")+"");
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }
}
