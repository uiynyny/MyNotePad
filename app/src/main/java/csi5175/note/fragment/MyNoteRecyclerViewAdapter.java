/*
 * Yan Zhang
 * 300052103*/
package csi5175.note.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import csi5175.note.R;
import csi5175.note.fragment.NoteFragment.OnListFragmentInteractionListener;
import csi5175.note.model.Note;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Note} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 *
 * The recyclerview is the list of notes displayed on the main page the adapter is the list
 */
public class MyNoteRecyclerViewAdapter extends RecyclerView.Adapter<MyNoteRecyclerViewAdapter.ViewHolder> {

    private final List<Note> notes;
    private final List<Note> notesCopy;
    private final OnListFragmentInteractionListener mListener;
    private int curpos;

    public MyNoteRecyclerViewAdapter(List<Note> items, OnListFragmentInteractionListener listener) {
        notes = items;
        notesCopy=new ArrayList<>();
        notesCopy.addAll(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = notes.get(position);
        holder.mTitleView.setText(notes.get(position).getTitle());
        holder.mContentView.setText(Html.fromHtml(notes.get(position).getContent()));
        holder.mDateView.setText(notes.get(position).getReadableModifiedDate());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                curpos = holder.getAdapterPosition();
                return false;
            }
        });
    }

    public int getCurpos() {
        return curpos;
    }

    public List<Note> getNotes(){
        return notes;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.mView.setOnClickListener(null);
        holder.mView.setOnLongClickListener(null);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void deleteItem(int pos) {
        notes.remove(pos);
    }

    public void filter(String text) {
        notes.clear();
        if(text.isEmpty()){
            notes.addAll(notesCopy);
        } else{
            text = text.toLowerCase();
            for(Note note: notesCopy){
                if(note.getTitle().toLowerCase().contains(text) || Html.fromHtml(note.getContent()).toString().toLowerCase().contains(text)){
                    notes.add(note);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mContentView;
        public final TextView mDateView;
        public Note mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.note_pre_title);
            mContentView = view.findViewById(R.id.note_pre_content);
            mDateView = view.findViewById(R.id.note_pre_date);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, 1, Menu.NONE, "Edit");
            menu.add(Menu.NONE, 2, Menu.NONE, "Delete");
        }
    }
}
