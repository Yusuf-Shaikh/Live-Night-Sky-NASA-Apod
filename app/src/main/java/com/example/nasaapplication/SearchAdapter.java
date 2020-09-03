package com.example.nasaapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    private Context mContext;
    private ArrayList<SearchItem>mSearchList;
    private OnItemClickListner onItemClickListner;

    public interface OnItemClickListner{
        void onItemclick(int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        onItemClickListner = listner;
    }

    public SearchAdapter(Context context,ArrayList<SearchItem> searchList){
        mContext = context;
        mSearchList = searchList;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_item,parent,false);
        return  new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        SearchItem currentText = mSearchList.get(position);

        String keyword = currentText.getKeywords();

        holder.mkeyword.setText(keyword);
    }

    @Override
    public int getItemCount() {
        return mSearchList.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder{

        public TextView mkeyword;
        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            mkeyword = itemView.findViewById(R.id.search_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListner != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListner.onItemclick(position);
                        }
                    }
                }
            });
        }
    }
}
