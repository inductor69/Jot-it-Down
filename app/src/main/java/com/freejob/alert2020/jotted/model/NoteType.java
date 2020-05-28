package com.freejob.alert2020.jotted.model;

public enum NoteType {
    ENTRY, DREAM, ToDo;
    public final static String entryString = "Entry";
    public final static String dreamString = "Dream";
    public final static String ToDoString = "ToDo";

    public String getNoteTypeString() {
        switch (this) {
            case ENTRY:
                return entryString;
            case DREAM:
                return dreamString;
            case ToDo:
                return ToDoString;
        }
        return entryString;
    }
}
