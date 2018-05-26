package hu.spykeh.darts.thedarts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import hu.spykeh.darts.thedarts.R;
import hu.spykeh.darts.thedarts.listeners.OnSwipeTouchListener;
import hu.spykeh.darts.thedarts.model.CricketSectionStatus;
import hu.spykeh.darts.thedarts.model.Team;
import hu.spykeh.darts.thedarts.view.CricketMatchActivity;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class CricketSectionAdapter extends ArrayAdapter<CricketSectionStatus> {
    public CricketSectionAdapter(Context context, List<CricketSectionStatus> sections) {
        super(context, R.layout.cricket_section_element, sections);
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View sectionView = inflater.inflate(R.layout.cricket_section_element, parent, false);


        final CricketSectionStatus sectionStatus = getItem(position);
        ImageView sectionStatusRed = (ImageView) sectionView.findViewById(R.id.section_status_red);
        RelativeLayout sectionButton = (RelativeLayout) sectionView.findViewById(R.id.sectionButton);
        ImageView sectionStatusBlue = (ImageView) sectionView.findViewById(R.id.section_status_blue);

        String text = sectionStatus.getSection() + "";
        if(text.equals("25")){
            text = "Bull";
        }
        ((TextView)sectionButton.findViewById(R.id.sectionText)).setText(text);
        if(sectionStatus.getCurRoundThrowCount() > 0) {
            ((TextView) sectionButton.findViewById(R.id.sectionHitCountText)).setText("x" + sectionStatus.getCurRoundThrowCount());
        }else{
            ((TextView) sectionButton.findViewById(R.id.sectionHitCountText)).setText("");
        }

        sectionButton.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            public void onSwipeRight(){
                int amt = 3;
                if(sectionStatus.getSection() == 25){
                    amt = 2;
                }
                ((CricketMatchActivity)getContext()).onThrowClick(sectionStatus.getSection(), amt);
                sectionStatus.setCurRoundThrowCount(sectionStatus.getCurRoundThrowCount() + amt);
            }

            public void onSwipeLeft(){
                ((CricketMatchActivity)getContext()).onThrowClick(sectionStatus.getSection(), 2);
                sectionStatus.setCurRoundThrowCount(sectionStatus.getCurRoundThrowCount() + 2);
            }

            public void onSingleTap(){
                ((CricketMatchActivity)getContext()).onThrowClick(sectionStatus.getSection(), 1);
                sectionStatus.setCurRoundThrowCount(sectionStatus.getCurRoundThrowCount() + 1);
            }
        });

        if(sectionStatus.getScore1() >= 3 && sectionStatus.getScore2() >=3){
            sectionButton.setEnabled(false);
            sectionButton.setAlpha(0.4f);
        }
        switch (sectionStatus.getScore1()){
            case 0:
                sectionStatusRed.setVisibility(View.INVISIBLE);
                break;
            case 1 :
                sectionStatusRed.setImageResource(R.drawable.cricket_1mark_red);
                break;
            case 2 :
                sectionStatusRed.setImageResource(R.drawable.cricket_2mark_red);
                break;
            default :
                sectionStatusRed.setImageResource(R.drawable.cricket_3mark_red);
                break;
        }

        switch (sectionStatus.getScore2()){
            case 0:
                sectionStatusBlue.setVisibility(View.INVISIBLE);
                break;
            case 1 :
                sectionStatusBlue.setImageResource(R.drawable.cricket_1mark_blue);
                break;
            case 2 :
                sectionStatusBlue.setImageResource(R.drawable.cricket_2mark_blue);
                break;
            default:
                sectionStatusBlue.setImageResource(R.drawable.cricket_3mark_blue);
                break;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sectionButton.findViewById(R.id.sectionHitCountText).getLayoutParams();
        if(((CricketMatchActivity)getContext()).getMatch().getTeamToThrow().getColor() == Team.TeamColor.RED){
            sectionStatusBlue.setAlpha(0.4f);
            sectionStatusRed.setAlpha(1.0f);
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
        }else{
            sectionStatusRed.setAlpha(0.4f);
            sectionStatusBlue.setAlpha(1.0f);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
        }

        sectionView.getLayoutParams().height = parent.getHeight() / 7;


        return sectionView;
    }

    @Override
    public boolean isEnabled(int position){
        return false;
    }
}
