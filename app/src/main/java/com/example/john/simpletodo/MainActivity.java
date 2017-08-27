package com.example.john.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //region REQUEST CODES
    private final int REQUEST_EDIT = 20;
    //endregion

    //region VARIABLES
    private ArrayList<String> todoItems;
    private ArrayAdapter<String> todoAdapter;
    private ListView lvItems;
    private EditText etNewItem;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateTodoItems();

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(todoAdapter);
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        todoItems.remove(i);
                        onTodoItemsChanged();
                        return false;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        launchEditView(i);
                    }
                }
        );
        etNewItem = (EditText) findViewById(R.id.etNewItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT) {
            int itemIndex = data.getExtras().getInt("itemIndex", -1);
            String itemText = data.getExtras().getString("itemText");
            if (itemIndex > -1)
            {
                todoItems.set(itemIndex, itemText);
                onTodoItemsChanged();
            }
        }
    }

    public void onAddItem(View view) {
        String itemText = etNewItem.getText().toString();
        if (!itemText.isEmpty()) {
            todoAdapter.add(itemText);
            etNewItem.setText("");
            saveItems();
        }
    }

    //region HELPER METHODS
    private void onTodoItemsChanged() {
        todoAdapter.notifyDataSetChanged();
        saveItems();
    }

    private void populateTodoItems() {
        loadItems();
        todoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    private void launchEditView(int itemIndex) {
        Intent editIntent = new Intent(MainActivity.this, EditItemActivity.class);
        editIntent.putExtra("itemIndex", itemIndex);
        editIntent.putExtra("itemText", todoAdapter.getItem(itemIndex));
        startActivityForResult(editIntent, REQUEST_EDIT);
    }
    //endregion

    //region SAVE LOAD
    private void loadItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            todoItems = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            todoItems = new ArrayList<>();
        }
    }

    private void saveItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, todoItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion

}
