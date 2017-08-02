package com.example.jooff.shuyi.common;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

public class CopyTranslateService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        //获取粘贴板管理对象，并设置监听器进行监听
        final ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.addPrimaryClipChangedListener(mListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    ClipboardManager.OnPrimaryClipChangedListener mListener =
            new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            final ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = cm.getPrimaryClip();
            String mOriginal = "";
            try {
                mOriginal = clipData.getItemAt(0).getText().toString();
            } catch (Exception e) {
//                Handler handler = new Handler(Looper.getMainLooper());
//                handler.post(new Runnable() {
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "获取复制结果失败，请重试",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//                return;
            }
            Intent intent = new Intent("com.example.jooff.quick");
            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("text/*");
            if (!TextUtils.isEmpty(mOriginal.trim())) {
                intent.putExtra("original", mOriginal);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放粘贴板监听器资源，防止内存溢出
        ClipboardManager clipboardManager =
                (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.removePrimaryClipChangedListener(mListener);
    }

}
