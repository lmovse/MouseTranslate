package com.example.jooff.shuyi.service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

public class CopyTranslateService extends Service {

    /**
     * 获取粘贴板资源，并添加自定义监听器
     */
    @Override
    public void onCreate() {
        super.onCreate();
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cm.addPrimaryClipChangedListener(mListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    ClipboardManager.OnPrimaryClipChangedListener mListener = () -> {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = cm.getPrimaryClip();
        String mOriginal = "";
        try {
            mOriginal = clipData.getItemAt(0).getText().toString();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent("com.example.jooff.quick");
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("text/*");
        if (!TextUtils.isEmpty(mOriginal.trim())) {
            intent.putExtra("original", mOriginal);
            startActivity(intent);
        }
    };

    /**
     * 释放粘贴板资源监听，防止内存泄漏
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.removePrimaryClipChangedListener(mListener);
    }

}
