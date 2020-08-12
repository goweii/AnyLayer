package per.goweii.android.anylayer;

import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;

import per.goweii.statusbarcompat.StatusBarCompat;

public class CommonActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.transparent(this);
        setContentView(R.layout.activity_common);
        initView();
    }

    private void initView() {
        findViewById(R.id.tv_show_menu).setOnClickListener(this);
        findViewById(R.id.tv_show_tip).setOnClickListener(this);
        findViewById(R.id.tv_show_tip_no_title).setOnClickListener(this);
        findViewById(R.id.tv_show_tip_no_title_single_yes).setOnClickListener(this);
        findViewById(R.id.tv_show_list).setOnClickListener(this);
        findViewById(R.id.tv_show_list_no_title_no_btn).setOnClickListener(this);
        findViewById(R.id.tv_show_loading).setOnClickListener(this);
        findViewById(R.id.tv_show_loading_no_text).setOnClickListener(this);
        findViewById(R.id.tv_toast).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_show_menu:
                break;
            case R.id.tv_show_tip:
                break;
            case R.id.tv_show_tip_no_title:
                break;
            case R.id.tv_show_tip_no_title_single_yes:
                break;
            case R.id.tv_show_list:
                break;
            case R.id.tv_show_list_no_title_no_btn:
                break;
            case R.id.tv_show_loading:
                break;
            case R.id.tv_show_loading_no_text:
                break;
            case R.id.tv_toast:
                break;
        }
    }
}