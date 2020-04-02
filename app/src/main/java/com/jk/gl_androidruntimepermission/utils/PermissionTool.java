package com.jk.gl_androidruntimepermission.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.runtime.Permission;
import com.yanzhenjie.permission.runtime.setting.SettingRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JK_Liu
 * @description
 * @date 2020/4/2 9:21
 */
public class PermissionTool {

    @SuppressLint("WrongConstant")
    public static void requestPermission(final Context context, final PermissionQuestListener listener, final String... permissionGroup){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //判断是否是同意的
            if (AndPermission.hasPermissions(context, permissionGroup)) {
                if (listener != null)
                    listener.onGranted();
                return;
            }

            // 没有权限，申请权限
            AndPermission.with(context)
                    .runtime()
                    .permission(permissionGroup)
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            //同意
                            if (listener != null){
                                listener.onGranted();
                            }
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(final List<String> data) {
                            //先判断是否勾选了不再询问提醒
                            if (AndPermission.hasAlwaysDeniedPermission(context,data)){

                                List<String> permissionNames = Permission.transformText(context,data);
                                new AlertDialog.Builder(context)
                                        .setTitle("温馨提示：")
                                        .setMessage(listener.onAlwaysDeniedData() + "\n" + permissionNames)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                AndPermission.with(context)
                                                        .runtime()
                                                        .setting()
                                                        .start(1);
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                if (listener != null){
                                                    listener.onDenied(data);
                                                }
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();


                            }else {
                                //重新请求一次
                                requestPermission(context,listener,permissionGroup);
                            }
                        }
                    })
                    .start();
        }
    }

    /**
     * 权限申请监听
     */
    public interface PermissionQuestListener{
        void onGranted(); //允许获得权限后操作
        void onDenied(List<String> data); //拒绝权限后操作
        String onAlwaysDeniedData();; //拒绝后不再提示信息
    }

}
