package com.tejas.tejas.homeworkplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tejas on 6/13/2017.
 */

public class PlaceAdapter extends ArrayAdapter<Place> {
    Context mContext;
    int myLayoutResourceId;
    ArrayList<Place> mData = null;

    public PlaceAdapter(Context context, int resource, ArrayList<Place> data) {
        super(context, resource, data);

        this.mContext = context;
        this.myLayoutResourceId = resource;
        this.mData = data;
    }

    @Override
    public Place getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        row = inflater.inflate(myLayoutResourceId, parent, false);

        TextView hwView = (TextView) row.findViewById(R.id.medText);
        TextView subjectView = (TextView) row.findViewById(R.id.smallText);
        ImageView imageView = (ImageView) row.findViewById(R.id.imageView);

        Place place = mData.get(position);

        hwView.setText(place.rName);
        subjectView.setText(place.rSubject);

        int resId = mContext.getResources().getIdentifier(place.rNameOfImage, "drawable", mContext.getPackageName());
        imageView.setImageResource(resId);

        return row;
    }
}
