package per.goweii.anylayer;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import per.goweii.burred.Blurred;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public final class LayerManager implements ViewManager.OnLifeListener, ViewManager.OnKeyListener, ViewManager.OnPreDrawListener {

    private final AnyLayer mAnyLayer;

    final Context mContext;
    final LayoutInflater mLayoutInflater;
    final ViewHolder mViewHolder;
    final ViewManager mViewManager;
    final AnimExecutor mContentInAnimExecutor;
    final AnimExecutor mBackgroundInAnimExecutor;
    final AnimExecutor mContentOutAnimExecutor;
    final AnimExecutor mBackgroundOutAnimExecutor;

    final Config mConfig;
    final ListenerHolder mListenerHolder;

    private boolean mInAnimRunning = false;
    private boolean mOutAnimRunning = false;
    private boolean mContentInAnimEnd = false;
    private boolean mBackgroundInAnimEnd = false;
    private boolean mContentOutAnimEnd = false;
    private boolean mBackgroundOutAnimEnd = false;

    LayerManager(AnyLayer anyLayer, Context context, ViewGroup rootView, View targetView, FrameLayout activityContentView) {
        mAnyLayer = anyLayer;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        FrameLayout container = (FrameLayout) mLayoutInflater.inflate(R.layout.layout_any_layer, rootView, false);
        mViewHolder = new ViewHolder(mAnyLayer, rootView, activityContentView, targetView, container);
        mViewManager = new ViewManager(rootView, mViewHolder.getContainer());
        mContentInAnimExecutor = new AnimExecutor();
        mBackgroundInAnimExecutor = new AnimExecutor();
        mContentOutAnimExecutor = new AnimExecutor();
        mBackgroundOutAnimExecutor = new AnimExecutor();
        mConfig = new Config();
        mListenerHolder = new ListenerHolder();
        init();
    }

    private void init() {
        mViewManager.setOnLifeListener(this);
        mViewManager.setOnPreDrawListener(this);
        mViewManager.setOnKeyListener(this);
        mContentInAnimExecutor.setListener(new AnimExecutor.Listener() {
            @Override
            public void onStart() {
                mContentInAnimEnd = false;
            }

            @Override
            public void onEnd() {
                mContentInAnimEnd = true;
                if (mBackgroundInAnimEnd) {
                    onShow();
                }
            }
        });
        mBackgroundInAnimExecutor.setListener(new AnimExecutor.Listener() {
            @Override
            public void onStart() {
                mBackgroundInAnimEnd = false;
            }

            @Override
            public void onEnd() {
                mBackgroundInAnimEnd = true;
                if (mContentInAnimEnd) {
                    onShow();
                }
            }
        });
        mContentOutAnimExecutor.setListener(new AnimExecutor.Listener() {
            @Override
            public void onStart() {
                mContentOutAnimEnd = false;
            }

            @Override
            public void onEnd() {
                mContentOutAnimEnd = true;
                if (mBackgroundOutAnimEnd) {
                    mViewManager.detach();
                }
            }
        });
        mBackgroundOutAnimExecutor.setListener(new AnimExecutor.Listener() {
            @Override
            public void onStart() {
                mBackgroundOutAnimEnd = false;
            }

            @Override
            public void onEnd() {
                mBackgroundOutAnimEnd = true;
                if (mContentOutAnimEnd) {
                    mViewManager.detach();
                }
            }
        });
    }

    /**
     * 显示
     */
    public void show() {
        mViewManager.attach();
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        onPerRemove();
    }

    @Override
    public void onAttach() {
        initContainer();
        initBackground();
        initContent();
        mViewHolder.bindListener();
        mContentInAnimExecutor.setTarget(mViewHolder.getContent(), new AnimExecutor.Creator() {
            @Nullable
            @Override
            public Animator create(View target) {
                return AnimHelper.createZoomAlphaInAnim(target);
            }
        });
        mContentOutAnimExecutor.setTarget(mViewHolder.getContent(), new AnimExecutor.Creator() {
            @Nullable
            @Override
            public Animator create(View target) {
                return AnimHelper.createZoomAlphaOutAnim(target);
            }
        });
        mBackgroundInAnimExecutor.setTarget(mViewHolder.getBackground(), new AnimExecutor.Creator() {
            @Nullable
            @Override
            public Animator create(View target) {
                return AnimHelper.createAlphaInAnim(target);
            }
        });
        mBackgroundOutAnimExecutor.setTarget(mViewHolder.getBackground(), new AnimExecutor.Creator() {
            @Nullable
            @Override
            public Animator create(View target) {
                return AnimHelper.createAlphaOutAnim(target);
            }
        });
        mListenerHolder.notifyVisibleChangeOnShow(mAnyLayer);
        mListenerHolder.notifyDataBinder(mAnyLayer);
    }

    @Override
    public void onPreDraw() {
        if (mInAnimRunning) {
            return;
        }
        mInAnimRunning = true;
        mListenerHolder.notifyLayerOnShowing(mAnyLayer);
        mContentInAnimExecutor.start();
        mBackgroundInAnimExecutor.start();
    }

    public void onShow() {
        mInAnimRunning = false;
        mListenerHolder.notifyLayerOnShown(mAnyLayer);
    }

    public void onPerRemove() {
        if (mOutAnimRunning) {
            return;
        }
        mOutAnimRunning = true;
        mListenerHolder.notifyLayerOnDismissing(mAnyLayer);
        mContentInAnimExecutor.cancel();
        mBackgroundInAnimExecutor.cancel();
        mContentOutAnimExecutor.start();
        mBackgroundOutAnimExecutor.start();
    }

    @Override
    public void onDetach() {
        mOutAnimRunning = false;
        mListenerHolder.notifyVisibleChangeOnDismiss(mAnyLayer);
        mListenerHolder.notifyLayerOnDismissed(mAnyLayer);
        mViewHolder.recycle();
    }

    @Override
    public boolean onKey(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mConfig.mCancelableOnClickKeyBack) {
                    dismiss();
                }
                return true;
            }
        }
        return false;
    }

    private void initContainer() {
        if (mConfig.mOutsideInterceptTouchEvent) {
            mViewHolder.getContainer().setClickable(true);
            if (mConfig.mCancelableOnTouchOutside) {
                mViewHolder.getContainer().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        } else {
            mViewHolder.getContainer().setClickable(false);
        }
        if (mViewHolder.getActivityContentView() != null) {
            // 非指定父布局的，添加到DecorView，此时mViewHolder.getRootView()为DecorView
            final int[] locationDecor = new int[2];
            mViewHolder.getRootView().getLocationOnScreen(locationDecor);
            final int[] locationActivityContent = new int[2];
            mViewHolder.getActivityContentView().getLocationOnScreen(locationActivityContent);
            FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) mViewHolder.getContainer().getLayoutParams();
            containerParams.leftMargin = locationActivityContent[0] - locationDecor[0];
            containerParams.topMargin = 0;
            containerParams.width = mViewHolder.getActivityContentView().getWidth();
            containerParams.height = mViewHolder.getActivityContentView().getHeight() + (locationActivityContent[1] - locationDecor[1]);
            mViewHolder.getContainer().setLayoutParams(containerParams);
        }
        if (mViewHolder.getTargetView() == null) {
            FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) mViewHolder.getContentWrapper().getLayoutParams();
            contentWrapperParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
            contentWrapperParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
            mViewHolder.getContentWrapper().setLayoutParams(contentWrapperParams);
        } else {
            initContainerWithTarget();
        }
    }

    private void initContainerWithTarget() {
        mViewHolder.getContainer().setClipToPadding(false);
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) mViewHolder.getContentWrapper().getLayoutParams();
        contentWrapperParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        contentWrapperParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        mViewHolder.getContentWrapper().setLayoutParams(contentWrapperParams);
        final int[] locationTarget = new int[2];
        mViewHolder.getTargetView().getLocationOnScreen(locationTarget);
        final int[] locationRoot = new int[2];
        mViewHolder.getRootView().getLocationOnScreen(locationRoot);
        final int targetX = (locationTarget[0] - locationRoot[0]);
        final int targetY = (locationTarget[1] - locationRoot[1]);
        final int targetWidth = mViewHolder.getTargetView().getWidth();
        final int targetHeight = mViewHolder.getTargetView().getHeight();
        final int[] padding = initContainerWithTargetPadding(targetX, targetY, targetWidth, targetHeight);
        mViewHolder.getContainer().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (mViewHolder.getContainer().getViewTreeObserver().isAlive()) {
                    mViewHolder.getContainer().getViewTreeObserver().removeOnPreDrawListener(this);
                }
                initContainerWithTargetMargin(targetX, targetY, targetWidth, targetHeight, padding[0], padding[1]);
                return false;
            }
        });
        if (!mConfig.mOutsideInterceptTouchEvent) {
            mViewHolder.getContainer().getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    final int[] locationTarget = new int[2];
                    mViewHolder.getTargetView().getLocationOnScreen(locationTarget);
                    final int[] locationRoot = new int[2];
                    mViewHolder.getRootView().getLocationOnScreen(locationRoot);
                    final int targetX = (locationTarget[0] - locationRoot[0]);
                    final int targetY = (locationTarget[1] - locationRoot[1]);
                    final int targetWidth = mViewHolder.getTargetView().getWidth();
                    final int targetHeight = mViewHolder.getTargetView().getHeight();
                    final int[] padding = initContainerWithTargetPadding(targetX, targetY, targetWidth, targetHeight);
                    initContainerWithTargetMargin(targetX, targetY, targetWidth, targetHeight, padding[0], padding[1]);
                }
            });
        }
    }

    private int[] initContainerWithTargetPadding(int targetX, int targetY, int targetWidth, int targetHeight) {
        final int[] padding = new int[]{0, 0, 0, 0};
        FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) mViewHolder.getContainer().getLayoutParams();
        if (mConfig.mAlignmentDirection == Alignment.Direction.HORIZONTAL) {
            if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
                padding[2] = containerParams.width - targetX;
            } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
                padding[0] = targetX + targetWidth;
            } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
                padding[0] = targetX;
            } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
                padding[2] = containerParams.width - targetX - targetWidth;
            }
        } else if (mConfig.mAlignmentDirection == Alignment.Direction.VERTICAL) {
            if (mConfig.mAlignmentVertical == Alignment.Vertical.ABOVE) {
                padding[3] = containerParams.height - targetY;
            } else if (mConfig.mAlignmentVertical == Alignment.Vertical.BELOW) {
                padding[1] = targetY + targetHeight;
            } else if (mConfig.mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
                padding[1] = targetY;
            } else if (mConfig.mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
                padding[3] = containerParams.height - targetY - targetHeight;
            }
        }
        mViewHolder.getContainer().setPadding(padding[0], padding[1], padding[2], padding[3]);
        return padding;
    }

    private void initContainerWithTargetMargin(int targetX, int targetY, int targetWidth, int targetHeight, int paddingLeft, int paddingTop) {
        final int width = mViewHolder.getContentWrapper().getWidth();
        final int height = mViewHolder.getContentWrapper().getHeight();
        int x = 0;
        int y = 0;
        if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.CENTER) {
            x = targetX - (width - targetWidth) / 2;
        } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
            x = targetX - width;
        } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
            x = targetX + targetWidth;
        } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
            x = targetX;
        } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
            x = targetX - (width - targetWidth);
        }
        if (mConfig.mAlignmentVertical == Alignment.Vertical.CENTER) {
            y = targetY - (height - targetHeight) / 2;
        } else if (mConfig.mAlignmentVertical == Alignment.Vertical.ABOVE) {
            y = targetY - height;
        } else if (mConfig.mAlignmentVertical == Alignment.Vertical.BELOW) {
            y = targetY + targetHeight;
        } else if (mConfig.mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
            y = targetY;
        } else if (mConfig.mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
            y = targetY - (height - targetHeight);
        }
        x = x - paddingLeft;
        y = y - paddingTop;
        if (mConfig.mAlignmentInside) {
            final int maxWidth = mViewHolder.getContainer().getWidth() - mViewHolder.getContainer().getPaddingLeft() - mViewHolder.getContainer().getPaddingRight();
            final int maxHeight = mViewHolder.getContainer().getHeight() - mViewHolder.getContainer().getPaddingTop() - mViewHolder.getContainer().getPaddingBottom();
            final int maxX = maxWidth - width;
            final int maxY = maxHeight - height;
            if (x < 0) {
                x = 0;
            } else if (x > maxX) {
                x = maxX;
            }
            if (y < 0) {
                y = 0;
            } else if (y > maxY) {
                y = maxY;
            }
        }
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) mViewHolder.getContentWrapper().getLayoutParams();
        contentWrapperParams.leftMargin = x;
        contentWrapperParams.topMargin = y;
        mViewHolder.getContentWrapper().setLayoutParams(contentWrapperParams);
    }

    private void initBackground() {
        if (mConfig.mBackgroundBlurPercent > 0 || mConfig.mBackgroundBlurRadius > 0) {
            mViewHolder.getBackground().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mViewHolder.getBackground().getViewTreeObserver().removeOnPreDrawListener(this);
                    Bitmap snapshot = Utils.snapshot(mViewHolder.getRootView());
                    int[] locationRootView = new int[2];
                    mViewHolder.getRootView().getLocationOnScreen(locationRootView);
                    int[] locationBackground = new int[2];
                    mViewHolder.getBackground().getLocationOnScreen(locationBackground);
                    int x = locationBackground[0] - locationRootView[0];
                    int y = locationBackground[1] - locationRootView[1];
                    Bitmap original = Bitmap.createBitmap(snapshot, x, y, mViewHolder.getBackground().getWidth(), mViewHolder.getBackground().getHeight());
                    snapshot.recycle();
                    Blurred.init(mContext);
                    Blurred blurred = Blurred.with(original)
                            .recycleOriginal(true)
                            .keepSize(false)
                            .scale(mConfig.mBackgroundBlurScale);
                    if (mConfig.mBackgroundBlurPercent > 0) {
                        blurred.percent(mConfig.mBackgroundBlurPercent);
                    } else if (mConfig.mBackgroundBlurRadius > 0) {
                        blurred.radius(mConfig.mBackgroundBlurRadius);
                    }
                    Bitmap blurBitmap = blurred.blur();
                    mViewHolder.getBackground().setScaleType(ImageView.ScaleType.CENTER_CROP);
                    mViewHolder.getBackground().setImageBitmap(blurBitmap);
                    mViewHolder.getBackground().setColorFilter(mConfig.mBackgroundColor);
                    return true;
                }
            });
        } else {
            if (mConfig.mBackgroundBitmap != null) {
                mViewHolder.getBackground().setImageBitmap(mConfig.mBackgroundBitmap);
                mViewHolder.getBackground().setColorFilter(mConfig.mBackgroundColor);
            } else if (mConfig.mBackgroundResource != -1) {
                mViewHolder.getBackground().setImageResource(mConfig.mBackgroundResource);
                mViewHolder.getBackground().setColorFilter(mConfig.mBackgroundColor);
            } else if (mConfig.mBackgroundDrawable != null) {
                mViewHolder.getBackground().setImageDrawable(mConfig.mBackgroundDrawable);
                mViewHolder.getBackground().setColorFilter(mConfig.mBackgroundColor);
            } else {
                mViewHolder.getBackground().setImageDrawable(new ColorDrawable(mConfig.mBackgroundColor));
            }
        }
    }

    private void initContent() {
        if (mViewHolder.getContent() != null) {
            ViewGroup contentParent = (ViewGroup) mViewHolder.getContent().getParent();
            if (contentParent != null) {
                contentParent.removeView(mViewHolder.getContent());
            }
            mViewHolder.getContent().setClickable(true);
            if (mViewHolder.getTargetView() == null && mConfig.mGravity != -1) {
                ViewGroup.LayoutParams params = mViewHolder.getContent().getLayoutParams();
                FrameLayout.LayoutParams contentParams;
                if (params == null) {
                    contentParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                } else if (params instanceof FrameLayout.LayoutParams) {
                    contentParams = (FrameLayout.LayoutParams) params;
                } else {
                    contentParams = new FrameLayout.LayoutParams(params.width, params.height);
                }
                contentParams.gravity = mConfig.mGravity;
                mViewHolder.getContent().setLayoutParams(contentParams);
            }
            if (mConfig.mAsStatusBarViewId > 0) {
                View statusBar = mViewHolder.getContent().findViewById(mConfig.mAsStatusBarViewId);
                if (statusBar != null) {
                    ViewGroup.LayoutParams params = statusBar.getLayoutParams();
                    params.height = Utils.getStatusBarHeight(mContext);
                    statusBar.setLayoutParams(params);
                    statusBar.setVisibility(View.VISIBLE);
                }
            }
            mViewHolder.getContentWrapper().addView(mViewHolder.getContent());
        }
    }

    public interface IAnim {
        /**
         * 内容进入动画
         *
         * @param target 内容
         */
        Animator inAnim(View target);

        /**
         * 内容消失动画
         *
         * @param target 内容
         */
        Animator outAnim(View target);
    }

    public interface IDataBinder {
        /**
         * 绑定数据
         *
         * @param anyLayer AnyLayer
         */
        void bind(AnyLayer anyLayer);
    }

    public interface OnLayerClickListener {
        /**
         * 点击事件回调
         *
         * @param anyLayer 浮层
         * @param v        点击控件
         */
        void onClick(AnyLayer anyLayer, View v);
    }

    public interface OnLayerDismissListener {
        /**
         * 开始隐藏，动画刚开始执行
         *
         * @param anyLayer 浮层
         */
        void onDismissing(AnyLayer anyLayer);

        /**
         * 已隐藏，浮层已被移除
         *
         * @param anyLayer 浮层
         */
        void onDismissed(AnyLayer anyLayer);
    }

    public interface OnLayerShowListener {
        /**
         * 开始显示，动画刚开始执行
         *
         * @param anyLayer 浮层
         */
        void onShowing(AnyLayer anyLayer);

        /**
         * 已显示，浮层已显示且动画结束
         *
         * @param anyLayer 浮层
         */
        void onShown(AnyLayer anyLayer);
    }

    public interface OnVisibleChangeListener {
        /**
         * 浮层显示，刚被添加到父布局，进入动画未开始
         *
         * @param anyLayer 浮层
         */
        void onShow(AnyLayer anyLayer);

        /**
         * 浮层隐藏，已被从父布局移除，隐藏动画已结束
         *
         * @param anyLayer 浮层
         */
        void onDismiss(AnyLayer anyLayer);
    }
}
