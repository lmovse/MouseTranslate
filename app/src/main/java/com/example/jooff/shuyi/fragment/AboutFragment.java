package com.example.jooff.shuyi.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.jooff.shuyi.R;

/**
 * Created by Jooff on 2017/1/30.
 * Tomorrow is a nice day
 */

public class AboutFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle(R.string.give_me_five)
                .setMessage(R.string.five)
                .setPositiveButton(R.string.positive, (dialogInterface, i) -> {
                    Toast.makeText(getContext(), R.string.positive_string, Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).setNegativeButton(R.string.negative, (dialog, which) -> Toast.makeText(getContext(), R.string.negative_string, Toast.LENGTH_SHORT).show()).create();
    }
}
