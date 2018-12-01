package com.randazzo.mario.cryptopost_it;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;


public abstract class BaseFragment extends Fragment {

    protected MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity)
            this.mActivity = (MainActivity) context;
        else throw new IllegalStateException("Fragment is not managed by MainActivity!");
    }

    public View inflateLayouts(@NonNull LayoutInflater inflater, ViewGroup container, int formLayoutRes) {
        View mainView = inflater.inflate(R.layout.fragment_base, container, false);
        ViewStub stub = mainView.findViewById(R.id.form_layout);
        stub.setLayoutResource(formLayoutRes);
        stub.inflate();
        return mainView;
    }

}
