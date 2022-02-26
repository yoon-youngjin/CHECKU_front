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
    private Dialog dlg;
    private Button okButton;
    private Button cancelButton;
    private TextView textView;
    private Lecture lecture;

    public interface OnItemClickListener {
        void OnItemClick(Lecture lecture);
    }
    private DeleteDialog.OnItemClickListener itemAcceptClickListener = null;
    private DeleteDialog.OnItemClickListener itemRejectClickListener = null;

    // ok
    public void setOnAcceptItemClickListener(DeleteDialog.OnItemClickListener listener) {
        this.itemAcceptClickListener = listener;
    }
    // no
    public void setOnItemRejectClickListener(DeleteDialog.OnItemClickListener listener) {
        this.itemRejectClickListener = listener;
    }

    public DeleteDialog(Context context) {
        this.context = context;
        this.dlg = new Dialog(context);

        dlg.setContentView(R.layout.delete_dialog);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        init();
    }

    private void init() {
        textView = dlg.findViewById(R.id.confirm_textview);
        okButton = dlg.findViewById(R.id.yesBtn);
        cancelButton = dlg.findViewById(R.id.noBtn);
        okButton.setOnClickListener(view -> {
            itemAcceptClickListener.OnItemClick(lecture);
        });

        cancelButton.setOnClickListener(view -> {
            itemRejectClickListener.OnItemClick(lecture);
        });
    }

    public void show(Lecture lecture) {
        this.lecture = lecture;
        textView.setText(lecture.getSubject_title() + "(" + String.format("%04d", lecture.getSubject_num()) + ")" + " 을(를) 삭제하시겠습니까? ");
        dlg.show();
    }

    public void dismiss() {
        dlg.dismiss();
    }


}
