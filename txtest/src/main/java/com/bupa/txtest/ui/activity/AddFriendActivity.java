package com.bupa.txtest.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bupa.txtest.R;
import com.bupa.txtest.adapter.AddFriendAdapter;
import com.bupa.txtest.presenter.impl.AddFriendPresenterImpl;
import com.bupa.txtest.view.AddFriendView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者: l on 2017/2/6 21:34
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class AddFriendActivity extends BaseActivity implements AddFriendView {
    public static final String TAG = "AddFriendActivity";
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.keyword)
    EditText mKeyword;
    @BindView(R.id.search)
    Button mSearch;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.search_empty)
    TextView mSearchEmpty;
    private AddFriendAdapter mAddFriendAdapter;
    private AddFriendPresenterImpl mAddFriendPresenter;

    /**
     * 添加好友界面
     *
     * @return
     */
    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void init() {

        super.init();
        mAddFriendPresenter = new AddFriendPresenterImpl(this);

        mTitle.setText(getString(R.string.add_friend));

        mKeyword.setOnEditorActionListener(mOnEditorActionListener);

        initRecyclerView();
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            }
            return false;
        }
    };

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAddFriendAdapter = new AddFriendAdapter(this, mAddFriendPresenter.getFriends());
        mRecyclerView.setAdapter(mAddFriendAdapter);
    }

    private void search() {
        hideKeyboard();
        String keyword = mKeyword.getText().toString().trim();
        mAddFriendPresenter.search(keyword);
    }

    @OnClick(R.id.search)
    public void onClick() {
        search();
    }

    @Override
    public void onSearchSuccess() {
        hideProgressDialog();
        mSearchEmpty.setVisibility(View.GONE);
        toast(getString(R.string.search_success));

        mAddFriendAdapter.notifyDataSetChanged();//通知RecyclerView刷新列表
    }

    @Override
    public void onSearchFailed() {
        hideProgressDialog();
        mSearchEmpty.setVisibility(View.GONE);
        toast(getString(R.string.search_failed));
    }

    @Override
    public void onSearchEmpty() {
        hideProgressDialog();
        mSearchEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartSearch() {
        showProgressDialog(getString(R.string.searching));
    }

    @Override
    public void sendAddFriendRequestSuccess() {
        toast(getString(R.string.add_friend_request_success));
    }

    @Override
    public void sendAddFriendRequestFailed() {
        toast(getString(R.string.add_friend_request_failed));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddFriendPresenter.onDestroy();
    }
}
