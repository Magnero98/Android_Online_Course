package com.example.linyansen.coursedatabase.DatabaseConnection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lin Yansen on 11/26/2017.
 */

public class DBHelpers extends SQLiteOpenHelper {

    public static final String DatabaseName = "CourseDB";
    public static final int DatabaseVersion = 1;

    public DBHelpers(Context context)
    {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query;

        query =
            "CREATE TABLE " + DBContract.StudentEntry.TABLE_NAME +
            "(" +
                DBContract.StudentEntry.STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.StudentEntry.STUDENT_FULLNAME + " VARCHAR(30)," +
                DBContract.StudentEntry.STUDENT_USERNAME + " VARCHAR(20)," +
                DBContract.StudentEntry.STUDENT_PASSWORD + " VARCHAR(20)," +
                DBContract.StudentEntry.STUDENT_EMAIL + " VARCHAR(20)," +
                DBContract.StudentEntry.STUDENT_PHONE + " VARCHAR(20)," +
                DBContract.StudentEntry.STUDENT_GENDER + " VARCHAR(10)," +
                DBContract.StudentEntry.STUDENT_IMAGE + " VARCHAR(20) DEFAULT '2131099771'" +
            ");";

        db.execSQL(query);

        query =
            "CREATE TABLE " + DBContract.CourseEntry.TABLE_NAME +
            "(" +
                DBContract.CourseEntry.COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.CourseEntry.COURSE_TITLE +  " VARCHAR(20)," +
                DBContract.CourseEntry.COURSE_CREDIT + " INTEGER," +
                DBContract.CourseEntry.COURSE_QUOTA + " INTEGER" +
            ");";

        db.execSQL(query);

        query =
            "CREATE TABLE " + DBContract.EnrollmentEntry.TABLE_NAME +
            "(" +
                DBContract.EnrollmentEntry.ENROLLMENT_STUDENT_ID + " INTEGER REFERENCES " + DBContract.StudentEntry.TABLE_NAME + "," +
                DBContract.EnrollmentEntry.ENROLLMENT_COURSE_ID + " INTEGER REFERENCES " + DBContract.CourseEntry.TABLE_NAME +
            ");";

        db.execSQL(query);

        query =
                "INSERT INTO MsCourse(Title, Credit, Quota)" +
                        " VALUES " +
                        "('Mathematics','3','5')," +
                        "('Physics','4','5')," +
                        "('Chemistry','5','5');";

        db.execSQL(query);

        query =
                "INSERT INTO MsStudent(Fullname, Username, Password, Gender, Email, Phone)" +
                        " VALUES " +
                        "('Brandon Smith','bsmith','bs', 'Male', 'bsmith@gmail.com', '087769361212')," +
                        "('Jason Mourgan','jmourgan','jm', 'Male', 'jmourgan@gmail.com', '081345543636')," +
                        "('Sheila Dion','sdion','sd', 'Female', 'sdion@gmail.com', '081922665858')," +
                        "('Michelle Mei','mmei','mm', 'Female', 'mmei@gmail.com', '087988797988')," +
                        "('Jordan Louis','jlouis','jl', 'Male', 'jlouis@gmail.com', '085569697997');";

        db.execSQL(query);

        query =
                "INSERT INTO MsEnrollment" +
                        " VALUES " +
                        "('1', '2')," +
                        "('1', '3')," +
                        "('2', '1')," +
                        "('2', '2')," +
                        "('2', '3')," +
                        "('3', '3')," +
                        "('4', '1')," +
                        "('4', '3')," +
                        "('5', '1')," +
                        "('5', '2');";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}
