package hu.spykeh.darts.thedarts.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import hu.spykeh.darts.thedarts.R;
import hu.spykeh.darts.thedarts.adapters.PlayerSelectionAdapter;
import hu.spykeh.darts.thedarts.db.DartsDBHelper;
import hu.spykeh.darts.thedarts.model.CricketMatch;
import hu.spykeh.darts.thedarts.model.CricketMatchSettings;
import hu.spykeh.darts.thedarts.model.Player;

public class MatchSettingsActivity extends AppCompatActivity {

    private ArrayList<Player> players;
    DartsDBHelper dbHelper;
    private ListView playerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_settings);
        playerListView = (ListView)findViewById(R.id.playerList);
        dbHelper = DartsDBHelper.getInstance(this);
        players = dbHelper.getAllPlayers();
        PlayerSelectionAdapter la = new PlayerSelectionAdapter(this, players);
        playerListView.setAdapter(la);

    }

    public void onStartMatchClick(View v){
        ArrayList<Player> selPlayers = ((PlayerSelectionAdapter)playerListView.getAdapter()).getSelectedPlayers();
        if(selPlayers != null && selPlayers.size() >= 2) {
            Intent intent = new Intent(this, CricketMatchActivity.class);
            CricketMatch match = new CricketMatch(new CricketMatchSettings(), selPlayers);
            match.initBoard();
            intent.putExtra("match", match);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Not enough players", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int res_id = item.getItemId();
        if(res_id == R.id.playerAddAction){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            builder.setTitle("Player name");
            builder.setView(input);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbHelper.insertPlayer(new Player((String)input.getText().toString()));
                    players.add(new Player((String)input.getText().toString()));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        return true;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
