package bi.konstrictor.pizzaphone;

import android.app.Activity;
import android.widget.Toast;

public class HOST {
    public static final String URL = "https://daviddurand.info/D228/pizza";

    public static void toast(final Activity activity, final String message, final int longueur){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, longueur).show();
            }
        });
    }
}
