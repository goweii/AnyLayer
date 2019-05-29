package per.goweii.android.anylayer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

import per.goweii.anylayer.common.ListLayer;
import per.goweii.anylayer.common.LoadingLayer;
import per.goweii.anylayer.common.MenuLayer;
import per.goweii.anylayer.common.TipLayer;

public class CommonActivity extends AppCompatActivity implements View.OnClickListener {

    private LoadingLayer mLoadingLayer;
    private LoadingLayer mLoadingLayer2;
    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.translucentStatusBar(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_show_menu:
                MenuLayer menuLayer = MenuLayer.target(findViewById(R.id.ll_action_bar));
                if (mRandom == null) {
                    mRandom = new Random();
                }
                if (mRandom.nextBoolean()) {
                    menuLayer.icons(R.mipmap.ic_launcher_round,
                            R.mipmap.ic_launcher_round,
                            R.mipmap.ic_launcher_round,
                            R.mipmap.ic_launcher_round,
                            R.mipmap.ic_launcher_round);
                }
                menuLayer.datas("喜欢", "收藏", "回复", "复制链接", "浏览器打开")
                        .listener(new MenuLayer.OnMenuClickListener() {
                            @Override
                            public void onClick(String data, int pos) {
                                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_tip:
                TipLayer.make()
                        .title("欢迎使用")
                        .message(R.string.dialog_msg)
                        .show();
                break;
            case R.id.tv_show_tip_no_title:
                TipLayer.make()
                        .message(R.string.dialog_msg)
                        .show();
                break;
            case R.id.tv_show_tip_no_title_single_yes:
                TipLayer.make()
                        .cancelable(false)
                        .singleYesBtn()
                        .message(R.string.dialog_msg)
                        .show();
                break;
            case R.id.tv_show_list:
                ListLayer.with()
                        .datas("非常喜欢", "喜欢", "还不错", "不喜欢", "我想打人了")
                        .currSelectPos(1)
                        .title("选择一个评价")
                        .selectedItemColorRes(R.color.colorPrimary)
                        .listener(new ListLayer.OnItemSelectedListener() {
                            @Override
                            public void onSelect(String data, int pos) {
                                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_list_no_title_no_btn:
                ListLayer.with()
                        .noBtn()
                        .datas("喜欢", "收藏", "回复", "删除", "举报")
                        .listener(new ListLayer.OnItemSelectedListener() {
                            @Override
                            public void onSelect(String data, int pos) {
                                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_loading:
                mLoadingLayer = LoadingLayer.with(CommonActivity.this);
                mLoadingLayer.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mLoadingLayer != null) {
                            mLoadingLayer.dismiss();
                        }
                    }
                }, 1000);
                break;
            case R.id.tv_show_loading_no_text:
                mLoadingLayer2 = LoadingLayer.with(CommonActivity.this);
                mLoadingLayer2.text("正在加载").show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mLoadingLayer2 != null) {
                            mLoadingLayer2.dismiss();
                        }
                    }
                }, 1000);
                break;
        }
    }
}