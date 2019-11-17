package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.androidassignments.ChatDatabaseHelper.TABLE_NAME;

public class ChatWindow extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static ArrayList<String> messages = new ArrayList<>();
    private static final String ACTIVITY_NAME = "ChatWindow";
    private SQLiteDatabase db;
    private ChatDatabaseHelper cdh = new ChatDatabaseHelper(this);
    private boolean frameExist;
    protected Cursor cursor;
    private ChatAdapter messageAdapter;
    private FragmentTransaction ft;
    private MessageFragment messageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i(ACTIVITY_NAME,"In onCreate");

        messages.clear();

        FrameLayout fl = findViewById(R.id.frame);
        if(fl==null)
            this.frameExist=false;
        else
            this.frameExist=true;
        Log.i(ACTIVITY_NAME,"frameExist: "+this.frameExist);

        db = cdh.getWritableDatabase();
        setCursor();
        while(!this.cursor.isAfterLast()) {
            messages.add(this.cursor.getString(this.cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + this.cursor.getString(this.cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            this.cursor.moveToNext();
        }
        setCursor();
        Log.i(ACTIVITY_NAME,"Cursor's column count="+this.cursor.getColumnCount());
        for(int i = 0; i < this.cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME,"Column name " + i + ": "+ this.cursor.getColumnName(i));
        }

        final EditText editText = findViewById(R.id.chatText);
        final ListView listView = findViewById(R.id.list_item);
        final Button button = findViewById(R.id.button_send);
        listView.setOnItemClickListener(this);

        this.messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                messages.add(text);
                ContentValues cv = new ContentValues();
                cv.put(ChatDatabaseHelper.KEY_MESSAGE,text);
                db.insert(TABLE_NAME, null, cv);
                messageAdapter.notifyDataSetChanged();
                editText.setText("");
                setCursor();
                while(!cursor.isAfterLast()){
                    Log.i(ACTIVITY_NAME,cursor.getLong(cursor.getColumnIndex("_id"))+":"+cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                    cursor.moveToNext();
                }
                setCursor();
            }
        });

    }

    private void setCursor(){
        this.cursor =  db.rawQuery("SELECT * from MESSAGES", null);
        this.cursor.moveToFirst();
    }

    public void onItemClick(AdapterView l, View view, final int position, long id){
        Log.i(ACTIVITY_NAME, "Item clicked");
        Log.i(ACTIVITY_NAME, "position: "+position);

        if(this.frameExist){
            Bundle args = new Bundle();
            args.putInt("position", position);
            messageFragment = new MessageFragment();
            messageFragment.setArguments(args);
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame,messageFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else {
            Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
            intent.putExtra("position",position);
            startActivityForResult(intent,10);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK){
            int position = data.getIntExtra("position",0);
            int id = position+1;
            messages.remove(position);
            db.execSQL("delete from " + TABLE_NAME + " where _id='" + id + "'");
            db.delete(TABLE_NAME, "_id = " + id, null);
            if (this.frameExist)
                ft.remove(messageFragment).commit();
            messageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy(){
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        db.close();
        cdh.close();
        super.onDestroy();
    }


    private class ChatAdapter extends ArrayAdapter<String>{

        public ChatAdapter(Context context){
            super(context, 0);
        }

        public int getCount(){
            return messages.size();
        }

        public String getItem(int position){
            return messages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup Parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;
            if (position%2==0)
                result = inflater.inflate(R.layout.chat_row_incoming,null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing,null);
            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }

        public long getItemId(int position){
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex("_id"));
        }

    }

}
