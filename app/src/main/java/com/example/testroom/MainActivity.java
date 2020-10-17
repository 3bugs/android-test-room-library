package com.example.testroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testroom.db.AppDatabase;
import com.example.testroom.model.User;
import com.example.testroom.util.DateFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private AppExecutors mExecutors = new AppExecutors();
  private Calendar mCalendar = Calendar.getInstance();

  private EditText mDateEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        AppDatabase db = AppDatabase.getInstance(MainActivity.this);
        final User[] users = db.userDao().getAll();
        String msg = "";
        for (User u : users) {
          msg = msg.concat(u.firstName) + "\n";
        }
        final String message = msg;
        mExecutors.mainThread().execute(new Runnable() {
          @Override
          public void run() {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
             if (users.length > 0) {
                mCalendar.setTime(users[0].birthDate);
                updateFeedDateEditText();
             } else {
                Toast.makeText(MainActivity.this, "Database is empty!", Toast.LENGTH_LONG).show();
             }
          }
        });
      }
    });

    mDateEditText = findViewById(R.id.date_edit_text);
    mDateEditText.setInputType(InputType.TYPE_NULL);
    mDateEditText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
         final DatePickerDialog.OnDateSetListener dateSetListener =
             new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                   mCalendar.set(Calendar.YEAR, year);
                   mCalendar.set(Calendar.MONTH, monthOfYear);
                   mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                   updateFeedDateEditText();
                }
             };
         new DatePickerDialog(
             MainActivity.this,
             dateSetListener,
             mCalendar.get(Calendar.YEAR),
             mCalendar.get(Calendar.MONTH),
             mCalendar.get(Calendar.DAY_OF_MONTH)
         ).show();
      }
    });
  }

   private void updateFeedDateEditText() {
      String formatDate = DateFormatter.formatForUi(mCalendar.getTime());
      mDateEditText.setText(formatDate);
   }
}
