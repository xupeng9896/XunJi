package com.example.xupeng.xunji.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.xupeng.xunji.R;

import java.util.ArrayList;
import java.util.List;

public class AlterPasswordActivity extends AppCompatActivity {

    private List<Fragment> fragmentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_password);
        fragmentList=new ArrayList<>();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
