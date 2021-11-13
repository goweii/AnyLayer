package per.goweii.android.anylayer;

import android.app.Application;
import android.os.Handler;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class App extends Application {

    public static Handler sHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
