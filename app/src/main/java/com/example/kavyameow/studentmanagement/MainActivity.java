package com.example.kavyameow.studentmanagement;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText ename, roll, marks;
    SQLiteDatabase db;
    Button view, viewall, add, delete, modify, show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ename = findViewById(R.id.sname);
        roll = findViewById(R.id.sroll);
        marks = findViewById(R.id.smark);
        view = findViewById(R.id.viewbtn);
        viewall = findViewById(R.id.viewallbtn);
        add = findViewById(R.id.addbtn);
        delete = findViewById(R.id.deletebtn);
        modify = findViewById(R.id.modifybtn);
        show = findViewById(R.id.showbtn);
        db = openOrCreateDatabase("student_manage", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno INTEGER,name VARCHAR,marks INTEGER);");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ename.getText().toString().trim().length() == 0 || roll.getText().toString().trim().length() == 0 ||
                        marks.getText().toString().trim().length() == 0) {
                    showmessage("Error", "Please enter all values");
                    return;
                }
                db.execSQL("INSERT INTO student VALUES('" + roll.getText() + "','" + ename.getText() + "','" + marks.getText() + "');");
                showmessage("Success", "Record added successfully");
                cleartext();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roll.getText().toString().trim().length() == 0) {
                    showmessage("Error", "Please enter rollno");
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + roll.getText() + "'", null);
                if (c.moveToFirst()) {
                    db.execSQL("DELETE FROM student WHERE rollno='" + roll.getText() + "'");
                    showmessage("success", "Record deleted");

                } else {
                    showmessage("Error", "Invalid rollno");
                }
                cleartext();
            }
        });
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roll.getText().toString().trim().length() == 0) {
                    showmessage("Error", "Please enter rollno");
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + roll.getText() + "'", null);
                if (c.moveToFirst()) {
                    db.execSQL("UPDATE student SET name='"+ename.getText()+"',marks='"+marks.getText()+"'WHERE rollno='"+roll.getText()+"'");
                    showmessage("success", "Record modified");

                } else {
                    showmessage("Error", "Invalid rollno");
                }
                cleartext();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roll.getText().toString().trim().length() == 0) {
                    showmessage("Error", "Please enter rollno");
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + roll.getText() + "'", null);
                StringBuffer buffer=new StringBuffer();

                if (c.moveToFirst()) {
                    buffer.append("Roll no="+c.getString(0)+"\n");
                    buffer.append("Name="+c.getString(1)+"\n");
                    buffer.append("marks="+c.getString(2)+"\n\n");
                    showmessage("Student details",buffer.toString());


                } else {
                    showmessage("Error", "Invalid rollno");
                    cleartext();
                }

            }
        });
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c = db.rawQuery("SELECT * FROM student",null);
                if (c.getCount()== 0) {
                    showmessage("Error", "No records found");
                    return;
                }
                StringBuffer buffer=new StringBuffer();


                while (c.moveToNext()) {
                    buffer.append("Roll no="+c.getString(0)+"\n");
                    buffer.append("Name="+c.getString(1)+"\n");
                    buffer.append("marks="+c.getString(2)+"\n\n");

                }
                showmessage("Student details",buffer.toString());

            }
        });

    }

    public void showmessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void cleartext() {
        ename.setText("");
        roll.setText("");
        marks.setText("");
        roll.requestFocus();
    }
}
