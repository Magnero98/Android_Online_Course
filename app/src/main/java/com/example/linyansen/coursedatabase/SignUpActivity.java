package com.example.linyansen.coursedatabase;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.linyansen.coursedatabase.DatabaseConnection.DBContract;
import com.example.linyansen.coursedatabase.DatabaseConnection.DBHelpers;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public int inputValidation(int attributeIndex, ArrayList<String> attributes)
    {
        for(int i = 0; i < attributes.size(); i++)
        {
            if(attributes.get(i).trim().length() < 1)
            {
                Toast.makeText(this, "All columns must be filled", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }

        // ======================= PHONE NUMBER VALIDATION ====

        if(attributes.get(4).length() < 8 || attributes.get(4).length() > 12)
        {
            Toast.makeText(this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
            return 0;
        }

        for(int i = 0; i < attributes.get(4).length(); i++)
        {
            if(attributes.get(4).charAt(i) < '0' || attributes.get(4).charAt(i) > '9')
            {
                Toast.makeText(this, "Phone number cannot contains any letter", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }

        // ======================= EMAIL VALIDATION ====

        if(!attributes.get(3).contains("@"))
        {
            Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
            return 0;
        }

        String domain, username;

        try
        {
            String[] emailElement = attributes.get(3).split("@");
            username = emailElement[0];
            domain = emailElement[1];

            if(emailElement[0].length() < 1 || emailElement[1].length() < 1)
            {
                Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
            return 0;
        }

        if(!domain.contains("."))
        {
            Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
            return 0;
        }

        if(username.contains("."))
        {
            String[] usernameElement = username.split("\\.");

            if(usernameElement.length < 2)
            {
                Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
                return 0;
            }

            for(String element:usernameElement)
            {
                if(element.length() < 1)
                {
                    Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
                    return 0;
                }
            }
        }

        if(domain.contains("."))
        {
            String[] domainElement = domain.split("\\.");

            if(domainElement.length < 2)
            {
                Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
                return 0;
            }

            for(String element: domainElement)
            {
                if(element.length() < 1)
                {
                    Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
                    return 0;
                }
            }
        }

        // ======================= GENDER RADIO BUTTON VALIDATION ====

        if(attributeIndex < 0)
        {
            Toast.makeText(this, "Gender must be checked", Toast.LENGTH_SHORT).show();
            return 0;
        }

        // ======================= USERNAME VALIDATION ====

        DBHelpers helpers = new DBHelpers(this);
        SQLiteDatabase db = helpers.getReadableDatabase();

        String query =
                "SELECT Username FROM MsStudent WHERE Username LIKE '" + attributes.get(0) + "';";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() > 0)
        {
            Toast.makeText(this, "this username is already exists", Toast.LENGTH_SHORT).show();
            return 0;
        }

        // ======================= PASSWORD VALIDATION ====

        if(!attributes.get(1).equals(attributes.get(5)))
        {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            return 0;
        }

        return 1;
    }

    public void register(View view)
    {
        EditText fullnameEditText = (EditText) findViewById(R.id.fullnameEditText);
        EditText emailEditText = (EditText) findViewById(R.id.emailEditText);
        EditText phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);

        RadioGroup genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        int genderCheckedId = genderRadioGroup.getCheckedRadioButtonId();

        ArrayList<String> UserAttribute = new ArrayList<String>();
        UserAttribute.add(usernameEditText.getText().toString());
        UserAttribute.add(passwordEditText.getText().toString());
        UserAttribute.add(fullnameEditText.getText().toString());
        UserAttribute.add(emailEditText.getText().toString());
        UserAttribute.add(phoneEditText.getText().toString());
        UserAttribute.add(confirmPasswordEditText.getText().toString());

        if(inputValidation(genderCheckedId, UserAttribute) == 0)
            return;

        String gender = "", image = "";

        switch (genderCheckedId)
        {
            case R.id.maleRadioButton:
                gender = "Male";
                image = "2131099771";
                break;
            case R.id.femaleRadioButton:
                gender = "Female";
                image = "2131099773";
                break;
        }

        DBHelpers helpers = new DBHelpers(this);
        SQLiteDatabase db = helpers.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.StudentEntry.STUDENT_USERNAME, UserAttribute.get(0));
        values.put(DBContract.StudentEntry.STUDENT_PASSWORD, UserAttribute.get(1));
        values.put(DBContract.StudentEntry.STUDENT_FULLNAME, UserAttribute.get(2));
        values.put(DBContract.StudentEntry.STUDENT_EMAIL, UserAttribute.get(3));
        values.put(DBContract.StudentEntry.STUDENT_PHONE, UserAttribute.get(4));
        values.put(DBContract.StudentEntry.STUDENT_GENDER, gender);
        values.put(DBContract.StudentEntry.STUDENT_IMAGE, image);

        long insertUserData = db.insert(DBContract.StudentEntry.TABLE_NAME, null, values);

        if(insertUserData == -1)
        {
            Toast.makeText(this, "Registration Failed, Try Again", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Registration Successfull", Toast.LENGTH_SHORT).show();
            goToSelectCourseActivity(UserAttribute.get(0));
        }
    }

    public void clearData()
    {
        EditText fullnameEditText = (EditText) findViewById(R.id.fullnameEditText);
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        RadioGroup genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);

        fullnameEditText.setText("");
        usernameEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
        genderRadioGroup.clearCheck();
    }

    public void goToLoginActivity(View view)
    {
        clearData();

        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToSelectCourseActivity(String username)
    {
        clearData();

        Intent intent = new Intent(SignUpActivity.this, SelectCourseActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
