package per.goweii.android.anylayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.LayerActivity;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.floats.FloatLayer;
import per.goweii.anylayer.notification.NotificationLayer;
import per.goweii.anylayer.popup.PopupLayer.Align;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.widget.SwipeLayout;

public class NormalActivity extends AppCompatActivity implements View.OnClickListener {

    private DialogLayer anyLayer_show_target_right = null;
    private boolean anyLayer_show_target_right_shown = false;
    private DialogLayer anyLayer_show_target_bottom = null;
    private Layer layer_dark_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        initView();
    }

    private void initView() {
        findViewById(R.id.tv_show_toast).setOnClickListener(this);
        findViewById(R.id.tv_show_notification).setOnClickListener(this);
        findViewById(R.id.tv_show_edit).setOnClickListener(this);
        findViewById(R.id.tv_show_full).setOnClickListener(this);
        findViewById(R.id.tv_show_app_context).setOnClickListener(this);
        findViewById(R.id.tv_show_no_context).setOnClickListener(this);
        findViewById(R.id.tv_show_delay).setOnClickListener(this);
        findViewById(R.id.tv_show_top).setOnClickListener(this);
        findViewById(R.id.tv_show_target_full).setOnClickListener(this);
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
        findViewById(R.id.tv_show_bottom_zoom_alpha_in).setOnClickListener(this);
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
        CardView floatCardView = new CardView(this);
        ImageView floatIconView = new ImageView(this);
        floatIconView.setImageResource(R.mipmap.ic_launcher_foreground);
        floatIconView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        floatIconView.setBackgroundResource(R.color.colorPrimary);
        floatCardView.addView(floatIconView);
        floatCardView.setCardBackgroundColor(Color.TRANSPARENT);
        floatCardView.setRadius(90);
        floatCardView.setLayoutParams(new ViewGroup.LayoutParams(180, 180));
        new FloatLayer(this)
                .floatView(floatCardView)
                .snapEdge(FloatLayer.Edge.ALL)
                .outside(true)
                .defPercentX(1)
                .defPercentY(0.6F)
                .defAlpha(0F)
                .defScale(0F)
                .normalAlpha(0.9F)
                .normalScale(1)
                .lowProfileDelay(3000)
                .lowProfileAlpha(0.6F)
                .lowProfileScale(0.9F)
                .lowProfileIndent(0.5F)
                .paddingLeft(45)
                .paddingTop(45)
                .paddingRight(45)
                .paddingBottom(45)
                .marginLeft(0)
                .marginTop(0)
                .marginRight(0)
                .marginBottom(0)
                .onFloatClick(new Layer.OnClickListener() {
                    @Override
                    public void onClick(@NonNull Layer layer, @NonNull View v) {
                        AnyLayer.toast().message("点击了悬浮按钮").gravity(Gravity.CENTER).show();
                    }
                })
                .show();
        Layer dialog = AnyLayer.dialog(this)
                .contentView(R.layout.dialog_normal)
                .backgroundDimDefault()
                .gravity(Gravity.CENTER)
                .cancelableOnTouchOutside(true)
                .cancelableOnClickKeyBack(true)
                .onClickToDismiss(R.id.fl_dialog_no);
        dialog.show();
        dialog.dismiss();
    }

    private Random mRandom = new Random();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_show_toast:
                boolean isSucc = mRandom.nextBoolean();
                AnyLayer.toast()
                        .icon(isSucc ? R.drawable.ic_success : R.drawable.ic_fail)
                        .message(isSucc ? "哈哈，成功了" : "哎呀，失败了")
                        .textColorInt(Color.WHITE)
                        .backgroundColorRes(isSucc ? R.color.colorPrimary : R.color.colorAccent)
                        .gravity(Gravity.CENTER)
                        .show();
                break;
            case R.id.tv_show_notification:
                AnyLayer.globalConfig().notificationTimePattern = "HH:mm";
                AnyLayer.globalConfig().notificationIcon = getResources().getDrawable(R.drawable.ic_notificstion);
                AnyLayer.globalConfig().notificationLabel = getString(R.string.app_name);
                new NotificationLayer(this)
                        .title("这是一个通知")
                        .desc(R.string.dialog_msg)
                        .onNotificationClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer layer, @NonNull View view) {
                                findViewById(R.id.tv_show_toast).performLongClick();
                                layer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_edit:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_edit)
                        .backgroundDimDefault()
                        .gravity(Gravity.BOTTOM)
                        .swipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .compatSoftInput(false)
                        .compatSoftInput(true, R.id.et_dialog_content4)
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                                EditText et = anyLayer.getView(R.id.et_dialog_content);
                                Toast.makeText(NormalActivity.this, et.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_full:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_fullscreen)
                        .onClickToDismiss(R.id.iv_1)
                        .show();
                break;
            case R.id.tv_show_app_context:
                AnyLayer.dialog(new LayerActivity.OnLayerCreatedCallback() {
                    @Override
                    public void onLayerCreated(@NonNull DialogLayer anyLayer) {
                        anyLayer.contentView(R.layout.dialog_normal)
                                .backgroundDimDefault()
                                .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                                .show();
                    }
                });
                break;
            case R.id.tv_show_no_context:
                AnyLayer.dialog()
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_delay:
                App.sHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnyLayer.dialog(NormalActivity.this)
                                .contentView(R.layout.dialog_normal)
                                .backgroundDimDefault()
                                .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                                .show();
                    }
                }, 5000);
                break;
            case R.id.tv_show_top:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_match_width)
                        .avoidStatusBar(true)
                        .backgroundDimDefault()
                        .gravity(Gravity.TOP)
                        .swipeDismiss(SwipeLayout.Direction.TOP)
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_target_full:
                AnyLayer.popup(findViewById(R.id.tv_show_target_full))
                        .contentView(R.layout.dialog_fullscreen)
                        .animStyle(DialogLayer.AnimStyle.TOP)
                        .show();
                break;
            case R.id.tv_show_target_right:
                if (anyLayer_show_target_right == null) {
                    anyLayer_show_target_right = AnyLayer.popup(findViewById(R.id.tv_show_target_right))
                            .align(Align.Direction.HORIZONTAL, Align.Horizontal.TO_RIGHT, Align.Vertical.CENTER, false)
                            .outsideInterceptTouchEvent(false)
                            .contentView(R.layout.popup_normal)
                            .animStyle(DialogLayer.AnimStyle.LEFT);
                }
                if (anyLayer_show_target_right_shown) {
                    anyLayer_show_target_right_shown = false;
                    anyLayer_show_target_right.dismiss();
                } else {
                    anyLayer_show_target_right_shown = true;
                    anyLayer_show_target_right.show();
                }
                break;
            case R.id.tv_show_target_left:
                AnyLayer.popup(findViewById(R.id.tv_show_target_left))
                        .align(Align.Direction.HORIZONTAL, Align.Horizontal.TO_LEFT, Align.Vertical.CENTER, true)
                        .contentView(R.layout.popup_normal)
                        .animStyle(DialogLayer.AnimStyle.RIGHT)
                        .show();
                break;
            case R.id.tv_show_target_top:
                AnyLayer.popup(findViewById(R.id.tv_show_target_top))
                        .align(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.ABOVE, true)
                        .contentView(R.layout.popup_match_width)
                        .backgroundDimDefault()
                        .gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .animStyle(DialogLayer.AnimStyle.BOTTOM)
                        .show();
                break;
            case R.id.tv_show_target_bottom:
                if (anyLayer_show_target_bottom == null) {
                    anyLayer_show_target_bottom = AnyLayer.popup(findViewById(R.id.tv_show_target_bottom))
                            .align(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.BELOW, false)
                            .contentClip(false)
                            .outsideInterceptTouchEvent(false)
                            .outsideTouchedToDismiss(true)
                            .contentView(R.layout.popup_meun)
                            .contentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createDelayedZoomInAnim(content, 0.5F, 0F);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createDelayedZoomOutAnim(content, 0.5F, 0F);
                                }
                            });
                }
                if (anyLayer_show_target_bottom.isShown()) {
//                    anyLayer_show_target_bottom.dismiss();
                } else {
                    anyLayer_show_target_bottom.show();
                }
                break;
            case R.id.tv_show_bottom:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_list)
                        .backgroundDimDefault()
                        .gravity(Gravity.BOTTOM)
                        .swipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_blur_bg:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_icon)
                        .backgroundBlurPercent(0.05f)
                        .backgroundColorInt(getResources().getColor(R.color.dialog_blur_bg))
                        .show();
                break;
            case R.id.tv_show_dark_bg:
                if (layer_dark_bg == null) {
                    layer_dark_bg = AnyLayer.dialog(NormalActivity.this)
                            .contentView(R.layout.dialog_normal)
                            .backgroundDimDefault()
                            .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                            .onInitialize(new Layer.OnInitialize() {
                                @Override
                                public void onInit(@NonNull Layer layer) {
                                    TextView tv_dialog_content = layer.getView(R.id.tv_dialog_content);
                                    tv_dialog_content.setText("这是第一次初始化时绑定的数据" + Math.random());
                                }
                            })
                            .bindData(new Layer.DataBinder() {
                                @Override
                                public void bindData(@NonNull Layer layer) {
                                    TextView tv_dialog_title = layer.getView(R.id.tv_dialog_title);
                                    tv_dialog_title.setText("标题View$" + System.identityHashCode(tv_dialog_title));
                                }
                            })
                            .onClick(new Layer.OnClickListener() {
                                @Override
                                public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                    anyLayer.dismiss();
                                }
                            }, R.id.fl_dialog_yes);
                }
                layer_dark_bg.show();
                break;
            case R.id.tv_show_tran_bg:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .swipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .swipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .swipeTransformer(new DialogLayer.SwipeTransformer() {
                            @Override
                            public void onSwiping(@NonNull DialogLayer layer,
                                                  @SwipeLayout.Direction int direction,
                                                  @FloatRange(from = 0F, to = 1F) float fraction) {
                                layer.getViewHolder().getContent().setAlpha(1 - fraction);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_zoom_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                AnimatorSet set = new AnimatorSet();
                                Animator a1 = AnimatorHelper.createBottomAlphaInAnim(content, 0.3F);
                                a1.setInterpolator(new DecelerateInterpolator(2.5f));
                                Animator a2 = AnimatorHelper.createZoomAlphaInAnim(content, 0.9F);
                                a2.setInterpolator(new DecelerateInterpolator(1.5f));
                                set.playTogether(a1, a2);
                                return set;
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                AnimatorSet set = new AnimatorSet();
                                Animator a1 = AnimatorHelper.createBottomAlphaOutAnim(content, 0.3F);
                                a1.setInterpolator(new DecelerateInterpolator(1.5f));
                                Animator a2 = AnimatorHelper.createZoomAlphaOutAnim(content, 0.9F);
                                a2.setInterpolator(new DecelerateInterpolator(2.5f));
                                set.playTogether(a1, a2);
                                return set;
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .swipeDismiss(SwipeLayout.Direction.TOP)
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_top_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_top_bottom:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_top:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_top_bottom_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_top_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .swipeDismiss(SwipeLayout.Direction.LEFT)
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .swipeDismiss(SwipeLayout.Direction.RIGHT)
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_right:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_left:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_right_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_left_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_reveal:
                AnyLayer.dialog(NormalActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    return AnimatorHelper.createCircularRevealInAnim(content, content.getMeasuredWidth() / 2, content.getMeasuredHeight() / 2);
                                } else {
                                    return null;
                                }
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
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
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
        }
    }
}

