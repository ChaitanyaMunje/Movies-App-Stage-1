package com.chinmay.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MovieAdapter extends ArrayAdapter<Movie> {
    public MovieAdapter(@NonNull Context context, ArrayList<Movie>movies) {
        super(context, 0,movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView=convertView;
        if (listitemView==null){
            listitemView= LayoutInflater.from(getContext()).inflate(R.layout.movie_item,parent,false);

        }


        final Movie movie=getItem(position);
        ImageView img=listitemView.findViewById(R.id.img);
        TextView title=listitemView.findViewById(R.id.title);
        assert movie != null;
        title.setText(movie.getTitle());
        String imgurl="http://image.tmdb.org/t/p/w185"+movie.getImgid();
        Picasso.get().load(imgurl).into(img);

        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),DetailActivity.class);
                i.putExtra("title",movie.getTitle());
                i.putExtra("imgid",movie.getImgid());
                i.putExtra("overview",movie.getMovie_Overview());
                i.putExtra("popularity",movie.getMovie_Popularity());
                i.putExtra("release_date",movie.getPublishDate());

                getContext().startActivity(i);
            }
        });


        return listitemView;
    }
}
