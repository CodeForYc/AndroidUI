package learn.yc.androidui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import view.FlodView;

public class MainActivity extends AppCompatActivity {

    FlodView mFlodView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlodView = (FlodView) findViewById(R.id.flodView);
    }
    public void onScale(View view){
        mFlodView.scale(mFlodView.getScaleFator() *0.8f);
    }
    public void onLarge(View view){
        mFlodView.scale(mFlodView.getScaleFator() *1.1f);
    }
}
