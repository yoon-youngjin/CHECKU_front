package com.example.nodeproject2.config;

import android.content.Context;
import android.view.Gravity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import com.example.nodeproject2.R;
import com.skydoves.balloon.*;

public class BuildBallon {

    public static Balloon getBalloon(Context context, LifecycleOwner lifecycleOwner, String text) {
        Balloon balloon = new Balloon.Builder(context)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.5f)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(12f)
                .setCornerRadius(8f)
                .setPadding(10)
                .setAlpha(0.9f)
                .setTextGravity(Gravity.START)
                .setBackgroundColorResource(com.example.nodeproject2.R.color.kukie_gray)
                .setTextColor(ContextCompat.getColor(context, R.color.black))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .setText(text)
                .setLifecycleOwner(lifecycleOwner)
                .setAutoDismissDuration(3000L)
                .build();
        return balloon;
    }



}
