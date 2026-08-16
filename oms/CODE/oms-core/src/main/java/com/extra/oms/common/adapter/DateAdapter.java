/**
 * 
 */
package com.extra.oms.common.adapter;

import java.io.IOException;
import java.util.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * @author aibrahim
 *
 */
public class DateAdapter extends TypeAdapter<Date> {

	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		if (value == null) {
			out.nullValue();
		} else {
			out.value(value.getTime());
		}
	}

	@Override
	public Date read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		} else {
			return new Date(in.nextLong());
		}
	}

}
