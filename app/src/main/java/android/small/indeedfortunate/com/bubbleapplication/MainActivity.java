package android.small.indeedfortunate.com.bubbleapplication;

import android.app.Activity;
import android.os.Bundle;
import android.small.indeedfortunate.com.bubbleapplication.view.BubbleView;

public class MainActivity extends Activity {


    private BubbleView mBubbleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBubbleView = (BubbleView) findViewById(R.id.window_bubble);
        mBubbleView.startAnimal();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBubbleView.cancelAnimal();
    }
}
