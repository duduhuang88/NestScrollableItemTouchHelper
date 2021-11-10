package com.duduhuang88.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.duduhuang88.demo.helper.NestScrollableItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContentFragment extends Fragment {

    protected RecyclerView rvContent;

    private static final String KEY_RES_ID = "KEY_RES_ID";
    private List<String> dataList = new ArrayList<>();

    public static ContentFragment create(int resId) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_RES_ID, resId);
        contentFragment.setArguments(args);
        return contentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rvContent = (RecyclerView) inflater.inflate(getArguments().getInt(KEY_RES_ID), container, false);
        return rvContent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataList.clear();
        for (int i = 0; i < 20; i++) {
            dataList.add(String.valueOf(i));
        }
        rvContent.setHasFixedSize(true);
        rvContent.setAdapter(new DemoAdapter(onLongClickListener, dataList));
        NestScrollableItemTouchHelper.Callback callback = new NestScrollableItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(dataList, from, to);
                RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
                if (adapter != null) {
                    adapter.notifyItemMoved(from, to);
                }
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };
        NestScrollableItemTouchHelper itemTouchHelper = new NestScrollableItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvContent);
    }

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    };

    private static class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.DemoVH> {

        private View.OnLongClickListener onLongClickListener;
        private List<String> dataList;

        public DemoAdapter(View.OnLongClickListener onLongClickListener, List<String> dataList) {
            this.onLongClickListener = onLongClickListener;
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public DemoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DemoVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo_content, parent, false), onLongClickListener);
        }

        @Override
        public void onBindViewHolder(@NonNull DemoVH holder, int position) {
            holder.tv_content.setText(dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        static class DemoVH extends RecyclerView.ViewHolder {

            TextView tv_content;

            public DemoVH(@NonNull View itemView, View.OnLongClickListener onLongClickListener) {
                super(itemView);
                itemView.setOnLongClickListener(onLongClickListener);
                tv_content = itemView.findViewById(R.id.tv_item);
            }
        }
    }
}
