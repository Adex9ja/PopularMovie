package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private String title,img,overview,voteAverage,releasedate;
    private int[] genre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.movietitle))){
            title = intent.getStringExtra(getString(R.string.movietitle));
            img = intent.getStringExtra(getString(R.string.movieimg));
            releasedate = intent.getStringExtra(getString(R.string.moviereleasedate));
            voteAverage = intent.getStringExtra(getString(R.string.movierating));
            overview = intent.getStringExtra(getString(R.string.movieoverview));
            genre = intent.getIntArrayExtra(getString(R.string.moviegenre));
        }

        ImageView imageView = (ImageView)findViewById(R.id.backdrop);

        Picasso.with(this).load(NetworkUtils.PIXURL + img).into(imageView);



        ((TextView)findViewById(R.id.movietitle)).setText(title);
        ((TextView)findViewById(R.id.movie_rating)).setText(voteAverage);
        ((TextView)findViewById(R.id.movie_releasedate)).setText(releasedate);
        ((TextView)findViewById(R.id.movieoverview)).setText(overview);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
