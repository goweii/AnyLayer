package per.goweii.android.anylayer;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Random;

import per.goweii.anylayer.Alignment;
import per.goweii.anylayer.AnimatorHelper;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.DialogLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.LayerActivity;

public class NormalActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout flContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        initView();
        AnyLayer.dialog(this)
                .contentView(R.layout.dialog_test_2)
                .gravity(Gravity.CENTER)
                .backgroundColorRes(R.color.dialog_bg)
                .cancelableOnTouchOutside(true)
                .cancelableOnClickKeyBack(true)
                .onClickToDismiss(R.id.fl_dialog_no)
                .show();
    }

    private void initView() {
        flContent = findViewById(R.id.fl_content);
        findViewById(R.id.tv_show_toast).setOnClickListener(this);
        findViewById(R.id.tv_show_full).setOnClickListener(this);
        findViewById(R.id.tv_show_app_context).setOnClickListener(this);
        findViewById(R.id.tv_show_no_context).setOnClickListener(this);
        findViewById(R.id.tv_show_delay).setOnClickListener(this);
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
            case R.id.tv_show_toast:
                boolean isSucc = new Random().nextBoolean();
                AnyLayer.toast()
                        .duration(3000)
                        .icon(isSucc ? R.drawable.ic_success : R.drawable.ic_fail)
                        .message(isSucc ? "哈哈，成功了" : "哎呀，失败了")
                        .show();
                break;
            case R.id.tv_show_full:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_1)
                        .onClickToDismiss(R.id.iv_1)
                        .show();
                break;
            case R.id.tv_show_app_context:
                AnyLayer.dialog(new LayerActivity.OnLayerCreatedCallback() {
                    @Override
                    public void onLayerCreated(@NonNull DialogLayer anyLayer) {
                        anyLayer.contentView(R.layout.dialog_test_2)
                                .backgroundColorRes(R.color.dialog_bg)
                                .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                                .show();
                    }
                });
                break;
            case R.id.tv_show_no_context:
                AnyLayer.dialog()
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_delay:
                App.sHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnyLayer.dialog(NormalActivity.this)
                                .contentView(R.layout.dialog_test_2)
                                .backgroundColorRes(R.color.dialog_bg)
                                .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                                .show();
                    }
                }, 5000);
                break;
            case R.id.tv_show_top:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_3)
                        .asStatusBar(R.id.v_status_bar)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.TOP)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createTopOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_view_group:
                AnyLayer.dialog(flContent)
                        .contentView(R.layout.dialog_test_3)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.TOP)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createTopOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_target_right:
                AnyLayer.popup(findViewById(R.id.tv_show_target_right))
                        .outsideInterceptTouchEvent(false)
                        .contentView(R.layout.dialog_test_5)
                        .alignment(Alignment.Direction.HORIZONTAL, Alignment.Horizontal.TO_RIGHT, Alignment.Vertical.CENTER, true)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createLeftInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createLeftOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_left:
                AnyLayer.popup(findViewById(R.id.tv_show_target_left))
                        .contentView(R.layout.dialog_test_5)
                        .alignment(Alignment.Direction.HORIZONTAL, Alignment.Horizontal.TO_LEFT, Alignment.Vertical.CENTER, true)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createRightInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createRightOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_top:
                AnyLayer.popup(findViewById(R.id.tv_show_target_top))
                        .contentView(R.layout.dialog_test_4)
                        .alignment(Alignment.Direction.VERTICAL, Alignment.Horizontal.CENTER, Alignment.Vertical.ABOVE, true)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_bottom:
                AnyLayer.popup(findViewById(R.id.tv_show_target_bottom))
                        .outsideInterceptTouchEvent(false)
                        .contentView(R.layout.dialog_test_4)
                        .alignment(Alignment.Direction.VERTICAL, Alignment.Horizontal.CENTER, Alignment.Vertical.BELOW, true)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createTopOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_bottom:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_3)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.BOTTOM)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_blur_bg:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundBlurPercent(0.05f)
                        .backgroundColorInt(getResources().getColor(R.color.dialog_blur_bg))
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_dark_bg:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_tran_bg:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createBottomAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createBottomAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_top_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createTopOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_top_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createTopAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createTopAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_top_bottom:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_top:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createTopOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_top_bottom_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createTopAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createBottomAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_top_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createBottomAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createTopAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createLeftInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createLeftOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createLeftAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createLeftAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createRightInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createRightOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createRightAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createRightAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_right:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createLeftInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createRightOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_left:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createRightInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createLeftOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_right_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createLeftAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createRightAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_left_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createRightAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createLeftAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_reveal:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_test_2)
                        .backgroundColorRes(R.color.dialog_bg)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    return AnimatorHelper.createCircularRevealInAnim(content, content.getMeasuredWidth() / 2, content.getMeasuredHeight() / 2);
                                } else {
                                    return null;
                                }
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    return AnimatorHelper.createCircularRevealOutAnim(content, content.getMeasuredWidth() / 2, content.getMeasuredHeight() / 2);
                                } else {
                                    return null;
                                }
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer anyLayer, View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
        }
    }
}

