package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.JSONObjectUtil;
import com.example.android.popularmovies.utilities.MoviePreference;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickListener {

    private RecyclerView movie_list;
    private ProgressBar mLoadingIndicator;
    private TextView errMessage;
    private MovieAdapter adapter;
    private ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bar = getSupportActionBar();

        movie_list = (RecyclerView)findViewById(R.id.movie_list);
        mLoadingIndicator = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        errMessage = (TextView)findViewById(R.id.tv_error_message_display);

        adapter = new MovieAdapter(this,this);
        GridLayoutManager layout = new GridLayoutManager(this,4);
        movie_list.setLayoutManager(layout);
        movie_list.setHasFixedSize(true);
        movie_list.setAdapter(adapter);

        loadMovie(getString(R.string.popular));

    }

    private void loadMovie(String sorttype) {
        showMovieView();
        changeTitlebar(sorttype);
        new FetchMovie().execute(sorttype,getString(R.string.apikey));
    }

    private void changeTitlebar(String sorttype) {
        if(sorttype.equals(getString(R.string.popular)))
            bar.setTitle(getString(R.string.titlepopular));
        else if(sorttype.equals(getString(R.string.toprated)))
            bar.setTitle(getString(R.string.titletoprated));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_refresh) {
            String sorttype = MoviePreference.getSortOrder(this);
           loadMovie(sorttype);
        }
        else if(id == R.id.nav_popular){
            MoviePreference.setSortOrder(this,getString(R.string.popular));
            loadMovie(getString(R.string.popular));
        }
        else if(id == R.id.nav_toprated){
            MoviePreference.setSortOrder(this,getString(R.string.toprated));
            loadMovie(getString(R.string.toprated));
        }

        return true;
    }

    @Override
    public void onclick(JSONObjectUtil.JSONResponse moviedata) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(getString(R.string.movietitle),moviedata.getOriginal_title());
        intent.putExtra(getString(R.string.movieimg),moviedata.getPoster_path());
        intent.putExtra(getString(R.string.movieoverview),moviedata.getOverview());
        intent.putExtra(getString(R.string.movierating),moviedata.getVote_average());
        intent.putExtra(getString(R.string.moviereleasedate),moviedata.getRelease_date());
        intent.putExtra(getString(R.string.moviegenre),moviedata.getGenre_ids());
        startActivity(intent);
    }

    public class FetchMovie extends AsyncTask<String, Void, List<JSONObjectUtil.JSONResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<JSONObjectUtil.JSONResponse> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String sortorder = params[0];
            String apikey = params[1];
            URL movieRequestURL = NetworkUtils.buildUrl(sortorder,apikey);

            try {
                String movieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                List<JSONObjectUtil.JSONResponse> jsonResponse = new JSONObjectUtil().getMovieListFromJSonResponse( movieResponse);

                return jsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<JSONObjectUtil.JSONResponse> moviedata) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviedata != null) {
                showMovieView();
                adapter.setMovieData(moviedata);
            } else {
                showErrorView();
            }
        }
    }

    
    private void showErrorView() {
        movie_list.setVisibility(View.INVISIBLE);
        errMessage.setVisibility(View.VISIBLE);
    }
    private void showMovieView() {
        errMessage.setVisibility(View.INVISIBLE);
        movie_list.setVisibility(View.VISIBLE);
    }
}
