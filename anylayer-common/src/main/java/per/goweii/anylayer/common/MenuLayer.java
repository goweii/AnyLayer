package per.goweii.anylayer.common;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import per.goweii.anylayer.Alignment;
import per.goweii.anylayer.AnimHelper;
import per.goweii.anylayer.BaseLayer;
import per.goweii.anylayer.LayerManager;

/**
 * @author CuiZhen
 * @date 2019/5/29
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class MenuLayer extends BaseLayer {

    private final View mTargetView;
    private List<String> mDatas = new ArrayList<>();
    private int[] mIcons = null;
    private OnMenuClickListener mListener = null;

    public static MenuLayer target(@NonNull View targetView) {
        return new MenuLayer(targetView);
    }

    private MenuLayer(@NonNull View targetView) {
        mTargetView = targetView;
    }

    @Override
    @Nullable
    protected View getTarget() {
        return mTargetView;
    }

    public MenuLayer datas(List<String> datas) {
        this.mDatas.addAll(datas);
        return this;
    }

    public MenuLayer datas(String... datas) {
        return datas(Arrays.asList(datas));
    }

    public MenuLayer icons(int... icons) {
        mIcons = icons;
        return this;
    }

    public MenuLayer listener(OnMenuClickListener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.anylayer_common_menu;
    }

    @Override
    protected void onCreateLayer() {
        super.onCreateLayer();
        mAnyLayer.backgroundColorRes(R.color.anylayer_common_bg);
        mAnyLayer.alignment(Alignment.Direction.VERTICAL,
                Alignment.Horizontal.ALIGN_RIGHT,
                Alignment.Vertical.BELOW,
                true);
        mAnyLayer.contentAnim(new LayerManager.IAnim() {
            @Override
            public Animator inAnim(View target) {
                return AnimHelper.createDelayedZoomInAnim(target, 1F, 0F);
            }

            @Override
            public Animator outAnim(View target) {
                return AnimHelper.createDelayedZoomOutAnim(target, 1F, 0F);
            }
        });
    }

    @Override
    protected void onLayerCreated() {
        RecyclerView rv = mAnyLayer.getView(R.id.anylayer_common_menu_rv);
        if (mIcons == null) {
            rv.setMinimumWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, rv.getContext().getResources().getDisplayMetrics()));
        } else {
            rv.setMinimumWidth(0);
        }

        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        final MenuAdapter adapter = new MenuAdapter(mDatas, mIcons, new MenuAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                if (mListener != null) {
                    mListener.onClick(mDatas.get(pos), pos);
                }
                dismiss();
            }
        });
        rv.setAdapter(adapter);
    }

    static class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

        private List<String> mDatas;
        private int[] mIcons = null;
        private OnItemClickListener mListener;

        MenuAdapter(List<String> datas, int[] icons, OnItemClickListener listener) {
            mDatas = datas;
            mIcons = icons;
            mListener = listener;
        }

        @NonNull
        @Override
        public MenuHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MenuHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.anylayer_common_menu_rv_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MenuHolder menuHolder, int i) {
            menuHolder.tvName.setText(mDatas.get(i));
            if (mIcons == null) {
                menuHolder.ivIcon.setVisibility(View.GONE);
                menuHolder.tvName.setGravity(Gravity.CENTER);
            } else {
                if (i < mIcons.length) {
                    menuHolder.ivIcon.setVisibility(View.VISIBLE);
                    menuHolder.ivIcon.setImageResource(mIcons[i]);
                } else {
                    menuHolder.ivIcon.setVisibility(View.INVISIBLE);
                }
                menuHolder.tvName.setGravity(Gravity.LEFT);
            }
        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        class MenuHolder extends RecyclerView.ViewHolder {

            TextView tvName;
            ImageView ivIcon;

            MenuHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.anylayer_common_menu_tv_name);
                ivIcon = itemView.findViewById(R.id.anylayer_common_menu_iv_icon);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onClick(getAdapterPosition());
                    }
                });
            }
        }

        interface OnItemClickListener {
            void onClick(int pos);
        }
    }

    public interface OnMenuClickListener {
        void onClick(String data, int pos);
    }

}
