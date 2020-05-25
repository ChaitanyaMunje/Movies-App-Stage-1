package com.chinmay.moviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private final ArrayList<Movie>movies= new ArrayList<>();
    private String sort_type;
    private GridView gridView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=findViewById(R.id.progressbar);
        sharedPreferences = getSharedPreferences("popular_movies",MODE_PRIVATE);
        sort_type = sharedPreferences.getString("sort_type", "popular");
        gridView =  findViewById(R.id.gridview);


        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute(sort_type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_sort_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final SharedPreferences.Editor editor=sharedPreferences.edit();
            int selected = 0;
            sort_type = sharedPreferences.getString("sort_type", "popular");
            if(sort_type.equals("popular"))
                selected = 0;
            else if(sort_type.equals("top_rated"))
                selected = 1;
            builder.setTitle(R.string.dialog_title);
            builder.setSingleChoiceItems(R.array.sort_types, selected,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0)
                                editor.putString("sort_type", "popular");
                            else if (which == 1)
                                editor.putString("sort_type", "top_rated");
                        }
                    });
            builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    editor.apply();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    class FetchMovies extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr;
            String LOG_TAG = "MainActivity";
            try {
                String base_url = "https://api.themoviedb.org/3/movie/";
                String api_key = "9cedd5af3119a926e92dbcf36510417b";
                URL url = new URL(base_url + params[0] + "?api_key=" + api_key);
                Log.d(LOG_TAG,"URL: " + url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if(inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                if(buffer.length() == 0) {
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.d(LOG_TAG, "JSON Parsed: " + moviesJsonStr);

                JSONObject main = new JSONObject(moviesJsonStr);
                JSONArray arr = main.getJSONArray("results");
                JSONObject movie;
                for(int i =0; i < arr.length(); i++) {
                    movie = arr.getJSONObject(i);
                    movies.add(new Movie(movie.getString("original_title"),movie.getString("overview"),movie.getString("vote_average"),movie.getString("poster_path"), movie.getString("release_date")));

                }


            }catch(Exception e){
                Log.e(LOG_TAG, "Error",e);

            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.INVISIBLE);
            MovieAdapter movieAdapter=new MovieAdapter(MainActivity.this,movies);
                gridView.setAdapter(movieAdapter);

        }
    }
}
