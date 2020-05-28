package com.freejob.alert2020.jotted.persistence;

import android.content.Context;
import android.util.Log;

import com.freejob.alert2020.jotted.model.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JSONSerializer {
    private String mFilename;
    private Context mContext;

    public JSONSerializer (String fn, Context ct) {
        mFilename = fn;
        mContext = ct;
    }

    public void saveNotes(List<Note> notes) throws IOException {
        JSONArray jArray = new JSONArray();
        for (Note note : notes) {
            try {
                jArray.put(JSONConverter.noteToJSON(note));
            } catch (JSONException e) {
                e.printStackTrace();
                // move on to next Note
            }
        }

        OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
        try (Writer writer = new OutputStreamWriter(out)) {
            writer.write(jArray.toString());
        } catch (FileNotFoundException e) {
            Log.e("Error: ", "FileNotFoundException thrown", e);
        }
    }

    public ArrayList<Note> loadNotes() throws IOException {
        ArrayList<Note> notes = new ArrayList<Note>();
        InputStream in = mContext.openFileInput(mFilename);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder jsonStr = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                jsonStr.append(line);
            }

            JSONArray jArray = (JSONArray) new JSONTokener(jsonStr.toString()).nextValue();
            for (int i = 0; i < jArray.length(); i++) {
                notes.add(JSONConverter.JSONtoNote(jArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            Log.e("error", "FileNotFoundException thrown: ", e);
        } catch (JSONException e) {
            Log.e("error", "JSONException thrown: ", e);
        }
        return notes;
    }

}
