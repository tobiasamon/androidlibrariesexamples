package de.tobiasamon.demoapplicationbutterknife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tv1) protected TextView textView1;
    @Bind(R.id.tv2) protected TextView textView2;
    @Bind(R.id.tv3) protected TextView textView3;
    @Bind(R.id.tv4) protected TextView textView4;
    @Bind(R.id.tv5) protected TextView textView5;
    @Bind(R.id.tv6) protected TextView textView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateData();
    }

    private void updateData() {
        textView1.setText("TextView1");
        textView2.setText("TextView2");
        textView3.setText("TextView3");
        textView4.setText("TextView4");
        textView5.setText("TextView5");
        textView6.setText("TextView6");
    }
}
