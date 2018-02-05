package com.example.taras.sqliteprojectmodify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by Taras on 05.02.2018.
 */

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    ArrayList<String> listItem;
    ArrayAdapter<String> adapter;
    DatabaseHelper mDatabaseHelper;
    ListView listView;
    Button btn_Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// получаем экземпляр элемента ListView
        btn_Add = (Button) findViewById(R.id.btn_add_ID);
        listView = (ListView) findViewById(R.id.listView_ID);
        final EditText editText = (EditText) findViewById(R.id.editable_ID);

        mDatabaseHelper = new DatabaseHelper(this);

        // Создаём пустой массив для хранения имен котов
        listItem = new ArrayList<>();

        // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listItem);
        // Привяжем массив через адаптер к ListView
        listView.setAdapter(adapter);


        // Прослушиваем нажатия клавиш
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newEntry = editText.getText().toString();
                if (editText.length() != 0) {
                    listItem.add(0, editText.getText().toString());
                    AddData(newEntry);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }

            }
        });

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Toast.makeText(getApplicationContext(),listItem.get(position),Toast.LENGTH_LONG);
           }
       });

        registerForContextMenu(listView);

        //поиск по листу
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCTW_ID);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, listItem));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_file, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo obj = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.delete:
                //String str = obj.toString();
                listItem.remove(obj.position);
                mDatabaseHelper.deleteName(obj.position,obj.toString());
                toastMessage("removed from database");
                adapter.notifyDataSetChanged();
                break;
        }


        return super.onContextItemSelected(item);
    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}

