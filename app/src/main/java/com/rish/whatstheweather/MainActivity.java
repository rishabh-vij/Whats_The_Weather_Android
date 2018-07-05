package com.rish.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView resultText;
    public class inBackground extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;
            try{
                url= new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in= urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data= reader.read();

                while (data != -1){
                    char cur=(char)data;
                    result+=cur;
                    data=reader.read();
                }
                return result;
            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
                return null;
                }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String mains, desc, temp, humid,fin="";
            JSONObject jPart=null;
            try {
                JSONObject object = new JSONObject(s);
                String weatherinfo=object.getString("weather");
                JSONObject tempinfo= object.getJSONObject("main");
                JSONArray arr= new JSONArray(weatherinfo);
                for(int i=0;i<arr.length();i++) {
                    jPart = arr.getJSONObject(i);
                }
                    mains= jPart.getString("main");
                    desc= jPart.getString("description");

                temp=tempinfo.getString("temp");
                humid= tempinfo.getString("humidity");
                fin="Temperature "+temp+"°C\nHumidity "+humid+"%\n"+mains+"-"+desc;
                if(!fin.equals(""))
                    resultText.setText(fin);
                else
                    Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
                }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
            }

    }
    }
    public void getweather(View view){
        String city="";
        city=editText.getText().toString();
        inBackground back= new inBackground();
        back.execute("https://openweathermap.org/data/2.5/weather?q="+city+"&appid=b6907d289e10d714a6e88b30761fae22");
        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText);
        resultText=findViewById(R.id.resultText);
         }

}
