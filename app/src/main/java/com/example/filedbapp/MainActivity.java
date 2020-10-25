package com.example.filedbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    /*

        Сохранение информации в устройстве (тлф/планшет/..)
        1. Базы данных
        2. Работа с файлами
            2.1 Локальное хранилище
            2.2 Хранилище общего доступа

     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnDbClick(View view) {
        Intent dbActivity = new Intent(this,DbActivity.class);
        startActivity(dbActivity);
    }

    public void btnFileClick(View view) {
        Intent fileActivity = new Intent(this,FileActivity.class);
        startActivity(fileActivity);
    }
}