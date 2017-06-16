package com.example.jooff.shuyi.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.common.AboutFragment;
import com.example.jooff.shuyi.common.Constant;
import com.example.jooff.shuyi.common.CopyTranslateService;
import com.example.jooff.shuyi.common.MySnackBar;
import com.example.jooff.shuyi.common.OnAppStatusListener;
import com.example.jooff.shuyi.common.SourceFragment;
import com.example.jooff.shuyi.common.ThemeFragment;
import com.example.jooff.shuyi.history.HistoryFragment;
import com.example.jooff.shuyi.settings.SettingsFragment;
import com.example.jooff.shuyi.translate.main.MainTranslateView;
import com.example.jooff.shuyi.util.ActivityCollector;
import com.example.jooff.shuyi.util.AnimationUtil;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.example.jooff.shuyi.common.MySnackBar.getSnack;


public class MainActivity extends AppCompatActivity implements MainContract.View, OnAppStatusListener {
    public MainContract.Presenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.result_spinner)
    Spinner mSpinner;

    @BindView(R.id.et)
    EditText mEditText;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.contentFrag)
    FrameLayout mContentFrag;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.fab_menu)
    FloatingActionsMenu mFabMenu;

    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.status_bar)
    Button mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter(getSharedPreferences(Constant.ARG_NAME, MODE_PRIVATE), this);
        mPresenter.initTheme();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        initView();
        Log.d("ok", "onCreate: ok");
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AboutFragment af = new AboutFragment();
        af.show(getSupportFragmentManager(), "af");
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @OnClick(R.id.fab_setting)
    public void openSetting() {
        SettingsFragment sf = new SettingsFragment();
        sf.show(getSupportFragmentManager(), "sf");
        mFabMenu.collapse();
    }

    @OnClick(R.id.fab_theme)
    public void setTheme() {
        ThemeFragment tf = new ThemeFragment();
        tf.show(getSupportFragmentManager(), "tf");
        mFabMenu.collapse();
    }

    @OnClick(R.id.fab_source)
    public void setSource() {
        SourceFragment sft = new SourceFragment();
        sft.show(getSupportFragmentManager(), "sft");
        mFabMenu.collapse();
    }

    @OnClick(R.id.original_send)
    public void onSend(ImageView send) {
        send.startAnimation(AnimationUtil.getScale(this));
        mPresenter.beginTrans(mEditText.getText().toString());
    }

    @OnClick(R.id.original_delete)
    public void onDelete(ImageView delete) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(mEditText.getWindowToken(), 0, 0);
        mProgressBar.setVisibility(View.GONE);
        mFabMenu.collapse();
        mEditText.setText("");
        mEditText.startAnimation(AnimationUtil.getAlpha(this));
        delete.startAnimation(AnimationUtil.getScale(this));
        mPresenter.loadData();
    }

    @OnItemSelected(R.id.result_spinner)
    public void onSelected(int position) {
        String resultLan = getResources().getStringArray(R.array.BDLanguageEN)[position];
        mPresenter.refreshResultLan(resultLan);
    }

    @Override
    public void showHistory() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.contentFrag, new HistoryFragment()).commit();
    }

    @Override
    public void showTrans(int transFrom, String original) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        mProgressBar.setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.contentFrag, MainTranslateView.newInstance(transFrom, original)).commit();
    }

    @Override
    public void showEmptyInput() {
        getSnack(mCoordinatorLayout, R.string.no_text).show();
        onSnackBarShow();
    }

    @Override
    public void showSpinner(String lan) {
        int i, position = 1;
        String[] resultLanguages = getResources().getStringArray(R.array.BDLanguageEN);
        for (i = 0; i < resultLanguages.length; i++) {
            if (getResources().getStringArray(R.array.BDLanguageEN)[i].equals(lan)) {
                position = i;
                break;
            }
        }
        mSpinner.startAnimation(AnimationUtil.getAlpha(this));
        mSpinner.setVisibility(View.VISIBLE);
        mSpinner.setSelection(position);
    }

    @Override
    public void hideSpinner() {
        mSpinner.setVisibility(View.GONE);
    }

    @Override
    public void startService() {
        startService(new Intent(this, CopyTranslateService.class));
    }

    @Override
    public void stopService() {
        stopService(new Intent(this, CopyTranslateService.class));
    }

    @Override
    public void showNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this.getString(R.string.share_intent));
        intent.setType("text/*");
        PendingIntent notifyIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher1)
                .setOngoing(true)
                .setContentIntent(notifyIntent)
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(this.getString(R.string.notifi_name)).build();
        manager.notify(1, notification);
    }

    @Override
    public void cancelNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setTransparent(int colorPrimary) {
        getWindow().setStatusBarColor(colorPrimary);
        getWindow().setNavigationBarColor(colorPrimary);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setMaterial(int colorPrimaryDark) {
        getWindow().setStatusBarColor(colorPrimaryDark);
        getWindow().setNavigationBarColor(colorPrimaryDark);
    }

    @Override
    public void initLayout() {
        mStatus.setVisibility(View.VISIBLE);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 281, this.getResources().getDisplayMetrics());
        params.width = CoordinatorLayout.LayoutParams.MATCH_PARENT;
        mAppBarLayout.setLayoutParams(params);
        params = (CoordinatorLayout.LayoutParams) mContentFrag.getLayoutParams();
        params.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 281, this.getResources().getDisplayMetrics()), 0, 0);
        mContentFrag.setLayoutParams(params);
    }

    @Override
    public void initTheme(int themeId, int colorPrimary) {
        Constant.sColorPrimary = colorPrimary;
        MySnackBar.color = colorPrimary;
        switch (themeId) {
            case 1024:
                setTheme(R.style.AppTheme_Dark);
                break;
            case 0:
                setTheme(R.style.AppTheme_Red);
                break;
            case 1:
                setTheme(R.style.AppTheme_Pink);
                break;
            case 2:
                setTheme(R.style.AppTheme_BlueGrey);
                break;
            case 3:
                setTheme(R.style.AppTheme_Blue);
                break;
            case 4:
                setTheme(R.style.AppTheme_Green);
                break;
            case 5:
                setTheme(R.style.AppTheme_Brown);
                break;
            case 6:
                setTheme(R.style.AppTheme_Teal);
                break;
            case 7:
                setTheme(R.style.AppTheme_Girl);
                break;
            case 8:
                setTheme(R.style.AppTheme_Purple);
                break;
            default:
                break;
        }
    }

    @Override
    public void setAppTheme(int colorPrimary, int colorPrimaryDark) {
        mAppBarLayout.setBackgroundColor(colorPrimary);
        mStatus.setBackgroundColor(colorPrimaryDark);
    }

    @Override
    public void openNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    public void closeNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void startIntent() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mPresenter.handleClick();
            return true;
        }
        return false;
    }

    @Override
    public void doFinish() {
        ActivityCollector.finishAll();
    }

    @Override
    public void showConfirmFinish() {
        MySnackBar.getSnack(mCoordinatorLayout, R.string.confirm_exit).show();
        onSnackBarShow();
    }

    @Override
    public void onSuccess(String original) {
        mProgressBar.setVisibility(View.GONE);
        mEditText.setText(original);
    }

    @Override
    public void onSnackBarShow() {
        mFabMenu.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_up));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1785);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFabMenu.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_down));
                    }
                });
            }
        }).start();
    }

    @Override
    public void onSettingChanged(int position, boolean isChecked) {
        mPresenter.updateSetting(position, isChecked);
    }

    @Override
    public void onSourceChanged(int source) {
        mPresenter.refreshSource(source);
    }

    @Override
    public void initView() {
        mPresenter.initSettings();
        mPresenter.loadData();
        String original = getIntent().getStringExtra("original");
        if (original != null) {
            mEditText.setText(original);
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && s.charAt(start) == '\n') {
                    mEditText.getText().replace(start, start + 1, "");
                    mPresenter.beginTrans(mEditText.getEditableText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
