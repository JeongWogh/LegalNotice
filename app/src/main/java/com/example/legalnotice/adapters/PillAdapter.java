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
import java.util.List;

public class PillAdapter extends RecyclerView.Adapter<PillAdapter.PillViewHolder> {

    public interface OnPillDeleteListener {
        void onPillDelete(Pill pill);
    }

    public interface OnPillAddListener {
        void onPillAdd(Pill pill);
    }

    private List<Pill> pillList;
    private OnPillDeleteListener deleteListener;
    private OnPillAddListener addListener;
    private boolean showDeleteButton;

    public PillAdapter(List<Pill> pillList, OnPillDeleteListener deleteListener, OnPillAddListener addListener, boolean showDeleteButton) {
        this.pillList = pillList;
        this.deleteListener = deleteListener;
        this.addListener = addListener;
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
        holder.pillInfoTextView.setText(pill.getEfcyQesitm());

        Picasso.get()
                .load(pill.getItemImage())
                .error(R.drawable.ic_default_image)
                .into(holder.pillImageView);

        // 추가 버튼 이벤트 설정 (검색 화면에서만 활성화됨)
        if (!showDeleteButton) {
            holder.addButton.setVisibility(View.VISIBLE);
            holder.addButton.setOnClickListener(v -> {
                if (addListener != null) {
                    addListener.onPillAdd(pill);
                }
            });
        } else {
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
        }
    }

    @Override
    public int getItemCount() {
        return pillList.size();
    }

    public static class PillViewHolder extends RecyclerView.ViewHolder {
        ImageView pillImageView;
        TextView pillNameTextView;
        TextView pillInfoTextView;
        Button addButton;
        Button deleteButton;

        public PillViewHolder(@NonNull View itemView) {
            super(itemView);
            pillImageView = itemView.findViewById(R.id.pillImageView);
            pillNameTextView = itemView.findViewById(R.id.pillNameTextView);
            pillInfoTextView = itemView.findViewById(R.id.pillInfoTextView);
            addButton = itemView.findViewById(R.id.addButton);

            // deleteButton은 item_pill_delete 레이아웃에만 존재함
            if (itemView.findViewById(R.id.deleteButton) != null) {
                deleteButton = itemView.findViewById(R.id.deleteButton);
            }
        }
    }
}
