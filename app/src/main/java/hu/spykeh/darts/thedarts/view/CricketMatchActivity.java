package hu.spykeh.darts.thedarts.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import hu.spykeh.darts.thedarts.R;
import hu.spykeh.darts.thedarts.adapters.CricketSectionAdapter;
import hu.spykeh.darts.thedarts.adapters.PlayerBoardAdapter;
import hu.spykeh.darts.thedarts.adapters.ShotAdapter;
import hu.spykeh.darts.thedarts.db.DartsDBHelper;
import hu.spykeh.darts.thedarts.model.CricketMatch;
import hu.spykeh.darts.thedarts.model.CricketMatchSettings;
import hu.spykeh.darts.thedarts.model.DTO.MatchDTO;
import hu.spykeh.darts.thedarts.model.Player;
import hu.spykeh.darts.thedarts.model.Shot;
import hu.spykeh.darts.thedarts.model.Team;
import hu.spykeh.darts.thedarts.model.Throw;
import hu.spykeh.darts.thedarts.webapi.WebApiAccess;

public class CricketMatchActivity extends AppCompatActivity {

    private ListView sections;
    private ListView team1Players;
    private ListView team2Players;
    private ListView shotHistoryR;
    private ListView shotHistoryB;
    private CricketMatch match;

    private TextView roundNumberText;
    private TextView gameModeText;
    private TextView redScoreText;
    private TextView blueScoreText;
    private ImageView scoreBoardRed;
    private ImageView scoreBoardBlue;

    private ArrayList<Shot> team1Shots;
    private ArrayList<Shot> team2Shots;
    private ArrayList<Throw> currentRoundThrows;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Intent intent = getIntent();
        setMatch((CricketMatch) intent.getSerializableExtra("match"));
        if(getMatch() != null){
            gameModeText = (TextView) findViewById(R.id.gameModeText);
            gameModeText.setText("Cricket");
            redScoreText = (TextView) findViewById(R.id.text_red_score);
            blueScoreText = (TextView) findViewById(R.id.text_blue_score);
            sections = (ListView) findViewById(R.id.sectionList);
            CricketSectionAdapter csa = new CricketSectionAdapter(this,getMatch().getScoreBoard());
            sections.setAdapter(csa);

            team1Players = (ListView) findViewById(R.id.playerList_team1);
            team2Players = (ListView) findViewById(R.id.playerList_team2);
            team1Players.setAdapter(new PlayerBoardAdapter(this, getMatch().getPlayersOfTeam(Team.TeamColor.RED), false));
            team2Players.setAdapter(new PlayerBoardAdapter(this, getMatch().getPlayersOfTeam(Team.TeamColor.BLUE), true));

            shotHistoryR = (ListView) findViewById(R.id.shotHistory_red);
            shotHistoryB = (ListView) findViewById(R.id.shotHistory_blue);

            team1Shots = getMatch().getShotsOfTeam(Team.TeamColor.RED);
            team2Shots = getMatch().getShotsOfTeam(Team.TeamColor.BLUE);
            shotHistoryR.setAdapter(new ShotAdapter(this, team1Shots, Team.TeamColor.RED));
            shotHistoryB.setAdapter(new ShotAdapter(this, team2Shots, Team.TeamColor.BLUE));

            currentRoundThrows = new ArrayList<>();
             roundNumberText = (TextView)findViewById(R.id.roundNumberText);
            scoreBoardRed = (ImageView) findViewById(R.id.scoreBoard_red);
            scoreBoardBlue = (ImageView) findViewById(R.id.scoreBoard_blue);

            updateViews();
        }


    }

    public void onThrowClick(View v, int section){
        Player playerToThrow = match.getPlayerTothrow();
        Throw _throw = new Throw(playerToThrow, section);
        getMatch().hitSection(section);

        updateMarkViews();
        if(currentRoundThrows.size() < 9) {
            currentRoundThrows.add(_throw);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.match_menu, menu);
        return true;
    }

    private void endMatch(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Match ended");
        DartsDBHelper db = DartsDBHelper.getInstance(this);
        db.insertMatch(match);
        new GetPlayerTask().execute();
        b.setMessage("Winner: " + match.getWinner().toString());
        b.show();
    }

    private class GetPlayerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                final String url = WebApiAccess.endPoint + "match";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                MatchDTO matchDTO = new MatchDTO(match);
                Gson gson = new Gson();
                String json = gson.toJson(matchDTO);
                Log.d("tag", json);
                restTemplate.put(url,matchDTO );
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int res_id = item.getItemId();
        if(res_id == R.id.nextRoundButton){
            Shot shot = new Shot(new ArrayList<>(currentRoundThrows), match.getRoundNumber());
            match.addShot(match.getPlayerTothrow(), shot);
            scrollListViewToBottom(shotHistoryR);
            scrollListViewToBottom(shotHistoryB);

            match.pickNextPlayerToThrow();
            Toast.makeText(this, match.getPlayerTothrow().getName() + " to throw!", Toast.LENGTH_SHORT).show();
            currentRoundThrows.clear();
            if(match.getWinner() != null){
               endMatch();
            }

            updateViews();
        } else if(res_id == R.id.undoButton){
            if(currentRoundThrows.size() > 0) {
                currentRoundThrows.clear();
                match.revertScoreBoard();
                updateViews();
            }else{
                match.undoScoreBoard();
                updateViews();
            }
        }
        return true;
    }

    private void updateMarkViews(){
        redScoreText.setText("" + getMatch().getTeamScore(Team.TeamColor.RED));
        blueScoreText.setText("" + getMatch().getTeamScore(Team.TeamColor.BLUE));
        sections.setAdapter(new CricketSectionAdapter(this, getMatch().getScoreBoard()));
    }

    private void updateViews(){
        updateMarkViews();

        roundNumberText.setText("Round "+ (match.getRoundNumber() + 1) + "/" + ((CricketMatchSettings)getMatch().getSettings()).getMaxRound());

        team1Shots = getMatch().getShotsOfTeam(Team.TeamColor.RED);
        team2Shots = getMatch().getShotsOfTeam(Team.TeamColor.BLUE);
        shotHistoryR.setAdapter(new ShotAdapter(this, team1Shots, Team.TeamColor.RED));
        shotHistoryB.setAdapter(new ShotAdapter(this, team2Shots, Team.TeamColor.BLUE));

        ((PlayerBoardAdapter)team1Players.getAdapter()).notifyDataSetChanged();
        ((PlayerBoardAdapter)team2Players.getAdapter()).notifyDataSetChanged();

        if(getMatch().getTeamToThrow().getColor() == Team.TeamColor.RED) {
            scoreBoardBlue.setAlpha(0.4f);
            scoreBoardRed.setAlpha(1.0f);
        }else{
            scoreBoardRed.setAlpha(0.4f);
            scoreBoardBlue.setAlpha(1.0f);
        }
    }

    private void scrollListViewToBottom(ListView lv) {
        final ListView lv_ = lv;
        lv_.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lv_.setSelection(lv_.getAdapter().getCount() - 1);
            }
        });
    }

    private void scrollListViewToPos(ListView lv, int pos) {
        final ListView lv_ = lv;
        final int pos_ = pos;
        lv_.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lv_.setSelection(pos_);
            }
        });
    }

    public CricketMatch getMatch() {
        return match;
    }

    public void setMatch(CricketMatch match) {
        this.match = match;
    }


}
