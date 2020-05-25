package com.chinmay.moviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

@SuppressWarnings("unused")
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView title_txt = findViewById(R.id.title);
        TextView release_date=findViewById(R.id.release_date);
        TextView rating_txt = findViewById(R.id.rating);
        TextView overview_txt = findViewById(R.id.overview);
        ImageView poster_img = findViewById(R.id.poster_img);
        String title_str = getIntent().getStringExtra("title");
        String rating_str = getIntent().getStringExtra("popularity");
        String overview_str = getIntent().getStringExtra("overview");
        String img_str = getIntent().getStringExtra("imgid");
        String release_str=getIntent().getStringExtra("release_date");
        String imgurl="http://image.tmdb.org/t/p/w185"+ img_str;
        title_txt.setText(title_str);

        String rating=rating_str+"/"+10;
        release_date.setText(release_str);
        rating_txt.setText(rating);
        overview_txt.setText(overview_str);
        Picasso.get().load(imgurl).into(poster_img);

    }

}
