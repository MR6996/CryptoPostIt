package com.randazzo.mario.cryptopost_it;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RSAFragment extends BaseFragment {


    public RSAFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflateLayouts(inflater, container, R.layout.fragment_rsa);
        return mainView;
    }

}
