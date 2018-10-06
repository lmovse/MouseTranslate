package com.example.jooff.shuyi.collect;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.data.AppDbRepository;
import com.example.jooff.shuyi.data.entity.Collect;
import com.example.jooff.shuyi.translate.main.MainTransView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jooff on 2017/1/14.
 * Tomorrow is a nice day
 */

public class CollectFragment extends Fragment implements CollectContract.View {
    private CollectRvAdapter mAdapter;
    private CollectContract.Presenter mPresenter;
    private Resources mResources;
    private Context mContext;

    @BindView(R.id.card_collect)
    CardView cardCollect;
    @BindView(R.id.rec_collect)
    RecyclerView recCollect;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new CollectPresenter(AppDbRepository.getInstance(getContext()), this);
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
        mAdapter = new CollectRvAdapter(new ArrayList<>(0), mListener);
        recCollect.setAdapter(mAdapter);
        recCollect.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recCollect.setNestedScrollingEnabled(false);
        recCollect.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new HistoryRvCallback());
        mItemTouchHelper.attachToRecyclerView(recCollect);
        mPresenter.initTheme();
        mPresenter.loadData();
    }

    @Override
    public void showCollects(ArrayList<Collect> items) {
        mAdapter.refreshItems(items);
        cardCollect.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTranslate(String original) {
        MainTransView fragment = MainTransView.newInstance(-1, original, original);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentFrag, fragment).commit();
    }

    @Override
    public void showCollectDeleted() {
        Toast.makeText(this.getActivity(), R.string.delete_success, Toast.LENGTH_SHORT).show();
    }

    /**
     * 重写 RecyclerView 触摸帮助类，实现滑动删除
     */
    class HistoryRvCallback extends ItemTouchHelper.Callback {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //设置滑动的方向
            int swipeFlags = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.RIGHT
            );
            return makeMovementFlags(0, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.remove(position);
            if (mAdapter.getItemCount() == 0) {
                cardCollect.setVisibility(View.GONE);
            }
            mPresenter.deleteCollect(position);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
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
                    // 如果在 getMovementFlags 指定了向左滑动（ItemTouchHelper。START）时则绘制工作可参考向右的滑动绘制，也可直接使用下面语句交友系统自己处理
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            } else {
                // 拖动时有系统自己完成
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    }

    /**
     * 重写 RecyclerView 适配器类
     */
    class CollectRvAdapter extends RecyclerView.Adapter<CollectRvAdapter.collectViewHolder> {
        private OnItemClickListener onItemClickListener;
        private ArrayList<Collect> items;

        @Override
        public CollectRvAdapter.collectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CollectRvAdapter.collectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.m_item_collect, parent, false));
        }

        public CollectRvAdapter(ArrayList<Collect> items, OnItemClickListener listener) {
            this.items = items;
            onItemClickListener = listener;
        }

        @Override
        public void onBindViewHolder(final CollectRvAdapter.collectViewHolder holder, int position) {
            Collect item = items.get(position);
            holder.textOriginal.setText(item.getOriginal());
            holder.textResult.setText(item.getResult());
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

        public void refreshItems(ArrayList<Collect> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        class collectViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_collect_original)
            TextView textOriginal;
            @BindView(R.id.item_collect_result)
            TextView textResult;

            collectViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    OnItemClickListener mListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            mPresenter.beginTranslate(position);
        }
    };

}
