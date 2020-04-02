package com.jk.gl_androidruntimepermission.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jk.gl_androidruntimepermission.R;
import com.jk.gl_androidruntimepermission.base.BaseActivty;
import com.jk.gl_androidruntimepermission.utils.PermissionTool;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * AndPermission  使用封装
 */
public class AndPermission2Activity extends BaseActivty {

    private Button btn_req, btn_req2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_andpermission2);
        btn_req = findViewById(R.id.btn_req);
        btn_req.setOnClickListener(this);
        btn_req2 = findViewById(R.id.btn_req2);
        btn_req2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_req:
                PermissionTool.requestPermission(this, new PermissionTool.PermissionQuestListener() {
                    @Override
                    public void onGranted() {
                        Toast.makeText(AndPermission2Activity.this, "权限请求成功了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDenied(List<String> data) {
                        List<String> permissionNames = Permission.transformText(AndPermission2Activity.this, data);
                        Toast.makeText(AndPermission2Activity.this, "权限请求被拒绝" + permissionNames, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public String onAlwaysDeniedData() {
                        return "为保证功能正常使用，需要获得以下权限：";
                    }
                }, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                break;
            case R.id.btn_req2:
                //测试：AndPermission.hasAlwaysDeniedPermission这个方法
                //这个方法，在从没获取过该权限的时候，会返回false
                //只有当获取过一次权限，并且拒绝了，不再提醒的情况下，这个方法才会返回true。

                //本意上，跟ActivityCompat.shouldShowRequestPermissionRationale()方法类似
                // 1，在允许询问时返回true ；
                // 2，在权限通过 或者权限被拒绝并且禁止询问时返回false 但是有一个例外，就是重来没有询问过的时候，也是返回的false
                // 所以单纯的使用shouldShowRequestPermissionRationale去做什么判断，是没用的，只能在请求权限回调后再使用。
                // Google的原意是： 1，没有申请过权限，申请就是了，所以返回false；
                // 2，申请了用户拒绝了，那你就要提示用户了，所以返回true；
                // 3，用户选择了拒绝并且不再提示，那你也不要申请了，也不要提示用户了，所以返回false；
                // 4，已经允许了，不需要申请也不需要提示，所以返回false；

                if (ContextCompat.checkSelfPermission(AndPermission2Activity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    //没有权限
                    ActivityCompat.requestPermissions(AndPermission2Activity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                } else {
                    //有权限
                    Toast.makeText(AndPermission2Activity.this, "已获取电话权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(AndPermission2Activity.this, "已获取电话权限", Toast.LENGTH_SHORT).show();
                }else {
                    if (AndPermission.hasAlwaysDeniedPermission(this, Manifest.permission.SEND_SMS)) {
                        //表示拒绝过
                        Toast.makeText(AndPermission2Activity.this, "已拒绝过且不再提醒", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AndPermission2Activity.this, "拒绝了权限", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }
}
