package  com.example.myapplication.recycler.differ;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.myapplication.entity.Meeting;
import com.example.myapplication.model.Event;

public class ItemDiffItemCallback extends DiffUtil.ItemCallback<Meeting> {
    @Override
    public boolean areItemsTheSame(@NonNull Meeting oldItem, @NonNull Meeting newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Meeting oldItem, @NonNull Meeting newItem) {
        return true;
    }
}
