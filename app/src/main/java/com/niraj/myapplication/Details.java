package com.niraj.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;

import java.util.Objects;

public class Details extends AppCompatActivity {

    CountryDatabase database;
    TextView name,capital,region,subregion,population,borders,languages;
    ImageView flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String namess = bundle.getString("name");
            database = CountryDatabase.getInstance(this);
            country details = database.countryDao().getData(namess);
            name = findViewById(R.id.country);
            capital = findViewById(R.id.capital);
            region = findViewById(R.id.region);
            subregion = findViewById(R.id.subregion);
            population  = findViewById(R.id.population);
            borders   = findViewById(R.id.borders);
            languages   = findViewById(R.id.languages);
            flag = (ImageView) findViewById(R.id.svgimg);

            name.setText("Country : " + details.getName());
            capital.setText("Capital : " + details.getCapital());
            region.setText("Region : " + details.getRegion());
            subregion.setText("Subregion : " + details.getSubregion());
            population.setText("Population : " + details.getPopulation());
            borders.setText("Borders : " + details.getBorders());
            languages.setText("Languages : " + details.getLanguages());

            //the following statement is not working in the emulator so please install it on a device before using it.
            //SvgLoader.pluck().with(this).load(details.getFlagurl(), flag);



        }
    }
}