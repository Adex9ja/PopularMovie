package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.popularmovies.R;

/**
 * Created by ADEOLU on 4/14/2017.
 */
public class MoviePreference {

    public static String getSortOrder(Context vcontext){
        SharedPreferences shared = vcontext.getSharedPreferences(vcontext.getString(R.string.prefrence),Context.MODE_PRIVATE);
        return shared.getString(vcontext.getString(R.string.sorttype),vcontext.getString(R.string.popular));
    }

    public static void setSortOrder(Context vcontext,String sorttype){
        SharedPreferences shared = vcontext.getSharedPreferences(vcontext.getString(R.string.prefrence),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(vcontext.getString(R.string.sorttype),sorttype);
        editor.commit();
    }

}
