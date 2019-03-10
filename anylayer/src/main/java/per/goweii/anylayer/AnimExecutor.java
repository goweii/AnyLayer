package per.goweii.anylayer;

import android.animation.Animator;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/3/4
 */
final class AnimExecutor {
    private static final long DURATION_DEF = 300;

    private View mTarget = null;
    private Creator mCreator = null;
    private Animator mAnimator = null;
    private Creator mDefCreator = null;
    private Animation mAnimation = null;
    private long mDuration = DURATION_DEF;
    private Listener mListener = null;

    AnimExecutor() {
    }

    public void setTarget(View target, Creator defCreator) {
        mTarget = target;
        mDefCreator = defCreator;
    }

    public void setCreator(Creator creator) {
        mCreator = creator;
    }

    public void setAnimation(Animation animation) {
        mAnimation = animation;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void start() {
        if (mCreator != null) {
            mAnimator = mCreator.create(mTarget);
        }
        if (mAnimator != null) {
            startAnimator(mAnimator);
        } else {
            if (mAnimation != null) {
                startAnimation(mAnimation);
            } else {
                mAnimator = mDefCreator.create(mTarget);
                if (mAnimator != null) {
                    mAnimator.setDuration(mDuration);
                    startAnimator(mAnimator);
                } else {
                    if (mListener != null) {
                        mListener.onStart();
                    }
                    if (mListener != null) {
                        mListener.onEnd();
                    }
                }
            }
        }
    }

    public void cancel(){
        if (mAnimator != null) {
            mAnimator.cancel();
        } else if (mAnimation != null){
            mAnimation.cancel();
        }
    }

    private void startAnimator(Animator animator) {
        if (animator.getDuration() <= 0) {
            animator.setDuration(mDuration);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mListener != null) {
                    mListener.onStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (mListener != null) {
                    mListener.onEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    private void startAnimation(Animation animation) {
        if (animation.getDuration() <= 0) {
            animation.setDuration(mDuration);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (mListener != null) {
                    mListener.onStart();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mListener != null) {
                    mListener.onEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mTarget.startAnimation(animation);
    }


    public interface Creator {
        @Nullable
        Animator create(View target);
    }

    public interface Listener {
        void onStart();
        void onEnd();
    }

}
