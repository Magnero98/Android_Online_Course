package com.example.linyansen.coursedatabase.DatabaseConnection;

import android.app.FragmentManager;
import android.provider.BaseColumns;

/**
 * Created by Lin Yansen on 11/26/2017.
 */

public class DBContract
{
    private DBContract(){}

    public static class StudentEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "MsStudent";
        public static final String STUDENT_ID = BaseColumns._ID;
        public static final String STUDENT_FULLNAME = "Fullname";
        public static final String STUDENT_USERNAME = "Username";
        public static final String STUDENT_PASSWORD = "Password";
        public static final String STUDENT_EMAIL = "Email";
        public static final String STUDENT_PHONE = "Phone";
        public static final String STUDENT_GENDER = "Gender";
        public static final String STUDENT_IMAGE = "Image";
    }

    public static class CourseEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "MsCourse";
        public static final String COURSE_ID = BaseColumns._ID;
        public static final String COURSE_TITLE = "Title";
        public static final String COURSE_CREDIT = "Credit";
        public static final String COURSE_QUOTA = "Quota";
    }

    public static class EnrollmentEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "MsEnrollment";
        public static final String ENROLLMENT_STUDENT_ID = "StudentID";
        public static final String ENROLLMENT_COURSE_ID = "CourseID";
    }
}
