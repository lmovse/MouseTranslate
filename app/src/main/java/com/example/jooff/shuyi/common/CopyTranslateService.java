package com.example.jooff.shuyi.common;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class CopyTranslateService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate", "onCreate: 服务已启动");
        final ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.addPrimaryClipChangedListener(mListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    ClipboardManager.OnPrimaryClipChangedListener mListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            final ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = cm.getPrimaryClip();
            String mOriginal = clipData.getItemAt(0).getText().toString();
            Log.d("copy", "onPrimaryClipChanged:  ok");
            Intent intent = new Intent("com.example.jooff.share");
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
        Log.d("服务已关闭", "onDestroy: 服务已停止");
        ClipboardManager clipboardManager =
                (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.removePrimaryClipChangedListener(mListener);
    }
}
