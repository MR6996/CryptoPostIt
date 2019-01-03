package com.randazzo.mario.cryptopost_it;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.math.BigInteger;

import cryptography.ElGamalHacker;
import cryptography.Groupable;
import cryptography.Number;
import cryptography.Point;


public class ElGamalFragment extends BaseFragment {

    private static final String TAG_TYPE_TOGGLE = "el_gamal_type_toggle";
    private static final String TAG_P_EDIT_FIELD = "el_gamal_p_edit_field";
    private static final String TAG_C_EDIT_FIELD = "el_gamal_c_edit_field";
    private static final String TAG_ALPHA_EDIT_FIELD = "el_gamal_alpha_edit_field";
    private static final String TAG_BETA_EDIT_FIELD = "el_gamal_beta_edit_field";
    private static final String TAG_GAMMA_EDIT_FIELD = "el_gamal_gamma_edit_field";

    private EditText mPEditText;
    private EditText mAlphaEditText;
    private EditText mBetaEditText;
    private EditText mGammaEditText;
    private EditText mCEditText;
    private ToggleButton mTypeToggle;

    private CompoundButton.OnCheckedChangeListener mToggleChengeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mActivity.hideSoftInput();
            mAlphaEditText.setText("");
            mBetaEditText.setText("");
            mGammaEditText.setText("");
            mCEditText.setText("");
            if (isChecked) {
                mAlphaEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                mBetaEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                mGammaEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                mCEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
            } else {
                mAlphaEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                mBetaEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                mGammaEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                mCEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }
    };


    private View.OnClickListener mCalculationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mActivity.hideSoftInput();

            Editable pText = mPEditText.getText();
            Editable alphaText = mAlphaEditText.getText();
            Editable betaText = mBetaEditText.getText();
            Editable gammaText = mGammaEditText.getText();
            Editable cText = mCEditText.getText();

            if (pText.length() == 0 || alphaText.length() == 0 || betaText.length() == 0 || gammaText.length() == 0 || cText.length() == 0) {
                mActivity.showErrorDialog(getString(R.string.empty_edit_text));
                return;
            }

            BigInteger p = new BigInteger(pText.toString());
            if (!p.isProbablePrime(4)) {
                mActivity.showErrorDialog(getString(R.string.dl_not_prime_integer));
                return;
            }

            if (mTypeToggle.isChecked())
                calculateResult(
                        new Number(alphaText.toString()),
                        new Number(betaText.toString()),
                        new Number(gammaText.toString()),
                        new Number(cText.toString()),
                        new BigInteger(pText.toString())
                );
            else {
                String format = "\\([0-9]+;[0-9]+\\)";
                if (!alphaText.toString().matches(format) || !betaText.toString().matches(format) || !gammaText.toString().matches(format) || !cText.toString().matches(format)) {
                    mActivity.showErrorDialog("Invalid syntax for points.");
                    return;
                }

                String[] alphaValues = alphaText.toString().replaceAll("[()]", "").split(";");
                String[] betaValues = betaText.toString().replaceAll("[()]", "").split(";");
                String[] gammaValues = gammaText.toString().replaceAll("[()]", "").split(";");
                String[] cValues = cText.toString().replaceAll("[()]", "").split(";");

                Point alpha = new Point(
                        new BigInteger(alphaValues[0]),
                        new BigInteger(alphaValues[1])
                );
                Point beta = new Point(
                        new BigInteger(betaValues[0]),
                        new BigInteger(betaValues[1])
                );
                Point gamma = new Point(
                        new BigInteger(gammaValues[0]),
                        new BigInteger(gammaValues[1])
                );
                Point c = new Point(
                        new BigInteger(cValues[0]),
                        new BigInteger(cValues[1])
                );

                calculateResult(alpha, beta, gamma, c, new BigInteger(pText.toString()));
            }

        }


        private <T extends Groupable<T>> void calculateResult(T alpha, T beta, T gamma, T c, BigInteger p) {
            try {
                ElGamalHacker<T> hacker = new ElGamalHacker<>(gamma, alpha, beta, p);
                BigInteger a = hacker.getA();
                BigInteger b = hacker.getB();
                T m = hacker.decrypt(c);

                String resultBuilder = getString(R.string.result_label) + String.format(
                        getString(R.string.el_gamal_result),
                        a.toString(), b.toString(),
                        hacker.getSecretKey(),
                        m
                );
                mResultView.setText(resultBuilder);
            } catch (ArithmeticException ex) {
                mActivity.showErrorDialog("Invalid data!");
            }
        }
    };


    public ElGamalFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflateLayouts(inflater, container, R.layout.fragment_el_gamal);

        mPEditText = mainView.findViewById(R.id.el_gamal_p_edit_text);
        mAlphaEditText = mainView.findViewById(R.id.el_gamal_alpha_edit_text);
        mBetaEditText = mainView.findViewById(R.id.el_gamal_beta_edit_text);
        mGammaEditText = mainView.findViewById(R.id.el_gamal_gamma_edit_text);
        mCEditText = mainView.findViewById(R.id.el_gamal_c_edit_text);

        mTypeToggle = mainView.findViewById(R.id.el_gamal_type_toggle);
        mTypeToggle.setOnCheckedChangeListener(mToggleChengeListener);

        Button calculateButton = mainView.findViewById(R.id.el_gamal_calculate_button);
        calculateButton.setOnClickListener(mCalculationListener);

        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_P_EDIT_FIELD, mPEditText.getText().toString());
        outState.putString(TAG_C_EDIT_FIELD, mCEditText.getText().toString());
        outState.putString(TAG_ALPHA_EDIT_FIELD, mAlphaEditText.getText().toString());
        outState.putString(TAG_BETA_EDIT_FIELD, mBetaEditText.getText().toString());
        outState.putString(TAG_GAMMA_EDIT_FIELD, mGammaEditText.getText().toString());
        outState.putBoolean(TAG_TYPE_TOGGLE, mTypeToggle.isChecked());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mTypeToggle.setChecked(savedInstanceState.getBoolean(TAG_TYPE_TOGGLE));
            mPEditText.setText(savedInstanceState.getString(TAG_P_EDIT_FIELD));
            mCEditText.setText(savedInstanceState.getString(TAG_C_EDIT_FIELD));
            mAlphaEditText.setText(savedInstanceState.getString(TAG_ALPHA_EDIT_FIELD));
            mBetaEditText.setText(savedInstanceState.getString(TAG_BETA_EDIT_FIELD));
            mGammaEditText.setText(savedInstanceState.getString(TAG_GAMMA_EDIT_FIELD));
        }
    }
}
