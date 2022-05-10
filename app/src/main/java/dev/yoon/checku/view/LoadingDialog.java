package com.example.nodeproject2.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import androidx.annotation.NonNull;
import com.example.nodeproject2.R;

public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog);
    }
}
