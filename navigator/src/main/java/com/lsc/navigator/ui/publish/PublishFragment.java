package com.lsc.navigator.ui.publish;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lsc.libnavannotation.FragmentNav;
import com.lsc.navigator.R;

@FragmentNav(pageUrl = "main/tabs/publish")
public class PublishFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("publish","onCreateView");
        return root;
    }
}