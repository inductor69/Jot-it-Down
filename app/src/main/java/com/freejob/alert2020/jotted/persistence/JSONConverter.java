package com.freejob.alert2020.jotted.persistence;

import com.freejob.alert2020.jotted.model.Note;
import com.freejob.alert2020.jotted.model.NoteType;
import com.freejob.alert2020.jotted.util.JournalDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

class JSONConverter {

    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_CONTENT = "content";
    private static final String JSON_TYPE = "type";

    static JSONObject noteToJSON(Note note) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(JSON_TITLE, note.getTitle());
        jo.put(JSON_DATE, JournalDate.formatDateToDataString(note.getDate()));
        jo.put(JSON_CONTENT, note.getContent());

        switch (note.getType()) {
            case ENTRY:
                jo.put(JSON_TYPE, NoteType.entryString);
                break;
            case DREAM:
                jo.put(JSON_TYPE, NoteType.dreamString);
                break;
            case ToDo:
                jo.put(JSON_TYPE, NoteType.ToDoString);
                break;
            default:
                jo.put(JSON_TYPE, NoteType.entryString);
                break;
        }

        return jo;
    }

    static Note JSONtoNote(JSONObject jo) throws JSONException {
        String title = jo.getString(JSON_TITLE);
        String content = jo.getString(JSON_CONTENT);

        String str_date = jo.getString(JSON_DATE);
        Date date = JournalDate.dataStringToDate(str_date);

        NoteType type;
        switch (jo.getString(JSON_TYPE)) {
            case NoteType.entryString:
                type = NoteType.ENTRY;
                break;
            case NoteType.dreamString:
                type = NoteType.DREAM;
                break;
            case NoteType.ToDoString:
                type = NoteType.ToDo;
                break;
            default:
                type = NoteType.ENTRY;
        }

        return new Note(title, date, content, type);
    }

}
