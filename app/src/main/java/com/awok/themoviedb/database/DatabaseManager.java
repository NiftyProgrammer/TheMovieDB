package com.awok.themoviedb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.awok.themoviedb.datamanager.models.DetailsModel;
import com.awok.themoviedb.datamanager.models.MovieModel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umair on 8/26/2017.
 */

public class DatabaseManager {

    private Context context;
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor fetchAllMovies() {
        return database.query(DBHelper.TABLE,
                new String[] { DBHelper.COL_ID, DBHelper.COL_TITLE, DBHelper.COL_DATA},
                null, null, null, null, null);
    }

    public DetailsModel fetchMovieDetailsById(int id) {
        Cursor cursor = database.query(DBHelper.TABLE,
                new String[] { DBHelper.COL_DATA },
                DBHelper.COL_ID + " = ?" , new String[]{String.valueOf(id)}, null, null, null);
        DetailsModel model = null;
        if (cursor != null && cursor.moveToFirst()){
            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(DBHelper.COL_DATA));
            if(bytes != null && bytes.length > 0) {
                String json = new String(bytes);
                Gson gson = new Gson();
                try {
                    model = gson.fromJson(json, DetailsModel.class);
                } catch (JsonSyntaxException e) {
                    model = null;
                }
            }
            cursor.close();
        }
        return model;
    }

    public List<String> fetchAllTitles() {
        Cursor cursor = fetchAllMovies();
        List<String> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(DBHelper.COL_TITLE));
                list.add(title);
            }
            cursor.close();
        }
        return list;
    }

    public void insertMovieDetails(DetailsModel model) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_TITLE, model.title);
        Gson gson = new Gson();
        values.put(DBHelper.COL_DATA, gson.toJson(model).getBytes());

        if (!duplicateCheck(model.id)) {
            values.put(DBHelper.COL_ID, model.id);
            database.insert(DBHelper.TABLE, null, values);
        } else
            database.update(DBHelper.TABLE, values, DBHelper.COL_ID + " = ?",
                    new String[]{String.valueOf(model.id)});
    }
    public List<String> insertAllMovie(MovieModel movies) {
        List<String> inserted = new ArrayList<>();
        for(MovieModel.Result model : movies.results) {
            if (!duplicateCheck(model.id)) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.COL_ID, model.id);
                values.put(DBHelper.COL_TITLE, model.title);

                database.insert(DBHelper.TABLE, null, values);
                inserted.add(model.title);
            }
        }
        return inserted;
    }

    private boolean duplicateCheck(int id){
        Cursor cursor = database.query(DBHelper.TABLE, null,
                DBHelper.COL_ID + " = ?" , new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.getCount()>0) {
            // duplicate found
            return true;
        }
        return false;
    }
}
