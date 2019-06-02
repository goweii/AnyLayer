package per.goweii.android.anylayer;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import per.goweii.anylayer.Alignment;
import per.goweii.anylayer.AnimHelper;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.LayerActivity;
import per.goweii.anylayer.LayerManager;

public class FullScreenActivity extends AppCompatActivity implements View.OnClickListener {

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
        findViewById(R.id.tv_show_app_context).setOnClickListener(this);
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
        findViewById(R.id.tv_show_delayed_zoom).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_title:
                Toast.makeText(FullScreenActivity.this, "点击了title", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_show_app_context:
                AnyLayer.with(this.getApplicationContext(), new LayerActivity.OnLayerCreatedCallback() {
                    @Override
                    public void onLayerCreated(@NonNull AnyLayer anyLayer) {
                        anyLayer.contentView(R.layout.dialog_test_2)
                                .backgroundColorRes(R.color.dialog_bg)
                                .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                                .show();
                    }
                });
                break;
            case R.id.tv_show_multi:
                showMulti();
                break;
            case R.id.tv_show_edit:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_7)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.CENTER)
                        .onVisibleChangeListener(new LayerManager.OnVisibleChangeListener() {
                            @Override
                            public void onShow(AnyLayer anyLayer) {
                                EditText editText = anyLayer.getView(R.id.et_dialog_content);
                                anyLayer.compatSoftInput(editText);
                            }

                            @Override
                            public void onDismiss(AnyLayer anyLayer) {
                                anyLayer.removeSoftInput();
                            }
                        })
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createZoomAlphaInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createZoomAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .onClick(R.id.fl_dialog_yes, new LayerManager.OnLayerClickListener() {
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
                        .onClickToDismiss(R.id.iv_1)
                        .show();
                break;
            case R.id.tv_show_top:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_3)
                        .asStatusBar(R.id.v_status_bar)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.TOP)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createTopOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_view_group:
                AnyLayer.with(flContent)
                        .contentView(R.layout.dialog_test_3)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.TOP)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createTopOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_target_right:
                AnyLayer.target(findViewById(R.id.tv_show_target_right))
                        .outsideInterceptTouchEvent(false)
                        .alignment(Alignment.Direction.HORIZONTAL, Alignment.Horizontal.TO_RIGHT, Alignment.Vertical.CENTER, true)
                        .contentView(R.layout.dialog_test_5)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createLeftInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createLeftOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_left:
                AnyLayer.target(findViewById(R.id.tv_show_target_left))
                        .contentView(R.layout.dialog_test_5)
                        .alignment(Alignment.Direction.HORIZONTAL, Alignment.Horizontal.TO_LEFT, Alignment.Vertical.CENTER, true)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createRightInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createRightOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_top:
                AnyLayer.target(findViewById(R.id.tv_show_target_top))
                        .contentView(R.layout.dialog_test_4)
                        .alignment(Alignment.Direction.VERTICAL, Alignment.Horizontal.CENTER, Alignment.Vertical.ABOVE, true)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createBottomOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_bottom:
                AnyLayer.target(findViewById(R.id.tv_show_target_bottom))
                        .outsideInterceptTouchEvent(false)
                        .contentView(R.layout.dialog_test_4)
                        .alignment(Alignment.Direction.VERTICAL, Alignment.Horizontal.CENTER, Alignment.Vertical.BELOW, true)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createTopOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_bottom:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_3)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.BOTTOM)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createBottomOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_blur_bg:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundBlurPercent(0.05f)
                        .backgroundColorInt(getResources().getColor(R.color.dialog_blur_bg))
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_dark_bg:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_tran_bg:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createBottomOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_alpha_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createBottomAlphaInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createBottomAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createTopOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_alpha_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createTopAlphaInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createTopAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_bottom:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createBottomOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_top:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createTopOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_bottom_alpha:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createTopAlphaInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createBottomAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_top_alpha:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createBottomAlphaInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createTopAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_left_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createLeftInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createLeftOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_left_alpha_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createLeftAlphaInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createLeftAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_right_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createRightInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createRightOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_right_alpha_in:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createRightAlphaInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createRightAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_left_right:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createLeftInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createRightOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_right_left:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createRightInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createLeftOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_left_right_alpha:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createLeftAlphaInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createRightAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_right_left_alpha:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createRightAlphaInAnim(content);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createLeftAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_reveal:
                AnyLayer.with(FullScreenActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    return AnimHelper.createCircularRevealInAnim(content, content.getMeasuredWidth() / 2, content.getMeasuredHeight() / 2);
                                }
                                return null;
                            }

                            @Override
                            public Animator outAnim(View content) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    return AnimHelper.createCircularRevealOutAnim(content, content.getMeasuredWidth() / 2, content.getMeasuredHeight() / 2);
                                }
                                return null;
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_delayed_zoom:
                AnyLayer.target(findViewById(R.id.tv_show_delayed_zoom))
                        .alignment(Alignment.Direction.VERTICAL, Alignment.Horizontal.ALIGN_RIGHT, Alignment.Vertical.BELOW, true)
                        .contentView(R.layout.dialog_test_8)
                        .contentAnim(new LayerManager.IAnim() {
                            @Override
                            public Animator inAnim(View content) {
                                return AnimHelper.createDelayedZoomInAnim(content, 1F, 0F);
                            }

                            @Override
                            public Animator outAnim(View content) {
                                return AnimHelper.createDelayedZoomOutAnim(content, 1F, 0F);
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
                .contentAnim(new LayerManager.IAnim() {
                    @Override
                    public Animator inAnim(View content) {
                        return AnimHelper.createZoomAlphaInAnim(content);
                    }

                    @Override
                    public Animator outAnim(View content) {
                        return AnimHelper.createZoomAlphaOutAnim(content);
                    }
                })
                .onClick(R.id.fl_dialog_no, new LayerManager.OnLayerClickListener() {
                    @Override
                    public void onClick(AnyLayer anyLayer, View v) {
                        anyLayer.dismiss();
                    }
                })
                .onClick(R.id.fl_dialog_yes, new LayerManager.OnLayerClickListener() {
                    @Override
                    public void onClick(AnyLayer anyLayer, View v) {
                        showMulti();
                    }
                })
                .show();
    }
}

