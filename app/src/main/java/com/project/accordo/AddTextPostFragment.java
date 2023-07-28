package com.project.accordo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.project.accordo.Service.RequestController;

public class AddTextPostFragment extends Fragment {

    private String TAG = this.getClass().getCanonicalName();
    private EditText etText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_text_post, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        etText = getActivity().findViewById(R.id.new_post);
    }

    public String getText(){
        return etText.getText().toString();
    }

}