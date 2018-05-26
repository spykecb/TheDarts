package hu.spykeh.darts.thedarts.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hu.spykeh.darts.thedarts.R;
import hu.spykeh.darts.thedarts.model.Player;
import hu.spykeh.darts.thedarts.view.CricketMatchActivity;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class PlayerBoardAdapter extends ArrayAdapter<Player> {

    private boolean isAlignRight;

    public PlayerBoardAdapter(Context context, ArrayList<Player> players, boolean isAlignRight){
        super(context, R.layout.player_board_element, players);
        this.isAlignRight = isAlignRight;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View pView = inflater.inflate(R.layout.player_board_element, parent, false);
        final Player player = getItem(position);
        final TextView text = (TextView) pView.findViewById(R.id.player_name);
        if(isAlignRight) {
            text.setGravity(Gravity.RIGHT);
        }
        if(((CricketMatchActivity)getContext()).getMatch().getPlayerTothrow().equals(player)){
            text.setTypeface(text.getTypeface(), Typeface.BOLD_ITALIC);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
        text.setText(player.getName() + "(" + String.format("%.2f", player.getMpr()) + ")");

        return pView;
    }

    @Override
    public boolean isEnabled(int position){
        return false;
    }

}

