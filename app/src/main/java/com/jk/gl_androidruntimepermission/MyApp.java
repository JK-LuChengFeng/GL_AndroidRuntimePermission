package com.jk.gl_androidruntimepermission;

import android.app.Application;
import android.content.Context;

/**
 * @author JK_Liu
 * @description
 * @date 2020/3/31 16:40
 */
public class MyApp extends Application {

    private Context context;


    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
