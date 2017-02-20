package com.bupa.txtest.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bupa.txtest.R;
import com.bupa.txtest.adapter.EmoViewPagerAdapter;
import com.bupa.txtest.adapter.EmoteAdapter;
import com.bupa.txtest.adapter.MessageListAdapter;
import com.bupa.txtest.app.Constant;
import com.bupa.txtest.event.FaceText;
import com.bupa.txtest.presenter.ChatPresenter;
import com.bupa.txtest.presenter.impl.ChatPresenterImpl;
import com.bupa.txtest.utils.FaceTextUtils;
import com.bupa.txtest.utils.ThreadUtils;
import com.bupa.txtest.view.ChatView;
import com.bupa.txtest.widget.EmoticonsEditText;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bupa.txtest.R.id.btn_chat_keyboard;
import static com.bupa.txtest.R.id.btn_chat_voice;
import static com.bupa.txtest.R.id.btn_speak;
import static com.bupa.txtest.R.id.edit_user_comment;
import static com.bupa.txtest.R.id.layout_add;
import static com.bupa.txtest.R.id.layout_emo;
import static com.bupa.txtest.R.id.layout_more;
import static com.bupa.txtest.R.id.pager_emo;
import static com.bupa.txtest.R.id.tv_picture;

/**
 * 作者: l on 2017/2/6 21:16
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ChatActivity extends BaseActivity implements ChatView {
    public static final String TAG = "ChatActivity";
    private static boolean SHOW = false;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.btn_chat_add)
    Button mBtnChatAdd;
    @BindView(R.id.btn_chat_emo)
    Button mBtnChatEmo;
    @BindView(edit_user_comment)
    EmoticonsEditText mEditUserComment;
    @BindView(btn_speak)
    Button mBtnSpeak;
    @BindView(btn_chat_voice)
    Button mBtnChatVoice;
    @BindView(btn_chat_keyboard)
    Button mBtnChatKeyboard;
    @BindView(R.id.btn_chat_send)
    Button mBtnChatSend;
    @BindView(pager_emo)
    ViewPager mPagerEmo;
    @BindView(layout_emo)
    LinearLayout mLayoutEmo;
    @BindView(tv_picture)
    TextView mTvPicture;
    @BindView(R.id.tv_camera)
    TextView mTvCamera;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(layout_more)
    LinearLayout mLayoutMore;


    private ChatPresenter mChatPresenter;
    private String mUserName;

    private MessageListAdapter mMessageListAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private LinearLayout mLayout;
    private String mLocalCameraPath;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void init() {
        super.init();

        mLayout = (LinearLayout) findViewById(layout_add);
        mChatPresenter = new ChatPresenterImpl(this);
        mUserName = getIntent().getStringExtra(Constant.Extra.USER_NAME);

        String title = String.format(getString(R.string.chat_title), mUserName);
        if (title.contains("bu")||title.contains("zha")) {

            title = "与 请叫我不怕丶 聊天中";
        }
        if (title.contains("beautif")) {

            title = "与 漂亮的女孩 聊天中";
        }
        if (title.contains("zl")) {

            title = "与 漂亮的女孩 聊天中";
        }

        mTitle.setText(title);

        mBack.setVisibility(View.VISIBLE);

        initRecyclerView();

        mEditUserComment.addTextChangedListener(mTextWatcher);
        mEditUserComment.setOnEditorActionListener(mOnEditorActionListener);

        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);

        mChatPresenter.loadMessages(mUserName);
    }


    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageListAdapter = new MessageListAdapter(this, mChatPresenter.getMessages());
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mRecyclerView.setAdapter(mMessageListAdapter);
    }

    // TODO: 2017/2/18 点击事件
    @OnClick({R.id.back, R.id.btn_chat_send, R.id.btn_chat_add, R.id.tv_camera, R.id.tv_picture})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_chat_send:
                sendMessage();
                mEditUserComment.getText().clear();
                break;
            case R.id.btn_chat_add:
                if (mLayoutMore.getVisibility() == View.GONE) {
                    mLayoutMore.setVisibility(View.VISIBLE);
                    mLayout.setVisibility(View.VISIBLE);
                    mLayoutEmo.setVisibility(View.GONE);
                    hideSoftInputView();
                } else {
                    if (mLayoutEmo.getVisibility() == View.VISIBLE) {
                        mLayoutEmo.setVisibility(View.GONE);
                        mLayout.setVisibility(View.VISIBLE);
                    } else {
                        mLayoutMore.setVisibility(View.GONE);
                    }
                }

                break;
            case R.id.tv_camera:
                selectImageFromCamera();
                break;
            case tv_picture:
                selectImageFromLocal();
                break;
            case R.id.tv_location:
                selectLocationFromMap();
                break;


        }
    }



    private void selectLocationFromMap() {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    private void selectImageFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, 2);
    }

    private void selectImageFromCamera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File("/storage/emulated/0/DCIM/Screenshots");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, String.valueOf(System.currentTimeMillis())
                + ".jpg");
        mLocalCameraPath = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, 1);
    }

    private void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void sendMessage() {
        hideKeyboard();
        String message = mEditUserComment.getText().toString().trim();
        mChatPresenter.sendMessage(mUserName, message);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                mBtnChatSend.setVisibility(View.VISIBLE);
                mBtnChatKeyboard.setVisibility(View.GONE);
                mBtnChatVoice.setVisibility(View.GONE);
            } else {
                if (mBtnChatVoice.getVisibility() != View.VISIBLE) {
                    mBtnChatVoice.setVisibility(View.VISIBLE);
                    mBtnChatSend.setVisibility(View.GONE);
                    mBtnChatKeyboard.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_FLAG_NO_FULLSCREEN) {
                sendMessage();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onSendMessageSuccess() {
        //清空编辑框
        mEditUserComment.getText().clear();
        toast(getString(R.string.send_success));
        //刷新列表
        mMessageListAdapter.notifyDataSetChanged();
        smoothScrollToBottom();


    }

    private void smoothScrollToBottom() {
        //滚动RecylcerView到最底部
        mRecyclerView.smoothScrollToPosition(mChatPresenter.getMessages().size() - 1);
    }

    @Override
    public void onSendMessageFailed() {
        toast(getString(R.string.send_failed));
        //刷新列表
        mMessageListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStartSendMessage() {
        mMessageListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessagesLoaded() {
        mMessageListAdapter.notifyDataSetChanged();
//        smoothScrollToBottom();
        scrollToBottom();
    }

    @Override
    public void onMoreMessagesLoaded(int size) {

        toast(getString(R.string.more_message_loaded));
        mMessageListAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(size);//加载完更多数据，还要滚动recyclerview到指定位置，让用户自己滑动去看上面的更多数据
    }

    @Override
    public void onNoMoreData() {
        toast(getString(R.string.no_more_data));
    }

    private void scrollToBottom() {
        //滚动RecylcerView到最底部
        mRecyclerView.scrollToPosition(mChatPresenter.getMessages().size() - 1);
    }

    private EMMessageListener mEMMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            Log.d(TAG, "onMessageReceived: ");
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast(getString(R.string.receive_message));
                    mMessageListAdapter.addNewMessage(list.get(0));
                    //标记该消息已读
                    mChatPresenter.markMessageRead(mUserName);
                    smoothScrollToBottom();
                }
            });
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

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //当滚动结束在空闲状态时判断RecyclerView是否滑动到顶部
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (mLinearLayoutManager.findFirstVisibleItemPosition() == 0) {
                    mChatPresenter.loadMoreMessages(mUserName);
                }
            }
        }
    };

    /**
     * 移除监听
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:

                    sendImageMessage(mLocalCameraPath);
                    break;
                case 2:
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(
                                    selectedImage, null, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex("_data");
                            String localSelectPath = cursor.getString(columnIndex);
                            cursor.close();
                            if (localSelectPath == null
                                    || localSelectPath.equals("null")) {
                                return;
                            }
                            sendImageMessage(localSelectPath);
                        }
                    }
                    break;

            }
        }
    }

    private void sendImageMessage(String localCameraPath) {
        if (mLayoutMore.getVisibility() == View.VISIBLE) {
            mLayoutMore.setVisibility(View.GONE);
            mLayout.setVisibility(View.GONE);
            mLayoutEmo.setVisibility(View.GONE);
        }

    }
}
