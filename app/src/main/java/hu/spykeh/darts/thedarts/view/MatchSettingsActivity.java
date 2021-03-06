package hu.spykeh.darts.thedarts.view;

import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import hu.spykeh.darts.thedarts.R;
import hu.spykeh.darts.thedarts.adapters.PlayerSelectionAdapter;
import hu.spykeh.darts.thedarts.db.DartsDBHelper;
import hu.spykeh.darts.thedarts.model.CricketMatch;
import hu.spykeh.darts.thedarts.model.CricketMatchSettings;
import hu.spykeh.darts.thedarts.model.DTO.PlayerDTO;
import hu.spykeh.darts.thedarts.model.Player;
import hu.spykeh.darts.thedarts.model.Team;
import hu.spykeh.darts.thedarts.webapi.WebApiAccess;

public class MatchSettingsActivity extends AppCompatActivity {

    private ArrayList<Player> players;
    DartsDBHelper dbHelper;
    private ListView playerListView;
    private Spinner gameTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_settings);
        playerListView = (ListView)findViewById(R.id.playerList);
        gameTypeSpinner = (Spinner)findViewById(R.id.gameTypeSpinner);
        dbHelper = DartsDBHelper.getInstance(this);
        players = dbHelper.getAllPlayers();
        PlayerSelectionAdapter la = new PlayerSelectionAdapter(this, players);
        playerListView.setAdapter(la);
        List<String> gameTypes = new ArrayList<>();
        gameTypes.add("Cricket");
        gameTypes.add("Random cricket");
        gameTypes.add("Random cricket II");
        ArrayAdapter<String> gameTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gameTypes);
        gameTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gameTypeSpinner.setAdapter(gameTypeAdapter);

    }

    private class Teams{
        Team t1;
        Team t2;

        Teams(Team t1, Team t2){
            this.t1 = t1;
            this.t2 = t2;
        }
    }

    public void onStartMatchClick(View v){
        PlayerSelectionAdapter psa = ((PlayerSelectionAdapter)playerListView.getAdapter());
        ArrayList<Player> selANYPlayers = psa.getSelectedANYPlayers();
        ArrayList<Player> selTEAM1Players = psa.getSelectedTEAM1Players();
        ArrayList<Player> selTEAM2Players = psa.getSelectedTEAM2Players();
        Teams teams = createTeams(selANYPlayers, selTEAM1Players, selTEAM2Players);

        int size = teams.t1.getPlayers().size() + teams.t2.getPlayers().size();

        if(size >= 2 && teams.t1.getPlayers().size() > 0 && teams.t2.getPlayers().size() > 0) {
            Intent intent = new Intent(this, CricketMatchActivity.class);
            CricketMatch match = new CricketMatch(new CricketMatchSettings(), teams.t1, teams.t2);
            if(gameTypeSpinner.getSelectedItem().toString().equals("Random cricket")){
                Random r = new Random();
                List<Integer> sections = new ArrayList<>();
                for(int i = 0; i < 6; i++){
                    int section = r.nextInt(20) + 1;
                    while(sections.contains(section)){
                        section = r.nextInt(20) + 1;
                    }

                    sections.add(section);
                }
                Collections.sort(sections, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer lhs, Integer rhs) {
                        return rhs.compareTo(lhs);
                    }
                });
                match.initBoard(sections);
            }else if(gameTypeSpinner.getSelectedItem().toString().equals("Random cricket II")){
                Random r = new Random();
                List<Integer> sections = new ArrayList<>();
                int section = r.nextInt(15) + 6;
                for(int i = section; i >= section - 6; i--){
                    sections.add(i);
                }
                Collections.sort(sections, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer lhs, Integer rhs) {
                        return rhs.compareTo(lhs);
                    }
                });
                match.initBoard(sections);
            }else{
                match.initBoard();
            }
            intent.putExtra("match", match);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Not enough players", Toast.LENGTH_SHORT).show();
        }
    }

    private Teams createTeams(ArrayList<Player> selANYPlayers, ArrayList<Player> selTEAM1Players, ArrayList<Player> selTEAM2Players){
        Collections.shuffle(selANYPlayers, new Random(System.nanoTime()));
        Collections.shuffle(selTEAM1Players, new Random(System.nanoTime()));
        Collections.shuffle(selTEAM2Players, new Random(System.nanoTime()));
        Team t1 = new Team(Team.TeamColor.RED);
        Team t2 = new Team(Team.TeamColor.BLUE);
        for(int i = 0; i < selTEAM1Players.size() ; i++){
            t1.addPlayer(selTEAM1Players.get(i));
        }
        for(int i = 0; i < selTEAM2Players.size() ; i++){
            t2.addPlayer(selTEAM2Players.get(i));
        }
        for(int i = 0; i < selANYPlayers.size(); i++){
            if(t1.getPlayers().size() > t2.getPlayers().size()){
                t2.addPlayer(selANYPlayers.get(i));
            }else{
                t1.addPlayer(selANYPlayers.get(i));
            }
        }
        Teams teams = new Teams(t1,t2);
        return teams;
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
                    Player p = new Player((String)input.getText().toString());
                    p.setQrcodeid(UUID.randomUUID().toString());
                    dbHelper.insertPlayer(p);
                    players.add(p);
                    try {
                        new AddPlayerTask().execute(p);
                    }catch (Exception e){
                        Log.e("MainActivity", e.getMessage(), e);
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            final Activity activity = this;
            builder.setNeutralButton("QR Scan", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.initiateScan();
                }
            });
            builder.show();
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            new GetPlayerTask().execute(result.getContents());
        }
    }

    private class GetPlayerTask extends AsyncTask<String, Void, PlayerDTO> {
        @Override
        protected PlayerDTO doInBackground(String... params) {
            try {
                final String url = WebApiAccess.endPoint + "player_qr";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
                Log.d("tag", url + "?qrcodeid=" + params[0]);
                PlayerDTO p = restTemplate.getForObject(url + "?qrcodeid=" + params[0], PlayerDTO.class);
                Log.d("tag", p.getUsername() + " " + p.getName());
                return p;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(PlayerDTO player) {
            if(player != null) {
                Player p = new Player();
                p.setName(player.getName());
                p.setUsername(player.getUsername());
                p.setQrcodeid(player.getQrcodeid());
                dbHelper.insertPlayer(p);
                players.add(p);

            }
        }

    }

    private class AddPlayerTask extends AsyncTask<Player, Void, PlayerDTO> {
        @Override
        protected PlayerDTO doInBackground(Player... params) {
            try {
                PlayerDTO reqPlayer = new PlayerDTO(params[0]);
                final String url = WebApiAccess.endPoint + "player";
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(new MediaType("application","json"));
                HttpEntity<PlayerDTO> requestEntity = new HttpEntity<PlayerDTO>( reqPlayer, requestHeaders);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity<PlayerDTO> p = restTemplate.exchange(url, HttpMethod.POST, requestEntity, PlayerDTO.class);
                return p.getBody();
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(PlayerDTO player) {
            if(player != null) {
                Player p = new Player();
            }
        }

    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
