package examples.lcd.sqlitepractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickAction(View v) {
        switch (v.getId()){
            case R.id.newReaderWithGlobalDatabase:{
                DbReader.generateDbReader(false);
                break;
            }
            case R.id.newReaderWithNewDatabase:{
                DbReader.generateDbReader(true);
                break;
            }
            case R.id.terminateReaders:{
                DbReader.terminateAll();
                break;
            }
            case R.id.newWriterWithGlobalDatabase:{
                DbWriter.generateDbWriter(false);
                break;
            }
            case R.id.newWriterWithGlobalDatabaseAndTransaction:{
                DbWriter.generateDbWriter(false, true);
                break;
            }
            case R.id.newWriterWithNewDatabaseAndTransaction:{
                DbWriter.generateDbWriter(true, true);
                break;
            }
            case R.id.newWriterWithNewDatabase:{
                DbWriter.generateDbWriter(true);
                break;
            }
            case R.id.terminateWriters:{
                DbWriter.terminateAll();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DbReader.terminateAll();
        DbWriter.terminateAll();
    }
}
