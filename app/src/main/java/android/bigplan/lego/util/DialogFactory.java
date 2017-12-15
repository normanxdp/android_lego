package android.bigplan.lego.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.bigplan.lego.R;

public class DialogFactory{
    @SuppressWarnings("deprecation")
	public static Dialog creatRequestDialog(final Context context, String tip) {
        
        final Dialog dialog = new Dialog(context, R.style.dialog);
         dialog.setCancelable(false);
         dialog.setCanceledOnTouchOutside(false);
        
        dialog.setContentView(R.layout.dialog_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        
        WindowManager manager = (WindowManager)context
            .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        
        int width = display.getWidth();
        lp.width = (int)(0.7 * width);
        
        TextView titleTxtv = (TextView)dialog.findViewById(R.id.tvLoad);
        titleTxtv.setText(tip);
        
        return dialog;
    }
}
