package com.jk.gl_androidruntimepermission.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jk.gl_androidruntimepermission.MyApp;
import com.jk.gl_androidruntimepermission.R;
import com.jk.gl_androidruntimepermission.base.BaseActivty;

import java.security.Permission;

/**
 * 原生的权限请求
 */
public class NativePermissionActivity extends BaseActivty implements View.OnClickListener {

    private Button btn_call,btn_sotrage,btn_groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 实例化相关数据
     */
    @Override
    public void initData() {

    }

    /**
     * 实例化相关控件
     */
    @Override
    public void initView() {
        setContentView(R.layout.activity_nativepermission);
        btn_call = findViewById(R.id.btn_call);
        btn_call.setOnClickListener(this);
        btn_sotrage = findViewById(R.id.btn_sotrage);
        btn_sotrage.setOnClickListener(this);
        btn_groups = findViewById(R.id.btn_groups);
        btn_groups.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call:
                //拨打电话
                call();
                break;
            case R.id.btn_sotrage:
                //内存
                storage();
                break;
            case R.id.btn_groups:
                //权限组
                groups();
                break;
            default:
                break;
        }
    }

    /**
     * 读取内存权限
     */
    private void storage() {
//        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            //没有权限
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},SRORAGE_PERMISSION);
//        }else {
//            //有权限
//            Toast.makeText(this,"有拍照权限",Toast.LENGTH_SHORT).show();
//        }

        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //有权限
            Toast.makeText(this,"有内存权限",Toast.LENGTH_SHORT).show();
        }else {
            //没有权限
           requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION);
        }
    }

    /**
     * 拨打电话
     */
    private void call() {
        if (checkPermission(Manifest.permission.CALL_PHONE)){
            //权限请求过并同意了
            try {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //权限没请求过，或者权限被拒绝了。
            requestPermission(Manifest.permission.CALL_PHONE,CALL_PERMISSION);
        }
    }


    //申请权限组
    private void groups() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            //无权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            //有权限
            Toast.makeText(this,"该干嘛干嘛去",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"不乖，有的权限没给",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(this,"不乖，权限都没给",Toast.LENGTH_SHORT).show();
                }
                break;
            case CALL_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //同意权限
                    call();
                }else {
                    //拒绝权限
                    showPermissionPop("为了保证功能的正常使用，我们需要获取您的电话权限",true,false);
                }
                break;
            case STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //同意授权
                    Toast.makeText(this,"有内存权限",Toast.LENGTH_SHORT).show();
                }else {
                    //拒绝授权
                    showPermissionPop("为了保证功能的正常使用，我们需要获取您内存权限",true,true);
                }
                break;
            default:
                break;
        }
    }
}
