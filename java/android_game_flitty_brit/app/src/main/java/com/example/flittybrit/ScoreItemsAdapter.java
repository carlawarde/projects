package com.example.flittybrit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoreItemsAdapter extends ArrayAdapter<ScoreItem> {

    public ScoreItemsAdapter(Context context, ArrayList<ScoreItem> scores) {
        super(context, 0, scores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScoreItem score = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.score, parent, false);
        }

        TextView tvPosition = convertView.findViewById(R.id.userPosition);
        TextView tvUserDisplayName = convertView.findViewById(R.id.userName);
        TextView tvUserScore = convertView.findViewById(R.id.userScore);

        String positionText = "#"+(position+1);
        String sScore = "High Score: "+score.getScore();
        tvPosition.setText(positionText);
        tvUserDisplayName.setText(score.getDisplayName());
        tvUserScore.setText(sScore);

        return convertView;
    }
}
