package com.example.testroom.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.testroom.model.User;

@Dao
public interface UserDao {
   @Query("SELECT * FROM users")
   User[] getAll();

   @Query("SELECT * FROM users WHERE id = :userId")
   User getById(int userId);

   @Insert
   void insertUsers(User... users);
}
