package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WeatherForecast extends AppCompatActivity {

    private TextView currentTempText, maxTempText, minTempText;
    private ImageView imageView;
    private String currentTemp, maxTemp, minTemp, iconName;
    private Bitmap image;
    private ProgressBar progressBar;
    private static final String ACTIVITY_NAME = "WeatherForecast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        this.currentTempText = findViewById(R.id.current_temp_text);
        this.maxTempText = findViewById(R.id.max_temp_text);
        this.minTempText = findViewById(R.id.min_temp_text);
        this.imageView = findViewById(R.id.weather_image);

        ForecastQuery f = new ForecastQuery();
        f.execute();
    }


    private class ForecastQuery extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings){
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=2b379bb9d69d30b3d236232e794f2404&mode=xml&units=metric");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(1000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream in = conn.getInputStream();
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(in, null);

                    while(parser.getEventType() != XmlPullParser.END_DOCUMENT){
                        if(parser.getEventType() == XmlPullParser.START_TAG){
                            if(parser.getName().equals("temperature")){
                                currentTemp = parser.getAttributeValue(null, "value");
                                publishProgress(25);
                                minTemp = parser.getAttributeValue(null, "min");
                                publishProgress(50);
                                maxTemp = parser.getAttributeValue(null, "max");
                                publishProgress(75);
                            } else if (parser.getName().equals("weather")){
                                iconName = parser.getAttributeValue(null, "icon");
                                String fileName = iconName + ".png";
                                Log.i(ACTIVITY_NAME, "Looking for file " + fileName);
                                if (fileExistence(fileName)){
                                    FileInputStream fis = null;
                                    try {
                                        fis = openFileInput(fileName);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i(ACTIVITY_NAME,"Found local file");
                                    image = BitmapFactory.decodeStream(fis);
                                } else {
                                    URL imgURL = new URL("http://openweathermap.org/img/w/"+fileName);
                                    image = getImage(imgURL);
                                    FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    Log.i(ACTIVITY_NAME, "Downloaded image");
                                    outputStream.flush();
                                    outputStream.close();
                                }
                                publishProgress(100);
                            }
                        }
                        parser.next();
                    }
                } finally {
                    in.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }

        private boolean fileExistence(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... value){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String a){
            progressBar.setVisibility(View.INVISIBLE);
            currentTempText.setText(currentTemp+"C\u00b0");
            minTempText.setText(minTemp+"C\u00b0");
            maxTempText.setText(maxTemp+"C\u00b0");
            imageView.setImageBitmap(image);
        }

        private Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}
