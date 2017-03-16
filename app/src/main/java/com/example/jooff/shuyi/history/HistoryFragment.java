package com.example.jooff.shuyi.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.common.OnAppStatusListener;
import com.example.jooff.shuyi.data.AppDbRepository;
import com.example.jooff.shuyi.data.bean.HistoryBean;
import com.example.jooff.shuyi.translate.main.MainTranslateView;
import com.example.jooff.shuyi.main.DividerItemDecoration;
import com.example.jooff.shuyi.common.MySnackBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jooff on 2017/1/14.
 */

public class HistoryFragment extends Fragment implements HistoryContract.View {
    private HistoryRvAdapter mAdapter;
    private HistoryContract.Presenter mPresenter;
    private OnAppStatusListener mAppListener;

    @BindView(R.id.card_history)
    CardView cardHistory;
    @BindView(R.id.rec_history)
    RecyclerView recHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new HistoryPresenter(AppDbRepository.getInstance(getContext()), this);
        initView();
        return view;
    }

    @Override
    public void initView() {
        mAdapter = new HistoryRvAdapter(new ArrayList<HistoryBean>(0), mListener);
        recHistory.setAdapter(mAdapter);
        recHistory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recHistory.setNestedScrollingEnabled(false);
        recHistory.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new HistoryRvCallback());
        mItemTouchHelper.attachToRecyclerView(recHistory);
        mPresenter.initTheme();
        mPresenter.loadData();
    }

    @Override
    public void showHistory(ArrayList<HistoryBean> items) {
        mAdapter.refreshItems(items);
        cardHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTranslate(String original) {
        MainTranslateView fragment = MainTranslateView.newInstance(0, original);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentFrag, fragment).commit();
    }

    @Override
    public void showHistoryDeleted() {
        MySnackBar.getSnack(cardHistory, R.string.delete_success).show();
        if (getActivity() != null) {
            mAppListener = (OnAppStatusListener) getActivity();
            mAppListener.onSnackBarShow();
        }
    }

    @Override
    public void setAppTheme(int color) {
        recHistory.setBackgroundColor(color);
    }

    /**
     * 重写 RecyclerView 触摸帮助类，实现滑动删除
     */

    class HistoryRvCallback extends ItemTouchHelper.Callback {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //设置滑动的方向
            int swipeFlags = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
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
                cardHistory.setVisibility(View.GONE);
            }
            mPresenter.deleteHistoryItem(position);
        }
    }

    /**
     * 重写 RecyclerView 适配器类
     */

    class HistoryRvAdapter extends RecyclerView.Adapter<HistoryRvAdapter.historyViewHolder> {
        private OnItemClickListener onItemClickListener;
        private ArrayList<HistoryBean> items;

        @Override
        public HistoryRvAdapter.historyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HistoryRvAdapter.historyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.m_item_history, parent, false));
        }

        public HistoryRvAdapter(ArrayList<HistoryBean> items, OnItemClickListener listener) {
            this.items = items;
            onItemClickListener = listener;
        }

        @Override
        public void onBindViewHolder(final HistoryRvAdapter.historyViewHolder holder, int position) {
            HistoryBean item = items.get(position);
            holder.textOriginal.setText(item.getTextOriginal());
            holder.textResult.setText(item.getTextResult());
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, pos);
                    }
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

        public void refreshItems(ArrayList<HistoryBean> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        class historyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_original)
            TextView textOriginal;
            @BindView(R.id.item_result)
            TextView textResult;

            historyViewHolder(View itemView) {
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
