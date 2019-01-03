package com.randazzo.mario.cryptopost_it;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigInteger;

import cryptography.RSAHacker;


public class RSAFragment extends BaseFragment {

    private static final String TAG_N_EDIT_FIELD = "RSA_n_edit_field";
    private static final String TAG_E_EDIT_FIELD = "RSA_e_edit_field";
    private static final String TAG_C_EDIT_FIELD = "RSA_c_edit_field";

    private EditText mNEditText;
    private EditText mEEditText;
    private EditText mCEditText;

    private View.OnClickListener mCalculationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mActivity.hideSoftInput();

            Editable nText = mNEditText.getText();
            Editable eText = mEEditText.getText();
            Editable cText = mCEditText.getText();

            if (nText.length() == 0 || cText.length() == 0 || eText.length() == 0) {
                mActivity.showErrorDialog(getString(R.string.empty_edit_text));
                return;
            }

            BigInteger n = new BigInteger(nText.toString());
            BigInteger e = new BigInteger(eText.toString());
            BigInteger c = new BigInteger(cText.toString());

            if (n.isProbablePrime(5))
                mActivity.showWarningDialog(getString(R.string.rsa_n_prime_number));


            if (e.compareTo(BigInteger.ZERO) < 0) {
                mActivity.showErrorDialog(getString(R.string.rsa_null_public_key));
                return;
            }

            try {
                RSAHacker hacker = new RSAHacker(n, e);
                BigInteger p = hacker.getP();
                BigInteger q = hacker.getQ();
                BigInteger m = hacker.decrypt(c);

                String resultBuilder = getString(R.string.result_label) + String.format(
                        getString(R.string.rsa_result),
                        p.toString(), q.toString(),
                        p.subtract(BigInteger.ONE).toString(), q.subtract(BigInteger.ONE).toString(),
                        hacker.getLambda().toString(),
                        hacker.getPrivateKey(),
                        m
                );
                mResultView.setText(resultBuilder);
            } catch (ArithmeticException ex) {
                mActivity.showErrorDialog("Invalid data!");
            }
        }
    };

    public RSAFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflateLayouts(inflater, container, R.layout.fragment_rsa);

        mNEditText = mainView.findViewById(R.id.rsa_n_edit_text);
        mEEditText = mainView.findViewById(R.id.rsa_e_edit_text);
        mCEditText = mainView.findViewById(R.id.rsa_c_edit_text);

        Button calculateButton = mainView.findViewById(R.id.rsa_calculate_button);
        calculateButton.setOnClickListener(mCalculationListener);

        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_N_EDIT_FIELD, mNEditText.getText().toString());
        outState.putString(TAG_E_EDIT_FIELD, mEEditText.getText().toString());
        outState.putString(TAG_C_EDIT_FIELD, mCEditText.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mNEditText.setText(savedInstanceState.getString(TAG_N_EDIT_FIELD));
            mEEditText.setText(savedInstanceState.getString(TAG_E_EDIT_FIELD));
            mCEditText.setText(savedInstanceState.getString(TAG_C_EDIT_FIELD));
        }
    }
}
