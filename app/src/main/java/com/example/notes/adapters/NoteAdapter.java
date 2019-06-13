package com.example.notes.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.diffutils.NoteDiffUtilCallback;
import com.example.notes.entities.Note;
import com.example.notes.enums.SortOrder;
import com.example.notes.holders.NoteHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
    private List<Note> mDataset;
    private List<Note> datasetCopy = new ArrayList<>();
    private final OnItemClickListener listener;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public interface OnItemClickListener {
        void onItemClick(NoteHolder viewHolder);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NoteAdapter(List<Note> myDataset, OnItemClickListener listener){
        mDataset = myDataset;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder, parent, false);

        return new NoteHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        // - get element from your dataset at this position
        // - add onClickListener to the holder
        // - replace the contents of the view with that element
        holder.bind(holder, listener, mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void filter(String text) {
        if (datasetCopy.isEmpty()) {
            datasetCopy.addAll(mDataset);
        }
        mDataset.clear();
        if (text.isEmpty()){
            mDataset.addAll(datasetCopy);
        } else {
            text = text.toLowerCase();
            for (Note item: datasetCopy){
                if (item.getText().toLowerCase().contains(text)){
                    mDataset.add(item);
                }
            }
        }

        NoteDiffUtilCallback noteDiffUtilCallback = new NoteDiffUtilCallback(datasetCopy, mDataset);
        DiffUtil.DiffResult noteDiffResult = DiffUtil.calculateDiff(noteDiffUtilCallback);

        noteDiffResult.dispatchUpdatesTo(this);
    }

    public void sort(SortOrder so){
        if (datasetCopy.isEmpty()) {
            datasetCopy.addAll(mDataset);
        }
        if (so == SortOrder.NewFirst) {
            Collections.sort(mDataset, (n1, n2) -> n2.getDateTime().compareTo(n1.getDateTime()));
        } else {
            Collections.sort(mDataset, (n1, n2) -> n1.getDateTime().compareTo(n2.getDateTime()));
        }

        NoteDiffUtilCallback noteDiffUtilCallback = new NoteDiffUtilCallback(datasetCopy, mDataset);
        DiffUtil.DiffResult noteDiffResult = DiffUtil.calculateDiff(noteDiffUtilCallback, false);

        noteDiffResult.dispatchUpdatesTo(this);
    }
}
