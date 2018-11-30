package com.randazzo.mario.cryptopost_it;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import cryptography.Number;
import cryptography.Point;
import io.github.kexanie.library.MathView;


public class DiscreteLogFragment extends Fragment {

    private ToggleButton mTypeToggle;
    private MainActivity mActivity;
    private EditText mPEditText;
    private EditText mAlphaEditText;
    private EditText mGammaEditText;
    private MathView mResultView;

    private View.OnClickListener mCalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mActivity.hideSoftInput();

            Editable pText = mPEditText.getText();
            Editable alphaText = mAlphaEditText.getText();
            Editable gammaText = mGammaEditText.getText();

            if (pText.length() == 0 || alphaText.length() == 0 || gammaText.length() == 0) {
                mActivity.showErrorDialog(getString(R.string.empty_edit_text));
                return;
            }

            BigInteger p = new BigInteger(pText.toString());
            if (!p.isProbablePrime(4)) {
                mActivity.showErrorDialog(getString(R.string.dl_not_prime_integer));
                return;
            }

            if (mTypeToggle.isChecked())
                calculateIntegerLogarithm(alphaText.toString(), gammaText.toString());
            else
                calculatePointLocarithm(alphaText.toString(), gammaText.toString());
        }

        private void calculateIntegerLogarithm(String alphaText, String gammaText) {
            Number alpha = new Number(new BigInteger(alphaText));
            Number gamma = new Number(new BigInteger(gammaText));

            if (alpha.getValue().compareTo(BigInteger.ZERO) <= 0 ||
                    gamma.getValue().compareTo(BigInteger.ZERO) <= 0) {
                mActivity.showErrorDialog("Invalid integers.");
                return;
            }

            mResultView.setText("To implement integer discrete log.");
        }

        private void calculatePointLocarithm(String alphaText, String gammaText) {
            String format = "\\([0-9]+;[0-9]+\\)";
            if (!alphaText.matches(format) || !gammaText.matches(format)) {
                mActivity.showErrorDialog("Invalid syntax for points.");
                return;
            }

            String[] alphaValues = alphaText.replaceAll("[\\(\\)]", "").split(";");
            String[] gammaValues = gammaText.replaceAll("[\\(\\)]", "").split(";");

            Point alpha = new Point(
                    new BigInteger(alphaValues[0]),
                    new BigInteger(alphaValues[1])
            );
            Point gamma = new Point(
                    new BigInteger(gammaValues[0]),
                    new BigInteger(gammaValues[1])
            );

            mResultView.setText("To implement point discrete log.");
        }
    };

    private CompoundButton.OnCheckedChangeListener mToggleChengeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mActivity.hideSoftInput();
            mAlphaEditText.setText("");
            mGammaEditText.setText("");
            if (isChecked) {
                mAlphaEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                mGammaEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
            } else {
                mAlphaEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                mGammaEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }
    };

    public DiscreteLogFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_discrete_log, container, false);

        mPEditText = mainView.findViewById(R.id.dl_p_edit_text);
        mAlphaEditText = mainView.findViewById(R.id.dl_alpha_edit_text);
        mGammaEditText = mainView.findViewById(R.id.dl_gamma_edit_text);
        mResultView = mainView.findViewById(R.id.dl_result_view);

        mTypeToggle = mainView.findViewById(R.id.dl_type_toggle);
        mTypeToggle.setOnCheckedChangeListener(mToggleChengeListener);

        Button calculateButton = mainView.findViewById(R.id.dl_calculate_button);
        calculateButton.setOnClickListener(mCalculateListener);

        return mainView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity)
            this.mActivity = (MainActivity) context;
        else throw new IllegalStateException("Fragment is not managed by MainActivity!");
    }
}
