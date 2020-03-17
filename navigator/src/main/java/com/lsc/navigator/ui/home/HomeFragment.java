package com.lsc.navigator.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lsc.libnavannotation.FragmentNav;
import com.lsc.navigator.R;

@FragmentNav(pageUrl = "main/tabs/home",asStarter = true)
public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("home","onCreateView");
        return root;
    }
}