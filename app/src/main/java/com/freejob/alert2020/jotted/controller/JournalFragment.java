package com.freejob.alert2020.jotted.controller;

import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.freejob.alert2020.jotted.R;
import com.freejob.alert2020.jotted.model.Note;
import com.freejob.alert2020.jotted.persistence.JSONSerializer;
import com.freejob.alert2020.jotted.util.JournalDate;

import java.util.List;
import java.util.Objects;

public class JournalFragment extends Fragment {

    private JournalAdapter mJournalAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewJournal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        mJournalAdapter = new JournalAdapter();
        recyclerView.setAdapter(mJournalAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNoteEditor dialog = new DialogNoteEditor();
                Objects.requireNonNull(getFragmentManager());
                dialog.show(getFragmentManager(), "new note");
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mJournalAdapter.saveNotes();
    }

    public void addNoteToJournal(Note note) {
        mJournalAdapter.addNote(note);
    }

    public void deleteNoteAtPosition(int pos) {
        mJournalAdapter.deleteNoteAtPosition(pos);
    }

    private void editNoteInJournal(Note note) {
        Bundle args = new Bundle();
        args.putString("note_title", note.getTitle());
        args.putString("note_date", JournalDate.formatDateToDataString(note.getDate()));
        args.putString("note_content", note.getContent());
        args.putString("note_type", note.getType().getNoteTypeString());
        args.putInt("note_index", ((MainActivity) Objects.requireNonNull(getActivity())).getIndexOfNote(note));

        DialogNoteEditor dialog = new DialogNoteEditor();
        dialog.setArguments(args);
        dialog.show(Objects.requireNonNull(getFragmentManager()), "edit note");
    }

    class JournalAdapter extends RecyclerView.Adapter {
        private JSONSerializer mSerializer;
        private List<Note> noteList;
        JournalAdapter(List<Note> noteList) {this.noteList = noteList;}

        JournalAdapter() {
            mSerializer = new JSONSerializer("Notes.json", Objects.requireNonNull(getActivity()).getApplicationContext());
            noteList = ((MainActivity) getActivity()).getNotes();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View view = LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false);
            return new JournalViewHolder(view);
        }

            @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
            final Note tempNote = noteList.get(i);
            ((JournalViewHolder) viewHolder).bindData(tempNote);
            ((JournalViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editNoteInJournal(tempNote);
                }
            });
            ((JournalViewHolder) viewHolder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                    deleteNote(tempNote);
                    return false;
                }
            });
        }

        @Override
        public int getItemViewType(int i) {
            return R.layout.card_note;
        }

        @Override
        public int getItemCount() {
            return noteList.size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        void addNote(Note note) {
            noteList.add(note);
            notifyDataSetChanged();
        }

        void deleteNote(Note note) {
            noteList.remove(note);
            notifyDataSetChanged();
        }

        void deleteNoteAtPosition(int pos) {
            noteList.remove(noteList.get(pos));
            notifyDataSetChanged();
        }

        void saveNotes() {
            try {
                mSerializer.saveNotes(noteList);
            } catch (Exception e) {
                Log.e("error", "Error saving notes: ", e);
            }
        }

    }

    class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView noteTitleTextView, noteDateTextView, noteContentTextView;
        private ImageView noteTypeImageView;

        JournalViewHolder(final View itemView) {
            super(itemView);
            noteTitleTextView = itemView.findViewById(R.id.noteTitle);
            noteDateTextView = itemView.findViewById(R.id.noteDate);
            noteContentTextView = itemView.findViewById(R.id.noteContentPreview);
            noteTypeImageView = itemView.findViewById(R.id.noteTypeImg);
            itemView.setOnClickListener(this);
        }

        void bindData(Note note) {
            noteTitleTextView.setText(note.getTitle());
            noteDateTextView.setText(JournalDate.formatDateToString(note.getDate()));
            noteContentTextView.setText(note.getContent());

            if (note.getType() != null) {
                switch (note.getType()) {
                    case ENTRY:
                        noteTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.entry_circle));
                        noteTypeImageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorEntry)));
                        break;
                    case DREAM:
                        noteTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.dream_circle));
                        noteTypeImageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDream)));
                        break;
                    case ToDo:
                        noteTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.todo_circle));
                        noteTypeImageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorToDo)));
                        break;
                    default:
                        noteTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.entry_circle));
                        break;
                }
            }
        }

        @Override
        public void onClick(View v) {}
    }

}
