package com.example.retrofitapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {



    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String AppId = "d65fc7b8f0116635bb19b0ba0b33fe32";
    public static String cn;

    public EditText cityName;
    public TextView weatherData;
    public TextView tv;
    public Button get;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        get = findViewById(R.id.button);
        tv = findViewById(R.id.textView2);
        cityName = findViewById(R.id.editText);
        weatherData = findViewById(R.id.textView);
        tv.animate().translationYBy(500f).setDuration(2500);
        tv.animate().alpha(1f).setDuration(2500);
        get.animate().translationYBy(-1200f).setDuration(2000);
        get.animate().alpha(1f).setDuration(2000);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherData.setText("");
                InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
                if(cityName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter the city name", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        cn = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        Toast.makeText(getApplicationContext(), "Couldn't find the weather", Toast.LENGTH_LONG).show();
                    }
                }
                getCurrentData();
            }
        });
    }
    void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(cn, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;

                    String stringBuilder = "Country: " +
                            weatherResponse.sys.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main.temp + "\u2109"+
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main.temp_min + "\u2109"+
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main.temp_max + "\u2109"+
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main.pressure;

                    weatherData.setText(stringBuilder);
                    weatherData.animate().alpha(1f).setDuration(2000);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                weatherData.setText(t.getMessage());
            }
        });
    }
}