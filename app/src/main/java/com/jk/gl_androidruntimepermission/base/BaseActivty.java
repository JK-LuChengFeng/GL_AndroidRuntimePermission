package com.jk.gl_androidruntimepermission.base;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jk.gl_androidruntimepermission.MyApp;

/**
 * @author JK_Liu
 * @description
 * @date 2020/3/31 16:38
 */
public abstract class BaseActivty extends AppCompatActivity implements View.OnClickListener {

    public final static int CALL_PERMISSION = 1001; //拨打电话
    public final static int CAMERA_PERMISSION = 1002; //拨打电话
    public final static int STORAGE_PERMISSION = 1003; //拨打电话

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    public void showPermissionPop(String msg,boolean positiveBtn,boolean nagativeBtn){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示：");
        builder.setMessage(msg);
        if (positiveBtn){
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (nagativeBtn){
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }


        builder.show();


    }


    public boolean checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            //表示没有权限
            return false;
        } else {
            //表示有权限
            return true;
        }
    }


    public void requestPermission(String permission, int requestCode) {
        //shouldShowRequestPermissionRationale 1，在允许询问时返回true ； 2，在权限通过 或者权限被拒绝并且禁止询问时返回false
        // 但是有一个例外，就是重来没有询问过的时候，也是返回的false
        // 所以单纯的使用shouldShowRequestPermissionRationale去做什么判断，是没用的，只能在请求权限回调后再使用。
        // Google的原意是： 1，没有申请过权限，申请就是了，所以返回false；
        // 2，申请了用户拒绝了，那你就要提示用户了，所以返回true；
        // 3，用户选择了拒绝并且不再提示，那你也不要申请了，也不要提示用户了，所以返回false；
        // 4，已经允许了，不需要申请也不需要提示，所以返回false；
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,permission)){
//            Log.e("123","---->" + true);
//        }else {
//            Log.e("123","---->" + false);
//        }
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    protected abstract void initData();

    protected abstract void initView();

    @Override
    public abstract void onClick(View v);

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case CALL_PERMISSION:
//               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                   //同意授权
//
//               }else {
//                   //不同意授权
//
//               }
//                break;
//            default:
//                break;
//        }
//
//
//    }


    //
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case
//        }
//    }
}
