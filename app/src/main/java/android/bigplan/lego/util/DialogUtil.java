package android.bigplan.lego.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by 指尖上的艺术 on 2016/4/1.
 */
public class DialogUtil {

    public static ProgressDialog loadingDialog(Context context, String message) {
        return loadingDialog(context, message, true, false, null);
    }

    public static ProgressDialog loadingDialog(Context context, int messageRes) {
        return loadingDialog(context, context.getResources().getString(messageRes), true, false, null);
    }

    public static ProgressDialog loadingDialog(Context context, String message, DialogInterface.OnDismissListener dismissListener) {
        return loadingDialog(context, message, true, false, dismissListener);
    }

    public static ProgressDialog loadingDialog(Context context, int messageRes, DialogInterface.OnDismissListener dismissListener) {
        return loadingDialog(context, context.getResources().getString(messageRes), true, false, dismissListener);
    }

    public static ProgressDialog loadingDialog(Context context, String message, boolean cancelable, boolean canceledOnTouchOutside, DialogInterface.OnDismissListener dismissListener) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(cancelable);
        progressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        progressDialog.setMessage(message);
        progressDialog.setOnDismissListener(dismissListener);
        return progressDialog;
    }
}
