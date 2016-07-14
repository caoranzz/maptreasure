package edu.feicui.maptreasure.components;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import edu.feicui.maptreasure.R;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class AlertDialogFragment extends DialogFragment {

    private static final String  KEY_TITLE="title";
    private static final String KEY_MESSAGE="message";

    public static AlertDialogFragment newInstance(int resTitle,String message){
        AlertDialogFragment fragment=new AlertDialogFragment();
        Bundle bundle=new Bundle();
        bundle.putString(KEY_MESSAGE,message);
        bundle.putInt(KEY_TITLE, resTitle);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message=getArguments().getString(KEY_MESSAGE);
        int title=getArguments().getInt(KEY_TITLE);
        return new AlertDialog.Builder(getActivity(),getTheme())
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }
}
