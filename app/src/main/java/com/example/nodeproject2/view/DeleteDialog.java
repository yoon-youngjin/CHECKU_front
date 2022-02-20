package com.example.nodeproject2.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.TextView;
import com.example.nodeproject2.R;
import com.example.nodeproject2.datas.Lecture;

public class DeleteDialog {

    private Context context;
    final Dialog dlg;
    final Button okButton;
    final Button cancelButton;
    private TextView textView;
    private boolean result;

    public interface OnItemClickListener {
        void OnItemClick();
    }

    private DeleteDialog.OnItemClickListener itemClickListener = null;
    private DeleteDialog.OnItemClickListener item2ClickListener = null;

    // ok
    public void setOnItemClickListener(DeleteDialog.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    //no
    public void setOnItemClickListener2(DeleteDialog.OnItemClickListener listener) {
        this.item2ClickListener = listener;
    }

    public DeleteDialog(Context context) {
        this.context = context;
        this.dlg = new Dialog(context);
        dlg.setContentView(R.layout.delete_dialog);

        textView = dlg.findViewById(R.id.confirm_textview);
        okButton = dlg.findViewById(R.id.yesBtn);
        cancelButton = dlg.findViewById(R.id.noBtn);
        okButton.setOnClickListener(view -> {
            itemClickListener.OnItemClick();
        });

        cancelButton.setOnClickListener(view -> {
            item2ClickListener.OnItemClick();
        });
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void show(Lecture lecture) {
        textView.setText(lecture.getSubject_title() + "(" + String.format("%04d", lecture.getSubject_num()) + ")" + " 을(를) 삭제하시겠습니까? ");

        dlg.show();


    }


}
