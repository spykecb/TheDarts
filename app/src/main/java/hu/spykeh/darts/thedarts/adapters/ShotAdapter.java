package hu.spykeh.darts.thedarts.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import hu.spykeh.darts.thedarts.R;
import hu.spykeh.darts.thedarts.model.Shot;
import hu.spykeh.darts.thedarts.model.Team;

/**
 * Created by spykeh on 2017. 10. 23..
 */

public class ShotAdapter extends ArrayAdapter<Shot>{
    Team.TeamColor color;
    List<Shot> shots;
    public ShotAdapter(Context context, List<Shot> shots) {
        this(context,shots, null);
    }

    public ShotAdapter(Context context, List<Shot> shots, Team.TeamColor color) {
        super(context, R.layout.shot_element, shots);
        this.shots = shots;
        this.color = color;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = LayoutInflater.from(getContext());
        View view = inf.inflate(R.layout.shot_element, parent, false);
        Shot shot = getItem(position);
        TextView rNum = (TextView) view.findViewById(R.id.shot_roundNumber_text);
        TextView hits = (TextView) view.findViewById(R.id.shot_hits_text);
        rNum.setText("R" + (shot.getRoundNumber() + 1));
        hits.setText(shot.toString());
        if(color == Team.TeamColor.RED){
            rNum.setTextColor(Color.parseColor("#C4030D"));
            hits.setTextColor(Color.parseColor("#C4030D"));
        }else if(color == Team.TeamColor.BLUE){
            rNum.setTextColor(Color.parseColor("#0761c1"));
            hits.setTextColor(Color.parseColor("#0761c1"));
        }
        return view;
    }
}
