package examples.lcd.sqlitepractice;

/**
 * Created by LCD on 2016/7/16.
 */
public class MyDatabaseHolder {

    private static final class Holder{
        private static final MyDatabaseHolder INSTANCE = new MyDatabaseHolder();
    }

    public static MyDatabaseHolder getInstance(){
        return Holder.INSTANCE;
    }

    MyDatabase database;

    private MyDatabaseHolder(){
        database = new MyDatabase(MyApplication.getApplication());
    }

    public MyDatabase getDatabase(){
        return database;
    }
}
