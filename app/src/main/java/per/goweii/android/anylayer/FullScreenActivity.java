package per.goweii.android.anylayer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.LayerActivity;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.effect.BackdropBlurView;
import per.goweii.anylayer.ext.CircularRevealAnimatorCreator;
import per.goweii.anylayer.ext.SimpleAnimatorCreator;
import per.goweii.anylayer.guide.GuideLayer;
import per.goweii.anylayer.popup.PopupLayer;
import per.goweii.anylayer.popup.PopupLayer.Align;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.widget.SwipeLayout;
import per.goweii.statusbarcompat.StatusBarCompat;

public class FullScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private PopupLayer anyLayer_show_target_right = null;
    private PopupLayer anyLayer_show_target_bottom = null;
    private PopupLayer anyLayer_show_target_full = null;
    private DialogLayer anyLayer_show_menu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.transparent(this);
        setContentView(R.layout.activity_full_screen);
        initView();
        showMenuGuide();
    }

    private void showMenuGuide() {
        TextView textView1 = new TextView(FullScreenActivity.this);
        textView1.setText("带动画的菜单效果");
        textView1.setTextColor(Color.WHITE);
        textView1.setTextSize(20F);
        TextView textView3 = new TextView(FullScreenActivity.this);
        textView3.setText("下一个");
        textView3.setPadding(90, 30, 90, 30);
        textView3.setBackgroundResource(R.drawable.shape_icon);
        textView3.setTextColor(Color.WHITE);
        textView3.setTextSize(16F);
        new GuideLayer(FullScreenActivity.this)
                .addMapping(new GuideLayer.Mapping()
                        .setTargetView(findViewById(R.id.tv_show_menu))
                        .setCornerRadius(9999F)
                        .setPaddingLeft(30)
                        .setPaddingRight(30)
                        .setPaddingTop(-10)
                        .setPaddingBottom(-10)
                        .setGuideView(textView1)
                        .setHorizontalAlign(GuideLayer.Align.Horizontal.ALIGN_RIGHT)
                        .setVerticalAlign(GuideLayer.Align.Vertical.BELOW)
                        .setMarginTop(30)
                        .setMarginRight(30))
                .addMapping(new GuideLayer.Mapping()
                        .setGuideView(textView3)
                        .setHorizontalAlign(GuideLayer.Align.Horizontal.CENTER_PARENT)
                        .setVerticalAlign(GuideLayer.Align.Vertical.ALIGN_PARENT_BOTTOM)
                        .setMarginBottom(60)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer layer, @NonNull View v) {
                                layer.dismiss();
                                showBlurGuide();
                            }
                        }))
                .show();
    }

    private void showBlurGuide() {
        TextView textView1 = new TextView(FullScreenActivity.this);
        textView1.setText("高斯模糊背景的弹窗\n实现起来也很方便");
        textView1.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.WHITE);
        textView1.setTextSize(20F);
        TextView textView3 = new TextView(FullScreenActivity.this);
        textView3.setText("我知道了");
        textView3.setPadding(90, 30, 90, 30);
        textView3.setBackgroundResource(R.drawable.shape_icon);
        textView3.setTextColor(Color.WHITE);
        textView3.setTextSize(16F);
        new GuideLayer(FullScreenActivity.this)
                .addMapping(new GuideLayer.Mapping()
                        .setTargetView(findViewById(R.id.tv_show_blur_bg))
                        .setCornerRadius(8F)
                        .setPadding(-30)
                        .setGuideView(textView1)
                        .setHorizontalAlign(GuideLayer.Align.Horizontal.CENTER)
                        .setVerticalAlign(GuideLayer.Align.Vertical.ABOVE)
                        .setMarginBottom(30))
                .addMapping(new GuideLayer.Mapping()
                        .setGuideView(textView3)
                        .setHorizontalAlign(GuideLayer.Align.Horizontal.CENTER)
                        .setVerticalAlign(GuideLayer.Align.Vertical.ALIGN_PARENT_BOTTOM)
                        .setMarginBottom(60)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer layer, @NonNull View v) {
                                layer.dismiss();
                            }
                        }))
                .show();
    }

    private void initView() {
        findViewById(R.id.ll_action_bar).setPadding(0, StatusBarCompat.getHeight(this), 0, 0);
        findViewById(R.id.tv_show_app_context).setOnClickListener(this);
        findViewById(R.id.tv_show_multi).setOnClickListener(this);
        findViewById(R.id.tv_show_input).setOnClickListener(this);
        findViewById(R.id.tv_show_edit).setOnClickListener(this);
        findViewById(R.id.tv_show_full).setOnClickListener(this);
        findViewById(R.id.tv_show_target_full).setOnClickListener(this);
        findViewById(R.id.tv_show_target_right).setOnClickListener(this);
        findViewById(R.id.tv_show_target_top).setOnClickListener(this);
        findViewById(R.id.tv_show_target_bottom).setOnClickListener(this);
        findViewById(R.id.tv_show_target_left).setOnClickListener(this);
        findViewById(R.id.tv_show_blur_bg).setOnClickListener(this);
        findViewById(R.id.tv_show_blur_content).setOnClickListener(this);
        findViewById(R.id.tv_show_tran_bg).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom_in).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_top_in).setOnClickListener(this);
        findViewById(R.id.tv_show_top_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_left_in).setOnClickListener(this);
        findViewById(R.id.tv_show_left_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_right_in).setOnClickListener(this);
        findViewById(R.id.tv_show_right_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_reveal).setOnClickListener(this);
        findViewById(R.id.tv_show_menu).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_title:
                break;
            case R.id.tv_show_app_context:
                AnyLayer.dialog(new LayerActivity.OnLayerCreatedCallback() {
                    @Override
                    public void onLayerCreated(@NonNull DialogLayer anyLayer) {
                        anyLayer.setContentView(R.layout.dialog_normal)
                                .setBackgroundDimDefault()
                                .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                                .show();
                    }
                });
                break;
            case R.id.tv_show_multi:
                showMulti();
                break;
            case R.id.tv_show_input:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_input)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.BOTTOM)
                        .setSwipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return ObjectAnimator.ofInt(content, "scrollY", -content.getHeight(), 0)
                                        .setDuration(200);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return ObjectAnimator.ofInt(content, "scrollY", 0, -content.getHeight())
                                        .setDuration(200);
                            }
                        })
                        .addInputMethodCompat(true)
                        .addOnInputMethodListener(new DialogLayer.OnInputMethodListener() {
                            @Override
                            public void onOpen(@NonNull DialogLayer layer, int height) {
                                AnyLayer.toast(FullScreenActivity.this)
                                        .setMessage("输入法打开->当前高度" + height)
                                        .setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                                        .setMarginTop(300)
                                        .show();
                            }

                            @Override
                            public void onClose(@NonNull DialogLayer layer, int height) {
                                layer.dismiss();
                                AnyLayer.toast(FullScreenActivity.this)
                                        .setMessage("输入法关闭->当前高度" + height)
                                        .setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                                        .setMarginTop(300)
                                        .show();
                            }

                            @Override
                            public void onHeightChange(@NonNull DialogLayer layer, int height) {
                                AnyLayer.toast(FullScreenActivity.this)
                                        .setMessage("输入法高度改变->当前高度" + height)
                                        .setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                                        .setMarginTop(300)
                                        .show();
                            }
                        })
                        .addOnShowListener(new Layer.OnShowListener() {
                            @Override
                            public void onPreShow(@NonNull Layer layer) {
                                EditText et = layer.requireViewById(R.id.et_input);
                                et.setFocusable(true);
                                et.setFocusableInTouchMode(true);
                                et.requestFocus();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
                            }

                            @Override
                            public void onPostShow(@NonNull Layer layer) {
                            }
                        })
                        .addOnDismissListener(new Layer.OnDismissListener() {
                            @Override
                            public void onPreDismiss(@NonNull Layer layer) {
                                EditText et = layer.requireViewById(R.id.et_input);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                            }

                            @Override
                            public void onPostDismiss(@NonNull Layer layer) {
                            }
                        })
                        .addOnClickToDismissListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer layer, @NonNull View v) {
                                EditText et = layer.requireViewById(R.id.et_input);
                                Toast.makeText(FullScreenActivity.this, et.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, R.id.tv_send)
                        .show();
                break;
            case R.id.tv_show_edit:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_edit)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.BOTTOM)
                        .setSwipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .addInputMethodCompat(false)
                        .addOnClickToDismiss(R.id.fl_dialog_no)
                        .addOnClickToDismissListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                EditText et = anyLayer.requireViewById(R.id.et_dialog_content);
                                Toast.makeText(FullScreenActivity.this, et.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_full:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_fullscreen)
                        .addOnClickToDismiss(R.id.iv_1)
                        .show();
                break;
            case R.id.tv_show_target_full:
                if (anyLayer_show_target_full == null) {
                    anyLayer_show_target_full = (PopupLayer) AnyLayer.popup(findViewById(R.id.tv_show_target_full))
                            .setOutsideInterceptTouchEvent(false)
                            .setContentView(R.layout.dialog_fullscreen)
                            .setContentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createTopInAnim(content);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createTopOutAnim(content);
                                }
                            });
                }
                if (anyLayer_show_target_full.isShown()) {
                    anyLayer_show_target_full.dismiss();
                } else {
                    anyLayer_show_target_full.show();
                }
                break;
            case R.id.tv_show_target_right:
                if (anyLayer_show_target_right == null) {
                    anyLayer_show_target_right = (PopupLayer) AnyLayer.popup(findViewById(R.id.tv_show_target_right))
                            .setDirection(Align.Direction.HORIZONTAL)
                            .setHorizontal(Align.Horizontal.TO_RIGHT)
                            .setVertical(Align.Vertical.CENTER)
                            .setInside(true)
                            .setOutsideInterceptTouchEvent(false)
                            .setContentView(R.layout.popup_normal)
                            .setContentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createLeftInAnim(content);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createLeftOutAnim(content);
                                }
                            });
                    anyLayer_show_target_right.setUpdateLocationInterceptor(new PopupLayer.UpdateLocationInterceptor() {
                        @Override
                        public void interceptor(@NonNull float[] popupXY, int popupWidth, int popupHeight, int targetX, int targetY, int targetWidth, int targetHeight, int parentX, int parentY, int parentWidth, int parentHeight) {
                            popupXY[1] = Math.max(100, popupXY[1]);
                        }
                    });
                }
                if (anyLayer_show_target_right.isShown()) {
                    anyLayer_show_target_right.dismiss();
                } else {
                    anyLayer_show_target_right.show();
                }
                break;
            case R.id.tv_show_target_left:
                AnyLayer.popup(findViewById(R.id.tv_show_target_left))
                        .setAlign(Align.Direction.HORIZONTAL, Align.Horizontal.TO_LEFT, Align.Vertical.CENTER, true)
                        .setContentView(R.layout.popup_normal)
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_top:
                AnyLayer.popup(findViewById(R.id.tv_show_target_top))
                        .setAlign(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.ABOVE, true)
                        .setContentView(R.layout.popup_match_width)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_bottom:
                if (anyLayer_show_target_bottom == null) {
                    anyLayer_show_target_bottom = (PopupLayer) AnyLayer.popup(findViewById(R.id.tv_show_target_bottom))
                            .setAlign(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.BELOW, true)
                            .setOutsideInterceptTouchEvent(false)
                            .setBackgroundDimDefault()
                            .setContentView(R.layout.popup_match_width)
                            .setContentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createTopInAnim(content);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createTopOutAnim(content);
                                }
                            });
                }
                if (anyLayer_show_target_bottom.isShown()) {
                    anyLayer_show_target_bottom.dismiss();
                } else {
                    anyLayer_show_target_bottom.show();
                }
                break;
            case R.id.tv_show_blur_bg:
                @SuppressLint("InflateParams")
                BackdropBlurView blurBackground = new BackdropBlurView(this);
                blurBackground.setBlurPercent(0.05F);
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_icon)
                        .setBackgroundView(blurBackground)
                        .setBackgroundColorInt(Color.parseColor("#33ffffff"))
                        .show();
                break;
            case R.id.tv_show_blur_content:
                @SuppressLint("InflateParams")
                BackdropBlurView blurContent = (BackdropBlurView) LayoutInflater.from(this).inflate(R.layout.dialog_content_blur, null);
                blurContent.setBlurRadius(8F);
                blurContent.setSimpleSize(8F);
                blurContent.setCornerRadius(30F);
                blurContent.setOverlayColor(Color.parseColor("#66ffffff"));
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(blurContent)
                        .setBackgroundColorInt(Color.parseColor("#33000000"))
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_tran_bg:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.BOTTOM))
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_alpha_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.BOTTOM_ALPHA))
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_zoom_alpha_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.BOTTOM_ZOOM_ALPHA))
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.TOP))
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_alpha_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.TOP_ALPHA))
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_left_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.LEFT))
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_left_alpha_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.LEFT_ALPHA))
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_right_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.RIGHT))
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_right_alpha_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.RIGHT_ALPHA))
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_reveal:
                AnyLayer.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                                        ? new CircularRevealAnimatorCreator()
                                        : new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.ALPHA)
                        )
                        .addOnClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_menu:
                if (anyLayer_show_menu == null) {
                    anyLayer_show_menu = AnyLayer.popup(findViewById(R.id.tv_show_menu))
                            .setAlign(Align.Direction.VERTICAL, Align.Horizontal.ALIGN_PARENT_RIGHT, Align.Vertical.BELOW, false)
                            .setOffsetYdp(-15)
                            .setOutsideTouchToDismiss(true)
                            .setOutsideInterceptTouchEvent(false)
                            .setContentView(R.layout.popup_meun)
                            .setContentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createDelayedZoomInAnim(content, 0.8F, 0F);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createDelayedZoomOutAnim(content, 0.8F, 0F);
                                }
                            });
                }
                if (anyLayer_show_menu.isShown()) {
                    anyLayer_show_menu.dismiss();
                } else {
                    anyLayer_show_menu.show();
                }
                break;
        }
    }

    private void showMulti() {
        AnyLayer.dialog(FullScreenActivity.this)
                .setContentView(R.layout.dialog_more)
                .setBackgroundDimDefault()
                .setGravity(Gravity.CENTER)
                .setCancelableOnTouchOutside(false)
                .setCancelableOnClickKeyBack(false)
                .setContentAnimator(new DialogLayer.AnimatorCreator() {
                    @Override
                    public Animator createInAnimator(@NonNull View content) {
                        return AnimatorHelper.createZoomAlphaInAnim(content);
                    }

                    @Override
                    public Animator createOutAnimator(@NonNull View content) {
                        return AnimatorHelper.createZoomAlphaOutAnim(content);
                    }
                })
                .addOnClickListener(new Layer.OnClickListener() {
                    @Override
                    public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                        anyLayer.dismiss();
                    }
                }, R.id.fl_dialog_no)
                .addOnClickListener(new Layer.OnClickListener() {
                    @Override
                    public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                        showMulti();
                    }
                }, R.id.fl_dialog_yes)
                .show();
    }
}

