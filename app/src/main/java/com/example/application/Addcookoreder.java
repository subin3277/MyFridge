package com.example.application;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;

public class Addcookoreder extends Dialog {

    private Context context;
    private CustomDialogClickListener customDialogClickListener;

    private MaterialEditText editText;
    private Button plus,cancle;

    public Addcookoreder(Context context,CustomDialogClickListener customDialogClickListener){
        super(context);
        this.context=context;
        this.customDialogClickListener=customDialogClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcooking);

        editText = findViewById(R.id.reciadd_cook_edit);
        plus = findViewById(R.id.reciadd_cook_btn);
        cancle = findViewById(R.id.reciadd_cook_canbtn);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }
}
