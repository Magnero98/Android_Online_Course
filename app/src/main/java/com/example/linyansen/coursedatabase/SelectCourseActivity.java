package com.example.linyansen.coursedatabase;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linyansen.coursedatabase.Class.Course;
import com.example.linyansen.coursedatabase.DatabaseConnection.DBContract;
import com.example.linyansen.coursedatabase.DatabaseConnection.DBHelpers;

import java.util.ArrayList;

public class SelectCourseActivity extends AppCompatActivity {

    public ArrayList<Integer> CourseList = new ArrayList<Integer>();
    public ArrayList<String> FullCourseList = new ArrayList<String>();

    public String currentStudentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getStudentId();
        getCourseQuota();
    }

    public void getCourseQuota()
    {
        TextView textView = new TextView(this);

        DBHelpers helpers = new DBHelpers(this);
        SQLiteDatabase db = helpers.getReadableDatabase();

        Cursor quotaCursor, countCursor;

        String query = "SELECT CourseId, COUNT(StudentId) AS Count FROM MsEnrollment GROUP BY CourseId";
        countCursor = db.rawQuery(query, null);

        //String getStudentCountQuery =
        //        "SELECT COUNT(StudentID) as counter FROM MsEnrollment WHERE CourseID = '" + courseId + "'";

        query = "SELECT Quota FROM MsCourse";
        quotaCursor = db.rawQuery(query,null);

        int quota, count;

        try {
            while (countCursor.moveToNext() && quotaCursor.moveToNext())
            {
                switch (countCursor.getInt(0))
                {
                    case 1:
                        textView = (TextView) findViewById(R.id.mathQuotaTextView);
                        if(countCursor.getInt(1) >= quotaCursor.getInt(0))
                            FullCourseList.add("Math");
                        break;
                    case 2:
                        textView = (TextView) findViewById(R.id.physicsQuotaTextView);
                        if(countCursor.getInt(1) >= quotaCursor.getInt(0))
                            FullCourseList.add("Physics");
                        break;
                    case 3:
                        textView = (TextView) findViewById(R.id.chemistryQuotaTextView);
                        if(countCursor.getInt(1) >= quotaCursor.getInt(0))
                            FullCourseList.add("Chemistry");
                        break;
                }

                quota = quotaCursor.getInt(0);
                count = countCursor.getInt(1);
                textView.setText(String.valueOf(count) + "/" + quota);
            }
        }
        finally {
            countCursor.close();
        }

        for(String element: FullCourseList)
        {
            TextView txtView;

            if(element.equals("Math"))
            {
                txtView = (TextView) findViewById(R.id.mathQuotaTextView);
                txtView.setTextColor(Color.RED);
            }
            else if(element.equals("Physics"))
            {
                txtView = (TextView) findViewById(R.id.physicsQuotaTextView);
                txtView.setTextColor(Color.RED);
            }
            else
            {
                txtView = (TextView) findViewById(R.id.chemistryQuotaTextView);
                txtView.setTextColor(Color.RED);
            }
        }
    }

    public void getStudentId()
    {
        DBHelpers helpers = new DBHelpers(this);
        SQLiteDatabase db = helpers.getReadableDatabase();

        String query =
                "SELECT " + DBContract.StudentEntry.STUDENT_ID +
                " FROM " + DBContract.StudentEntry.TABLE_NAME +
                " WHERE " + DBContract.StudentEntry.STUDENT_USERNAME + " LIKE '" + getIntent().getStringExtra("username") + "';";

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToNext();

        currentStudentId = cursor.getString(0);
    }

    public int checkCourseQuota(String courseName)
    {
        for(String element:FullCourseList)
        {
            if(element.equals(courseName))
            {
                return 0;
            }
        }

        return 1;
    }

    public void selectCourses(View view)
    {
        if(view.getId() == R.id.mathCourse)
        {
            if(view.getAlpha() == (float)0.5)
            {
                view.setAlpha(1);
                CourseList.remove("Math");
            }
            else
            {
                if(checkCourseQuota("Math") == 1) {
                    view.setAlpha((float)0.5);
                    CourseList.add(1);
                }
                else
                    Toast.makeText(this, "Mathematics class is Full", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.getId() == R.id.physicsCourse)
        {
            if(view.getAlpha() == (float)0.5)
            {
                view.setAlpha(1);
                CourseList.remove("Physics");
            }
            else
            {
                if(checkCourseQuota("Physics") == 1) {
                    view.setAlpha((float) 0.5);
                    CourseList.add(2);
                }
                else
                    Toast.makeText(this, "Physics class is Full", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.getId() == R.id.chemistryCourse)
        {
            if(view.getAlpha() == (float)0.5)
            {
                view.setAlpha(1);
                CourseList.remove("Chemistry");
            }
            else
            {
                if(checkCourseQuota("Chemistry") == 1) {
                    view.setAlpha((float) 0.5);
                    CourseList.add(3);
                }
                else
                    Toast.makeText(this, "Chemistry class is Full", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goToLoginActivity()
    {
        Intent intent = new Intent(SelectCourseActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void enroll(View view)
    {
        if(CourseList.isEmpty())
        {
            Toast.makeText(this, "You need to choose at least 1 course", Toast.LENGTH_SHORT).show();
            return;
        }

        DBHelpers helpers = new DBHelpers(this);
        SQLiteDatabase db = helpers.getWritableDatabase();

        String query;

        for(int element: CourseList)
        {
            query =
                    "INSERT INTO " + DBContract.EnrollmentEntry.TABLE_NAME +
                            " VALUES ('" +
                            currentStudentId + "','" +
                            element + "');";

            db.execSQL(query);
        }

        goToLoginActivity();
    }
}
