package per.goweii.android.anylayer;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import per.goweii.anylayer.AnimatorHelper;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.DialogLayer;
import per.goweii.anylayer.DragLayout;

public class DragActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        initView();
    }

    private void initView() {
        findViewById(R.id.tv_show_left).setOnClickListener(this);
        findViewById(R.id.tv_show_right).setOnClickListener(this);
        findViewById(R.id.tv_show_top).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_show_left:
                AnyLayer.dialog(DragActivity.this)
                        .contentView(R.layout.dialog_drag_h)
                        .backgroundColorRes(R.color.dialog_bg)
                        .asStatusBar(R.id.dialog_drag_h_v)
                        .gravity(Gravity.LEFT)
                        .dragDismiss(DragLayout.DragStyle.Left)
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
                        .onClickToDismiss(R.id.dialog_drag_h_tv_close)
                        .show();
                break;
            case R.id.tv_show_right:
                AnyLayer.dialog(DragActivity.this)
                        .contentView(R.layout.dialog_drag_h)
                        .backgroundColorRes(R.color.dialog_bg)
                        .asStatusBar(R.id.dialog_drag_h_v)
                        .gravity(Gravity.RIGHT)
                        .dragDismiss(DragLayout.DragStyle.Right)
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
                        .onClickToDismiss(R.id.dialog_drag_h_tv_close)
                        .show();
                break;
            case R.id.tv_show_top:
                AnyLayer.dialog(DragActivity.this)
                        .contentView(R.layout.dialog_list)
                        .backgroundColorRes(R.color.dialog_bg)
                        .avoidStatusBar(true)
                        .gravity(Gravity.TOP)
                        .dragDismiss(DragLayout.DragStyle.Top)
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
            case R.id.tv_show_bottom:
                AnyLayer.dialog(DragActivity.this)
                        .contentView(R.layout.dialog_list)
                        .backgroundColorRes(R.color.dialog_bg)
                        .gravity(Gravity.BOTTOM)
                        .dragDismiss(DragLayout.DragStyle.Bottom)
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
        }
    }
}

