package com.example.john.simpletodo.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = TodoListDatabase.NAME, version = TodoListDatabase.VERSION)
public class TodoListDatabase {
    public static final String NAME = "TodoListDatabase";
    public static final int VERSION = 1;

}
