package com.example.john.simpletodo.ui.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.john.simpletodo.R;
import com.example.john.simpletodo.database.TodoItem;
import com.example.john.simpletodo.ui.editscreen.EditItemActivity;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //region REQUEST CODES
    private final int REQUEST_EDIT = 20;
    //endregion

    //region VARIABLES
    private ArrayList<TodoItem> todoItems;
    private ArrayAdapter<TodoItem> todoAdapter;
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
                        DeleteItem(i);
                        return true;
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
            String itemDescription = data.getExtras().getString("itemDescription");
            if (itemIndex > -1) {
                EditItem(itemIndex, itemDescription);
            }
        }
    }

    //region EVENT HANDLERS
    public void onAddItem(View view) {
        String itemText = etNewItem.getText().toString();
        if (!itemText.isEmpty()) {
            AddItem(itemText);
            etNewItem.setText("");
        }
    }
    //endregion

    //region HELPER METHODS
    private void populateTodoItems() {
        todoItems = new ArrayList<>(SQLite.select().
                from(TodoItem.class).
                queryList()
        );
        todoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    private void launchEditView(int itemIndex) {
        Intent editIntent = new Intent(MainActivity.this, EditItemActivity.class);
        TodoItem todoItem = todoAdapter.getItem(itemIndex);
        editIntent.putExtra("itemIndex", itemIndex);
        editIntent.putExtra("itemDescription", todoItem.description);
        startActivityForResult(editIntent, REQUEST_EDIT);
    }

    private void AddItem(String description)
    {
        TodoItem todoItem = new TodoItem();
        todoItem.description = description;
        todoItem.save();

        todoAdapter.add(todoItem);
    }

    private void EditItem(int index, String description)
    {
        TodoItem todoItem = todoAdapter.getItem(index);
        todoItem.description = description;
        todoItem.save();

        todoAdapter.notifyDataSetChanged();
    }

    private void DeleteItem(int index)
    {
        TodoItem todoItem = todoAdapter.getItem(index);
        todoItem.delete();

        todoAdapter.remove(todoItem);
    }
    //endregion

    //region FILE SAVE LOAD
    /*
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
    */
    //endregion

}
