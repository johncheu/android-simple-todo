package com.example.john.simpletodo.ui.editscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.john.simpletodo.R;

public class EditItemActivity extends AppCompatActivity {

    //region VARIABLES
    private int editItemIndex;
    private EditText etEditItem;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        editItemIndex = getIntent().getIntExtra("itemIndex", -1);

        etEditItem = (EditText)findViewById(R.id.etEditItem);
        etEditItem.setText(getIntent().getStringExtra("itemDescription"));
    }

    public void onSaveItem(View view) {
        Intent data = new Intent();
        data.putExtra("itemIndex", editItemIndex);
        data.putExtra("itemDescription", etEditItem.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
