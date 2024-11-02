package com.mankind.petidea.spinkit;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.mankind.petidea.R;

import java.lang.ref.WeakReference;

public class SpinKitLoader {
    private Dialog dialog;
    private Context context;

    public SpinKitLoader(Context context){
        this.context = context;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(
                R.layout.spinkit_view, null
        );
        dialog.setContentView(view);
        dialog.setCancelable(false);
    }

    public void showDialog(){
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }
}
