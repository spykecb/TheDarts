package hu.spykeh.darts.thedarts.listeners;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by spykeh on 2018. 05. 26..
 */

public class OnSwipeTouchListener implements View.OnTouchListener {


    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;


        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            boolean result = false;
            float diff_X = e2.getX() - e1.getX();
            if(Math.abs(diff_X) > SWIPE_THRESHOLD){
                if(diff_X > 0){
                    onSwipeRight();
                }else{
                    onSwipeLeft();
                }
                result = true;
            }
            return result;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            float diff_X = e2.getX() - e1.getX();
            if(Math.abs(diff_X) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                if(diff_X > 0){
                    onSwipeRight();
                }else{
                    onSwipeLeft();
                }
                result = true;
            }
            return result;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onSingleTap();
            return true;
        }
    }

    public void onSingleTap(){

    }

    public void onSwipeRight(){

    }

    public void onSwipeLeft() {

    }
}
