package com.tejas.tejas.homeworkplanner;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CompletedHomework extends AppCompatActivity {
    public ListView listView2;
    private DBHelper dbHelper;
    private RelativeLayout relativeLayout;
    Cursor cursor;
    SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_homework);

        relativeLayout = (RelativeLayout)findViewById(R.id.completedLayout);
        listView2 = (ListView) findViewById(R.id.listView2);
        TextView listHeader2 = new TextView(getApplicationContext());
        listHeader2.setText(" Completed Homework");
        listHeader2.setTextSize(20);
        listHeader2.setTextColor(Color.BLACK);
        listView2.addHeaderView(listHeader2, null, false);

        dbHelper = new DBHelper(this);

        cursor = dbHelper.getAllCompletedHomework();
        String[] columns = new String[] {
                DBHelper.COMPLETED_COLUMN_NAME,
                DBHelper.COMPLETED_COLUMN_CLASS,
        };
        int[] widgets = new int[] {
                R.id.medText,
                R.id.smallText,
        };
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.completed_row, cursor, columns, widgets, 0);
        listView2.setAdapter(cursorAdapter);

        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView mainText = (TextView) view.findViewById(R.id.medText);
                TextView subText = (TextView) view.findViewById(R.id.smallText);

                if ((mainText.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
                    return true;
                }
                mainText.setPaintFlags(mainText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                subText.setPaintFlags(subText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                completedAssignmentMessage();
                dbHelper.deleteCompletedHomework((int) id);
                return true;
            }
        });
    }

    public void returnToHome2(View v) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        cursor = dbHelper.getAllCompletedHomework();
        cursorAdapter.changeCursor(cursor);
        cursorAdapter.notifyDataSetChanged();
        listView2.setAdapter(cursorAdapter);
        super.onResume();
    }

    public void completedAssignmentMessage() {
        Snackbar snackbar = Snackbar
                .make(relativeLayout, "Deleted Completed Homework", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}