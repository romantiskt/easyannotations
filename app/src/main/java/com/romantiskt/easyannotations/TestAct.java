package com.romantiskt.easyannotations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.romantiskt.easyannotations.annotations.Bind;
import com.romantiskt.easyannotations.core.BindUtil;

/**
 * Created by romantiskt on 2018/8/15.
 */

public class TestAct extends AppCompatActivity {
    @Bind(R.id.tv)
    TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindUtil.bind(this);
        tv.setText("Are you OK");
    }
}
