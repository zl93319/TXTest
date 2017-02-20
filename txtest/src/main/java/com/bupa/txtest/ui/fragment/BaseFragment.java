package com.bupa.txtest.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * 作者: l on 2017/2/6 08:19
 * 邮箱: xjs250@163.com
 * 描述:
 */
public abstract class BaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutResId(), null);
        ButterKnife.bind(this, root);
        init();
        return root;
    }

    protected void init() {
    }

    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void startActivity(Class activity, boolean isFinish) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
    }

    public abstract int getLayoutResId();

    protected void startActivity(Class activity) {
        startActivity(activity, true);
    }

}
