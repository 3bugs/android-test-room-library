package com.example.testroom.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.testroom.util.DateConverter;

import java.util.Date;

@Entity(tableName = "users")
public class User {
   public static int GENDER_MALE = 0;
   public static int GENDER_FEMALE = 1;

   @PrimaryKey(autoGenerate = true)
   public int id;

   @ColumnInfo(name = "first_name")
   public String firstName;

   @ColumnInfo(name = "last_name")
   public String lastName;

   @ColumnInfo(name = "birth_date")
   @TypeConverters(DateConverter.class)
   public Date birthDate;

   public int gender;
   public boolean single;

   public User(int id, String firstName, String lastName, Date birthDate, int gender, boolean single) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.birthDate = birthDate;
      this.gender = gender;
      this.single = single;
   }
}
