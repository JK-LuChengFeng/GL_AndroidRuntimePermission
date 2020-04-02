package com.jk.gl_androidruntimepermission.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jk.gl_androidruntimepermission.R;
import com.jk.gl_androidruntimepermission.base.BaseActivty;

public class MainActivity extends BaseActivty{

    private Button btn_native,btn_andpermission,btn_andpermission2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        btn_native = findViewById(R.id.btn_native);
        btn_native.setOnClickListener(this);
        btn_andpermission = findViewById(R.id.btn_andpermission);
        btn_andpermission.setOnClickListener(this);
        btn_andpermission2 = findViewById(R.id.btn_andpermission2);
        btn_andpermission2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_native:
                //原生的权限申请
                intent.setClass(MainActivity.this,NativePermissionActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_andpermission:
                //使用AndPermission 权限申请
                intent.setClass(MainActivity.this, AndPermissionActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_andpermission2:
                //使用AndPermission 权限申请，并且对其封装
                intent.setClass(MainActivity.this,AndPermission2Activity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
