package univ.yonsei.eagle_eleven;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Popup extends Dialog {
    private TextView tvContents;
    private Button btnClose;

    public Popup(@NonNull Context context, String contents) {
        super(context);
        setContentView(R.layout.activity_popup);

        tvContents = findViewById(R.id.tvContents);
        tvContents.setText(contents);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
