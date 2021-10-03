package ca.mohawk.hanushchak.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Movie {
    public String Title;
    public String Year;
    public String imdbID;
    public String Poster;
    public String Runtime;
    public String Genre;
    public String Actors;
    public String Plot;

    public String toString() {
        return Title + " (" + Year + ")";
    }
}
