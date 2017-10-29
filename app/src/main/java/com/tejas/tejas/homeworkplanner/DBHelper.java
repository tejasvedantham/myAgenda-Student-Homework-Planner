package com.tejas.tejas.homeworkplanner;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Homework.db";
    public static final int DATABASE_VERSION = 1;

    public static final String PLACE_TABLE_NAME = "Place";
    public static final String CLASS_TABLE_NAME = "Classes";

    public static final String PLACE_COLUMN_ID = "_id";
    public static final String PLACE_COLUMN_NAME = "title";
    public static final String PLACE_COLUMN_SUBJECT = "subject";
    public static final String PLACE_COLUMN_CATEGORY = "category";
    public static final String PLACE_COLUMN_DUEDATE = "dueDate";
    public static final String PLACE_COLUMN_IMPORTANCE = "importance";
    public static final String CLASS_COLUMN_CLASS = "class";

    public static final String COMPLETED_TABLE_NAME = "Completed";
    public static final String COMPLETED_COLUMN_NAME = "completedTitle";
    public static final String COMPLETED_COLUMN_CLASS = "completedClass";

    public String query1 = "CREATE TABLE " + PLACE_TABLE_NAME + "(" +
            PLACE_COLUMN_ID + " INTEGER PRIMARY KEY," +
            PLACE_COLUMN_NAME + " TEXT," +
            PLACE_COLUMN_CATEGORY + " TEXT," +
            PLACE_COLUMN_SUBJECT + " TEXT," +
            PLACE_COLUMN_DUEDATE + " TEXT," +
            PLACE_COLUMN_IMPORTANCE + " TEXT);";

    public String query2 = "CREATE TABLE " + CLASS_TABLE_NAME + "(" +
            PLACE_COLUMN_ID + " INTEGER PRIMARY KEY," +
            CLASS_COLUMN_CLASS + " TEXT);";

    public String query3 = "CREATE TABLE " + COMPLETED_TABLE_NAME + "(" +
            PLACE_COLUMN_ID + " INTEGER PRIMARY KEY," +
            COMPLETED_COLUMN_NAME + " TEXT," +
            COMPLETED_COLUMN_CLASS + " TEXT);";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public boolean insertPlace(String title, String category, String subject, String dueDate, String importance) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLACE_COLUMN_NAME, title);
        contentValues.put(PLACE_COLUMN_CATEGORY, category);
        contentValues.put(PLACE_COLUMN_SUBJECT, subject);
        contentValues.put(PLACE_COLUMN_DUEDATE, dueDate);
        contentValues.put(PLACE_COLUMN_IMPORTANCE, importance);
        db.insert(PLACE_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertClass(String className) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CLASS_COLUMN_CLASS, className);
        db.insert(CLASS_TABLE_NAME, null, cv);
        return true;
    }

    public void updatePlace(Integer id, String newTitle, String newCategory, String newSubject, String newDueDate, String newImportance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLACE_COLUMN_NAME, newTitle);
        contentValues.put(PLACE_COLUMN_CATEGORY, newCategory);
        contentValues.put(PLACE_COLUMN_SUBJECT, newSubject);
        contentValues.put(PLACE_COLUMN_DUEDATE, newDueDate);
        contentValues.put(PLACE_COLUMN_IMPORTANCE, newImportance);
        db.update(PLACE_TABLE_NAME, contentValues, PLACE_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});

    }

    public Cursor getPlace(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + PLACE_TABLE_NAME + " WHERE " +
                PLACE_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllPlaces() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + PLACE_TABLE_NAME, null);
        return res;
    }

    public Cursor getAllCompletedHomework() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res1 = db.rawQuery("SELECT * FROM " + COMPLETED_TABLE_NAME, null);
        return res1;
    }

    public boolean insertCompletedHomework(String hwName, String hwClass) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COMPLETED_COLUMN_NAME, hwName);
        cv.put(COMPLETED_COLUMN_CLASS, hwClass);
        db.insert(COMPLETED_TABLE_NAME, null, cv);
        return true;
    }

    public Cursor getAllClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + CLASS_TABLE_NAME, null);
        return res;
    }

    public Integer deletePlace(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PLACE_TABLE_NAME,
                PLACE_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
    }

    public Integer deleteCompletedHomework(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(COMPLETED_TABLE_NAME, PLACE_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
    }

    public String[] getClassArray() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor crs = db.rawQuery("SELECT * FROM " + CLASS_TABLE_NAME, null);

        String[] classes = new String[crs.getCount()];
        int i = 0;
        while (crs.moveToNext()) {
            String tempClass = crs.getString(crs.getColumnIndex(CLASS_COLUMN_CLASS));
            classes[i] = tempClass;
            i++;
        }

        return classes;
    }

}
