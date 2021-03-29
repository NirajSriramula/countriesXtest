package com.niraj.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Dao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    List<country> data = new ArrayList<>();
    List<String> names = new ArrayList<>();
    CountryDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listview);
        database = CountryDatabase.getInstance(this);
        data = database.countryDao().getAll();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        final String url ="https://restcountries.eu/rest/v2/all";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int index=0;index<response.length();index++) {
                    try{
                        JSONObject object = response.getJSONObject(index);
                        JSONArray languages = object.getJSONArray("languages");
                        String lang="";
                        for(int i=0;i<languages.length();i++){
                            JSONObject object1 = languages.getJSONObject(i);
                            lang+= object1.getString("name");
                            if(i<languages.length()-1){
                                lang+=",";
                            }
                        }
                        JSONArray array = object.getJSONArray("borders");
                        List<String> exampleList = new ArrayList<String>();
                        for (int i = 0; i < array.length(); i++) {
                            exampleList.add(array.getString(i));
                        }
                        String borders="";
                        int size = exampleList.size();
                        String[] stringArray  = exampleList.toArray(new String[size]);
                        for (int i=0;i<stringArray.length;i++) {
                            borders+=stringArray[i];
                            if(i<stringArray.length-1){
                                borders+=" , ";
                            }
                        }
                        String pop = NumberFormat.getNumberInstance(Locale.US).format(object.getInt("population"));
                        country country = new country(object.getString("name"),object.getString("capital"),
                                object.getString("flag"),object.getString("region"),
                                object.getString("subregion"),pop,
                                lang,borders);
                        database.countryDao().insert(country);
                        data = database.countryDao().getAll();
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "JSON Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        for(int i=0;i<data.size();i++){
            country country = data.get(i);
            names.add(country.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,names);
        listView.setAdapter(adapter);
        queue.add(request);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("name", names.get(position));
                Intent intent = new Intent(MainActivity.this, Details.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}