package com.randazzo.mario.cryptopost_it;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import io.github.kexanie.library.MathView;


public class BezoutFragment extends Fragment {

    private static final String TAG_A_EDIT_FIELD = "bezout_a_edit_field";
    private static final String TAG_B_EDIT_FIELD = "bezout_b_edit_field";
    private static final String TAG_RESULT = "bezout_result_math_view";

    private MathView mResultView;
    private EditText mAEditText;
    private EditText mBEditText;
    private MainActivity mActivity;

    public BezoutFragment() {
    }

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

                StringBuilder resultBuilder = new StringBuilder(getString(R.string.bezout_result_label));
                resultBuilder.append(String.format(
                        getString(R.string.bezout_result),
                        lastStep.getT(), lastStep.getS(), lastStep.getR()
                ));

                resultBuilder.append(getString(R.string.bezout_step_label));
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

                String resultText = resultBuilder.toString();
                mResultView.setText(resultText);
            }
        });
        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_A_EDIT_FIELD, mAEditText.getText().toString());
        outState.putString(TAG_B_EDIT_FIELD, mBEditText.getText().toString());
        outState.putString(TAG_RESULT, mResultView.getText());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mAEditText.setText(savedInstanceState.getString(TAG_A_EDIT_FIELD));
            mBEditText.setText(savedInstanceState.getString(TAG_B_EDIT_FIELD));

            String resultText = savedInstanceState.getString(TAG_RESULT);
            if (resultText != null)
                mResultView.setText(resultText);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity)
            this.mActivity = (MainActivity) context;
        else throw new IllegalStateException("Fragment is not managed by MainActivity!");
    }
}
