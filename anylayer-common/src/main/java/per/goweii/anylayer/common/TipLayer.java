package per.goweii.anylayer.common;

import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.BaseLayer;
import per.goweii.anylayer.LayerManager;

/**
 * @author Goweii
 * @date 2019/5/29
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class TipLayer extends BaseLayer {

    private int mTitleId;
    private int mMsgId;
    private int mYesTextId;
    private int mNoTextId;
    private CharSequence mTitle;
    private CharSequence mMsg;
    private CharSequence mYesText;
    private CharSequence mNoText;

    private boolean mSingleBtnYes = false;
    private boolean mCancelable = true;

    private OnYesClickListener mOnYesClickListener = null;
    private OnNoClickListener mOnNoClickListener = null;

    public static TipLayer make() {
        return new TipLayer();
    }

    public TipLayer() {
    }

    public TipLayer yesText(CharSequence yesText) {
        this.mYesText = yesText;
        return this;
    }

    public TipLayer yesText(@StringRes int yesText) {
        this.mYesTextId = yesText;
        return this;
    }

    public TipLayer noText(CharSequence noText) {
        this.mNoText = noText;
        return this;
    }

    public TipLayer noText(@StringRes int noText) {
        this.mNoTextId = noText;
        return this;
    }

    public TipLayer title(CharSequence title) {
        this.mTitle = title;
        return this;
    }

    public TipLayer title(@StringRes int title) {
        this.mTitleId = title;
        return this;
    }

    public TipLayer message(CharSequence msg) {
        this.mMsg = msg;
        return this;
    }

    public TipLayer message(@StringRes int msg) {
        this.mMsgId = msg;
        return this;
    }

    public TipLayer singleYesBtn() {
        mSingleBtnYes = true;
        return this;
    }

    public TipLayer cancelable(boolean cancelable) {
        this.mCancelable = cancelable;
        return this;
    }

    public TipLayer onYes(OnYesClickListener listener) {
        mOnYesClickListener = listener;
        return this;
    }

    public TipLayer onNo(OnNoClickListener listener) {
        mOnNoClickListener = listener;
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.anylayer_common_tip;
    }

    @Override
    protected void onCreateLayer() {
        super.onCreateLayer();
        mAnyLayer.backgroundColorRes(R.color.anylayer_common_bg);
        mAnyLayer.cancelableOnTouchOutside(mCancelable);
        mAnyLayer.cancelableOnClickKeyBack(mCancelable);
        mAnyLayer.gravity(Gravity.CENTER);
        mAnyLayer.onClickToDismiss(new LayerManager.OnLayerClickListener() {
            @Override
            public void onClick(AnyLayer anyLayer, View v) {
                if (mOnYesClickListener != null) {
                    mOnYesClickListener.onYes();
                }
            }
        }, R.id.anylayer_common_tip_tv_yes);
        mAnyLayer.onClickToDismiss(new LayerManager.OnLayerClickListener() {
            @Override
            public void onClick(AnyLayer anyLayer, View v) {
                if (mOnNoClickListener != null) {
                    mOnNoClickListener.onNo();
                }
            }
        }, R.id.anylayer_common_tip_tv_no);
    }

    @Override
    public void onLayerCreated() {
        TextView tvTitle = mAnyLayer.getView(R.id.anylayer_common_tip_tv_title);
        TextView tvContent = mAnyLayer.getView(R.id.anylayer_common_tip_tv_content);
        TextView tvYes = mAnyLayer.getView(R.id.anylayer_common_tip_tv_yes);
        TextView tvNo = mAnyLayer.getView(R.id.anylayer_common_tip_tv_no);
        View vLine = mAnyLayer.getView(R.id.anylayer_common_tip_v_line);

        if (mTitle == null && mTitleId > 0) {
            mTitle = tvTitle.getContext().getString(mTitleId);
        }
        if (mTitle == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mTitle);
        }
        if (mMsg == null && mMsgId > 0) {
            mMsg = tvTitle.getContext().getString(mMsgId);
        }
        if (mMsg != null) {
            tvContent.setText(mMsg);
        }

        if (mSingleBtnYes) {
            tvNo.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
        } else {
            tvNo.setVisibility(View.VISIBLE);
            vLine.setVisibility(View.VISIBLE);
            if (mNoText == null && mNoTextId > 0) {
                mNoText = tvTitle.getContext().getString(mNoTextId);
            }
            if (mNoText != null) {
                tvNo.setText(mNoText);
            } else {
                tvNo.setText(R.string.anylayer_common_btn_no);
            }
        }
        if (mYesText == null && mYesTextId > 0) {
            mYesText = tvTitle.getContext().getString(mYesTextId);
        }
        if (mYesText != null) {
            tvYes.setText(mYesText);
        } else {
            tvYes.setText(R.string.anylayer_common_btn_yes);
        }
    }

    public interface OnYesClickListener {
        void onYes();
    }

    public interface OnNoClickListener {
        void onNo();
    }
}
