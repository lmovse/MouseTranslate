package com.example.jooff.shuyi.translate.quick;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.constant.AppPref;
import com.example.jooff.shuyi.data.AppDbRepository;
import com.example.jooff.shuyi.main.MainActivity;
import com.example.jooff.shuyi.util.AnimationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class QuickTransView extends AppCompatActivity implements QuickTransContract.View {
    private QuickTransContract.Presenter mPresenter;

    @BindView(R.id.quick_trans_result)
    TextView mResult;

    @BindView(R.id.quick_trans_phonetic)
    TextView mPhonetic;

    @BindView(R.id.quick_trans_speech)
    ImageView mShareSpeech;

    @BindView(R.id.quick_trans_et)
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick);
        ButterKnife.bind(this);
        initView();
        mPresenter = new QuickTransPresenter(getSharedPreferences(AppPref.ARG_NAME, MODE_PRIVATE),
                AppDbRepository.getInstance(this.getApplicationContext()), this);
        mPresenter.loadData();
    }

    @Override
    public void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.y = -dm.heightPixels;
        p.alpha = 0.9f;
        getWindow().setAttributes(p);

        InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        methodManager.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);

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

    @OnClick(R.id.quick_trans_speech)
    public void playSpeech(View v) {
        v.setBackgroundResource(R.drawable.m_speech_black);
        AnimationDrawable drawable = (AnimationDrawable) v.getBackground();
        drawable.stop();
        drawable.start();
        mPresenter.playSpeech();
    }

    @OnClick(R.id.quick_trans_result)
    public void toApp() {
        Intent intent = new Intent(QuickTransView.this, MainActivity.class);
        intent.putExtra("original", mEditText.getText().toString());
        startActivity(intent);
    }

    @OnClick(R.id.quick_trans_reset)
    public void showQuickTrans(ImageView delete) {
        mEditText.setText("");
        mEditText.setHint(R.string.et_hint);
        mShareSpeech.setVisibility(View.GONE);
        mPhonetic.setVisibility(View.GONE);
        mEditText.startAnimation(AnimationUtil.getAlpha(this));
        delete.startAnimation(AnimationUtil.getScale(this));
    }

    @Override
    public void showTrans(String original, String result) {
        mEditText.setText(original);
        mResult.setText(result);
        mResult.startAnimation(AnimationUtil.getAlpha(QuickTransView.this));
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.invalid_translate, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSpeechAndPhonetic(String phonetic) {
        mEditText.setHint("");
        mPhonetic.setVisibility(View.VISIBLE);
        mPhonetic.setText("[" + phonetic + "]");
        mShareSpeech.startAnimation(AnimationUtil.getAlpha(this));
        mShareSpeech.setVisibility(View.VISIBLE);
    }

}


