package com.tejas.tejas.homeworkplanner;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Tejas on 6/18/2017.
 */

public class ClassView extends AppCompatActivity {

    public DBHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_view);

        ListView classListView = (ListView)findViewById(R.id.classListView);
        TextView listHeader = new TextView((getApplicationContext()));
        listHeader.setText(" Classes");
        listHeader.setTextSize(20);
        listHeader.setTextColor(Color.BLACK);
        classListView.addHeaderView(listHeader, null, false);

        dbHelper = new DBHelper(this);

        final Cursor cursor = dbHelper.getAllClasses();
        String[] columns = new String[]{
                DBHelper.CLASS_COLUMN_CLASS,
                };
        int[] widgets = new int[]{
                R.id.classText,
                };
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.class_row, cursor, columns, widgets, 0);
        classListView.setAdapter(cursorAdapter);



    }

    public void addClass(View v) {
        Intent addClassIntent = new Intent(this, AddClass.class);
        startActivity(addClassIntent);
    }

    public void returnToHome(View v) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
