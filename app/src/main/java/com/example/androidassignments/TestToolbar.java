package com.example.androidassignments;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    String snackbarString = "You selected Item 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.SnackbarLetter, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

    }

    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu,m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){

        int id = mi.getItemId();
        switch(id){
            case R.id.action_one:
                Log.d("Toolbar","Action 1 selected");
                Snackbar.make(findViewById(R.id.action_one), snackbarString, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.action_two:
                Log.d("Toolbar","Action 2 selected");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.DialogToolbar);
                builder.setPositiveButton(R.string.DialogYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TestToolbar.this.finish();
                    }
                });
                builder.setNegativeButton(R.string.DialogNo, null);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.action_three:
                Log.d("Toolbar","Action 3 selected");
                AlertDialog.Builder customDialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View v = inflater.inflate(R.layout.test_toolbar_customdialog, null);
                customDialogBuilder.setView(v);
                final EditText newMessageET = v.findViewById(R.id.new_message);
                customDialogBuilder.setPositiveButton(R.string.DialogYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        snackbarString = newMessageET.getText().toString();
                    }
                });
                customDialogBuilder.setNegativeButton(R.string.DialogNo, null);
                AlertDialog customDialog = customDialogBuilder.create();
                customDialog.show();
                break;
            case R.id.action_about:
                Log.d("Toolbar","About selected");
                int text = R.string.ToastVersion;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(TestToolbar.this, text, duration);
                toast.show();
                break;
        }
        return true;
    }

}
