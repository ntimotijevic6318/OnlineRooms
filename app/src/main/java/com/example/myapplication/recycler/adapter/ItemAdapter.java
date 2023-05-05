package com.example.myapplication.recycler.adapter;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.entity.Meeting;

import java.util.function.Function;


public class ItemAdapter  extends ListAdapter<Meeting, ItemAdapter.ViewHolder> {


    Function<Meeting, Void> onItemDeleted;
    Function<Meeting , Void> onItemClicked ;

    public ItemAdapter(@NonNull DiffUtil.ItemCallback<Meeting> diffCallback , Function<Meeting , Void> onItemDeleted , Function<Meeting , Void> onItemClicked) {
        super(diffCallback);
        this.onItemDeleted =  onItemDeleted;
        this.onItemClicked = onItemClicked ;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_item, parent , false);
        return new ViewHolder(view , parent.getContext() , positionDeleted -> {
            Meeting meeting  = getItem(positionDeleted);
            onItemDeleted.apply(meeting);
            return  null;
        } , positionClicked -> {
            Meeting meet = getItem(positionClicked);
            onItemClicked.apply(meet);
            return null;
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Meeting meeting = getItem(position);
        holder.bind(meeting);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        public ViewHolder(View itemView, Context context ,  Function<Integer, Void> onItemDeleted ,  Function<Integer, Void> onItemClicked ) {
            super(itemView);
            this.context = context;


            itemView.setOnLongClickListener(v-> {
                if(getAdapterPosition() != RecyclerView.NO_POSITION){
                    onItemDeleted.apply(getAdapterPosition());
                }
                return false;
            });


            itemView.setOnClickListener(v-> {
                if(getAdapterPosition() != RecyclerView.NO_POSITION){
                    onItemClicked.apply(getAdapterPosition());
                }
               // return false;
            });
        }

        public void bind(Meeting meeting) {

            ((TextView)itemView.findViewById(R.id.event_name)).setText(meeting.getName());
            ((TextView)itemView.findViewById(R.id.event_date)).setText(meeting.getDate().toString());
            ((TextView)itemView.findViewById(R.id.event_time)).setText(meeting.getTime());
            ((TextView)itemView.findViewById(R.id.event_url)).setText(meeting.getUrl());
        }



}
}
