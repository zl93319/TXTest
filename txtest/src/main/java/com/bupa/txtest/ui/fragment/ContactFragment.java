package com.bupa.txtest.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bupa.txtest.R;
import com.bupa.txtest.adapter.ContactsAdapter;
import com.bupa.txtest.app.Constant;
import com.bupa.txtest.model.ContactListItem;
import com.bupa.txtest.presenter.impl.ContactPresenterImpl;
import com.bupa.txtest.ui.activity.AddFriendActivity;
import com.bupa.txtest.ui.activity.ChatActivity;
import com.bupa.txtest.view.ContactView;
import com.bupa.txtest.widget.SlideBar;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者: l on 2017/2/6 14:39
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ContactFragment extends BaseFragment implements ContactView {
    private static final String TAG = "ContactFragment";
    private static final int POSITION_NOT_FOUND = -1;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.add)
    ImageView mAdd;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.slide_bar)
    SlideBar mSlideBar;

    @BindView(R.id.first_letter)
    TextView mFirstLetter;

    private ContactsAdapter mContactsAdapter;
    private ContactPresenterImpl mContactPresenter;

    // 获取布局
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void init() {
        super.init();
        mContactPresenter = new ContactPresenterImpl(this);
        mTitle.setText(getString(R.string.contacts));// 显示title 联系人
        mAdd.setVisibility(View.VISIBLE);  // 设置+号可见
        // 设置刷新的 颜色总数
        mSwipeRefreshLayout.setColorSchemeResources(R.color.qq_blue, R.color.colorAccent, R.color.colorPrimary);
        // 下拉刷新的 监听事件
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        mSlideBar.setOnSectionChangeListener(mOnSectionChangeListener);
        initRecyclerView();
        mContactPresenter.loadContacts();
        //监听好友状态
        EMClient.getInstance().contactManager().setContactListener(mEMContactListener);
    }

    /**
     * 滑动的位置
     */
    private SlideBar.OnSectionChangeListener mOnSectionChangeListener = new SlideBar.OnSectionChangeListener() {

        @Override
        public void onSectionChange(String section) {
//            toast(section);
            mFirstLetter.setVisibility(View.VISIBLE);
            mFirstLetter.setText(section);

            //滚动联系人列表都对应首字符
            //找出首字符为section的第一个联系人位置
            int position = findFirstLetterPosition(section);
            if (position != POSITION_NOT_FOUND) {
                mRecyclerView.smoothScrollToPosition(position);
            }
        }

        @Override
        public void onSlideFinish() {
            mFirstLetter.setVisibility(View.GONE);
        }
    };

    /**
     * 找出首字符为section的第一个联系人
     *
     * @param section
     * @return
     */
    private int findFirstLetterPosition(String section) {
        List<ContactListItem> contacts = mContactPresenter.getContacts();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getFirstLetter().equals(section)) {
                return i;
            }
        }
        return POSITION_NOT_FOUND;
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactsAdapter = new ContactsAdapter(getContext(), mContactPresenter.getContacts());
        mContactsAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mContactsAdapter);
    }

    /**
     * 点击item的监听事件
     */
    private ContactsAdapter.OnItemClickListener mOnItemClickListener = new ContactsAdapter.OnItemClickListener() {
        @Override
        public void onLongClick(String userName) {
            // 长按事件    弹出 删除对话框
            showDeleteDialog(userName);
        }

        @Override
        public void onItemClick(String userName) {
            //单击事件    跳转到聊天界面
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra(Constant.Extra.USER_NAME, userName);
            startActivity(intent);
        }
    };

    /**
     * 显示删除好友的对话框
     *
     * @param userName
     */
    private void showDeleteDialog(final String userName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String message = String.format(getString(R.string.delete_friend_message), userName);
        builder.setTitle(getString(R.string.delete_friend)).setMessage(message)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //删除好友
                        mContactPresenter.deleteFriend(userName);
                    }
                });
        builder.show();
    }

    /**
     * 刷新成功 隐藏SwipeRefreshLayout
     */
    @Override
    public void onLoadContactsSuccess() {
        //隐藏swiperefreshlayout

        mSwipeRefreshLayout.setRefreshing(false);


        mContactsAdapter.notifyDataSetChanged();//通知列表刷新
    }

    @Override
    public void onLoadContactsFailed() {
        mSwipeRefreshLayout.setRefreshing(false);
        //toast(getString(R.string.load_contacts_failed));
    }

    @Override
    public void onDeleteFriendSuccess() {
        toast(getString(R.string.delete_friend_success));
    }

    @Override
    public void onDeleteFriendFailed() {
        toast(getString(R.string.delete_friend_failed));
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // 刷新联系人列表
            mContactPresenter.refreshContacts();
        }
    };

    /**
     * 点击加号按钮
     */
    @OnClick(R.id.add)
    public void onClick() {
        startActivity(AddFriendActivity.class, false);
    }

    private EMContactListener mEMContactListener = new EMContactListener() {

        /**
         * 新增联系人的回调  当默认接受好友请求的时候
         * @param s
         */
        @Override
        public void onContactAdded(String s) {
            //刷新联系人
            mContactPresenter.refreshContacts();
        }

        /**
         * 删除或者被删除时回调
         * @param s
         */
        @Override
        public void onContactDeleted(String s) {
            mContactPresenter.refreshContacts();
        }

        /**
         * 收到好友请求的回调 options.setAcceptInvitationAlways(false);
         * @param s
         * @param s1
         */
        @Override
        public void onContactInvited(String s, String s1) {
//            EMClient.getInstance().contactManager().acceptInvitation(username);同意

//            EMClient.getInstance().contactManager().declineInvitation(username);
        }

        /**
         * 好友请求被同意 EMClient.getInstance().contactManager().acceptInvitation(username);
         * @param s
         */
        @Override
        public void onContactAgreed(String s) {

        }

        /**
         * 好友请求被拒绝 EMClient.getInstance().contactManager().declineInvitation(username);
         * @param s
         */
        @Override
        public void onContactRefused(String s) {

        }
    };
}
