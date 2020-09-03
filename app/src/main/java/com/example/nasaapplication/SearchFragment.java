package com.example.nasaapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchAdapter.OnItemClickListner {

    private String nasa_id,title;
    JSONArray items;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private ArrayList<SearchItem> searchList;
    private RequestQueue requestQueue;
    private SearchView searchView;
    private TextView textView;
    private Context context;
    private ArrayList<String>links;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = (SearchView) view.findViewById(R.id.search_bar);
        textView = view.findViewById(R.id.text_view);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        searchList = new ArrayList<>();
        links = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(view.getContext());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                getJson(newText,view);

                return false;
            }
        });

        return view;
    }

    private void getJson(String s,View view) {

        String URL = "https://images-api.nasa.gov/search?q="+s;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject collection = response.getJSONObject("collection");
                            items = collection.getJSONArray("items");

                        } catch (JSONException e) {
                        }

                        try {

                            links.clear();
                            searchList.clear();
                            for(int j=0;j<items.length();j++){
                                nasa_id = items.getJSONObject(j).getJSONArray("data").getJSONObject(0).getString("nasa_id");
                                title = items.getJSONObject(j).getJSONArray("data").getJSONObject(0).getString("title");
                                links.add(items.getJSONObject(j).getJSONArray("links").getJSONObject(0).getString("href"));
                                searchList.add(new SearchItem(title));
                            }

                            searchAdapter = new SearchAdapter(context,searchList);
                            recyclerView.setAdapter(searchAdapter);
                            searchAdapter.setOnItemClickListner(SearchFragment.this::onItemclick);
                        } catch (JSONException e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onItemclick(int position) {
        Intent searchImgIntent = new Intent(context,SearchResultsActivity.class);
        searchImgIntent.putExtra("nasa_id", links.get(position));
        startActivity(searchImgIntent);
        searchList.clear();
    }
}