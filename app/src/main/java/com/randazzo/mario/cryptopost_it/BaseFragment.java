package com.randazzo.mario.cryptopost_it;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import io.github.kexanie.library.MathView;


public abstract class BaseFragment extends Fragment {

    protected MainActivity mActivity;
    protected MathView mResultView;
    protected ProgressBar mLoadingBar;

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

        mResultView = mainView.findViewById(R.id.result_view);
        mLoadingBar = mainView.findViewById(R.id.loading_bar);

        mResultView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mLoadingBar.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }
        });

        return mainView;
    }

}
