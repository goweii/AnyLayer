package per.goweii.android.anylayer;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import per.goweii.anylayer.Align;
import per.goweii.anylayer.AnimatorHelper;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.DialogLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.LayerActivity;
import per.goweii.anylayer.SwipeLayout;
import per.goweii.statusbarcompat.StatusBarCompat;

public class FullScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private DialogLayer anyLayer_show_target_right = null;
    private DialogLayer anyLayer_show_target_bottom = null;
    private DialogLayer anyLayer_show_target_full = null;
    private DialogLayer anyLayer_show_menu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.transparent(this);
        setContentView(R.layout.activity_full_screen);
        initView();
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

    private void initView() {
        findViewById(R.id.ll_action_bar).setPadding(0, StatusBarCompat.getHeight(this), 0, 0);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setOnClickListener(this);
        findViewById(R.id.tv_show_app_context).setOnClickListener(this);
        findViewById(R.id.tv_show_multi).setOnClickListener(this);
        findViewById(R.id.tv_show_edit).setOnClickListener(this);
        findViewById(R.id.tv_show_full).setOnClickListener(this);
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
        findViewById(R.id.tv_show_menu).setOnClickListener(this);
    }

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
                        anyLayer.contentView(R.layout.dialog_normal)
                                .backgroundDimDefault()
                                .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                                .show();
                    }
                });
                break;
            case R.id.tv_show_multi:
                showMulti();
                break;
            case R.id.tv_show_edit:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_edit)
                        .backgroundDimDefault()
                        .gravity(Gravity.BOTTOM)
                        .swipeDismiss(SwipeLayout.Direction.Bottom)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .onVisibleChangeListener(new Layer.OnVisibleChangeListener() {
                            @Override
                            public void onShow(@NonNull Layer layer) {
                                DialogLayer dialogLayer = (DialogLayer) layer;
                                dialogLayer.compatSoftInput(
                                        layer.getView(R.id.et_dialog_content2),
                                        layer.getView(R.id.et_dialog_content3),
                                        layer.getView(R.id.et_dialog_content4),
                                        layer.getView(R.id.et_dialog_content5)
                                );
                            }

                            @Override
                            public void onDismiss(@NonNull Layer layer) {
                                DialogLayer dialogLayer = (DialogLayer) layer;
                                dialogLayer.removeSoftInput();
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .onClick(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                anyLayer.dismiss();
                                EditText et = anyLayer.getView(R.id.et_dialog_content);
                                Toast.makeText(FullScreenActivity.this, et.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_full:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_fullscreen)
                        .onClickToDismiss(R.id.iv_1)
                        .show();
                break;
            case R.id.tv_show_top:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_match_width)
                        .avoidStatusBar(true)
                        .backgroundDimDefault()
                        .gravity(Gravity.TOP)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_target_full:
                if (anyLayer_show_target_full == null) {
                    anyLayer_show_target_full = AnyLayer.popup(findViewById(R.id.tv_show_target_full))
                            .outsideInterceptTouchEvent(false)
                            .contentView(R.layout.dialog_fullscreen)
                            .contentAnimator(new DialogLayer.AnimatorCreator() {
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
                if (anyLayer_show_target_full.isShow()) {
                    anyLayer_show_target_full.dismiss();
                } else {
                    anyLayer_show_target_full.show();
                }
                break;
            case R.id.tv_show_target_right:
                if (anyLayer_show_target_right == null) {
                    anyLayer_show_target_right = AnyLayer.popup(findViewById(R.id.tv_show_target_right))
                            .direction(Align.Direction.HORIZONTAL)
                            .horizontal(Align.Horizontal.TO_RIGHT)
                            .vertical(Align.Vertical.CENTER)
                            .inside(true)
                            .outsideInterceptTouchEvent(false)
                            .contentView(R.layout.popup_normal)
                            .contentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createLeftInAnim(content);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createLeftOutAnim(content);
                                }
                            });
                }
                if (anyLayer_show_target_right.isShow()) {
                    anyLayer_show_target_right.dismiss();
                } else {
                    anyLayer_show_target_right.show();
                }
                break;
            case R.id.tv_show_target_left:
                AnyLayer.popup(findViewById(R.id.tv_show_target_left))
                        .align(Align.Direction.HORIZONTAL, Align.Horizontal.TO_LEFT, Align.Vertical.CENTER, true)
                        .contentView(R.layout.popup_normal)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
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
                        .align(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.ABOVE, true)
                        .contentView(R.layout.popup_match_width)
                        .backgroundDimDefault()
                        .gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
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
                    anyLayer_show_target_bottom = AnyLayer.popup(findViewById(R.id.tv_show_target_bottom))
                            .align(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.BELOW, true)
                            .outsideInterceptTouchEvent(false)
                            .backgroundDimDefault()
                            .contentView(R.layout.popup_match_width)
                            .contentAnimator(new DialogLayer.AnimatorCreator() {
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
                if (anyLayer_show_target_bottom.isShow()) {
                    anyLayer_show_target_bottom.dismiss();
                } else {
                    anyLayer_show_target_bottom.show();
                }
                break;
            case R.id.tv_show_bottom:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_match_width)
                        .backgroundDimDefault()
                        .gravity(Gravity.BOTTOM)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_blur_bg:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_icon)
                        .backgroundBlurPercent(0.05f)
                        .backgroundColorInt(getResources().getColor(R.color.dialog_blur_bg))
                        .show();
                break;
            case R.id.tv_show_dark_bg:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_tran_bg:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_alpha_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomAlphaInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomAlphaOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createTopOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_alpha_in:
                AnyLayer.dialog(FullScreenActivity.this)
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
                        .show();
                break;
            case R.id.tv_show_top_bottom:
                AnyLayer.dialog(FullScreenActivity.this)
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
                        .show();
                break;
            case R.id.tv_show_bottom_top:
                AnyLayer.dialog(FullScreenActivity.this)
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
                        .show();
                break;
            case R.id.tv_show_top_bottom_alpha:
                AnyLayer.dialog(FullScreenActivity.this)
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
                        .show();
                break;
            case R.id.tv_show_bottom_top_alpha:
                AnyLayer.dialog(FullScreenActivity.this)
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
                        .show();
                break;
            case R.id.tv_show_left_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createLeftOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_left_alpha_in:
                AnyLayer.dialog(FullScreenActivity.this)
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
                        .show();
                break;
            case R.id.tv_show_right_in:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightOutAnim(content);
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_right_alpha_in:
                AnyLayer.dialog(FullScreenActivity.this)
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
                        .show();
                break;
            case R.id.tv_show_left_right:
                AnyLayer.dialog(FullScreenActivity.this)
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
                        .show();
                break;
            case R.id.tv_show_right_left:
                AnyLayer.dialog(FullScreenActivity.this)
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
                        .show();
                break;
            case R.id.tv_show_left_right_alpha:
                AnyLayer.dialog(FullScreenActivity.this)
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
                        .show();
                break;
            case R.id.tv_show_right_left_alpha:
                AnyLayer.dialog(FullScreenActivity.this)
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
                        .show();
                break;
            case R.id.tv_show_reveal:
                AnyLayer.dialog(FullScreenActivity.this)
                        .contentView(R.layout.dialog_normal)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    return AnimatorHelper.createCircularRevealInAnim(content, content.getMeasuredWidth() / 2, content.getMeasuredHeight() / 2);
                                }
                                return null;
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    return AnimatorHelper.createCircularRevealOutAnim(content, content.getMeasuredWidth() / 2, content.getMeasuredHeight() / 2);
                                }
                                return null;
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_menu:
                if (anyLayer_show_menu == null) {
                    anyLayer_show_menu = AnyLayer.popup(findViewById(R.id.tv_show_menu))
                            .align(Align.Direction.VERTICAL, Align.Horizontal.ALIGN_RIGHT, Align.Vertical.BELOW, true)
                            .offsetYdp(15)
                            .outsideTouchedToDismiss(true)
                            .outsideInterceptTouchEvent(false)
                            .contentView(R.layout.popup_meun)
                            .contentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createDelayedZoomInAnim(content, 1F, 0F);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createDelayedZoomOutAnim(content, 1F, 0F);
                                }
                            });
                }
                if (anyLayer_show_menu.isShow()) {
                    anyLayer_show_menu.dismiss();
                } else {
                    anyLayer_show_menu.show();
                }
                break;
        }
    }

    private void showMulti() {
        AnyLayer.dialog(FullScreenActivity.this)
                .contentView(R.layout.dialog_more)
                .backgroundDimDefault()
                .gravity(Gravity.CENTER)
                .cancelableOnTouchOutside(false)
                .cancelableOnClickKeyBack(false)
                .contentAnimator(new DialogLayer.AnimatorCreator() {
                    @Override
                    public Animator createInAnimator(@NonNull View content) {
                        return AnimatorHelper.createZoomAlphaInAnim(content);
                    }

                    @Override
                    public Animator createOutAnimator(@NonNull View content) {
                        return AnimatorHelper.createZoomAlphaOutAnim(content);
                    }
                })
                .onClick(new Layer.OnClickListener() {
                    @Override
                    public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                        anyLayer.dismiss();
                    }
                }, R.id.fl_dialog_no)
                .onClick(new Layer.OnClickListener() {
                    @Override
                    public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                        showMulti();
                    }
                }, R.id.fl_dialog_yes)
                .show();
    }
}

