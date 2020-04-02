package com.jk.gl_androidruntimepermission.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jk.gl_androidruntimepermission.R;
import com.jk.gl_androidruntimepermission.base.BaseActivty;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * AndPermission的使用 不封装。
 */
public class AndPermissionActivity extends BaseActivty implements Rationale<List<String>> {

    private Button btn_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_andpermission);
        btn_request = findViewById(R.id.btn_request);
        btn_request.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request:
                //链式调用权限申请
                requestPermissions(this, Manifest.permission.SEND_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            default:
                break;
        }
    }

    /**
     * 权限申请
     */
    @SuppressLint("WrongConstant")
    private void requestPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断当前android版本是不是大于6.0
            AndPermission.with(context)
                    .runtime()
                    .permission(permissions)
                    .rationale(this)//添加拒绝权限的回调拦截
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            //授予权限(只有当所有权限都申请通过了，才走这个回调)
                            Log.e("123", "-------授予了权限");
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            //拒绝权限（只要有一个权限拒绝了，就走这个回调）
                            Log.e("123", "-------拒绝了权限");
                            if (AndPermission.hasAlwaysDeniedPermission(AndPermissionActivity.this, data)) {
                                //true，弹窗再次向用户索取权限
                                for (int i = 0; i < data.size(); i++) {
                                    Log.e("123","-----:" + data.get(i));
                                }

                                List<String> permissionNames = Permission.transformText(AndPermissionActivity.this, data);
                                String message = "请授权该下的权限" + "\n" + permissionNames;
                                new AlertDialog.Builder(AndPermissionActivity.this)
                                        .setTitle("温馨提示：")
                                        .setMessage(message)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                AndPermission.with(AndPermissionActivity.this)
                                                        .runtime()
                                                        .setting()
                                                        .start(1);
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                            }
                        }
                    }).start();
        }
    }


    /**
     * 拒绝权限的接口
     * rationale相当于是个拦截器，当没有权限时会执行。
     * 这个执行，是指：第一次正常申请权限时，直接弹出原生的申请权限的按钮，用户拒绝后，下次再来的时候，就会先走这个拦截器
     * 拦截器内做提示作用，重新申请权限。
     * 如果呢，用户点击了拒绝，并且下次不再询问提示，则不会走这个回调，直接走onDenied();
     * 这个有什么用呢？比如说，App需要申请全局悬浮窗权限，相比直接跳到授权页，弹个提示框由用户选择是否去授权就显得友好的多。
     * 当showRationale()被回调后说明没有权限，此时开发者必须回调RequestExecutor#execute()来启动设置或者RequestExecutor#cancel()来取消启动设置，
     * 否则将不会回调onGranted()或者onDenied()中的任何一个，也就是说AndPermission将不会有任何响应。
     *
     * @param context
     * @param data
     * @param executor
     */
    @Override
    public void showRationale(Context context, List<String> data, final RequestExecutor executor) {
        //拦截器的作用
        Log.e("123", "---------------这是全部拒绝？");
        List<String> permissionNames = Permission.transformText(context, data);
        String message = "请授权该下的权限" + "\n" + permissionNames;
        new AlertDialog.Builder(this)
                .setTitle("温馨提示：")
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.execute();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.cancel();
                    }
                })
                .show();
    }
}
