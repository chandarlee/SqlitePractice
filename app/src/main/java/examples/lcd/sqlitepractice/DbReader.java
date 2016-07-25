package examples.lcd.sqlitepractice;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LCD on 2016/7/16.
 */
public class DbReader extends Thread{

    private static AtomicInteger ID = new AtomicInteger(0);

    private static List<DbReader> WORKERS = new ArrayList<>();

    private static final String SELECT = "SELECT * FROM mytable where id = ?";

    public static void generateDbReader(boolean newDbHelper){
        DbReader dbReader = new DbReader(newDbHelper);
        WORKERS.add(dbReader);
        dbReader.start();
        Log.d("DbReader", "total readers: " + WORKERS.size());
    }

    public static void terminateAll(){
        for (DbReader reader : WORKERS){
            reader.terminate();
        }
        WORKERS.clear();
    }

    final boolean newDbHelper;

    private MyDatabase database;

    boolean terminated;

    private DbReader(boolean newDbHelper){
        super("DbReader-" + ID.getAndIncrement());
        this.newDbHelper = newDbHelper;
    }

    @Override
    public void run() {
        super.run();
        Cursor cursor;
        SQLiteDatabase database = getReadableDatabase().getReadableDatabase();
        while (!terminated) {
            for (int index = 1; !terminated;++index){
                cursor = database.rawQuery(SELECT, new String[]{index+""});
                if (cursor == null) break;
                if (cursor.getCount() <=0){
                    cursor.close();
                    break;
                }
                printCursor(cursor);
                cursor.close();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void terminate(){
        terminated = true;
    }

    private MyDatabase getReadableDatabase(){
        if (database != null) return database;
        if (newDbHelper){
            database = new MyDatabase(MyApplication.getApplication());
        }else {
            database = MyDatabaseHolder.getInstance().getDatabase();
        }
        return database;
    }

    private void printCursor(Cursor cursor){
        cursor.moveToFirst();
        if (cursor.getCount() <=0) return;
        int columns = cursor.getColumnCount();
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < columns; ++index){
            builder.append(cursor.getColumnName(index));
            builder.append("=");
            if (index == 0){
                builder.append(cursor.getInt(0));
            }else {
                builder.append(cursor.getString(index));
            }
            if (index != columns - 1){
                builder.append(", ");
            }
        }
        Log.d(getName(), builder.toString());
    }
}
