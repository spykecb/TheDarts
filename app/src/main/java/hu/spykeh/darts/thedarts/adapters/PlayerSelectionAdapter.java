package hu.spykeh.darts.thedarts.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.ArrayList;

import hu.spykeh.darts.thedarts.db.DartsDBHelper;
import hu.spykeh.darts.thedarts.model.Player;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class PlayerSelectionAdapter extends ArrayAdapter<Player> {
    private ArrayList<Player> selectedPlayers;

    public PlayerSelectionAdapter(Context context, ArrayList<Player> players){
        super(context, android.R.layout.simple_list_item_multiple_choice, players);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View sectionView = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        final CheckedTextView cview = (CheckedTextView) sectionView.findViewById(android.R.id.text1);
        final Player player = getItem(position);
        cview.setText(player.getName());

        cview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cview.isChecked()) {
                    cview.setChecked(true);
                    if (selectedPlayers == null) {
                        selectedPlayers = new ArrayList<Player>();
                    }
                    selectedPlayers.add(player);
                }else{
                    cview.setChecked(false);
                    if (selectedPlayers == null) {
                        selectedPlayers = new ArrayList<Player>();
                    }
                    selectedPlayers.remove(player);
                }

            }
        });

        final int pos = position;
        cview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int id = player.getId();
                CharSequence[] choices = new CharSequence[]{"Delete"};
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DartsDBHelper db = DartsDBHelper.getInstance(getContext());
                        db.removePlayer(id);
                        remove(getItem(pos));
                        notifyDataSetChanged();
                    }
                });
                b.show();
                return true;
            }
        });
        return cview;
    }

    public ArrayList<Player> getSelectedPlayers() {
        return selectedPlayers;
    }

}
