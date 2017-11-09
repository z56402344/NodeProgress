package com.nodeprogress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CustomNodeProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mPb = (CustomNodeProgressBar) findViewById(R.id.mPb);
    }


    @Override
    public void onClick(View v) {
        int value = mPb.getProgressbarValue();
        switch (v.getId()){
        case R.id.mTvPlus:
            mPb.setProgressbarValue(value + 10);
            break;
        case R.id.mTvReduce:
            mPb.setProgressbarValue(value - 10);
            break;
        }
    }
}
