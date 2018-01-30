package hu.spykeh.darts.thedarts.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import hu.spykeh.darts.thedarts.model.CricketMatch;
import hu.spykeh.darts.thedarts.model.Match;
import hu.spykeh.darts.thedarts.model.Player;
import hu.spykeh.darts.thedarts.model.Shot;
import hu.spykeh.darts.thedarts.model.Team;
import hu.spykeh.darts.thedarts.model.Throw;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class DartsDBHelper extends SQLiteOpenHelper{

    private static DartsDBHelper instance;

    private static final String DATABASE_NAME = "TheDarts.db";
    private static final String PLAYERS_TABLE_NAME = "players";
    private static final String PLAYERS_COLUMN_ID = "id";
    private static final String PLAYERS_COLUMN_NAME = "name";
    private static final String PLAYERS_COLUMN_QRCODEID = "qrCodeId";

    private static final String MATCHES_TABLE_NAME = "matches";
    private static final String MATCHES_COLUMN_ID = "id";
    private static final String MATCHES_COLUMN_TYPE = "type";
    private static final String MATCH_PLAYERS_TABLE_NAME = "match_players";
    private static final String MATCH_PLAYERS_MATCH_ID = "matchId";
    private static final String MATCH_PLAYERS_PLAYER_ID = "playerId";
    private static final String MATCH_PLAYERS_TEAM = "team";
    private static final String THROWS_TABLE_NAME = "throws";
    private static final String THROWS_COLUMN_ID = "id";
    private static final String THROWS_COLUMN_ROUND = "round";
    private static final String THROWS_COLUMN_MATCH_ID = "matchId";
    private static final String THROWS_COLUMN_SCORE = "score";
    private static final String THROWS_COLUMN_PLAYER_ID = "playerId";


    public DartsDBHelper(Context context){
        super(context, DATABASE_NAME, null, 9);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + PLAYERS_TABLE_NAME + "(" +
                PLAYERS_COLUMN_ID + " integer PRIMARY KEY, " +
                PLAYERS_COLUMN_NAME + " text, " +
                PLAYERS_COLUMN_QRCODEID + " text" +
                ")";
        db.execSQL(sql);
        sql = "CREATE TABLE " + MATCHES_TABLE_NAME + "(" +
                MATCHES_COLUMN_ID + " integer PRIMARY KEY, " +
                MATCHES_COLUMN_TYPE + " integer" +
                ")";
        db.execSQL(sql);

        sql = "CREATE TABLE " + MATCH_PLAYERS_TABLE_NAME + "(" +
                MATCH_PLAYERS_PLAYER_ID + " integer, " +
                MATCH_PLAYERS_TEAM + " integer," +
                MATCH_PLAYERS_MATCH_ID + " integer, " +
                "FOREIGN KEY (" + MATCH_PLAYERS_MATCH_ID + ") REFERENCES " + PLAYERS_TABLE_NAME + "(" + PLAYERS_COLUMN_ID + ")" +
                ")";
        db.execSQL(sql);

        sql = "CREATE TABLE " + THROWS_TABLE_NAME + "(" +
                THROWS_COLUMN_ID + " integer PRIMARY KEY, " +
                THROWS_COLUMN_MATCH_ID + " integer, " +
                THROWS_COLUMN_ROUND + " integer, " +
                THROWS_COLUMN_PLAYER_ID + " integer," +
                THROWS_COLUMN_SCORE + " integer," +
                "FOREIGN KEY (" + THROWS_COLUMN_MATCH_ID + ") REFERENCES " + MATCHES_TABLE_NAME + "(" + MATCHES_COLUMN_ID + ")" +
                "FOREIGN KEY (" + THROWS_COLUMN_PLAYER_ID + ") REFERENCES " + PLAYERS_TABLE_NAME + "(" + PLAYERS_COLUMN_ID + ")" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + THROWS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MATCH_PLAYERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MATCHES_TABLE_NAME);

        onCreate(db);
    }

    public void insertPlayer(Player player){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PLAYERS_COLUMN_NAME, player.getName());
        String qrCodeId = player.getQrcodeid();
        cv.put(PLAYERS_COLUMN_QRCODEID, qrCodeId);
        db.insert(PLAYERS_TABLE_NAME, null, cv);
    }

    public void removePlayer(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PLAYERS_TABLE_NAME, PLAYERS_COLUMN_ID + "=" + id, null);
    }

    public Player getPlayer(int playerId){
        Player player = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + PLAYERS_TABLE_NAME +
                " WHERE " + PLAYERS_COLUMN_ID + " = " + playerId;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex(PLAYERS_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(PLAYERS_COLUMN_NAME));
            String qrCodeId = cursor.getString(cursor.getColumnIndex(PLAYERS_COLUMN_QRCODEID));
            player = new Player(name);
            player.setId(id);
            player.setQrcodeid(qrCodeId);
            cursor.moveToNext();
        }
        cursor.close();
        return player;

    }

    public ArrayList<Player> getAllPlayers(){
        ArrayList<Player> players = new ArrayList<Player>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + PLAYERS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex(PLAYERS_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(PLAYERS_COLUMN_NAME));
            String qrCodeId = cursor.getString(cursor.getColumnIndex(PLAYERS_COLUMN_QRCODEID));
            Player player = new Player(name);
            player.setId(id);
            player.setQrcodeid(qrCodeId);
            players.add(player);
            cursor.moveToNext();
        }
        cursor.close();
        return players;
    }

    public ArrayList<Match> getMatchesOfPlayer(int playerId){
        ArrayList<Match> matches = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + MATCHES_TABLE_NAME + ", " + MATCH_PLAYERS_TABLE_NAME + " " +
                "WHERE " + MATCH_PLAYERS_MATCH_ID + " = " + MATCHES_COLUMN_ID + " AND " +
                MATCH_PLAYERS_PLAYER_ID + " = " + playerId;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex(MATCHES_COLUMN_ID));
            int pid = cursor.getInt(cursor.getColumnIndex(MATCH_PLAYERS_PLAYER_ID));
            int pTeam = cursor.getInt(cursor.getColumnIndex(MATCH_PLAYERS_TEAM));
            int matchType = cursor.getInt(cursor.getColumnIndex(MATCHES_COLUMN_TYPE));
            Player p = getPlayer(pid);
            boolean isFound = false;
            for(Match m : matches){
                if(m.getId() == id){
                    m.addPlayer(p);
                    m.movePlayerToTeam(p, pTeam == 0 ? Team.TeamColor.RED : Team.TeamColor.BLUE);
                    isFound = true;
                    break;
                }
            }
            if(!isFound) {
                Match match;
                if (matchType == 1)
                     match = new CricketMatch(null, new ArrayList<Player>());
                else
                    match = new Match(null, new ArrayList<Player>());
                match.addPlayer(p);
                match.movePlayerToTeam(p, pTeam == 0 ? Team.TeamColor.RED : Team.TeamColor.BLUE);
                match.setId(id);

                matches.add(match);
            }
            cursor.moveToNext();
        }
        cursor.close();

        return matches;
    }

    public ArrayList<Shot> getShotsOfMatch(int matchId){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + THROWS_TABLE_NAME + " " +
                "WHERE " + THROWS_COLUMN_MATCH_ID + " = " + matchId;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        ArrayList<Shot> shots = new ArrayList<>();
        while(!cursor.isAfterLast()){
            int playerId = cursor.getInt(cursor.getColumnIndex(THROWS_COLUMN_PLAYER_ID));
            int round = cursor.getInt(cursor.getColumnIndex(THROWS_COLUMN_ROUND));
            int score = cursor.getInt(cursor.getColumnIndex(THROWS_COLUMN_SCORE));
            Player p = getPlayer(playerId);
            Throw th = new Throw(p, score);
            th.setRoundNumber(round);
            boolean isCreated = false;
            for(Shot s: shots){
                if(s.getRoundNumber() == round){
                    isCreated = true;
                    s.getThrowList().add(th);
                    break;
                }
            }
            if(!isCreated){
                Shot shot = new Shot();
                shot.getThrowList().add(th);
                shot.setPlayer(th.getPlayer());
                shots.add(shot);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return shots;
    }

    public void insertMatch(Match match){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        int matchId = 0;
        if(matchId != 0){
            cv.put(MATCHES_COLUMN_ID, matchId);
        }
        cv.put(MATCHES_COLUMN_TYPE, match instanceof CricketMatch ? 1 : 0);
        matchId = (int)db.insert(MATCHES_TABLE_NAME, null, cv);
        match.setId(matchId);
        Log.d("myTag", "inserted Match record (id:" + matchId + " )");
        for(Player p : match.getPlayers()) {
            insertMatchPlayer(match, p);
            for(Shot s : p.getShots()){
                for(Throw t: s.getThrowList()){
                    insertThrow(t, matchId, s.getRoundNumber());
                }
            }
        }

    }

    public void insertMatchPlayer(Match match, Player p ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MATCH_PLAYERS_MATCH_ID, match.getId());
        cv.put(MATCH_PLAYERS_PLAYER_ID, p.getId());
        cv.put(MATCH_PLAYERS_TEAM, match.getTeamOfPlayer(p).getColor() == Team.TeamColor.RED ? 1 : 0);

        Log.d("myTag", "inserted Match Player (match id:" + match.getId() + " , playerId : " + p.getId() +" )");
        db.insert(MATCH_PLAYERS_TABLE_NAME, null, cv);

    }

    public void insertThrow(Throw th, int matchId, int round){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(THROWS_COLUMN_PLAYER_ID, th.getPlayer().getId());
        cv.put(THROWS_COLUMN_SCORE, th.getScore());
        cv.put(THROWS_COLUMN_MATCH_ID, matchId);
        cv.put(THROWS_COLUMN_ROUND, round);
        db.insert(THROWS_TABLE_NAME, null, cv);
        Log.d("myTag", "inserted Throw record (matchId:" + matchId + " , playerId : " + th.getPlayer().getId() +", score: " + th.getScore() +" )");
    }

    public static DartsDBHelper getInstance(Context context){
        if(instance == null){
            instance = new DartsDBHelper(context);
        }
        return instance;
    }
}
