package com.randazzo.mario.cryptopost_it;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
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
        mAEditText = mainView.findViewById(R.id.bezout_a_edit_text);
        mBEditText = mainView.findViewById(R.id.bezout_b_edit_text);

        Button calculateButton = mainView.findViewById(R.id.bezout_calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable aText = mAEditText.getText();
                Editable bText = mBEditText.getText();

                if (aText.length() == 0 || bText.length() == 0) {
                    showErrorDialog(getString(R.string.empty_edit_text));
                    return;
                }

                BigInteger a = new BigInteger(aText.toString());
                BigInteger b = new BigInteger(bText.toString());

                if (a.equals(BigInteger.ZERO) || b.equals(BigInteger.ZERO)) {
                    showErrorDialog(getString(R.string.null_bezout_parameter));
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

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.error_dialog_title))
                .setMessage(message)
                .setPositiveButton(getString(R.string.error_dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

}
