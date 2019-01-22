package per.goweii.android.anylayer;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import per.goweii.anylayer.AnimHelper;
import per.goweii.anylayer.AnyLayer;

public class FullScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private static final long ANIM_DURATION = 350;
    private FrameLayout flContent;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.translucentStatusBar(this);
        setContentView(R.layout.activity_full_screen);
        initView();
    }

    private void initView() {
        flContent = findViewById(R.id.fl_content);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setOnClickListener(this);
        findViewById(R.id.tv_show_multi).setOnClickListener(this);
        findViewById(R.id.tv_show_edit).setOnClickListener(this);
        findViewById(R.id.tv_show_full).setOnClickListener(this);
        findViewById(R.id.tv_show_top).setOnClickListener(this);
        findViewById(R.id.tv_show_top_view_group).setOnClickListener(this);
        findViewById(R.id.tv_show_target_right).setOnClickListener(this);
        findViewById(R.id.tv_show_target_top).setOnClickListener(this);
        findViewById(R.id.tv_show_target_bottom).setOnClickListener(this);
        findViewById(R.id.tv_show_target_left).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom).setOnClickListener(this);
        findViewById(R.id.tv_show_blur_bg).setOnClickListener(this);
        findViewById(R.id.tv_show_dark_bg).setOnClickListener(this);
        findViewById(R.id.tv_show_tran_bg).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom_in).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_top_in).setOnClickListener(this);
        findViewById(R.id.tv_show_top_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_top_bottom).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom_top).setOnClickListener(this);
        findViewById(R.id.tv_show_top_bottom_alpha).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom_top_alpha).setOnClickListener(this);
        findViewById(R.id.tv_show_left_in).setOnClickListener(this);
        findViewById(R.id.tv_show_left_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_right_in).setOnClickListener(this);
        findViewById(R.id.tv_show_right_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_left_right).setOnClickListener(this);
        findViewById(R.id.tv_show_right_left).setOnClickListener(this);
        findViewById(R.id.tv_show_left_right_alpha).setOnClickListener(this);
        findViewById(R.id.tv_show_right_left_alpha).setOnClickListener(this);
        findViewById(R.id.tv_show_reveal).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_title:
                Toast.makeText(FullScreenActivity.this, "点击了title", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_show_multi:
                showMulti();
                break;
            case R.id.tv_show_edit:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_7)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.CENTER)
                        .cancelableOnTouchOutside(false)
                        .cancelableOnClickKeyBack(false)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startZoomInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startZoomOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                                EditText et = anyLayer.getView(R.id.et_dialog_content);
                                Toast.makeText(FullScreenActivity.this, et.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_full:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_1)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .onClickToDismiss(R.id.iv_1)
                        .show();
                break;
            case R.id.tv_show_top:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_3)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.TOP)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startTopInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startTopOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_top_view_group:
                AnyLayer.with(flContent)
                        .contentView(R.layout.dialog_test_3)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.TOP)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startTopInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startTopOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_right:
                AnyLayer.target(findViewById(R.id.tv_show_target_right))
                        .direction(AnyLayer.Direction.RIGHT)
                        .contentView(R.layout.dialog_test_5)
                        .gravity(Gravity.LEFT | Gravity.CENTER_VERTICAL)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startLeftInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startLeftOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_left:
                AnyLayer.target(findViewById(R.id.tv_show_target_left))
                        .direction(AnyLayer.Direction.LEFT)
                        .contentView(R.layout.dialog_test_5)
                        .gravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startRightInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startRightOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_top:
                AnyLayer.target(findViewById(R.id.tv_show_target_top))
                        .direction(AnyLayer.Direction.TOP)
                        .contentView(R.layout.dialog_test_4)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startBottomInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startBottomOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_bottom:
                AnyLayer.target(findViewById(R.id.tv_show_target_bottom))
                        .direction(AnyLayer.Direction.BOTTOM)
                        .contentView(R.layout.dialog_test_4)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startTopInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startTopOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_bottom:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_3)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.BOTTOM)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startBottomInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startBottomOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_blur_bg:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundBlurRadius(8)
                        .backgroundBlurScale(8)
                        .backgroundColorInt(Color.WHITE)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_dark_bg:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_tran_bg:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_bottom_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startBottomInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startBottomOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_bottom_alpha_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startBottomAlphaInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startBottomAlphaOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_top_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startTopInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startTopOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_top_alpha_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startTopAlphaInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startTopAlphaOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_top_bottom:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startTopInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startBottomOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_bottom_top:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startBottomInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startTopOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_top_bottom_alpha:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startTopAlphaInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startBottomAlphaOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_bottom_top_alpha:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startBottomAlphaInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startTopAlphaOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_left_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startLeftInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startLeftOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_left_alpha_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startLeftAlphaInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startLeftAlphaOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_right_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startRightInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startRightOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_right_alpha_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startRightAlphaInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startRightAlphaOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_left_right:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startLeftInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startRightOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_right_left:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startRightInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startLeftOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_left_right_alpha:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startLeftAlphaInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startRightAlphaOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_right_left_alpha:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                AnimHelper.startRightAlphaInAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                AnimHelper.startLeftAlphaOutAnim(content, ANIM_DURATION);
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_reveal:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .cancelableOnTouchOutside(true)
                        .cancelableOnClickKeyBack(true)
                        .contentAnim(new AnyLayer.IAnim() {
                            @Override
                            public long inAnim(View content) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    AnimHelper.startCircularRevealInAnim(content, content.getMeasuredWidth() / 2, content.getMeasuredHeight() / 2, ANIM_DURATION);
                                }
                                return ANIM_DURATION;
                            }

                            @Override
                            public long outAnim(View content) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    AnimHelper.startCircularRevealOutAnim(content, content.getMeasuredWidth() / 2, content.getMeasuredHeight() / 2, ANIM_DURATION);
                                }
                                return ANIM_DURATION;
                            }
                        })
                        .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                            @Override
                            public void onClick(AnyLayer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    private void showMulti() {
        AnyLayer.with(FullScreenActivity.this)
                .contentView(R.layout.dialog_test_6)
                .backgroundColorRes(R.color.dialog_bg)
                .gravity(Gravity.CENTER)
                .cancelableOnTouchOutside(false)
                .cancelableOnClickKeyBack(false)
                .contentAnim(new AnyLayer.IAnim() {
                    @Override
                    public long inAnim(View content) {
                        AnimHelper.startZoomInAnim(content, ANIM_DURATION);
                        return ANIM_DURATION;
                    }

                    @Override
                    public long outAnim(View content) {
                        AnimHelper.startZoomOutAnim(content, ANIM_DURATION);
                        return ANIM_DURATION;
                    }
                })
                .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
                    @Override
                    public void onClick(AnyLayer anyLayer, View v) {
                        anyLayer.dismiss();
                    }
                })
                .onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() {
                    @Override
                    public void onClick(AnyLayer anyLayer, View v) {
                        showMulti();
                    }
                })
                .show();
    }
}

