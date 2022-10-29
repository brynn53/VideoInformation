package com.example.myapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author brynn
 * @since 2022/06/18 15:12
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    private SharedPreferences sp;

    protected abstract int initLayout();
    protected abstract void initView();
    protected abstract void initData();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(initLayout());
        initView();
        initData();
    }

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToastSync(String msg) {
        Looper.prepare();
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateTo(Class cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }

    protected void saveStringToSp(String key, String val) {
        sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        sp.edit().putString(key, val).commit();
    }

    protected String getStringFromSp(String key) {
        sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        return sp.getString(key, "default");
    }
}
