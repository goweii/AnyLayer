package per.goweii.anylayer.common;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import per.goweii.anylayer.AnimHelper;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.LayerManager;

/**
 * @author CuiZhen
 * @date 2019/5/29
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class LoadingLayer {

    private static final long ANIM_DURATION = 200;
    private final Context context;
    private AnyLayer mAnyLayer;
    private int count = 0;

    private String mText = null;
    private int mTextId = -1;

    private LoadingLayer(Context context) {
        this.context = context;
    }

    public static LoadingLayer with(Context context) {
        return new LoadingLayer(context);
    }

    public LoadingLayer text(String text) {
        mText = text;
        return this;
    }

    public LoadingLayer textId(int text) {
        mTextId = text;
        return this;
    }

    public void show() {
        if (count <= 0) {
            count = 0;
            mAnyLayer = AnyLayer.with(context)
                    .contentView(R.layout.anylayer_common_loading)
                    .backgroundColorInt(Color.TRANSPARENT)
                    .cancelableOnClickKeyBack(false)
                    .cancelableOnTouchOutside(false)
                    .gravity(Gravity.CENTER)
                    .bindData(new LayerManager.IDataBinder() {
                        @Override
                        public void bind(AnyLayer anyLayer) {
                            TextView tv = anyLayer.getView(R.id.anylayer_common_loading_tv);
                            if (mText == null && mTextId > 0) {
                                mText = tv.getContext().getString(mTextId);
                            }
                            if (mText == null) {
                                tv.setVisibility(View.GONE);
                            } else {
                                tv.setVisibility(View.VISIBLE);
                                tv.setText(mText);
                            }
                        }
                    })
                    .contentAnim(new LayerManager.IAnim() {
                        @Override
                        public Animator inAnim(View target) {
                            return AnimHelper.createZoomInAnim(target).setDuration(ANIM_DURATION);
                        }

                        @Override
                        public Animator outAnim(View target) {
                            return AnimHelper.createZoomOutAnim(target).setDuration(ANIM_DURATION);
                        }
                    });
            mAnyLayer.show();
        }
        count++;
    }

    public void dismiss() {
        count--;
        if (count <= 0) {
            clear();
        }
    }

    public void clear() {
        if (mAnyLayer != null) {
            mAnyLayer.dismiss();
            mAnyLayer = null;
        }
        count = 0;
    }
}
