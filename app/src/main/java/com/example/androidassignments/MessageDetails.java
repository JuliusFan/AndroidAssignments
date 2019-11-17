package com.example.androidassignments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MessageDetails extends AppCompatActivity {

    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        MessageFragment messageFragment = new MessageFragment();
        this.position = getIntent().getIntExtra("position",0);
        Bundle args = new Bundle();
        args.putInt("position", this.position);
        messageFragment.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.flContainer,messageFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}
