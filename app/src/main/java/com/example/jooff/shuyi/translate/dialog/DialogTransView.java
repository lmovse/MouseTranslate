package com.example.jooff.shuyi.translate.dialog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.common.AboutFragment;
import com.example.jooff.shuyi.common.Constant;
import com.example.jooff.shuyi.data.AppDbRepository;
import com.example.jooff.shuyi.main.MainActivity;
import com.example.jooff.shuyi.util.AnimationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DialogTransView extends AppCompatActivity implements DialogTransContract.View, Toolbar.OnMenuItemClickListener {
    private DialogTransContract.Presenter mPresenter;

    @BindView(R.id.share_result)
    TextView mResult;

    @BindView(R.id.share_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.share_appbar)
    AppBarLayout mAppBar;

    @BindView(R.id.share_speech)
    ImageView mShareSpeech;

    @BindView(R.id.share_et)
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        mPresenter = new DialogTransPresenter(getSharedPreferences(Constant.ARG_NAME, MODE_PRIVATE)
                , AppDbRepository.getInstance(this.getApplicationContext())
                , this);
        initView();
        mPresenter.loadData();
    }

    @Override
    public void initView() {
        mPresenter.initTheme();

        // 改变 dialog 的显示位置与大小
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.y = -dm.heightPixels;
        p.width = (int) (dm.widthPixels * 0.8);
        getWindow().setAttributes(p);

        mToolbar.setTitle(R.string.app_name);
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(this);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && s.charAt(start) == '\n') {
                    mEditText.getText().replace(start, start + 1, "");
                    mPresenter.beginTrans(mEditText.getEditableText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        AboutFragment fragment = new AboutFragment();
        fragment.show(getSupportFragmentManager(), "dialogAbout");
        return false;
    }

    @OnClick(R.id.share_speech)
    public void playSpeech(View v) {
        v.setBackgroundResource(R.drawable.m_speech1);
        AnimationDrawable drawable = (AnimationDrawable) v.getBackground();
        drawable.stop();
        drawable.start();
        mPresenter.playSpeech();
    }

    @OnClick(R.id.share_result)
    public void toApp() {
        Intent intent = new Intent(DialogTransView.this, MainActivity.class);
        intent.putExtra("original", mEditText.getText().toString());
        startActivity(intent);
    }

    @OnClick(R.id.share_original_delete)
    public void onDelete(ImageView delete) {
        mEditText.setText("");
        mShareSpeech.setVisibility(View.GONE);
        mEditText.startAnimation(AnimationUtil.getAlpha(this));
        delete.startAnimation(AnimationUtil.getScale(this));
    }

    @OnClick(R.id.share_original_send)
    public void onSend(ImageView send) {
        send.startAnimation(AnimationUtil.getScale(this));
        mPresenter.beginTrans(mEditText.getText().toString());
    }

    @OnClick(R.id.show_quick_trans)
    public void showQuikTrans(ImageView quickTrans) {
        quickTrans.startAnimation(AnimationUtil.getScale(this));
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this.getString(R.string.share_intent));
        intent.setType("text/*");
        PendingIntent notifyIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_notifi)
                .setOngoing(true)
                .setContentIntent(notifyIntent)
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(this.getString(R.string.notifi_name)).build();
        manager.notify(1, notification);
        Toast.makeText(this, "已开启快速翻译", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTrans(String original, String result) {
        mEditText.setText(original);
        mResult.setText(result);
        mResult.startAnimation(AnimationUtil.getAlpha(DialogTransView.this));
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.invalid_translate, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSpeech() {
        mShareSpeech.startAnimation(AnimationUtil.getAlpha(this));
        mShareSpeech.setVisibility(View.VISIBLE);
    }


    @Override
    public void setAppTheme(int colorPrimary) {
        mAppBar.setBackgroundColor(colorPrimary);
    }

}


