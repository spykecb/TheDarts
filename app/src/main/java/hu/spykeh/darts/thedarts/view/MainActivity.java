package hu.spykeh.darts.thedarts.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import hu.spykeh.darts.thedarts.R;
import hu.spykeh.darts.thedarts.db.DartsDBHelper;
import hu.spykeh.darts.thedarts.model.CricketMatch;
import hu.spykeh.darts.thedarts.model.Match;
import hu.spykeh.darts.thedarts.model.Player;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNewGameButtonClick(View v){
        Intent intent = new Intent(this, MatchSettingsActivity.class);
        startActivity(intent);
    }

    public void onStatisticsButtonClick(View v){
        DartsDBHelper db = DartsDBHelper.getInstance(this);
        for(Player p : db.getAllPlayers()){
            for(Match m : db.getMatchesOfPlayer(p.getId())){
                if(m instanceof CricketMatch){
                    Log.d("myTag", "Match played by " + p.getName() + " "+  p.getMpr());
                }else{
                    Log.d("myTag", "Match played by " + p.getName());
                }
            }
        }

    }
}
