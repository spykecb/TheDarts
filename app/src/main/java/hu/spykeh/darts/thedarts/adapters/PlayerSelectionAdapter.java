package hu.spykeh.darts.thedarts.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import hu.spykeh.darts.thedarts.R;
import hu.spykeh.darts.thedarts.db.DartsDBHelper;
import hu.spykeh.darts.thedarts.model.Player;
import hu.spykeh.darts.thedarts.view.ProfileActivity;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class PlayerSelectionAdapter extends ArrayAdapter<Player> {
    private ArrayList<Player> selectedANYPlayers;
    private ArrayList<Player> selectedTEAM1Players;
    private ArrayList<Player> selectedTEAM2Players;

    public PlayerSelectionAdapter(Context context, ArrayList<Player> players){
        super(context, R.layout.player_select_element, players);
        selectedANYPlayers = new ArrayList<>();
        selectedTEAM1Players = new ArrayList<>();
        selectedTEAM2Players = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View pSelectView = inflater.inflate(R.layout.player_select_element, parent, false);
        final TextView pName = (TextView) pSelectView.findViewById(R.id.selectionPlayerName);
        final CheckBox t1CB = (CheckBox) pSelectView.findViewById(R.id.team1CheckBox);
        final CheckBox t2CB = (CheckBox) pSelectView.findViewById(R.id.team2CheckBox);
        final CheckBox anyCB = (CheckBox) pSelectView.findViewById(R.id.anyCheckBox);
        final Player player = getItem(position);
        pName.setText(player.getName());
        t1CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    t2CB.setChecked(false);
                    anyCB.setChecked(false);
                    removePlayerFromSelection(player);
                    selectedTEAM1Players.add(player);
                }else{
                    selectedTEAM1Players.remove(player);
                }
            }
        });
        t2CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    t1CB.setChecked(false);
                    anyCB.setChecked(false);
                    removePlayerFromSelection(player);
                    selectedTEAM2Players.add(player);
                }
                else{
                    selectedTEAM2Players.remove(player);
                }
            }
        });
        anyCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    t1CB.setChecked(false);
                    t2CB.setChecked(false);
                    removePlayerFromSelection(player);
                    selectedANYPlayers.add(player);
                }else{
                    selectedANYPlayers.remove(player);
                }
            }
        });

        pSelectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!anyCB.isChecked()) {
                    anyCB.setChecked(true);
                    if(selectedANYPlayers.contains(player)) {
                        selectedANYPlayers.add(player);
                    }
                }else if(anyCB.isChecked()){
                    anyCB.setChecked(false);
                    removePlayerFromSelection(player);
                }

            }
        });

        final int pos = position;
        pSelectView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int id = player.getId();
                CharSequence[] choices = new CharSequence[]{"Delete" , "Profile"};
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            DartsDBHelper db = DartsDBHelper.getInstance(getContext());
                            db.removePlayer(id);
                            remove(getItem(pos));
                            notifyDataSetChanged();
                        }else if(which == 1){
                            Intent intent = new Intent(getContext(), ProfileActivity.class);
                            intent.putExtra("player", player);
                            getContext().startActivity(intent);
                        }
                    }
                });
                b.show();
                return true;
            }
        });
        return pSelectView;
    }

    private void removePlayerFromSelection(Player player){
        selectedANYPlayers.remove(player);
        selectedTEAM1Players.remove(player);
        selectedTEAM2Players.remove(player);
    }

    public ArrayList<Player> getSelectedANYPlayers() {
        return selectedANYPlayers;
    }

    public ArrayList<Player> getSelectedTEAM1Players() {
        return selectedTEAM1Players;
    }

    public ArrayList<Player> getSelectedTEAM2Players() {
        return selectedTEAM2Players;
    }
}
