package com.example.legalnotice.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.legalnotice.R;
import com.example.legalnotice.models.Pill;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class PillAdapter extends RecyclerView.Adapter<PillAdapter.PillViewHolder> {

    public interface OnPillDeleteListener {
        void onPillDelete(Pill pill);
    }

    public interface OnPillAddListener {
        void onPillAdd(Pill pill);
    }

    public interface OnPillClickListener {
        void onPillClick(Pill pill);
    }

    private List<Pill> pillList;
    private OnPillDeleteListener deleteListener;
    private OnPillAddListener addListener;
    private OnPillClickListener clickListener;
    private boolean showDeleteButton;

    public PillAdapter(List<Pill> pillList, OnPillDeleteListener deleteListener, OnPillAddListener addListener, OnPillClickListener clickListener, boolean showDeleteButton) {
        this.pillList = (pillList != null) ? pillList : new ArrayList<>(); // null 체크 및 초기화
        this.deleteListener = deleteListener;
        this.addListener = addListener;
        this.clickListener = clickListener;
        this.showDeleteButton = showDeleteButton;
    }

    @NonNull
    @Override
    public PillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = showDeleteButton ? R.layout.item_pill_delete : R.layout.item_pill;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new PillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PillViewHolder holder, int position) {
        Pill pill = pillList.get(position);
        holder.pillNameTextView.setText(pill.getItemName());

        Picasso.get()
                .load(pill.getItemImage())
                .error(R.drawable.ic_default_image)
                .into(holder.pillImageView);

        // 클릭 리스너 설정 (약물 클릭 시)
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPillClick(pill);
            }
        });

        // 추가 버튼 이벤트 설정 (검색 화면에서만 활성화됨)
        if (!showDeleteButton && holder.addButton != null) {
            holder.addButton.setVisibility(View.VISIBLE);
            holder.addButton.setOnClickListener(v -> {
                if (addListener != null) {
                    addListener.onPillAdd(pill);
                }
            });
        } else if (holder.addButton != null) {
            holder.addButton.setVisibility(View.GONE);
        }

        // 삭제 버튼이 있는 경우에만 설정
        if (showDeleteButton && holder.deleteButton != null) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onPillDelete(pill);
                }
            });
        } else if (holder.deleteButton != null) {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return pillList.size(); // null이 아니므로 바로 size() 호출 가능
    }

    public static class PillViewHolder extends RecyclerView.ViewHolder {
        ImageView pillImageView;
        TextView pillNameTextView;
        Button addButton;
        Button deleteButton;

        public PillViewHolder(@NonNull View itemView) {
            super(itemView);
            pillImageView = itemView.findViewById(R.id.pillImageView);
            pillNameTextView = itemView.findViewById(R.id.pillNameTextView);
            addButton = itemView.findViewById(R.id.addButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
