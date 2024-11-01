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

    // 약물 삭제 이벤트를 위한 인터페이스
    public interface OnPillDeleteListener {
        void onPillDelete(Pill pill);
    }

    // 약물 추가 이벤트를 위한 인터페이스
    public interface OnPillAddListener {
        void onPillAdd(Pill pill);
    }

    // 약물 클릭 이벤트를 위한 인터페이스
    public interface OnPillClickListener {
        void onPillClick(Pill pill);
    }

    // 약물 목록, 이벤트 리스너, 삭제 버튼 표시 여부를 담는 변수들
    private List<Pill> pillList;
    private OnPillDeleteListener deleteListener;
    private OnPillAddListener addListener;
    private OnPillClickListener clickListener;
    private boolean showDeleteButton;

    // 생성자 - 약물 목록과 리스너, 삭제 버튼 표시 여부를 초기화
    public PillAdapter(List<Pill> pillList, OnPillDeleteListener deleteListener, OnPillAddListener addListener, OnPillClickListener clickListener, boolean showDeleteButton) {
        // 약물 목록이 null일 경우 빈 리스트로 초기화
        this.pillList = (pillList != null) ? pillList : new ArrayList<>();
        this.deleteListener = deleteListener;
        this.addListener = addListener;
        this.clickListener = clickListener;
        this.showDeleteButton = showDeleteButton;
    }

    @NonNull
    @Override
    public PillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 레이아웃을 선택하여 뷰 생성
        int layoutId = showDeleteButton ? R.layout.item_pill_delete : R.layout.item_pill;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new PillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PillViewHolder holder, int position) {
        // 해당 위치의 약물 객체 가져오기
        Pill pill = pillList.get(position);
        // 약물 이름 설정
        holder.pillNameTextView.setText(pill.getItemName());

        // 이미지 로딩 (Picasso 라이브러리 사용)
        Picasso.get()
                .load(pill.getItemImage())
                .error(R.drawable.ic_default_image) // 오류 시 기본 이미지
                .into(holder.pillImageView);

        // 아이템 클릭 리스너 설정 (약물 클릭 시 이벤트 처리)
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPillClick(pill);
            }
        });

        // 추가 버튼 설정 (검색 화면에서만 활성화됨)
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

        // 삭제 버튼 설정 (삭제 버튼을 표시하는 경우에만 활성화)
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
        // 약물 리스트의 크기를 반환
        return pillList.size();
    }

    // 뷰 홀더 클래스 - 각 리스트 항목에 대한 뷰 참조를 저장
    public static class PillViewHolder extends RecyclerView.ViewHolder {
        ImageView pillImageView; // 약물 이미지
        TextView pillNameTextView; // 약물 이름
        Button addButton; // 약물 추가 버튼
        Button deleteButton; // 약물 삭제 버튼

        public PillViewHolder(@NonNull View itemView) {
            super(itemView);
            pillImageView = itemView.findViewById(R.id.pillImageView);
            pillNameTextView = itemView.findViewById(R.id.pillNameTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
