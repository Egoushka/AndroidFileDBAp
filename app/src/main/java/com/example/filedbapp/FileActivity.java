package com.example.filedbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileActivity extends AppCompatActivity {

    private TextView tvLog;
    private EditText newText;
    private String newStr;
    private final String FILE_NAME = "data.txt";
    private Runnable showLog;
    private Runnable rFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tvLog = findViewById(R.id.file_tvLog);
        tvLog.setMovementMethod(new ScrollingMovementMethod());

        newText = findViewById(R.id.file_newStr);

        (new Thread(rFile)).start();
    }

    public FileActivity(){

        showLog = () -> {
            tvLog.setText(newStr);
        };

        rFile = () ->{
            FileInputStream r;
            byte[] buf = new byte[1024];
            newStr = newText.getText().toString() + '\n';

            try {
                r = openFileInput(FILE_NAME);
                r.read(buf);
                r.close();
            } catch (FileNotFoundException e) {
                newStr = new String(buf);
                runOnUiThread(showLog);
                return;
            } catch (IOException e) {
                newStr = new String(buf);
                runOnUiThread(showLog);
                return;
            }
            newStr = new String(buf);
            runOnUiThread(showLog);
        };
    }
    public void btnFileStrAddClick(View view) {
        newStr = newText.getText().toString();
        FileActivity.Inserter ins = new FileActivity.Inserter(newStr);
        (new Thread(ins)).start();

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
            if (newStr.contentEquals((""))) {
                Toast.makeText(getApplicationContext(), R.string.empty_str_msg, Toast.LENGTH_SHORT).show();
                return;
            }
            FileOutputStream f;
            byte[] buf = newStr.getBytes();
            try {
                f = openFileOutput(FILE_NAME, Context.MODE_APPEND);
                f.write(buf);
                f.flush();
                f.close();
            } catch (FileNotFoundException e) {
                tvLog.setText(e.getMessage());
                return;
            } catch (IOException e) {
                tvLog.setText(e.getMessage());
                return;
            }
            (new Thread(rFile)).start();
        }
    }
}