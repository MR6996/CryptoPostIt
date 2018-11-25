package com.randazzo.mario.cryptopost_it;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigInteger;

import cryptography.Utils;
import io.github.kexanie.library.MathView;


public class BezoutFragment extends Fragment {

    private MathView mResultView;
    private EditText mAEditText;
    private EditText mBEditText;

    public BezoutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_bezout, container, false);

        mResultView = mainView.findViewById(R.id.bezout_result_view);
        mAEditText = mainView.findViewById(R.id.a_edit_text);
        mBEditText = mainView.findViewById(R.id.b_edit_text);

        Button calculateButton = mainView.findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BigInteger a = new BigInteger(mAEditText.getText().toString());
                BigInteger b = new BigInteger(mBEditText.getText().toString());
                BigInteger[] results = Utils.Bezout(a, b);
                String resultText = String.format(getResources().getString(R.string.bezout_result),
                        results[0].toString(), results[1].toString());
                mResultView.setText(resultText);
            }
        });

        return mainView;
    }

}
