package com.example.jooff.shuyi.fragment.history;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.constant.TransSource;
import com.example.jooff.shuyi.data.AppDataRepository;
import com.example.jooff.shuyi.data.entity.History;
import com.example.jooff.shuyi.translate.main.MainTransView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jooff on 2017/1/14.
 * Tomorrow is a nice day
 */

public class HistoryFragment extends Fragment implements HistoryContract.View {

    private HistoryRvAdapter mAdapter;

    private HistoryContract.Presenter mPresenter;

    private Resources mResources;

    private Context mContext;

    @BindView(R.id.card_history)
    CardView cardHistory;

    @BindView(R.id.rec_history)
    RecyclerView recHistory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new HistoryPresenter(AppDataRepository.getInstance(getContext()), this);
        initView();
        return view;
    }

    @Override
    public void onStart() {
        mContext = this.getActivity();
        mResources = this.getResources();
        super.onStart();
    }

    @Override
    public void initView() {
        mAdapter = new HistoryRvAdapter(new ArrayList<>(0), mListener);
        recHistory.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recHistory.setLayoutManager(layoutManager);
        recHistory.setNestedScrollingEnabled(false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        recHistory.addItemDecoration(itemDecoration);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new HistoryRvCallback());
        mItemTouchHelper.attachToRecyclerView(recHistory);
        mPresenter.initTheme();
        mPresenter.loadData();
    }

    @Override
    public void showHistory(ArrayList<History> items) {
        mAdapter.refreshItems(items);
        cardHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTranslate(String original) {
        MainTransView fragment = MainTransView.newInstance(TransSource.FROM_HISTORY, original);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFrag, fragment)
                .commit();
    }

    @Override
    public void showHistoryDeleted() {
        Toast.makeText(this.getActivity(), R.string.delete_success, Toast.LENGTH_SHORT).show();
    }

    /**
     * 重写 RecyclerView 触摸帮助类，实现滑动删除
     */
    class HistoryRvCallback extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                    @NonNull ViewHolder viewHolder) {
            //设置滑动的方向
            int swipeFlags = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.RIGHT);
            return makeMovementFlags(0, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder,
                              @NonNull ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.remove(position);
            if (mAdapter.getItemCount() == 0) {
                cardHistory.setVisibility(View.GONE);
            }
            mPresenter.deleteHistoryItem(position);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                @NonNull ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //dX 大于 0 时向右滑动，小于 0 向左滑动
                View itemView = viewHolder.itemView;// 获取滑动的 view
                Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.ic_delete_sweep_black_24dp);// 获取删除指示的背景图片
                int padding = 10;// 图片绘制的 padding
                int maxDrawWidth = 2 * padding + bitmap.getWidth();// 最大的绘制宽度
                Paint paint = new Paint();
                paint.setColor(ContextCompat.getColor(mContext, R.color.background));
                int x = Math.round(Math.abs(dX));
                int drawWidth = Math.min(x, maxDrawWidth);// 实际的绘制宽度，取实时滑动距离 x 和最大绘制距离 maxDrawWidth 最小值
                int itemTop = itemView.getBottom() - itemView.getHeight();// 绘制的 top 位置
                // 向右滑动
                if (dX > 0) {
                    // 根据滑动实时绘制一个背景
                    c.drawRect(itemView.getLeft(), itemTop, drawWidth, itemView.getBottom(), paint);
                    // 在背景上面绘制图片
                    if (x > padding) {// 滑动距离大于 padding 时开始绘制图片
                        // 指定图片绘制的位置
                        Rect rect = new Rect();// 画图的位置
                        rect.left = itemView.getLeft() + padding;
                        rect.top = itemTop + (itemView.getBottom() - itemTop - bitmap.getHeight()) / 2;// 图片居中
                        int maxRight = rect.left + bitmap.getWidth();
                        rect.right = Math.min(x, maxRight);
                        rect.bottom = rect.top + bitmap.getHeight();
                        // 指定图片的绘制区域
                        Rect rect1 = null;
                        if (x < maxRight) {
                            rect1 = new Rect();// 不能再外面初始化，否则 dx 大于画图区域时，删除图片不显示
                            rect1.left = 0;
                            rect1.top = 0;
                            rect1.bottom = bitmap.getHeight();
                            rect1.right = x - padding;
                        }
                        c.drawBitmap(bitmap, rect1, rect, paint);
                    }
                    float alpha = 1.0f - Math.abs(dX) / (float) itemView.getWidth();
                    itemView.setAlpha(alpha);
                    // 绘制时需调用平移动画，否则滑动看不到反馈
                    itemView.setTranslationX(dX);
                } else {
                    // 如果在 getMovementFlags 指定了向左滑动（ItemTouchHelper。START）时则绘制工作可参考向右的滑动绘制，也可直接使用下面语句交由系统自己处理
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            } else {
                // 拖动时由系统自己完成
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    }

    /**
     * 重写 RecyclerView 适配器类
     */
    class HistoryRvAdapter extends RecyclerView.Adapter<HistoryRvAdapter.HistoryViewHolder> {

        private OnItemClickListener onItemClickListener;

        private ArrayList<History> items;

        @NonNull
        @Override
        public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View inflate = inflater.inflate(R.layout.m_item_history, parent, false);
            return new HistoryViewHolder(inflate);
        }

        public HistoryRvAdapter(ArrayList<History> items, OnItemClickListener listener) {
            this.items = items;
            onItemClickListener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull final HistoryViewHolder holder, int position) {
            History item = items.get(position);
            holder.textOriginal.setText(item.getOriginal());
            holder.textResult.setText(item.getResult());
            if (item.getCollected() == 0) {
                holder.collect.setTag(false);
                holder.collect.setImageResource(R.drawable.ic_star_border_black_24dp);
            } else {
                holder.collect.setImageResource(R.drawable.ic_star_yellow_800_24dp);
                holder.collect.setTag(true);
            }
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(view -> {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                });
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void remove(int position) {
            items.remove(position);
            notifyItemRemoved(position);

        }

        public void refreshItems(ArrayList<History> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        class HistoryViewHolder extends ViewHolder {

            @BindView(R.id.item_original)
            TextView textOriginal;

            @BindView(R.id.item_result)
            TextView textResult;

            @BindView(R.id.collect)
            ImageView collect;

            @OnClick(R.id.collect)
            public void show(ImageView collect) {
                if ((boolean) collect.getTag()) {
                    collect.setImageResource(R.drawable.ic_star_border_black_24dp);
                    collect.setTag(false);
                    mCollectListener.onCollectClickListener(collect, this.getLayoutPosition());
                    Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT).show();
                } else {
                    collect.setImageResource(R.drawable.ic_star_yellow_800_24dp);
                    collect.setTag(true);
                    mCollectListener.onCollectClickListener(collect, this.getLayoutPosition());
                    Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                }
            }

            HistoryViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface OnCollectClickListener {
        void onCollectClickListener(View view, int position);
    }

    OnItemClickListener mListener = new OnItemClickListener() {

        @Override
        public void onItemClick(View v, int position) {
            mPresenter.beginTranslate(position);
        }
    };

    OnCollectClickListener mCollectListener = new OnCollectClickListener() {

        @Override
        public void onCollectClickListener(View view, int position) {
            if ((boolean) view.getTag()) {
                mPresenter.collectHistory(position);
            } else {
                mPresenter.unCollectHistory(position);
            }
        }
    };

}
