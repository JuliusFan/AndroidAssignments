package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.androidassignments.ChatDatabaseHelper.TABLE_NAME;

public class ChatWindow extends AppCompatActivity {

    final ArrayList<String> messages = new ArrayList<String>();
    private static final String ACTIVITY_NAME = "ChatWindow";
    private SQLiteDatabase db;
    private ChatDatabaseHelper cdh = new ChatDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i(ACTIVITY_NAME,"In onCreate");
        db = cdh.getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * from MESSAGES", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            messages.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }
        Log.i(ACTIVITY_NAME,"Cursor's column count="+cursor.getColumnCount());
        for(int i = 0; i < cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME,"Column name " + i + ": "+ cursor.getColumnName(i));
        }

        final EditText editText = findViewById(R.id.chatText);
        final ListView listView = findViewById(R.id.list_item);
        final Button button = findViewById(R.id.button_send);

        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                messages.add(text);
                messageAdapter.notifyDataSetChanged();
                ContentValues cv = new ContentValues();
                cv.put(ChatDatabaseHelper.KEY_MESSAGE,text);
                db.insert(TABLE_NAME, null, cv);
                editText.setText("");
            }
        });

    }

    @Override
    protected void onDestroy(){
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

    }

}
