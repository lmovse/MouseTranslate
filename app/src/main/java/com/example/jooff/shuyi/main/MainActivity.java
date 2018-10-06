package com.example.jooff.shuyi.main;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.collect.CollectFragment;
import com.example.jooff.shuyi.common.MyApp;
import com.example.jooff.shuyi.constant.AppPref;
import com.example.jooff.shuyi.constant.ThemeColor;
import com.example.jooff.shuyi.constant.TransSource;
import com.example.jooff.shuyi.data.AppDbRepository;
import com.example.jooff.shuyi.fragment.AboutFragment;
import com.example.jooff.shuyi.fragment.SourceFragment;
import com.example.jooff.shuyi.history.HistoryFragment;
import com.example.jooff.shuyi.listener.OnAppStatusListener;
import com.example.jooff.shuyi.service.CopyTranslateService;
import com.example.jooff.shuyi.setting.SettingsFragment;
import com.example.jooff.shuyi.translate.main.MainTransView;
import com.example.jooff.shuyi.util.ActivityCollector;
import com.example.jooff.shuyi.util.AnimationUtil;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class MainActivity extends AppCompatActivity implements MainContract.View, OnAppStatusListener {

    private static final String TAG = "MainActivity";

    private MainContract.Presenter mPresenter;

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
        mPresenter = new MainPresenter(getSharedPreferences(AppPref.ARG_NAME, MODE_PRIVATE),
                this, AppDbRepository.getInstance(this));
        mPresenter.initTheme();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        initView();
        ActivityCollector.addActivity(this);
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

    // 解决旋转屏幕时报空指针
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @OnClick(R.id.fab_setting)
    public void openSetting() {
        showFrag(new SettingsFragment(), "sf");
    }

    @OnClick(R.id.fab_book)
    public void showBook() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.contentFrag, new CollectFragment()).commit();
        mFabMenu.collapse();
    }

    @OnClick(R.id.fab_source)
    public void setSource() {
        showFrag(new SourceFragment(), "sft");
    }

    private void showFrag(DialogFragment fragment, String name) {
        fragment.show(getSupportFragmentManager(), name);
        mFabMenu.collapse();
    }

    @OnClick(R.id.original_send)
    public void onSend(ImageView send) {
        send.startAnimation(AnimationUtil.getScale(this));
        mPresenter.beginTrans(mEditText.getText().toString());
    }

    @OnClick(R.id.delete_all_history)
    public void onDeleteAllHistory(ImageView deleteAll) {
        deleteAll.startAnimation(AnimationUtil.getScale(this));
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("是否确定删除所有的历史记录？")
                .setPositiveButton("确定", (dialog, which) -> {
                    mPresenter.removeAllHistory();
                    showHistory();
                    dialog.dismiss();
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .create();
        alertDialog.show();
    }

    @OnClick(R.id.original_delete)
    public void onDelete(ImageView delete) {
        mProgressBar.setVisibility(View.GONE);
        mFabMenu.collapse();
        mEditText.setText("");
        mEditText.startAnimation(AnimationUtil.getAlpha(this));
        InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(mEditText.getWindowToken(), 0, 0);
        delete.startAnimation(AnimationUtil.getScale(this));
        mPresenter.loadData();
    }

    @OnItemSelected(R.id.result_spinner)
    public void onSelected(AdapterView<?> parent, View p1, int position, long p3) {
        ((TextView) parent.getChildAt(0)).setTextColor(0xffffffff);
        String resultLan;
        int source = mPresenter.getSource();
        if (source == R.id.source_google) {
            resultLan = getResources().getStringArray(R.array.GoogleLanguageEN)[position];
        } else {
            resultLan = getResources().getStringArray(R.array.BDLanguageEN)[position];
        }
        mPresenter.refreshResultLan(resultLan);
    }

    @Override
    public void showHistory() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.contentFrag, new HistoryFragment()).commit();
    }

    @Override
    public void showTrans(int transFrom, String original, String transUrl) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        mProgressBar.setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.contentFrag, MainTransView.newInstance(transFrom, original, transUrl)).commit();
    }

    @Override
    public void showEmptyInput() {
        Toast.makeText(this, R.string.no_text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSpinner(String lan, int transFrom) {
        int i, position = 1;
        String[] resultLanguages = {};
        if (transFrom == R.id.source_google) {
            mSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.GoogleLanguageCN)));
            resultLanguages = getResources().getStringArray(R.array.GoogleLanguageEN);
        } else if (transFrom == R.id.source_baidu) {
            mSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.BDLanguageCN)));
            resultLanguages = getResources().getStringArray(R.array.BDLanguageEN);
        }
        for (i = 0; i < resultLanguages.length; i++) {
            if (resultLanguages[i].equals(lan)) {
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
                .setSmallIcon(R.mipmap.ic_notifi)
                .setOngoing(true)
                .setContentIntent(notifyIntent)
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(this.getString(R.string.notify_name)).build();
        manager.notify(1, notification);
    }

    @Override
    public void cancelNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    @Override
    public void setTransparent(int colorPrimary) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(colorPrimary);
            getWindow().setNavigationBarColor(colorPrimary);
        } else {
            Toast.makeText(this, "当前系统版本不支持此操作", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setMaterial(int colorPrimaryDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(colorPrimaryDark);
            getWindow().setNavigationBarColor(colorPrimaryDark);
        } else {
            Toast.makeText(this, "当前系统版本不支持此操作", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initTheme(int themeId, int colorPrimary) {
        MyApp.sColorPrimary = colorPrimary;
        switch (themeId) {
            case ThemeColor.DARK:
                setTheme(R.style.AppTheme_Dark);
                break;
            case ThemeColor.RED:
                setTheme(R.style.AppTheme_Red);
                break;
            case ThemeColor.PINK:
                setTheme(R.style.AppTheme_Pink);
                break;
            case ThemeColor.BLUE_GREY:
                setTheme(R.style.AppTheme_BlueGrey);
                break;
            case ThemeColor.BLUE:
                setTheme(R.style.AppTheme_Blue);
                break;
            case ThemeColor.GREEN:
                setTheme(R.style.AppTheme_Green);
                break;
            case ThemeColor.BROWN:
                setTheme(R.style.AppTheme_Brown);
                break;
            case ThemeColor.TEAL:
                setTheme(R.style.AppTheme_Teal);
                break;
            case ThemeColor.GIRL:
                setTheme(R.style.AppTheme_Girl);
                break;
            case ThemeColor.PURPLE:
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
        Toast.makeText(this, R.string.confirm_exit, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(int transFrom, String original) {
        if (transFrom == TransSource.FROM_HISTORY) {
            mEditText.setText(original);
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSettingChanged(int position, boolean isChecked) {
        mPresenter.updateSetting(position, isChecked);
    }

    @Override
    public void onSourceChanged(int source) {
        mPresenter.refreshSource(source);
        mPresenter.beginTrans(mEditText.getText().toString());
    }

    @Override
    public void initView() {
        mPresenter.initSettings();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            initKitKatLayout();
        }
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

    @Override
    public void initKitKatLayout() {
        mStatus.setVisibility(View.VISIBLE);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();

        // 设置高度 将输入的整形数值转换为相对应的 dp 值
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 281, this.getResources().getDisplayMetrics());
        params.width = CoordinatorLayout.LayoutParams.MATCH_PARENT;
        mAppBarLayout.setLayoutParams(params);

        //　设置边距
        params = (CoordinatorLayout.LayoutParams) mContentFrag.getLayoutParams();
        params.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 281, this.getResources().getDisplayMetrics()), 0, 0);
        mContentFrag.setLayoutParams(params);
    }

}
