package com.developer.base.utils.android.extras;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.base.utils.android.lists.BaseList;

import java.util.ArrayList;

public abstract class BaseAdapter<T, VH extends BaseAdapter.BaseHolder<T>>
        extends RecyclerView.Adapter<VH> {
    private BaseList<T> mList;
    private BaseList<T> mSearchList;
    private Integer mSelectPos = -1;
    private String mSearch = "";
    private OnItemUnselected mOnItemUnselected;
    private OnItemSelected mOnItemSelected;

    public BaseAdapter(ArrayList<T> list) {
        this.mList = (list != null) ? new BaseList<>(list) : new BaseList<>();
        this.mSearchList = this.mList;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(getItem(position), position, this.mSelectPos);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH v = getNewViewHolder(
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(getLayout(), parent, false)
        );

        v.setNotify(() -> {
            Integer newPos = (Integer) v.getItemView().getTag();

            if (newPos.equals(BaseAdapter.this.mSelectPos)) {
                BaseAdapter.this.notifyItemChanged(BaseAdapter.this.mSelectPos);
                if (BaseAdapter.this.mOnItemUnselected != null) {
                    BaseAdapter.this.mOnItemUnselected.onUnselect(
                            BaseAdapter.this.mSelectPos
                    );
                }
                this.mSelectPos = -1;
            } else {
                Integer oldPos = BaseAdapter.this.mSelectPos;
                BaseAdapter.this.mSelectPos = newPos;
                BaseAdapter.this.notifyItemChanged(BaseAdapter.this.mSelectPos);
                BaseAdapter.this.notifyItemChanged(oldPos);

                if (BaseAdapter.this.mOnItemSelected != null) {
                    BaseAdapter.this.mOnItemSelected.onSelect(v.getItemView(), newPos, oldPos);
                }
            }
        });

        return v;
    }

    @Override
    public int getItemCount() {
        return this.mSearchList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract int getLayout();

    public abstract boolean onSearch(T t, String search);

    protected abstract VH getNewViewHolder(View v);

    public void search(String search) {
        this.mSearch = search;
        if (!this.mSearch.equals("")) {
            this.mSearchList = this.mList.extract((index, t, count) ->
                onSearch(t, this.mSearch) ? t : null
            );
        } else {
            if (this.mSearchList.size() == this.mList.size())
                return;
            this.mSearchList = this.mList;
        }

        if(this.mOnItemUnselected != null)
            this.mOnItemUnselected.onUnselect(this.mSelectPos);
        this.mSelectPos = -1;
        this.notifyDataSetChanged();
    }

    public BaseList<T> getList() {
        return this.mList;
    }

    public T getItem(int pos) {
        return this.mSearchList.get(pos);
    }

    public T getSelectedItem() {
        return this.mSelectPos != -1 ? this.mSearchList.get(this.mSelectPos) : null ;
    }

    public void addItem(T t) {
        this.mList.add(t);
        this.search(this.mSearch);
    }

    public void addItem(int pos, T t) {
        this.mList.add(pos, t);
        this.search(this.mSearch);
    }

    public void setList(ArrayList<T> ts) {
        this.mList = new BaseList<>(ts);
        this.search(this.mSearch);
    }

    public void setOnItemSelected(OnItemSelected i) {
        this.mOnItemSelected = i;
    }

    public void setOnItemUnselected(OnItemUnselected i) {
        this.mOnItemUnselected = i;
    }

    public static abstract class BaseHolder<T> extends RecyclerView.ViewHolder {
        private Runnable mNotify;

        public BaseHolder(@NonNull View itemView) {
            super(itemView);
        }

        protected void bind(T t, int pos, int selectedPos) {
            this.itemView.setOnClickListener(v -> {
                if (this.mNotify != null) {
                    this.mNotify.run();
                }

                this.onClick(v);
            });

            this.itemView.setTag(pos);

            if (selectedPos == pos) {
                this.itemView.setSelected(true);
                bindSelectViewHolder(t);
            } else {
                this.itemView.setSelected(false);
                bindUnselectViewHolder(t);
            }
        }

        protected abstract void bindUnselectViewHolder(T t);

        protected abstract void bindSelectViewHolder(T t);

        protected void onClick(View v) {
        }

        public void setNotify(Runnable r) {
            this.mNotify = r;
        }

        public View getItemView() {
            return this.itemView;
        }

    }

    public interface OnItemSelected {
        void onSelect(View v, int pos, int oldPos);
    }

    public interface OnItemUnselected {
        void onUnselect(int pos);
    }
}