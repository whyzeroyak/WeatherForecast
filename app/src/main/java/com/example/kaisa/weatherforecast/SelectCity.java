package com.example.kaisa.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectCity extends AppCompatActivity {

    public class City {
        private String ChineseName;
        private String spellingName;

        public City(String ChineseName, String spellingName) {
            this.ChineseName = ChineseName;
            this.spellingName = spellingName;
        }

        public String getChineseName() {
            return ChineseName;
        }

        public String getSpellingName() {
            return spellingName;
        }
    }

    public class CityAdapter extends ArrayAdapter<City> {
        private int resourceId;

        public CityAdapter(Context context, int textViewResouceId, List<City> objects) {
            super(context, textViewResouceId, objects);
            resourceId = textViewResouceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            City city = getItem(position);
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.cityName = (TextView) view.findViewById(R.id.city_name);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.cityName.setText(city.getChineseName());
            return view;
        }

        class ViewHolder {
            TextView cityName;
        }
    }

    private List<City> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city_layout);
        initCities();
        CityAdapter adapter = new CityAdapter(SelectCity.this, R.layout.city_item, cityList);
        ListView listView = (ListView) findViewById(R.id.LV1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = cityList.get(position);
                Intent intent = new Intent();
                intent.putExtra("data_return", city.getSpellingName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initCities() {
        City Beijing = new City("北京", "beijing");
        cityList.add(Beijing);
        City Shanghai = new City("上海", "shanghai");
        cityList.add(Shanghai);
        City Wenzhou = new City("温州", "wenzhou");
        cityList.add(Wenzhou);
        City Guangzhou = new City("广州", "guangzhou");
        cityList.add(Guangzhou);
        City Shenzhen = new City("深圳", "shenzhen");
        cityList.add(Shenzhen);
        City Hangzhou = new City("杭州", "hangzhou");
        cityList.add(Hangzhou);
        City Nanjing = new City("南京", "nanjing");
        cityList.add(Nanjing);
        City Wuhan = new City("武汉", "wuhan");
        cityList.add(Wuhan);
        City Chengdu = new City("成都", "chengdu");
        cityList.add(Chengdu);
    }
}
