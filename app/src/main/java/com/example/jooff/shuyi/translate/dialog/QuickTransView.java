package com.example.jooff.shuyi.translate.dialog;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.data.AppDbRepository;
import com.example.jooff.shuyi.main.MainActivity;
import com.example.jooff.shuyi.util.AnimationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class QuickTransView extends Activity implements QuickTransContract.View {
    private QuickTransContract.Presenter mPresenter;

    @BindView(R.id.quick_trans_original)
    TextView mOriginal;

    @BindView(R.id.quick_trans_result)
    TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_dialog_copy_trans);
        ButterKnife.bind(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.y = -dm.heightPixels;
        p.alpha = 0.9f;      // 设置本身透明度
        getWindow().setAttributes(p);
        mPresenter = new QuickTransPresenter(AppDbRepository.getInstance(this.getApplicationContext())
                , getIntent()
                , this);
        initView();
        mPresenter.loadData();
    }

    @OnClick(R.id.quick_trans_original)
    public void toApp() {
        Intent intent = new Intent(QuickTransView.this, MainActivity.class);
        intent.putExtra("original", mOriginal.getText().toString());
        startActivity(intent);
    }

    @OnClick(R.id.show_quick)
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
        mOriginal.setTextSize(16);
        mResult.setTextSize(16);
        mOriginal.setText(original);
        mResult.setText(result);
        mResult.startAnimation(AnimationUtil.getAlpha(QuickTransView.this));
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.invalid_translate, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initView() {}
}


