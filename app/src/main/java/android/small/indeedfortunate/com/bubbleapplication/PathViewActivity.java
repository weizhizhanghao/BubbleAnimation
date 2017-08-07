package android.small.indeedfortunate.com.bubbleapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PathViewActivity extends Activity implements View.OnClickListener{

    private Button mMenuButton;
    private Button mItemButton1;
    private Button mItemButton2;
    private Button mItemButton3;
    private Button mItemButton4;
    private Button mItemButton5;

    private boolean mIsMenuOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_view);
        initView();
    }

    private void initView(){
        mMenuButton = (Button) findViewById(R.id.menu);
        mMenuButton.setOnClickListener(this);

        mItemButton1 = (Button) findViewById(R.id.item1);
        mItemButton1.setOnClickListener(this);

        mItemButton2 = (Button) findViewById(R.id.item2);
        mItemButton2.setOnClickListener(this);

        mItemButton3 = (Button) findViewById(R.id.item3);
        mItemButton3.setOnClickListener(this);

        mItemButton4 = (Button) findViewById(R.id.item4);
        mItemButton4.setOnClickListener(this);

        mItemButton5 = (Button) findViewById(R.id.item5);
        mItemButton5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mMenuButton){
            if (!mIsMenuOpened){
                mIsMenuOpened = true;
                // do open animation
                doAnimationOpen(mItemButton1, 0, 5, 300);
                doAnimationOpen(mItemButton2, 1, 5, 300);
                doAnimationOpen(mItemButton3, 2, 5, 300);
                doAnimationOpen(mItemButton4, 3, 5, 300);
                doAnimationOpen(mItemButton5, 4, 5, 300);
            } else {
                mIsMenuOpened = false;
                //do close animation
                doAnimateClose(mItemButton1, 0, 5, 300);
                doAnimateClose(mItemButton2, 1, 5, 300);
                doAnimateClose(mItemButton3, 2, 5, 300);
                doAnimateClose(mItemButton4, 3, 5, 300);
                doAnimateClose(mItemButton5, 4, 5, 300);
            }
        } else {
            Toast.makeText(this, "你在点击button", Toast.LENGTH_SHORT).show();
        }
    }

    private void doAnimationOpen(View view, int index, int total, int radius){
        if (view.getVisibility() != View.VISIBLE){
            view.setVisibility(View.VISIBLE);
        }

        double degree = Math.toRadians(90) / (total - 1) * index;
        int translationX = -(int) (radius * Math.sin(degree));
        int translationY = -(int) (radius * Math.cos(degree));

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1));
        set.setDuration(1 * 500).start();
    }

    public void doAnimateClose(final View view, int index, int total, int radius){
        if (view.getVisibility() != View.VISIBLE){
            view.setVisibility(View.VISIBLE);
        }

        double degree = Math.PI * index / ((total - 1) * 2);
        int translationX = -(int) (radius * Math.sin(degree));
        int translationY = -(int) (radius * Math.cos(degree));
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX, 0),
                ObjectAnimator.ofFloat(view, "translationY", translationY, 0),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.1f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.1f),
                ObjectAnimator.ofFloat(view, "alpha", 1, 0f));
        set.setDuration(1 * 500).start();
    }

}
