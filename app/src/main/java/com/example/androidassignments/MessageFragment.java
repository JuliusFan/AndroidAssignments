package com.example.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {

    private int position = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            if(getArguments() != null && getArguments().containsKey("position") ) {
                position = getArguments().getInt("position", 0);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView msg = view.findViewById(R.id.text_message_here);
        msg.setText(ChatWindow.messages.get(position));

        TextView id = view.findViewById(R.id.text_ID);
        id.setText(Integer.toString(position));

        final Button deleteButton = view.findViewById(R.id.delete_msg_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("position", position);
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

}
