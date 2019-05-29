package per.goweii.anylayer.common;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class ListLayer extends BaseLayer {

    private int mTitleId = -1;
    private int mYesTextId = -1;
    private int mNoTextId = -1;
    private CharSequence mTitle = null;
    private CharSequence mYesText = null;
    private CharSequence mNoText = null;

    private int mSelectedItemColorInt = -1;
    private int mSelectedItemColorRes = -1;

    private boolean noBtn = false;
    private boolean singleBtnYes = false;
    private boolean cancelable = true;

    private OnItemSelectedListener listener = null;

    private List<String> mDatas = new ArrayList<>();
    private int currSelectPos = -1;

    public static ListLayer with() {
        return new ListLayer();
    }

    public ListLayer() {
    }

    public ListLayer title(CharSequence title) {
        this.mTitle = title;
        return this;
    }

    public ListLayer title(@StringRes int title) {
        this.mTitleId = title;
        return this;
    }

    public ListLayer yesText(CharSequence yesText) {
        this.mYesText = yesText;
        return this;
    }

    public ListLayer yesText(@StringRes int yesText) {
        this.mYesTextId = yesText;
        return this;
    }

    public ListLayer noText(CharSequence noText) {
        this.mNoText = noText;
        return this;
    }

    public ListLayer noText(@StringRes int noText) {
        this.mNoTextId = noText;
        return this;
    }

    public ListLayer selectedItemColorInt(@ColorInt int color) {
        this.mSelectedItemColorInt = color;
        return this;
    }

    public ListLayer selectedItemColorRes(@ColorRes int color) {
        this.mSelectedItemColorRes = color;
        return this;
    }

    public ListLayer noBtn() {
        noBtn = true;
        return this;
    }

    public ListLayer singleYesBtn() {
        singleBtnYes = true;
        return this;
    }

    public ListLayer cancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public ListLayer datas(List<String> datas) {
        this.mDatas.addAll(datas);
        return this;
    }

    public ListLayer datas(String... datas) {
        return datas(Arrays.asList(datas));
    }

    public ListLayer currSelectPos(int currSelectPos) {
        this.currSelectPos = currSelectPos;
        return this;
    }

    public ListLayer listener(OnItemSelectedListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.anylayer_common_list;
    }

    @Override
    protected void onCreateLayer() {
        super.onCreateLayer();
        mAnyLayer.backgroundColorRes(R.color.anylayer_common_bg);
        mAnyLayer.cancelableOnTouchOutside(cancelable);
        mAnyLayer.cancelableOnClickKeyBack(cancelable);
        mAnyLayer.gravity(Gravity.CENTER);
        mAnyLayer.onClickToDismiss(new LayerManager.OnLayerClickListener() {
            @Override
            public void onClick(AnyLayer anyLayer, View v) {
                if (listener != null) {
                    listener.onSelect(mDatas.get(currSelectPos), currSelectPos);
                }
            }
        }, R.id.anylayer_common_list_tv_yes);
        mAnyLayer.onClickToDismiss(new LayerManager.OnLayerClickListener() {
            @Override
            public void onClick(AnyLayer anyLayer, View v) {
            }
        }, R.id.anylayer_common_list_tv_no);
    }

    @Override
    protected void onLayerCreated() {
        TextView tvTitle = mAnyLayer.getView(R.id.anylayer_common_list_tv_title);
        RecyclerView rv = mAnyLayer.getView(R.id.anylayer_common_list_rv);
        LinearLayout llYesNo = mAnyLayer.getView(R.id.anylayer_common_list_ll_yes_no);
        View vLineH = mAnyLayer.getView(R.id.anylayer_common_list_v_line_h);

        if (noBtn) {
            vLineH.setVisibility(View.GONE);
            llYesNo.setVisibility(View.GONE);
        } else {
            vLineH.setVisibility(View.VISIBLE);
            llYesNo.setVisibility(View.VISIBLE);

            TextView tvYes = mAnyLayer.getView(R.id.anylayer_common_list_tv_yes);
            TextView tvNo = mAnyLayer.getView(R.id.anylayer_common_list_tv_no);
            View vLine = mAnyLayer.getView(R.id.anylayer_common_list_v_line);

            if (mYesText == null && mYesTextId > 0) {
                mYesText = tvTitle.getContext().getString(mYesTextId);
            }
            if (mYesText != null) {
                tvYes.setText(mYesText);
            } else {
                tvYes.setText(R.string.anylayer_common_btn_yes);
            }
            if (singleBtnYes) {
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
        }

        if (mTitle == null && mTitleId > 0) {
            mTitle = tvTitle.getContext().getString(mTitleId);
        }
        if (mTitle == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mTitle);
        }

        int normalColor = ContextCompat.getColor(rv.getContext(), R.color.anylayer_common_text_title);
        int selectedColr = normalColor;
        if (mSelectedItemColorInt >= 0) {
            selectedColr = mSelectedItemColorInt;
        } else if (mSelectedItemColorRes > 0){
            selectedColr = ContextCompat.getColor(rv.getContext(), mSelectedItemColorRes);
        }

        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        final ListAdapter adapter = new ListAdapter(mDatas, currSelectPos, normalColor, selectedColr, new ListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                currSelectPos = pos;
                if (noBtn) {
                    if (listener != null) {
                        listener.onSelect(mDatas.get(currSelectPos), currSelectPos);
                    }
                    dismiss();
                }
            }
        });
        rv.setAdapter(adapter);
    }

    static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

        private List<String> mDatas;
        private int mNormalColor;
        private int mSelectedColor;
        private int mCurrPos;
        private OnItemClickListener mListener;

        ListAdapter(List<String> datas, int currPos, int normalColor, int selectedColor, OnItemClickListener listener) {
            mDatas = datas;
            mCurrPos = currPos;
            mNormalColor = normalColor;
            mSelectedColor = selectedColor;
            mListener = listener;
        }

        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ListHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.anylayer_common_list_rv_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder listHolder, int i) {
            if (listHolder.getAdapterPosition() == mCurrPos) {
                listHolder.tvName.setTextColor(mSelectedColor);
            } else {
                listHolder.tvName.setTextColor(mNormalColor);
            }
            listHolder.tvName.setText(mDatas.get(i));
        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        class ListHolder extends RecyclerView.ViewHolder {

            TextView tvName;

            ListHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.anylayer_common_list_tv_name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrPos = getAdapterPosition();
                        notifyDataSetChanged();
                        mListener.onClick(mCurrPos);
                    }
                });
            }
        }

        interface OnItemClickListener {
            void onClick(int pos);
        }
    }

    public interface OnItemSelectedListener {
        void onSelect(String data, int pos);
    }
}
