package com.bupa.txtest.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bupa.txtest.R;
import com.bupa.txtest.adapter.ConversationsAdapter;
import com.bupa.txtest.presenter.ConversationsPresenter;
import com.bupa.txtest.presenter.impl.ConversationsPresenterImpl;
import com.bupa.txtest.view.ConversationsView;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import butterknife.BindView;

/**
 * 作者: l on 2017/2/6 14:39
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ConversationFragment extends BaseFragment implements ConversationsView {


    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ConversationsPresenter mConversationsPresenter;

    private ConversationsAdapter mConversationsAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_conversation;
    }

    @Override
    protected void init() {
        super.init();
        mConversationsPresenter = new ConversationsPresenterImpl(this);
        mTitle.setText(getString(R.string.conversations));

        initRecyclerView();

        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);

    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mConversationsAdapter = new ConversationsAdapter(getContext(), mConversationsPresenter.getConversations());
        mRecyclerView.setAdapter(mConversationsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mConversationsPresenter.loadConversations();
    }

    @Override
    public void onConversationsLoaded() {
       // toast(getString(R.string.load_conversation_success));
        //通知recyclerview刷新列表
        mConversationsAdapter.notifyDataSetChanged();
    }

    private EMMessageListener mEMMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //重新加载会话数据
            mConversationsPresenter.loadConversations();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
    }
}
