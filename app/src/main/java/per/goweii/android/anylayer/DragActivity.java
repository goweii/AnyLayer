package per.goweii.android.anylayer;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.widget.SwipeLayout;

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
                        .backgroundDimDefault()
                        .asStatusBar(R.id.dialog_drag_h_v)
                        .gravity(Gravity.LEFT)
                        .swipeDismiss(SwipeLayout.Direction.LEFT)
                        .onClickToDismiss(R.id.dialog_drag_h_tv_close)
                        .show();
                break;
            case R.id.tv_show_right:
                AnyLayer.dialog(DragActivity.this)
                        .contentView(R.layout.dialog_drag_h)
                        .backgroundDimDefault()
                        .asStatusBar(R.id.dialog_drag_h_v)
                        .gravity(Gravity.RIGHT)
                        .swipeDismiss(SwipeLayout.Direction.RIGHT)
                        .onClickToDismiss(R.id.dialog_drag_h_tv_close)
                        .show();
                break;
            case R.id.tv_show_top:
                AnyLayer.dialog(DragActivity.this)
                        .contentView(R.layout.dialog_list)
                        .backgroundDimDefault()
                        .avoidStatusBar(true)
                        .gravity(Gravity.TOP)
                        .swipeDismiss(SwipeLayout.Direction.TOP)
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom:
                AnyLayer.dialog(DragActivity.this)
                        .contentView(R.layout.dialog_list)
                        .backgroundDimDefault()
                        .gravity(Gravity.BOTTOM)
                        .swipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .bindData(new Layer.DataBinder() {
                            @Override
                            public void bindData(@NonNull Layer layer) {
                                layer.getView(R.id.tv_dialog_title).setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        findViewById(R.id.tv_show_top).performClick();
                                        return false;
                                    }
                                });
                            }
                        })
                        .show();
                break;
        }
    }
}

