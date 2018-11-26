package com.randazzo.mario.cryptopost_it;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigInteger;
import java.util.Objects;

import cryptography.Utils;
import io.github.kexanie.library.MathView;


public class BezoutFragment extends Fragment {

    private MathView mResultView;
    private EditText mAEditText;
    private EditText mBEditText;
    private MainActivity activity;

    public BezoutFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_bezout, container, false);

        mResultView = mainView.findViewById(R.id.bezout_result_view);
        mAEditText = mainView.findViewById(R.id.bezout_a_edit_text);
        mBEditText = mainView.findViewById(R.id.bezout_b_edit_text);

        Button calculateButton = mainView.findViewById(R.id.bezout_calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)
                        Objects.requireNonNull(getActivity())
                                .getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Editable aText = mAEditText.getText();
                Editable bText = mBEditText.getText();

                if (aText.length() == 0 || bText.length() == 0) {
                    activity.showErrorDialog(getString(R.string.empty_edit_text));
                    return;
                }

                BigInteger a = new BigInteger(aText.toString());
                BigInteger b = new BigInteger(bText.toString());

                if (a.equals(BigInteger.ZERO) || b.equals(BigInteger.ZERO)) {
                    activity.showErrorDialog(getString(R.string.null_bezout_parameter));
                    return;
                }

                BigInteger[] results = Utils.Bezout(a, b);
                String resultText = String.format(getString(R.string.bezout_result),
                        results[0].toString(), results[1].toString());
                mResultView.setText(resultText);
            }
        });

        return mainView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity)
            this.activity = (MainActivity)context;
        else throw new IllegalStateException("Fragment is not managed by MainActivity!");
    }
}
