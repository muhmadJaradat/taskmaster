package com.example.taskmaster;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataAccessObject {
    @Query("SELECT * FROM task")
    List<Task> getAllTasks();

    @Query("SELECT * FROM task WHERE id = :id")
    Task loadOneById(int id);

    @Insert
    void insertOne (Task task);

    @Delete
    void deleteOne(Task taskMode);
}