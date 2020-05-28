package com.freejob.alert2020.jotted.controller;

import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.freejob.alert2020.jotted.R;
import com.freejob.alert2020.jotted.model.Note;
import com.freejob.alert2020.jotted.model.NoteType;
import com.freejob.alert2020.jotted.util.JournalDate;

import java.util.ArrayList;
import java.util.Objects;

import static com.freejob.alert2020.jotted.model.NoteType.DREAM;
import static com.freejob.alert2020.jotted.model.NoteType.ENTRY;
import static com.freejob.alert2020.jotted.model.NoteType.ToDo;

// orientation changes reference:
// https://medium.com/hootsuite-engineering/handling-orientation-changes-on-android-41a6b62cb43f

public class HomeFragment extends Fragment {
    private String strWelcome;
    private View mCard;
    private ArrayList<Note> notes;          // ArrayList implements Serializable, List does not
    private Note recentNote;

    private static final String NOTES_STRING = "notes";

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strWelcome = JournalDate.welcomeString(JournalDate.currentDate());
        if (savedInstanceState != null) {
            notes = (ArrayList<Note>) savedInstanceState.getSerializable(NOTES_STRING);
        } else {
            notes = ((MainActivity) Objects.requireNonNull(getActivity())).getNotes();
        }
        if (notes != null && notes.size() != 0) {
            recentNote = notes.get(notes.size() - 1);
        } else {
            recentNote = new Note("Create a new note!", JournalDate.currentDate(),"Click one of the above buttons.", ENTRY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView mTextGreeting = view.findViewById(R.id.textGreeting);
        mTextGreeting.setText(strWelcome);

        setButtonViewsAndListeners(view);

        mCard = view.findViewById(R.id.card_home);
        setRecentNoteCardViews();
        mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {          // Change later if possible (coupling with JournalFragment)
                Bundle args = new Bundle();
                args.putString("note_title", recentNote.getTitle());
                args.putString("note_date", JournalDate.formatDateToDataString(recentNote.getDate()));
                args.putString("note_content", recentNote.getContent());
                args.putString("note_type", recentNote.getType().getNoteTypeString());
                args.putInt("note_index", ((MainActivity) Objects.requireNonNull(getActivity())).getIndexOfNote(recentNote));

                DialogNoteEditor dialog = new DialogNoteEditor();
                dialog.setArguments(args);
                dialog.show(Objects.requireNonNull(getFragmentManager()), "edit note");

                ((MainActivity) getActivity()).switchToJournalFragment();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(NOTES_STRING, notes);
    }

    private void setButtonViewsAndListeners(View view) {
        Button mButtonEntry = view.findViewById(R.id.buttonEntry);
        mButtonEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewNote(ENTRY, "new entry");
            }
        });

        Button mButtonDream = view.findViewById(R.id.buttonDream);
        mButtonDream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewNote(DREAM, "new dream");
            }
        });

        Button mButtonToDo = view.findViewById(R.id.buttonToDo);
        mButtonToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewNote(ToDo, "new ToDo");
            }
        });
    }

    private void setRecentNoteCardViews() {
        ((TextView) mCard.findViewById(R.id.noteTitle)).setText(recentNote.getTitle());
        ((TextView) mCard.findViewById(R.id.noteDate)).setText(JournalDate.formatDateToString(recentNote.getDate()));
        ((TextView) mCard.findViewById(R.id.noteContentPreview)).setText(recentNote.getContent());

        ImageView noteTypeImageView = mCard.findViewById(R.id.noteTypeImg);
        if (recentNote.getType() != null) {
            switch (recentNote.getType()) {
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

    private void makeNewNote(NoteType type, String tag) {
        DialogNoteEditor dialog = new DialogNoteEditor();
        dialog.setEntryType(type);
        dialog.show(Objects.requireNonNull(getFragmentManager()), tag);
        ((MainActivity) Objects.requireNonNull(getActivity())).switchToJournalFragment();
    }
}
