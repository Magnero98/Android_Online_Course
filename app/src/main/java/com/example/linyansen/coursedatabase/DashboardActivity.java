package com.example.linyansen.coursedatabase;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.linyansen.coursedatabase.Class.Student;
import com.example.linyansen.coursedatabase.DatabaseConnection.DBContract;
import com.example.linyansen.coursedatabase.DatabaseConnection.DBHelpers;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity
{
    public Student currentStudent;
    public ArrayList<LinearLayout> courseListLayouts = new ArrayList<LinearLayout>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //setText();
        initialize();
    }

    public void setText()
    {
        DBHelpers helpers = new DBHelpers(this);
        SQLiteDatabase db = helpers.getReadableDatabase();

        String query =
                "SELECT CourseId, StudentId FROM MsEnrollment ORDER BY CourseId ASC;";

        Cursor cursor = db.rawQuery(query, null);

        TextView txt = (TextView) findViewById(R.id.text);
        try
        {
            while (cursor.moveToNext())
            {
                txt.append(cursor.getInt(0) + " " + cursor.getInt(1) + "\n");
            }
        }
        finally {
            cursor.close();
        }
    }

    public void getStudentDataFromDB()
    {
        DBHelpers helpers = new DBHelpers(this);
        SQLiteDatabase db = helpers.getReadableDatabase();

        String[] projection =
        {
                DBContract.StudentEntry.STUDENT_ID,
                DBContract.StudentEntry.STUDENT_FULLNAME,
                DBContract.StudentEntry.STUDENT_USERNAME,
                DBContract.StudentEntry.STUDENT_GENDER,
                DBContract.StudentEntry.STUDENT_EMAIL,
                DBContract.StudentEntry.STUDENT_PHONE,
                DBContract.StudentEntry.STUDENT_IMAGE
        };

        String WHERE_CLAUSE = DBContract.StudentEntry.STUDENT_ID + " = '" + getIntent().getStringExtra("id") + "'";

        Cursor cursor = db.query(
                DBContract.StudentEntry.TABLE_NAME,
                projection,
                WHERE_CLAUSE,
                null,
                null,
                null,
                null
        );

        cursor.moveToNext();

        int idColumnIndex = cursor.getColumnIndex(DBContract.StudentEntry.STUDENT_ID);
        int fullnameColumnIndex = cursor.getColumnIndex(DBContract.StudentEntry.STUDENT_FULLNAME);
        int usernameColumIndex = cursor.getColumnIndex(DBContract.StudentEntry.STUDENT_USERNAME);
        int genderColumnIndex = cursor.getColumnIndex(DBContract.StudentEntry.STUDENT_GENDER);
        int emailColumnIndex = cursor.getColumnIndex(DBContract.StudentEntry.STUDENT_EMAIL);
        int phoneColumnIndex = cursor.getColumnIndex(DBContract.StudentEntry.STUDENT_PHONE);
        int imageColumnIndex = cursor.getColumnIndex(DBContract.StudentEntry.STUDENT_IMAGE);

        try
        {
            currentStudent = new Student(
                    cursor.getInt(idColumnIndex),
                    cursor.getString(fullnameColumnIndex),
                    cursor.getString(usernameColumIndex),
                    cursor.getString(genderColumnIndex),
                    cursor.getString(emailColumnIndex),
                    cursor.getString(phoneColumnIndex),
                    cursor.getString(imageColumnIndex)
            );
        }
        finally
        {
            cursor.close();
        }

        initializeProfile();
        initilaizeCourses();
    }

    public void initializeProfile()
    {
        TextView fullnameTextView = (TextView) findViewById(R.id.fullnameTextView);
        TextView usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        TextView genderTextView = (TextView) findViewById(R.id.genderTextView);
        TextView emailTextView = (TextView) findViewById(R.id.emailTextView);
        TextView phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        ImageView profileImageView = (ImageView) findViewById(R.id.profileImageView);

        fullnameTextView.setText(currentStudent.getFullname());
        usernameTextView.setText(currentStudent.getUsername());
        genderTextView.setText(currentStudent.getGender());
        emailTextView.setText(currentStudent.getEmail());
        phoneTextView.setText(currentStudent.getPhone());

        if(currentStudent.getGender().equals("Female")) {
            profileImageView.setBackgroundResource(R.drawable.profile_picture_female_radius);
            profileImageView.setImageResource(R.drawable.woman);
        }
        else {
            profileImageView.setBackgroundResource(R.drawable.profile_picture_male_radius);
            profileImageView.setImageResource(R.drawable.user);
        }

    }

    public void showFriendList(int courseId, int layoutListIndex)
    {
        // setting Layout of Linear Layout
        LinearLayout.LayoutParams layoutMPWC = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutWCWCW = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(60, 60);

        DBHelpers helpers = new DBHelpers(this);
        SQLiteDatabase db = helpers.getReadableDatabase();

        String query =
            "SELECT " +
                "Fullname," +
                "Phone," +
                "Gender" +
            " FROM " +
                "MsEnrollment me JOIN MsStudent ms ON ms._id = me.StudentID" +
            " WHERE " +
                    "CourseID = '" + courseId + "';";

        Cursor cursor = db.rawQuery(query, null);

        try
        {
            int counter = 1;

            while(cursor.moveToNext())
            {
                String fullname = cursor.getString(0);
                final String phone = cursor.getString(1);
                String gender = cursor.getString(2);

                //setting friend parent linear layout
                LinearLayout friendParentLinerLayout = new LinearLayout(this);
                layoutMPWC.setMargins(0,50,0,10);
                friendParentLinerLayout.setLayoutParams(layoutMPWC);
                friendParentLinerLayout.setBackgroundResource(R.drawable.friend_radius_gradient_layout);
                friendParentLinerLayout.setPadding(10,10,10,10);
                friendParentLinerLayout.setGravity(Gravity.CENTER);
                friendParentLinerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        startActivity(intent);
                    }
                });

                //setting friend Profile picture
                ImageView friendImageView = new ImageView(this);
                friendImageView.setLayoutParams(imageViewParams);
                friendImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if(gender.equals("Female")) {
                    friendImageView.setBackgroundResource(R.drawable.profile_picture_female_radius);
                    friendImageView.setImageResource(R.drawable.woman);
                }
                else {
                    friendImageView.setBackgroundResource(R.drawable.profile_picture_male_radius);
                    friendImageView.setImageResource(R.drawable.user);
                }

                //setting friend desc linear layout
                LinearLayout friendDescLinearLayout = new LinearLayout(this);
                friendDescLinearLayout.setLayoutParams(layoutWCWCW);
                friendDescLinearLayout.setOrientation(LinearLayout.VERTICAL);
                friendDescLinearLayout.setPadding(10,0,10,0);

                //setting friend name inside Friend desc
                TextView friendNameTextView = new TextView(this);
                layoutMPWC.setMargins(0,0,0,0);
                friendNameTextView.setLayoutParams(layoutMPWC);
                friendNameTextView.setText(fullname);
                friendNameTextView.setTextSize(14);
                friendNameTextView.setTextColor(Color.WHITE);

                //setting friend phone inside Friend desc
                TextView friendPhoneTextView = new TextView(this);
                layoutMPWC.setMargins(0,0,0,0);
                friendPhoneTextView.setLayoutParams(layoutMPWC);
                friendPhoneTextView.setText(phone);
                friendPhoneTextView.setTextSize(10);
                friendPhoneTextView.setTextColor(Color.WHITE);

                friendDescLinearLayout.addView(friendNameTextView);
                friendDescLinearLayout.addView(friendPhoneTextView);

                friendParentLinerLayout.addView(friendImageView);
                friendParentLinerLayout.addView(friendDescLinearLayout);

                courseListLayouts.get(layoutListIndex).addView(friendParentLinerLayout);

                counter++;
            }
        }
        finally {
            cursor.close();
        }
    }

    public void initilaizeCourses()
    {
        DBHelpers helpers = new DBHelpers(this);
        SQLiteDatabase db = helpers.getReadableDatabase();

        String[] projection =
        {
            DBContract.CourseEntry.COURSE_ID,
            DBContract.CourseEntry.COURSE_TITLE,
            DBContract.CourseEntry.COURSE_CREDIT,
            DBContract.CourseEntry.COURSE_QUOTA
        };

        String query =
                "SELECT\n" +
                "\tme.CourseID,\n" +
                "\tTitle,\n" +
                "\tCredit,\n" +
                "\tQuota\n" +
                "FROM\n" +
                "\tMsCourse mc JOIN MsEnrollment me ON mc._id = me.CourseId\n" +
                "WHERE StudentId = '" + currentStudent.getId() + "';";

        Cursor cursor = db.rawQuery(query, null);

        LinearLayout courseListLinearLayout = (LinearLayout) findViewById(R.id.courseListLinearLayout);
        courseListLinearLayout.removeAllViews();

        // setting Layout of Linear Layout
        LinearLayout.LayoutParams layoutMPWC = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutWCWCW = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        LinearLayout.LayoutParams layoutWCWC = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(80, 80);

        try
        {
            while (cursor.moveToNext())
            {
                int courseId = cursor.getInt(0);
                String title = cursor.getString(1);
                String credit = cursor.getString(2);
                String quota = cursor.getString(3);

                // Setting Parent Linear Layout
                LinearLayout parentLinerLayout = new LinearLayout(this);
                parentLinerLayout.setLayoutParams(layoutMPWC);
                parentLinerLayout.setOrientation(LinearLayout.VERTICAL);
                parentLinerLayout.setPadding(30,30,30,0);

                //Setting Friend Linear Layout
                final LinearLayout friendListLinearLayout = new LinearLayout(this);
                friendListLinearLayout.setOrientation(LinearLayout.VERTICAL);
                friendListLinearLayout.setLayoutParams(layoutMPWC);
                friendListLinearLayout.setPadding(50,5,0,0);

                final int finalCourseId = courseId;
                final int courseListLayoutIndex = courseListLayouts.size();

                // Setting Course Linear Layout
                LinearLayout courseLinearLayout = new LinearLayout(this);
                layoutMPWC.setMargins(0,0,0, 5);
                courseLinearLayout.setLayoutParams(layoutMPWC);
                courseLinearLayout.setPadding(15,15,15,15);
                courseLinearLayout.setGravity(Gravity.CENTER);
                if(title.equals("Mathematics"))
                    courseLinearLayout.setBackgroundResource(R.drawable.math_semi_radius_yellow);
                else if(title.equals("Physics"))
                    courseLinearLayout.setBackgroundResource(R.drawable.physics_semi_radius_red);
                else if(title.equals("Chemistry"))
                    courseLinearLayout.setBackgroundResource(R.drawable.chemistry_semi_radius_green);
                courseLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(friendListLinearLayout.getChildCount() > 0)
                        {
                            friendListLinearLayout.removeAllViews();
                        }
                        else
                        {
                            showFriendList(finalCourseId, courseListLayoutIndex);
                        }
                    }
                });

                //setting ImageView of Course Linear Layout
                ImageView courseImageView = new ImageView(this);
                courseImageView.setLayoutParams(imageViewParams);
                courseImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                courseImageView.setBackgroundResource(R.drawable.profile_image_radius);
                if(title.equals("Mathematics"))
                    courseImageView.setImageResource(R.drawable.math);
                else if(title.equals("Physics"))
                    courseImageView.setImageResource(R.drawable.physic);
                else if(title.equals("Chemistry"))
                    courseImageView.setImageResource(R.drawable.chemistry);

                //setting Course Desc Linear Layout
                LinearLayout courseDecsLinearLayout = new LinearLayout(this);
                courseDecsLinearLayout.setLayoutParams(layoutWCWCW);
                courseDecsLinearLayout.setOrientation(LinearLayout.VERTICAL);
                courseDecsLinearLayout.setPadding(15,0,15,0);

                //setting Title inside course desc linaer layout
                TextView titleTextView = new TextView(this);
                layoutMPWC.setMargins(0,0,0,0);
                titleTextView.setLayoutParams(layoutMPWC);
                titleTextView.setText(title);
                titleTextView.setTextSize(17);
                titleTextView.setTextColor(Color.WHITE);

                //setting credits inside course desc linear layout
                TextView creditsTextView = new TextView(this);
                creditsTextView.setLayoutParams(layoutMPWC);
                creditsTextView.setText(String.valueOf(credit) + " credit(s)");
                creditsTextView.setTextSize(11);
                creditsTextView.setTextColor(Color.WHITE);

                //setting Quota Linear Layout
                LinearLayout quotaLinearLayout = new LinearLayout(this);
                quotaLinearLayout.setLayoutParams(layoutWCWC);
                quotaLinearLayout.setOrientation(LinearLayout.VERTICAL);

                //setting text quota title TextView inside Quota linear layout
                TextView quotaTitle = new TextView(this);
                quotaTitle.setLayoutParams(layoutWCWC);
                quotaTitle.setText("Quota");
                quotaTitle.setTextSize(11);
                quotaTitle.setTextColor(Color.WHITE);

                String getStudentCountQuery =
                        "SELECT COUNT(StudentID) as counter FROM MsEnrollment WHERE CourseID = '" + courseId + "'";

                Cursor studentCount = db.rawQuery(getStudentCountQuery, null);

                studentCount.moveToNext();

                String count = studentCount.getString(0);

                //setting text quota TextView inside Quota linear layout
                TextView quotaTextView = new TextView(this);
                quotaTextView.setLayoutParams(layoutWCWC);
                quotaTextView.setText(count + "/" + String.valueOf(quota));
                quotaTextView.setTextSize(17);
                quotaTextView.setTextColor(Color.parseColor("#FFFF00"));

                courseDecsLinearLayout.addView(titleTextView);
                courseDecsLinearLayout.addView(creditsTextView);

                quotaLinearLayout.addView(quotaTitle);
                quotaLinearLayout.addView(quotaTextView);

                courseLinearLayout.addView(courseImageView);
                courseLinearLayout.addView(courseDecsLinearLayout);
                courseLinearLayout.addView(quotaLinearLayout);

                parentLinerLayout.addView(courseLinearLayout);
                parentLinerLayout.addView(friendListLinearLayout);

                courseListLinearLayout.addView(parentLinerLayout);

                courseListLayouts.add(friendListLinearLayout);
            }
        }
        finally {
            cursor.close();
        }
    }

    public void initialize()
    {
        RelativeLayout profileLayout = (RelativeLayout) findViewById(R.id.profileLayout);
        LinearLayout coursesLayout = (LinearLayout) findViewById(R.id.coursesLayout);

        profileLayout.setVisibility(View.VISIBLE);
        coursesLayout.setVisibility(View.GONE);

        getStudentDataFromDB();
    }

    public void changeActivity(View view)
    {
        RelativeLayout profileLayout = (RelativeLayout) findViewById(R.id.profileLayout);
        LinearLayout coursesLayout = (LinearLayout) findViewById(R.id.coursesLayout);

        if(view.getId() == R.id.leftButton)
        {
            profileLayout.setVisibility(View.VISIBLE);
            coursesLayout.setVisibility(View.GONE);
        }
        else if(view.getId() == R.id.rightButton)
        {
            profileLayout.setVisibility(View.GONE);
            coursesLayout.setVisibility(View.VISIBLE);
        }
    }
}
