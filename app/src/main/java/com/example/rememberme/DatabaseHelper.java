package com.example.rememberme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "remember_me.db";
    public static final String COLUMN_SMS_PERMISSION = "sms_permission";
    private static final int DATABASE_VERSION = 5;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String TABLE_SETTINGS = "settings";
    public static final String COLUMN_SETTING_KEY = "setting_key";
    public static final String COLUMN_SETTING_VALUE = "setting_value";
    public static final String KEY_SMS_PERMISSION = "sms_permission";
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_EVENT_ID = "id";
    public static final String COLUMN_EVENT_USERNAME = "username";
    public static final String COLUMN_EVENT_NAME = "name";
    public static final String COLUMN_EVENT_DATE = "event_date";
    public static final String COLUMN_EVENT_TIME = "event_time";
    public static final String COLUMN_EVENT_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createUsersTable =
            "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_SMS_PERMISSION + " INTEGER"
            + ");";

        database.execSQL(createUsersTable);

        String createEventsTable =
        "CREATE TABLE " + TABLE_EVENTS + " ("
            + COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_EVENT_USERNAME + " TEXT NOT NULL, "
            + COLUMN_EVENT_NAME + " TEXT NOT NULL, "
            + COLUMN_EVENT_DATE + " TEXT NOT NULL, "
            + COLUMN_EVENT_TIME + " TEXT NOT NULL, "
            + COLUMN_EVENT_DESCRIPTION + " TEXT"
        + ");";

        database.execSQL(createEventsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(database);
    }

    public boolean createUser(String username, String password) {
        if (userExists(username)) {
            return false;
        }

        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = database.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean userExists(String username) {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean validateLogin(String username, String password) {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password},
                null,
                null,
                null
        );

        boolean valid = cursor.moveToFirst();
        cursor.close();
        return valid;
    }

    public Integer getSmsPermissionState(String username) {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(
                TABLE_USERS,
                new String[]{COLUMN_SMS_PERMISSION},
                COLUMN_USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null
        );

        Integer result = null;

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            result = cursor.getInt(0);
        }

        cursor.close();
        return result;
    }

    public boolean setSmsPermissionState(String username, int permissionValue) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SMS_PERMISSION, permissionValue);

        int rowsUpdated = database.update(
                TABLE_USERS,
                values,
                COLUMN_USERNAME + " = ?",
                new String[]{username}
        );

        return rowsUpdated > 0;
    }

    public long insertEvent(String username, String name, String date, String time, String description) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_USERNAME, username);
        values.put(COLUMN_EVENT_NAME, name);
        values.put(COLUMN_EVENT_DATE, date);
        values.put(COLUMN_EVENT_TIME, time);
        values.put(COLUMN_EVENT_DESCRIPTION, description);

        return database.insert(TABLE_EVENTS, null, values);
    }

    public boolean updateEvent(int eventId, String username, String name, String date, String time, String description) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_USERNAME, username);
        values.put(COLUMN_EVENT_NAME, name);
        values.put(COLUMN_EVENT_DATE, date);
        values.put(COLUMN_EVENT_TIME, time);
        values.put(COLUMN_EVENT_DESCRIPTION, description);

        int rowsUpdated = database.update(
                TABLE_EVENTS,
                values,
                COLUMN_EVENT_ID + " = ?",
                new String[]{String.valueOf(eventId)}
        );

        return rowsUpdated > 0;
    }

    public java.util.List<EventRecord> getEventsForUserAndDate(
        String username,
        String date
    ) {
        java.util.List<EventRecord> events = new java.util.ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(
            TABLE_EVENTS,
            new String[]{
                COLUMN_EVENT_ID,
                COLUMN_EVENT_USERNAME,
                COLUMN_EVENT_NAME,
                COLUMN_EVENT_DATE,
                COLUMN_EVENT_TIME,
                COLUMN_EVENT_DESCRIPTION
            },
            COLUMN_EVENT_USERNAME + " = ? AND " + COLUMN_EVENT_DATE + " = ?",
            new String[]{username, date},
            null,
            null,
            COLUMN_EVENT_TIME + " ASC"
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String eventUsername = cursor.getString(1);
            String name = cursor.getString(2);
            String eventDate = cursor.getString(3);
            String time = cursor.getString(4);
            String description = cursor.getString(5);

            events.add(new EventRecord(id, eventUsername, name, eventDate, time, description));
        }

        cursor.close();

        return events;
    }

    public EventRecord getEventById(int eventId) {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(
                TABLE_EVENTS,
            new String[]{
                COLUMN_EVENT_ID,
                COLUMN_EVENT_USERNAME,
                COLUMN_EVENT_NAME,
                COLUMN_EVENT_DATE,
                COLUMN_EVENT_TIME,
                COLUMN_EVENT_DESCRIPTION
            },
                COLUMN_EVENT_ID + " = ?",
            new String[]{String.valueOf(eventId)},
            null,
            null,
            null
        );

        EventRecord eventRecord = null;

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String eventUsername = cursor.getString(1);
            String name = cursor.getString(2);
            String eventDate = cursor.getString(3);
            String time = cursor.getString(4);
            String description = cursor.getString(5);

            eventRecord = new EventRecord(id, eventUsername, name, eventDate, time, description);
        }

        cursor.close();
        return eventRecord;
    }

    public static class EventRecord {
        public final int id;
        public final String username;
        public final String name;
        public final String date;
        public final String time;
        public final String description;

        public EventRecord(
            int id,
            String username,
            String name,
            String date,
            String time,
            String description
        ) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.date = date;
            this.time = time;
            this.description = description;
        }
    }
}