package com.example.john.simpletodo.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = TodoListDatabase.class)
public class TodoItem extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String description;

    public String toString()
    {
        return description;
    }
}
