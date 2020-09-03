package com.example.nasaapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;


public class ApodFragment extends Fragment {
    private static ProgressBar progressBar;
    private static Context context;
    public static final String api_key="5bNiKDblUePKicONvBS5b88SifBu7fv6XCFrCppb";
    public static String date,media_type,title,url;
    public static ImageView imageView;
    public static WebView webView;
    public static Button pick_date;
    public static TextView textView;
    private static RequestQueue requestQueue;
    public ApodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apod, container, false);
        imageView = view.findViewById(R.id.image_view);
        textView = view.findViewById(R.id.text_view);
        webView = view.findViewById(R.id.web_view);
        pick_date = view.findViewById(R.id.pick_date);
        requestQueue = Volley.newRequestQueue(view.getContext());
        context = view.getContext();
        progressBar = view.findViewById(R.id.apod_progress_bar);

        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                DialogFragment fragment = new DatePicker();
                fragment.show(getFragmentManager(),"Date Picker");
            }
        });
        return view;
    }
    public static void getDate(int year, int month, int day){

        String mmonth=Integer.toString(month),mday=Integer.toString(day);
        if(month<10){
            mmonth = "0"+Integer.toString(month);
        }
        if(day<10){
            mday = "0"+Integer.toString(day);
        }
        date = Integer.toString(year)+"-"+mmonth+"-"+mday;
        getJson();
    }

    private static void getJson() {
        String URL = "https://api.nasa.gov/planetary/apod?api_key="+api_key+"&date="+date;
        progressBar.setVisibility(View.VISIBLE);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            media_type = response.getString("media_type");
                            title = response.getString("title");
                            url = response.getString("url");

                            setdata();
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("Something went wrong try again...");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private static void setdata() {
        if(media_type.equals("image")){
            Glide.with(context).load(url).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.INVISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    imageView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    return false;
                }
            }).into(imageView);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
        }
    }

}