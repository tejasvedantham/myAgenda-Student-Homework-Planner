package com.tejas.tejas.homeworkplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;


public class AddItem extends AppCompatActivity {

    DBHelper dbHelper;

    String[] hwTypes = {"Homework", "Quiz", "Test", "Essay", "Project", "Lab",};
    String[] importanceTypes = {"High", "Medium", "Low"};

    private FloatingActionButton fab2;
    private AutoCompleteTextView typeTextView;
    private AutoCompleteTextView importanceTextView;
    private EditText titleText;
    private AutoCompleteTextView subjectText;
    private EditText dueDateText;
    private Calendar mCurrentDate;
    private int year, month, day;
    private DatePicker datePicker;
    private Calendar calendar;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        Intent intent = getIntent();
        dbHelper = new DBHelper(this);

        relativeLayout = (RelativeLayout) findViewById(R.id.addItem);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        titleText = (EditText) findViewById(R.id.titleText);
        subjectText = (AutoCompleteTextView) findViewById(R.id.subjectText);
        dueDateText = (EditText) findViewById(R.id.dueDateText);
        typeTextView = (AutoCompleteTextView) findViewById(R.id.typeText);
        importanceTextView = (AutoCompleteTextView) findViewById(R.id.importanceText);

        if (intent.hasExtra("KEY_EXTRA_CONTACT_ID")) {
            int newPersonID = intent.getIntExtra("KEY_EXTRA_CONTACT_ID", 0);
            String dueDate = intent.getStringExtra("DUE_DATE");
            Cursor rs = dbHelper.getPlace(newPersonID);
            rs.moveToFirst();
            String rTitle = rs.getString(rs.getColumnIndex(DBHelper.PLACE_COLUMN_NAME));
            String rCategory = rs.getString(rs.getColumnIndex(DBHelper.PLACE_COLUMN_CATEGORY));
            String rSubject = rs.getString(rs.getColumnIndex(DBHelper.PLACE_COLUMN_SUBJECT));
            String rDueDate = rs.getString(rs.getColumnIndex(DBHelper.PLACE_COLUMN_DUEDATE));
            String rImportance = rs.getString(rs.getColumnIndex(DBHelper.PLACE_COLUMN_IMPORTANCE));

            titleText.setText(rTitle);
            typeTextView.setText(rCategory);
            subjectText.setText(rSubject);
            dueDateText.setText(rDueDate);
            importanceTextView.setText(rImportance);
        } else {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            showDate(year, month + 1, day);
        }


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hwTypes);

        typeTextView.setThreshold(0);
        typeTextView.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, importanceTypes);

        importanceTextView.setThreshold(0);
        importanceTextView.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dbHelper.getClassArray());
        subjectText.setThreshold(0);
        subjectText.setAdapter(adapter3);

    }

    @SuppressWarnings("deprecation")
    public void setDate(View v) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int year, int month, int day) {
                    showDate(year, month + 1, day);
                }
            };

    private void showDate(int year, int month, int day) {
        //dueDateText.setText(new StringBuilder().append(month).append("/")
        //      .append(day).append("/").append(year));
        dueDateText.setText(String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year));
    }

    public void update(View v) {

        if (titleText.getText().toString().matches("")) {
            noNameFound();
            return;
        }

        if (getIntent().hasExtra("KEY_EXTRA_CONTACT_ID")) {
            int newPersonID = getIntent().getIntExtra("KEY_EXTRA_CONTACT_ID", 0);
            dbHelper.updatePlace(newPersonID, titleText.getText().toString(), typeTextView.getText().toString(), subjectText.getText().toString(), dueDateText.getText().toString(), importanceTextView.getText().toString());
            assignmentUpdated();
        } else if (dbHelper.insertPlace(titleText.getText().toString(), typeTextView.getText().toString(), subjectText.getText().toString(), dueDateText.getText().toString(), importanceTextView.getText().toString())) {
            //Toast.makeText(getApplicationContext(), "Assignment Added", Toast.LENGTH_SHORT).show();
            homeworkAddedMessage();
        } else {
            // Toast.makeText(getApplicationContext(), "Assignment Not Added", Toast.LENGTH_SHORT).show();
            homeworkNotAddedMessage();
        }

        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void homeworkAddedMessage() {
        Toast.makeText(getApplicationContext(), "Assignment Added", Toast.LENGTH_LONG).show();
    }

    public void homeworkNotAddedMessage() {
        Toast.makeText(getApplicationContext(), "Assignment Not Added", Toast.LENGTH_LONG).show();
    }

    public void assignmentUpdated() {
        Toast.makeText(getApplicationContext(), "Assignment Updated", Toast.LENGTH_LONG).show();
    }

    public void noNameFound() {
        Snackbar snackbar = Snackbar.make(relativeLayout, "Assignment Needs a Title", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


}
