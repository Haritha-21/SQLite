package com.example.sqlitelistview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Databasehelper db;
    Button adddata;
    EditText  addname;
    ListView userlist;
    ArrayList<String>listItem;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new Databasehelper(this);
        listItem=new ArrayList<>();

        adddata=findViewById(R.id.adddata);
        addname=findViewById(R.id.addname);
        userlist=findViewById(R.id.users_list);

        viewData();
        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                String text= userlist.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this,""+text,Toast.LENGTH_SHORT).show();

            }
        });

        adddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=addname.getText().toString();
                if (!name.equals("")&& db.insertData(name)){
                    Toast.makeText(MainActivity.this ,"Data added", Toast.LENGTH_SHORT).show();
                    addname.setText("");
                    viewData();
                }else{
                    Toast.makeText(MainActivity.this,"Data not added",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void viewData() {
        Cursor cursor =db.viewData();
        if(cursor.getCount()==0){
            Toast.makeText(this,"No data to show",Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                listItem.add(cursor.getString(1));
            }
            adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listItem);
            userlist.setAdapter((ListAdapter) adapter);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        MenuItem searchItem=menu.findItem(R.id.item_search);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String>userslist=new ArrayList<>();
                for (String user : listItem){
                    if(user.toLowerCase().contains(newText.toLowerCase())){
                        userslist.add(user);
                    }
                }
                ArrayAdapter<String>adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,userslist);
                userlist.setAdapter(adapter);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}