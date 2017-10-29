package com.tejas.tejas.homeworkplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;
    Intent addItemIntent;
    NavigationView navigationView;
    private boolean exit;
    DBHelper dbHelper;
    RelativeLayout relativeLayout;
    Cursor cursor;
    SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_homework);

        dbHelper = new DBHelper(this);

        cursor = dbHelper.getAllPlaces();
        String[] columns = new String[]{
                DBHelper.PLACE_COLUMN_NAME,
                DBHelper.PLACE_COLUMN_SUBJECT
        };
        int[] widgets = new int[]{
                R.id.medText,
                R.id.smallText
        };

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.row,
                cursor, columns, widgets, 0);
        listView = (ListView) findViewById(R.id.listView);

        final TextView listHeader = new TextView((getApplicationContext()));

        listHeader.setText(" Homework");
        listHeader.setTextSize(20);
        listHeader.setTextColor(Color.BLACK);
        listView.addHeaderView(listHeader, null, false);

        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) HomePageActivity.this.listView.getItemAtPosition(position);
                int personID = itemCursor.getInt(itemCursor.getColumnIndex(DBHelper.PLACE_COLUMN_ID));
                String dueDate = itemCursor.getString(itemCursor.getColumnIndex(DBHelper.PLACE_COLUMN_DUEDATE));
                Intent intent = new Intent(getApplicationContext(), AddItem.class);
                intent.putExtra("KEY_EXTRA_CONTACT_ID", personID);
                intent.putExtra("DUE_DATE", dueDate);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                TextView mainText = (TextView) view.findViewById(R.id.medText);
                TextView subText = (TextView) view.findViewById(R.id.smallText);

                if ((mainText.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
                    return true;
                }
                dbHelper.insertCompletedHomework(mainText.getText().toString(), subText.getText().toString());
                mainText.setPaintFlags(mainText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                subText.setPaintFlags(subText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                assignmentCompletedMessage();

                dbHelper.deletePlace((int) id);

                return true;
            }
        });


    }

    @Override
    public void onResume() {
        navigationView.setCheckedItem(R.id.nav_homework);
        cursor = dbHelper.getAllPlaces();
        cursorAdapter.changeCursor(cursor);
        cursorAdapter.notifyDataSetChanged();
        listView.setAdapter(cursorAdapter);

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        exit = false;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit App?")
                    .setMessage("Do you wish to exit?")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_classes) {
            Intent classesViewIntent = new Intent(this, ClassView.class);
            startActivity(classesViewIntent);
        } else if (id == R.id.nav_homework) {


        } else if (id == R.id.nav_schedule) {
            Intent scheduleIntent = new Intent(this, ScheduleView.class);
            startActivity(scheduleIntent);

        } else if (id == R.id.nav_completed) {
            Intent completedIntent = new Intent(this, CompletedHomework.class);
            startActivity(completedIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addItem(View v) {
        addItemIntent = new Intent(v.getContext(), AddItem.class);
        //addItemIntent.putExtra("KEY_EXTRA_CONTACT_ID", 0);
        startActivity(addItemIntent);
    }

    public void assignmentCompletedMessage() {
        Snackbar snackbar = Snackbar
                .make(relativeLayout, "Assignment Completed", Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}


