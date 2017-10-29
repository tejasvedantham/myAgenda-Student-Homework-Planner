package com.tejas.tejas.homeworkplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Tejas on 6/18/2017.
 */

public class AddClass extends AppCompatActivity {
    DBHelper dbHelper;
    EditText classEdtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_class);
        classEdtText = (EditText)findViewById(R.id.classEditText);

        dbHelper = new DBHelper(this);
    }

    public void confirmClass(View v) {
        if (dbHelper.insertClass(classEdtText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Class Added", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Class Not Added", Toast.LENGTH_SHORT).show();
        }

        Intent classViewIntent = new Intent(this, ClassView.class);
        classViewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(classViewIntent);
    }

}
