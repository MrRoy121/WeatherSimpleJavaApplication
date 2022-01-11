package com.example.waetherforcasting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    String CITY1;
    Button retry;
    TextView addressT, updated_atT, statusT, tempT, temp_minTxt, temp_maxT, sunriseT,sunsetT, windT, pressureT, humidityT;
    EditText CITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.presd).setVisibility(View.GONE);
        findViewById(R.id.sund).setVisibility(View.GONE);
        findViewById(R.id.sunsd).setVisibility(View.GONE);
        findViewById(R.id.vunsd).setVisibility(View.GONE);
        findViewById(R.id.wnd).setVisibility(View.GONE);
        findViewById(R.id.humd).setVisibility(View.GONE);
        CITY=findViewById(R.id.city);


    }
    public void run(View view){
        CITY1 = CITY.getText().toString();
        new weatherTask().execute();
    }
    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
            findViewById(R.id.city).setVisibility(View.GONE);
            findViewById(R.id.button2).setVisibility(View.GONE);
        }
        protected String doInBackground(String args[]) {
            return HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q="+CITY1+"&appid=e887613d590d119253250c66161f56cc&lang=en-US");
        }
        @Override
        protected void onPostExecute(String result) {
            addressT = findViewById(R.id.address);
            updated_atT = findViewById(R.id.updated_at);
            statusT = findViewById(R.id.status);
            tempT = findViewById(R.id.temp);
            temp_minTxt = findViewById(R.id.temp_min);
            temp_maxT = findViewById(R.id.temp_max);
            sunriseT = findViewById(R.id.sunrise);
            sunsetT = findViewById(R.id.sunset);
            windT = findViewById(R.id.wind);
            pressureT = findViewById(R.id.pressure);
            humidityT = findViewById(R.id.humidity);

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");
                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");
                String address = jsonObj.getString("name") + ", " + sys.getString("country");
                addressT.setText(address);
                updated_atT.setText(updatedAtText);
                statusT.setText(weatherDescription.toUpperCase());
                tempT.setText(temp);
                temp_minTxt.setText(tempMin);
                temp_maxT.setText(tempMax);
                sunriseT.setText (new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                sunsetT.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                windT.setText(windSpeed);
                pressureT.setText(pressure);
                humidityT.setText(humidity);
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
                findViewById(R.id.presd).setVisibility(View.VISIBLE);
                findViewById(R.id.sund).setVisibility(View.VISIBLE);
                findViewById(R.id.sunsd).setVisibility(View.VISIBLE);
                findViewById(R.id.vunsd).setVisibility(View.VISIBLE);
                findViewById(R.id.wnd).setVisibility(View.VISIBLE);
                findViewById(R.id.humd).setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
                findViewById(R.id.b2).setVisibility(View.VISIBLE);
            }
        }
    }

    public void refresh(View v){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}