package per.goweii.anylayer;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author CuiZhen
 * @date 2019/7/20
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public abstract class Layer implements ViewManager.OnLifeListener, ViewManager.OnKeyListener, ViewManager.OnPreDrawListener {

    protected ViewManager mViewManager;

    protected boolean mCancelableOnClickKeyBack = true;

    private Animator mAnimatorIn = null;
    private Animator mAnimatorOut = null;

    public void show() {
        if (mViewManager == null) {
            ViewGroup parent = onGetParent();
            Context context = parent.getContext();
            View child = onCreateView(LayoutInflater.from(context), parent);
            mViewManager = new ViewManager(parent, child);
            mViewManager.setOnLifeListener(this);
            mViewManager.setOnPreDrawListener(this);
            mViewManager.setOnKeyListener(this);
        }
        mViewManager.attach();
    }

    public void dismiss() {
        onPerRemove();
    }

    @NonNull
    protected abstract ViewGroup onGetParent();

    @NonNull
    protected abstract View onCreateView(LayoutInflater inflater, ViewGroup parent);

    @Nullable
    protected abstract Animator onCreateViewInAnimator(@NonNull View view);

    @Nullable
    protected abstract Animator onCreateViewOutAnimator(@NonNull View view);

    @Override
    public void onAttach() {
    }

    @Override
    public void onPreDraw() {
        mAnimatorIn = onCreateViewInAnimator(mViewManager.getChild());
        if (mAnimatorIn != null) {
            mAnimatorIn.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    onShow();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            mAnimatorIn.start();
        } else {
            onShow();
        }
    }

    public void onShow() {
        if (mAnimatorIn != null) {
            mAnimatorIn = null;
        }
    }

    public void onPerRemove() {
        mAnimatorOut = onCreateViewOutAnimator(mViewManager.getChild());
        if (mAnimatorOut != null) {
            mAnimatorOut.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mViewManager.detach();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            mAnimatorOut.start();
        } else {
            mViewManager.detach();
        }
    }

    @Override
    public void onDetach() {
        if (mAnimatorOut != null) {
            mAnimatorOut = null;
        }
    }

    @Override
    public boolean onKey(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mCancelableOnClickKeyBack) {
                    dismiss();
                }
                return true;
            }
        }
        return false;
    }
}
