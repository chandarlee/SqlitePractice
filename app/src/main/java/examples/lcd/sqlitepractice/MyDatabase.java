package examples.lcd.sqlitepractice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by LCD on 2016/7/16.
 */
public class MyDatabase extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DB_NAME = "mydatabase.db";
    private static final String TABLE_NAME = "mytable";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_MOBILE = "mobile";
    private static final String COLUMN_EMAIL = "email";

    public MyDatabase(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createSql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " VARCHAR(32) NOT NULL,"
                + COLUMN_MOBILE + " VARCHAR(11) DEFAULT NULL,"
                + COLUMN_EMAIL + " VARCHAR(32) DEFAULT NULL"
                + ")";
        db.execSQL(createSql);
        db.execSQL("INSERT INTO mytable(name, mobile) values('lcd', '123');");
        db.execSQL("INSERT INTO mytable(name, mobile) values('zpp', '124');");
        db.execSQL("INSERT INTO mytable(name, mobile) values('zs', '125');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //NO OP
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("MyDatabase", "onOpen: enable write ahead logging:" + db.enableWriteAheadLogging());
    }
}
