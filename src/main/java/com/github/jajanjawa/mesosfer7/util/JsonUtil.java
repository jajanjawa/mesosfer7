package com.github.jajanjawa.mesosfer7.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Date;

public class JsonUtil {

	/**
	 * Tambah properti pada {@link JsonObject}
	 * @param dest tambah kesini
	 * @param name nama
	 * @param value isi
	 * @return JsonObject
	 */
	public static JsonObject addProperty(JsonObject dest, String name, Object value) {
		if (value instanceof String) {
			dest.addProperty(name, (String) value);
		} else if (value instanceof Number) {
			dest.addProperty(name, (Number) value);
		} else if (value instanceof Boolean) {
			dest.addProperty(name, (Boolean) value);
		} else if (value instanceof Date) {
			dest.addProperty(name, DateFormat.getInstance().format((Date) value));
		} else if (value instanceof JsonElement) {
			dest.add(name, (JsonElement) value);
		} else {
			throw new IllegalArgumentException("class " + value.getClass().getName() + " tidak didukung");
		}
		return dest;
	}
	
	/**
	 * Buat {@link JsonPrimitive} sesuai objek class.
	 * {@link Date} akan dikonversi menjadi String. 
	 * @param object instance dari String, Number, Boolean, Date.
	 * @return JsonPrimitive
	 */
	public static JsonPrimitive newJsonPrimitive(Object object) {
		JsonPrimitive primitive = null;
		if (object instanceof String) {
			primitive = new JsonPrimitive((String)object);
		} else if (object instanceof Number) {
			primitive = new JsonPrimitive((Number)object);
		} else if (object instanceof Boolean) {
			primitive = new JsonPrimitive((Boolean)object);
		} else if (object instanceof Date) {
			String date = DateFormat.getInstance().format((Date)object);
			primitive = new JsonPrimitive(date);
		} else {
			throw new IllegalArgumentException("class " + object.getClass().getName() + " tidak didukung");
		}
		return primitive;
	}
}
