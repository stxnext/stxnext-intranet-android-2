package com.stxnext.intranet2.adapter.json;

import android.util.Log;

import com.google.common.primitives.Longs;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mariusz on 31.03.2016.
 */
public class TeamAdapter extends TypeAdapter<long[]> {

    private static final String TAG = TeamAdapter.class.getName();

    @Override
    public void write(JsonWriter out, long[] value) throws IOException {

    }

    @Override
    public long[] read(JsonReader in) throws IOException {
        List<Long> longs = new ArrayList<>();
        in.beginArray();
        while (in.hasNext()) {
            in.beginObject();
            while (in.hasNext()) {
                if ("user_id".equals(in.nextName())) {
                    try {
                        longs.add(in.nextLong());
                    } catch (IllegalStateException | NumberFormatException e) {
                        Log.w(TAG, "user_id field received from json is NULL");
                        in.skipValue();
                    }
                } else {
                    in.skipValue();
                }
            }
            in.endObject();
        }
        in.endArray();
        return Longs.toArray(longs);
    }
}
