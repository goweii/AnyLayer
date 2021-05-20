package per.goweii.android.anylayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Random;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.LayerActivity;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.overlay.OverlayLayer;
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
        new OverlayLayer(this)
                .setOverlayView(floatCardView)
                .setSnapEdge(OverlayLayer.Edge.ALL)
                .setOutside(true)
                .setDefPercentX(1)
                .setDefPercentY(0.6F)
                .setDefAlpha(0F)
                .setDefScale(0F)
                .setNormalAlpha(0.9F)
                .setNormalScale(1)
                .setLowProfileDelay(3000)
                .setLowProfileAlpha(0.6F)
                .setLowProfileScale(0.9F)
                .setLowProfileIndent(0.5F)
                .setPaddingLeft(45)
                .setPaddingTop(45)
                .setPaddingRight(45)
                .setPaddingBottom(45)
                .setMarginLeft(0)
                .setMarginTop(0)
                .setMarginRight(0)
                .setMarginBottom(0)
                .addOnOverlayClickListener(new Layer.OnClickListener() {
                    @Override
                    public void onClick(@NonNull Layer layer, @NonNull View v) {
                        AnyLayer.toast().setMessage("点击了悬浮按钮").setGravity(Gravity.CENTER).show();
                    }
                })
                .show();
        Layer dialog = AnyLayer.dialog(this)
                .setContentView(R.layout.dialog_normal)
                .setBackgroundDimDefault()
                .setGravity(Gravity.CENTER)
                .setCancelableOnTouchOutside(true)
                .setCancelableOnClickKeyBack(true)
                .addOnClickToDismissListener(R.id.fl_dialog_no);
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
                        .setIcon(isSucc ? R.drawable.ic_success : R.drawable.ic_fail)
                        .setMessage(isSucc ? "哈哈，成功了" : "哎呀，失败了")
                        .setTextColorInt(Color.WHITE)
                        .setBackgroundColorRes(isSucc ? R.color.colorPrimary : R.color.colorAccent)
                        .setGravity(Gravity.CENTER)
                        .show();
                break;
            case R.id.tv_show_notification:
                AnyLayer.getGlobalConfig().notificationTimePattern = "HH:mm";
                AnyLayer.getGlobalConfig().notificationIcon = getResources().getDrawable(R.drawable.ic_notificstion);
                AnyLayer.getGlobalConfig().notificationLabel = getString(R.string.app_name);
                new NotificationLayer(this)
                        .setTitle("这是一个通知")
                        .setDesc(R.string.dialog_msg)
                        .setOnNotificationClickListener(new Layer.OnClickListener() {
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
                        .setContentView(R.layout.dialog_edit)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.BOTTOM)
                        .setSwipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .addSoftInputCompat(false)
                        .addSoftInputCompat(true, R.id.et_dialog_content4)
                        .addOnClickToDismissListener(R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                                EditText et = anyLayer.findView(R.id.et_dialog_content);
                                Toast.makeText(NormalActivity.this, et.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_full:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_fullscreen)
                        .addOnClickToDismissListener(R.id.iv_1)
                        .show();
                break;
            case R.id.tv_show_app_context:
                AnyLayer.dialog(new LayerActivity.OnLayerCreatedCallback() {
                    @Override
                    public void onLayerCreated(@NonNull DialogLayer anyLayer) {
                        anyLayer.setContentView(R.layout.dialog_normal)
                                .setBackgroundDimDefault()
                                .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                                .show();
                    }
                });
                break;
            case R.id.tv_show_no_context:
                AnyLayer.dialog()
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_delay:
                App.sHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnyLayer.dialog(NormalActivity.this)
                                .setContentView(R.layout.dialog_normal)
                                .setBackgroundDimDefault()
                                .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                                .show();
                    }
                }, 5000);
                break;
            case R.id.tv_show_top:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_match_width)
                        .setAvoidStatusBar(true)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.TOP)
                        .setSwipeDismiss(SwipeLayout.Direction.TOP)
                        .addOnClickToDismissListener(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_target_full:
                AnyLayer.popup(findViewById(R.id.tv_show_target_full))
                        .setContentView(R.layout.dialog_fullscreen)
                        .setAnimStyle(DialogLayer.AnimStyle.TOP)
                        .show();
                break;
            case R.id.tv_show_target_right:
                if (anyLayer_show_target_right == null) {
                    anyLayer_show_target_right = AnyLayer.popup(findViewById(R.id.tv_show_target_right))
                            .setAlign(Align.Direction.HORIZONTAL, Align.Horizontal.TO_RIGHT, Align.Vertical.CENTER, false)
                            .setOutsideInterceptTouchEvent(false)
                            .setContentView(R.layout.popup_normal)
                            .setAnimStyle(DialogLayer.AnimStyle.LEFT);
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
                        .setAlign(Align.Direction.HORIZONTAL, Align.Horizontal.TO_LEFT, Align.Vertical.CENTER, true)
                        .setContentView(R.layout.popup_normal)
                        .setAnimStyle(DialogLayer.AnimStyle.RIGHT)
                        .show();
                break;
            case R.id.tv_show_target_top:
                AnyLayer.popup(findViewById(R.id.tv_show_target_top))
                        .setAlign(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.ABOVE, true)
                        .setContentView(R.layout.popup_match_width)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .setAnimStyle(DialogLayer.AnimStyle.BOTTOM)
                        .show();
                break;
            case R.id.tv_show_target_bottom:
                if (anyLayer_show_target_bottom == null) {
                    anyLayer_show_target_bottom = AnyLayer.popup(findViewById(R.id.tv_show_target_bottom))
                            .setAlign(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.BELOW, false)
                            .setContentClip(false)
                            .setOutsideInterceptTouchEvent(false)
                            .setOutsideTouchToDismiss(true)
                            .setContentView(R.layout.popup_meun)
                            .setContentAnimator(new DialogLayer.AnimatorCreator() {
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
                        .setContentView(R.layout.dialog_list)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.BOTTOM)
                        .setSwipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .addOnClickToDismissListener(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_blur_bg:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_icon)
                        .setBackgroundBlurPercent(0.05f)
                        .setBackgroundColorInt(getResources().getColor(R.color.dialog_blur_bg))
                        .show();
                break;
            case R.id.tv_show_dark_bg:
                if (layer_dark_bg == null) {
                    layer_dark_bg = AnyLayer.dialog(NormalActivity.this)
                            .setContentView(R.layout.dialog_normal)
                            .setBackgroundDimDefault()
                            .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                            .addOnInitializeListener(new Layer.OnInitializeListener() {
                                @Override
                                public void onInitialize(@NonNull Layer layer) {
                                    TextView tv_dialog_content = layer.findView(R.id.tv_dialog_content);
                                    tv_dialog_content.setText("这是第一次初始化时绑定的数据" + Math.random());
                                }
                            })
                            .addOnBindDataListener(new Layer.OnBindDataListener() {
                                @Override
                                public void onBindData(@NonNull Layer layer) {
                                    TextView tv_dialog_title = layer.findView(R.id.tv_dialog_title);
                                    tv_dialog_title.setText("标题View$" + System.identityHashCode(tv_dialog_title));
                                }
                            })
                            .addOnClickListener(new Layer.OnClickListener() {
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
                        .setContentView(R.layout.dialog_normal)
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_in:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setSwipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setSwipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .setSwipeTransformer(new DialogLayer.SwipeTransformer() {
                            @Override
                            public void onSwiping(@NonNull DialogLayer layer,
                                                  @SwipeLayout.Direction int direction,
                                                  @FloatRange(from = 0F, to = 1F) float fraction) {
                                layer.getViewHolder().getContent().setAlpha(1 - fraction);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_zoom_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
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
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_in:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setSwipeDismiss(SwipeLayout.Direction.TOP)
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_top_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopAlphaOutAnim(content);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_top_bottom:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_top:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopOutAnim(content);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_top_bottom_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomAlphaOutAnim(content);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_bottom_top_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopAlphaOutAnim(content);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_in:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setSwipeDismiss(SwipeLayout.Direction.LEFT)
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftAlphaOutAnim(content);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_in:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setSwipeDismiss(SwipeLayout.Direction.RIGHT)
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_alpha_in:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightAlphaOutAnim(content);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_right:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightOutAnim(content);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_left:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftOutAnim(content);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_left_right_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightAlphaOutAnim(content);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_right_left_alpha:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftAlphaOutAnim(content);
                            }
                        })
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_reveal:
                AnyLayer.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
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
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .addOnClickListener(new Layer.OnClickListener() {
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

