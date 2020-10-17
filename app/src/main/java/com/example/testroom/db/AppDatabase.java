package com.example.testroom.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.testroom.AppExecutors;
import com.example.testroom.MainActivity;
import com.example.testroom.model.User;
import com.example.testroom.util.DateFormatter;

import java.util.Calendar;

@Database(entities = {User.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
  private static final String TAG = "AppDatabase";
  private static final String DB_NAME = "user.db";

  private static AppDatabase sInstance;

  public abstract UserDao userDao();

  public static synchronized AppDatabase getInstance(final Context context) {
    if (sInstance == null) {
      sInstance = Room.databaseBuilder(
          context.getApplicationContext(),
          AppDatabase.class,
          DB_NAME
      ).addCallback(new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
          super.onCreate(db);
          Handler handler = new Handler(Looper.getMainLooper());
          handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              insertInitialData(context);
            }
          }, 1000);
        }
      }).build();
    }
    return sInstance;
  }

  private static void insertInitialData(final Context context) {
    AppExecutors executors = new AppExecutors();
    executors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        AppDatabase db = getInstance(context);
        User[] users = {
            new User(0, "Promlert", "Lovichit", new DateFormatter().parseDateString("1974-11-21"), User.GENDER_MALE, false),
            new User(0, "Natpaphat", "Lovichit", new DateFormatter().parseDateString("2004-10-18"), User.GENDER_FEMALE, true),
            new User(0, "Banlop", "Lovichit", new DateFormatter().parseDateString("2010-01-07"), User.GENDER_MALE, true)
        };
        db.userDao().insertUsers(users);
      }
    });
  }
}
