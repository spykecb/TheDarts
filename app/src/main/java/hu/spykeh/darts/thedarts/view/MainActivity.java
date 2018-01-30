package hu.spykeh.darts.thedarts.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import hu.spykeh.darts.thedarts.R;
import hu.spykeh.darts.thedarts.db.DartsDBHelper;
import hu.spykeh.darts.thedarts.model.CricketMatch;
import hu.spykeh.darts.thedarts.model.Match;
import hu.spykeh.darts.thedarts.model.Player;
import hu.spykeh.darts.thedarts.webapi.WebApiAccess;

public class MainActivity extends AppCompatActivity {

    private Player loggedInPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetPlayerTask().execute();

    }

    public void onNewGameButtonClick(View v){
        Intent intent = new Intent(this, MatchSettingsActivity.class);
        startActivity(intent);
    }

    public void onStatisticsButtonClick(View v){
        /*
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
      //  WebApiAccess acc = WebApiAccess.getInstance();
        //Player pa = acc.getPlayer(5);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("player", loggedInPlayer);
        startActivity(intent);
        */

        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();

    }

    private class GetPlayerTask extends AsyncTask<Void, Void, Player> {
        @Override
        protected Player doInBackground(Void... params) {
            try {
                final String url = WebApiAccess.endPoint + "player/154";
                Log.d("tag", url);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                loggedInPlayer = restTemplate.getForObject(url, Player.class);
                return loggedInPlayer;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Player player) {
            if(player != null) {
                Log.d("log", player.getName());
            }
        }

    }
}
