package com.example.filedbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DbActivity extends AppCompatActivity {

    private TextView tvLog;
    // Ресурс базы данных
    private SQLiteDatabase db;

    private EditText etNewStr;
    private Runnable showDb;
    private Runnable showLog;
    private Runnable installDb;
    private String logText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        etNewStr = findViewById(R.id.etNewStr);

        tvLog = findViewById(R.id.db_tvLog);
        tvLog.setMovementMethod(new ScrollingMovementMethod());
        (new Thread(installDb)).start();

    }

    public void btnDbStrAddClick(View view) {
        String str = etNewStr.getText().toString();
        if (str.contentEquals((""))) {
            Toast.makeText(getApplicationContext(), R.string.empty_str_msg, Toast.LENGTH_SHORT).show();
            return;
        }


        Inserter ins = new Inserter(str);
        (new Thread(ins)).start();
    }

    public DbActivity() {
        super();


        showLog = () -> {
            tvLog.setText(logText);
        };
        showDb = () -> {
            if (db == null)
                db = openOrCreateDatabase("storage.db", MODE_PRIVATE, null);
            String txt = "";
            String query = "SELECT * FROM Strings";
            Cursor res = null;
            try {
                res = db.rawQuery(query, null);
            } catch (SQLException e) {
                logText = e.getMessage();
                runOnUiThread(showLog);
                return;
            }
            boolean hasNext = res.moveToFirst();
            while (hasNext) {
                txt += res.getInt(res.getColumnIndex("id")) + ", " + res.getString(res.getColumnIndex("str")) + "\n";
                hasNext = res.moveToNext();
            }
            logText = txt;
            runOnUiThread(showLog);
        };
        installDb = () ->{
            if (db == null)
                db = openOrCreateDatabase("storage.db", MODE_PRIVATE, null);
            String query = "CREATE TABLE IF NOT EXISTS Strings ( " +
                    "id INTEGER PRIMARY KEY," +
                    "str VARCHAR(256)," +
                    "moment DATETIME DEFAULT CURRENT_TIMESTAMP )";
            try {
                db.execSQL(query);
            } catch (SQLException e) {
                tvLog.setText(e.getMessage());
                return;
            }
          (new Thread(showDb)).start();
        };
    }
    class Inserter implements Runnable {

        private String value;
        private String query;
        public Inserter(String value) {
            setValue(value);
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public void run() {
            if (db == null)
                db = openOrCreateDatabase("storage.db", MODE_PRIVATE, null);
            query = "INSERT INTO Strings(str) VALUES ('" + value + "')";
            try {
                db.execSQL(query);
            } catch (SQLException e) {
                logText = e.getMessage();
                runOnUiThread(showLog);
                return;
            }
            (new Thread(showDb)).start();
        }
    }
}