package examples.lcd.sqlitepractice;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LCD on 2016/7/16.
 */
public class DbWriter extends Thread{

    private static AtomicInteger ID = new AtomicInteger(0);

    private static List<DbWriter> WORKERS = new ArrayList<>();

    public static void generateDbWriter(boolean newDbHelper){
        generateDbWriter(newDbHelper, false);
    }

    public static void generateDbWriter(boolean newDbHelper, boolean withTransaction){
        DbWriter dbWriter = new DbWriter(newDbHelper, withTransaction);
        WORKERS.add(dbWriter);
        dbWriter.start();
        Log.d("DbWriter", "total writers: " + WORKERS.size());
    }

    public static void terminateAll(){
        for (DbWriter writer : WORKERS){
            writer.terminate();
        }
        WORKERS.clear();
    }

    final boolean newDbHelper;

    final boolean withTransaction;

    private MyDatabase database;

    boolean terminated;

    private DbWriter(boolean newDbHelper, boolean withTransaction){
        super("DbWriter-" + ID.getAndIncrement());
        this.newDbHelper = newDbHelper;
        this.withTransaction = withTransaction;
    }

    @Override
    public void run() {
        super.run();
        SQLiteDatabase database = getWritableDatabase().getWritableDatabase();
        String sql = getInsertSql();
        int loop = 0;
        while (!terminated) {
            try {
                if (withTransaction){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && database.isWriteAheadLoggingEnabled()){
                        database.beginTransactionNonExclusive();
                    }else {
                        database.beginTransaction();
                    }
                    for (loop = 0; loop < 1000000000 && !terminated; loop++){
                        if (loop % 100000 == 0){
                            database.execSQL(sql);
                        }
                    }
                }else {
                    database.execSQL(sql);
                }
                if (withTransaction){
                    database.setTransactionSuccessful();
                }
                Log.d(getIdentification(), "insert one row, loop: " + loop);
            }catch (Exception e){
                Log.d(getIdentification(), "insert error: " + e.getMessage());
            }finally {
                if (withTransaction){
                    database.endTransaction();
                }
            }

        }
    }

    private String getIdentification(){
        return getName() + ", newDbHelper:" + newDbHelper + ", withTransaction:" + withTransaction;
    }

    private void terminate(){
        terminated = true;
    }

    private MyDatabase getWritableDatabase(){
        if (database != null) return database;
        if (newDbHelper){
            database = new MyDatabase(MyApplication.getApplication());
        }else {
            database = MyDatabaseHolder.getInstance().getDatabase();
        }
        return database;
    }

    /*private void printCursor(Cursor cursor){
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
    }*/

    private String getInsertSql(){
        return "insert into mytable(name, mobile, email) VALUES('" + getName() + "', '127', 'ww@163.com')";
    }

   /* private void effectiveInsert(){
        SQLiteDatabase database = getWritableDatabase().getWritableDatabase();
        String insertSql = "";

        database.beginTransaction();
        try{

            SQLiteStatement statement = database.compileStatement(insertSql);
            while (...) {
                statement.clearBindings();//清除绑定关系
                statement.bindLong(0, 1L);//绑定数据
                statement.bindString(1, "xxx");
                statement.executeInsert();//执行
            }

            database.setTransactionSuccessful();
        }finally {
            database.endTransaction();
        }

    }*/
}
