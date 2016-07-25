package examples.lcd.sqlitepractice;

import android.app.Application;

/**
 * Created by LCD on 2016/7/16.
 */
public class MyApplication extends Application {

    static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static MyApplication getApplication(){
        return application;
    }
}
