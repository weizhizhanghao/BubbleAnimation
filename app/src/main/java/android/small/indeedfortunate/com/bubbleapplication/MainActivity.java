package android.small.indeedfortunate.com.bubbleapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.small.indeedfortunate.com.bubbleapplication.view.BubbleView;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnClickListener{


    private RelativeLayout bubbleContainer;
    private BubbleView mBubbleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bubbleContainer = (RelativeLayout) findViewById(R.id.bubble_container);
        bubbleContainer.setOnClickListener(this);

        mBubbleView = (BubbleView) findViewById(R.id.window_bubble);
        mBubbleView.startAnimal();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bubble_container:
                Intent intent = new Intent(this, PathViewActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBubbleView.cancelAnimal();
    }
}
