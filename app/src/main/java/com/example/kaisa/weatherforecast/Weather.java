package com.example.kaisa.weatherforecast;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Weather extends AppCompatActivity implements View.OnClickListener {

    private android.os.Handler handler = new android.os.Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String[] Result = (String[]) msg.obj;
                TextView textView1 = (TextView) findViewById(R.id.TV1);
                TextView textView2 = (TextView) findViewById(R.id.TV2);
                TextView textView4 = (TextView) findViewById(R.id.TV4);
                TextView textView5 = (TextView) findViewById(R.id.TV5);
                TextView textView6 = (TextView) findViewById(R.id.TV6);
                TextView textView7 = (TextView) findViewById(R.id.TV7);
                TextView textView8 = (TextView) findViewById(R.id.TV8);
                TextView textView9 = (TextView) findViewById(R.id.TV9);
                ImageView imageView1 = (ImageView) findViewById(R.id.IV1);

                Calendar calendar = Calendar.getInstance();
                String Date = getDate(calendar);
                textView1.setText(Date);
                textView2.setText(Result[0]);
                textView4.setText(Result[1] + "(实时)");
                textView5.setText("湿度" + " " + Result[2]);
                textView6.setText("体感温度" + " " + Result[3] + "℃");
                textView7.setText(Result[4] + Result[5] + "级");
                textView8.setText("更新时间:" + " " + Result[6]);
                textView9.setText(Result[7]);
                if ("晴".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.sunny);
                } else if ("多云".equals(Result[1]) || "晴间多云".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.cloudy);
                } else if ("阴".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.overcast);
                } else if ("有风".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.windy);
                } else if ("阵雨".equals(Result[1]) || "小雨".equals(Result[1]) || "雨".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.lightrain);
                } else if ("中雨".equals(Result[1]) || "大雨".equals(Result[1]) || "暴雨".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.heavyrain);
                } else if ("雷阵雨".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.thundershower);
                } else if ("太阳雨".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.sunnyrain);
                } else if ("阵雪".equals(Result[1]) || "小雪".equals(Result[1]) || "雪".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.lightsnow);
                } else if ("中雪".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.moderatesnow);
                } else if ("大雪".equals(Result[1]) || "暴雪".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.heavysnow);
                } else if ("雨夹雪".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.sleet);
                } else if ("雾".equals(Result[1]) || "霾".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.fog);
                } else if ("冰雹".equals(Result[1])) {
                    imageView1.setImageResource(R.drawable.haily);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        weatherRun("beijing");
        Button button = (Button) findViewById(R.id.BT1);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BT1:
                Intent intent = new Intent(Weather.this, SelectCity.class);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String spellingName = data.getStringExtra("data_return");
                    weatherRun(spellingName);
                }
                break;
            default:
                break;
        }
    }

    public void weatherRun(final String city) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    initView(city);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 30 * 60 * 1000, TimeUnit.MILLISECONDS);
    }

    private void initView(final String city) {

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://free-api.heweather.net/s6/weather/now?location="
                + city + "&key=e6fafde008d3422990d311c028a2d6f9";
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String[] res = new String[8];
                            JSONArray jsonArray = response.getJSONArray("HeWeather6");
                            JSONObject temp = jsonArray.getJSONObject(0);
                            JSONObject basic = temp.getJSONObject("basic");
                            JSONObject update = temp.getJSONObject("update");
                            JSONObject now = temp.getJSONObject("now");
                            String tmp = now.getString("tmp");
                            String cond_txt = now.getString("cond_txt");
                            String hum = now.getString("hum");
                            String fl = now.getString("fl");
                            String wind_dir = now.getString("wind_dir");
                            String wind_sc = now.getString("wind_sc");
                            String time = update.getString("loc");
                            String city_ch = basic.getString("location");

                            res[0] = tmp;
                            res[1] = cond_txt;
                            res[2] = hum;
                            res[3] = fl;
                            res[4] = wind_dir;
                            res[5] = wind_sc;
                            res[6] = time;
                            res[7] = city_ch;
                            Message msg = new Message();
                            msg.obj = res;
                            msg.what = 0;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mRequestQueue.add(mJsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getDate(Calendar calendar) {
        String mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);   //获取日期的月
        String mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));  //获取日期的天
        String mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));   //获取日期的星期
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return (mMonth + "月" + mDay + "日" + "   " + "星期" + mWay);
    }

}




