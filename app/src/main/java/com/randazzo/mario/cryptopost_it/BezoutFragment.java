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
import java.util.List;

import cryptography.BezoutStep;
import cryptography.Utils;


public class BezoutFragment extends BaseFragment {

    private static final String TAG_A_EDIT_FIELD = "bezout_a_edit_field";
    private static final String TAG_B_EDIT_FIELD = "bezout_b_edit_field";

    private EditText mAEditText;
    private EditText mBEditText;

    private View.OnClickListener mCalculateListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mActivity.hideSoftInput();

            Editable aText = mAEditText.getText();
            Editable bText = mBEditText.getText();

            if (aText.length() == 0 || bText.length() == 0) {
                mActivity.showErrorDialog(getString(R.string.empty_edit_text));
                return;
            }

            BigInteger a = new BigInteger(aText.toString());
            BigInteger b = new BigInteger(bText.toString());

            if (a.equals(BigInteger.ZERO) || b.equals(BigInteger.ZERO)) {
                mActivity.showErrorDialog(getString(R.string.null_bezout_parameter));
                return;
            }

            List<BezoutStep> steps = Utils.bezoutSteps(a, b);
            BezoutStep lastStep = steps.get(steps.size() - 1);

            StringBuilder resultBuilder = new StringBuilder(getString(R.string.result_label));
            resultBuilder.append(String.format(
                    getString(R.string.bezout_result),
                    lastStep.getT(), lastStep.getS(), lastStep.getR()
            ));

            resultBuilder.append(getString(R.string.step_label));
            for (int i = 0; i < steps.size(); i++) {
                BezoutStep s = steps.get(i);
                if (s.isInitialStep())
                    resultBuilder.append(String.format(
                            getString(R.string.bezout_initial_step),
                            i, s.getR(), i, s.getS(), i, s.getT()
                    ));
                else
                    //r_%d = r_%d - %s \\cdot r_%d = %s
                    resultBuilder.append(String.format(
                            getString(R.string.bezout_step),
                            i, i - 2, s.getQ(), i - 1, s.getR(),
                            i, i - 2, s.getQ(), i - 1, s.getS(),
                            i, i - 2, s.getQ(), i - 1, s.getT()
                    ));

            }

            mResultView.setText(resultBuilder.toString());
        }
    };

    public BezoutFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflateLayouts(inflater, container, R.layout.fragment_bezout);

        mAEditText = mainView.findViewById(R.id.bezout_a_edit_text);
        mBEditText = mainView.findViewById(R.id.bezout_b_edit_text);

        Button calculateButton = mainView.findViewById(R.id.bezout_calculate_button);
        calculateButton.setOnClickListener(mCalculateListner);
        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_A_EDIT_FIELD, mAEditText.getText().toString());
        outState.putString(TAG_B_EDIT_FIELD, mBEditText.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mAEditText.setText(savedInstanceState.getString(TAG_A_EDIT_FIELD));
            mBEditText.setText(savedInstanceState.getString(TAG_B_EDIT_FIELD));
        }
    }

}
