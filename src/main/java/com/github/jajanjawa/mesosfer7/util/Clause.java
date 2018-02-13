package com.github.jajanjawa.mesosfer7.util;

import com.github.jajanjawa.mesosfer7.Mesosfer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.regex.Pattern;

public class Clause {

	/**
	 * Untuk membuat where clause.
	 * 
	 * @author irwantoro
	 *
	 */
	public static class Builder {

		private String column;
		private String prefix;
		private JsonElement simple;
		private Map<String, Object> where;

		public Builder() {
			prefix = "";
			where = new HashMap<>();
		}

		/**
		 * Siap membuat {@link Clause}. jika {@link Builder builder ini} dipakai
		 * lagi jangan lupa panggil {@link #clear()}
		 * 
		 * @return Clause
		 * @see #clear()
		 */
		public Clause build() {
			Clause clause = new Clause();
			clause.column = prefix + column;

			if (!where.isEmpty()) {
				JsonElement json = Mesosfer.getGson().toJsonTree(where);
				clause.value = json;
			} else {
				clause.value = simple;
			}

			return clause;
		}

		/**
		 * Menghapus where clause sebelumnya, sehingga bisa dipakai lagi.
		 * 
		 * @return Builder
		 */
		public Builder clear() {
			where.clear();
			return this;
		}

		public Builder column(String name) {
			column = name;
			return this;
		}

		public Builder containedIn(Collection<? extends Object> values) {
			where.put("$in", new ArrayList(values));
			return this;
		}

		public Builder contains(String substring, boolean caseSensitive) {
			matches(new StringBuilder(caseSensitive ? "" : "(?i)").append(Pattern.quote(substring)).toString());
			return this;
		}

		public Builder endsWith(String suffix, boolean caseSensitive) {
			matches(new StringBuilder(caseSensitive ? "" : "(?i)").append(Pattern.quote(suffix)).append("$")
					.toString());
			return this;
		}

		/**
		 * if (value &gt;= min &amp;&amp; value &lt;= max)
		 * 
		 * @param min terkecil
		 * @param max paling besar
		 * @return Builder
		 */
		public Builder clamp(int min, int max) {
			greaterThanOrEqualTo(min);
			lessThanOrEqualTo(max);
			return this;
		}

		/**
		 * @param value nilai
		 * @return Builder
		 * @see #clear()
		 */
		public Builder equalTo(Object value) {
			if (value instanceof JsonElement) {
				simple = (JsonElement) value;
			} else {
				simple = JsonUtil.newJsonPrimitive(value);
			}
			return this;
		}

		public Builder greaterThan(Number number) {
			where.put("$gt", number);
			return this;
		}

		public Builder greaterThanOrEqualTo(Number number) {
			where.put("$gte", number);
			return this;
		}

		public Builder lessThan(Number number) {
			where.put("$lt", number);
			return this;
		}

		public Builder lessThanOrEqualTo(Number number) {
			where.put("$lte", number);
			return this;
		}

		/**
		 * pakai regular expression
		 * 
		 * @param regex expression
		 * @return Builder
		 */
		public Builder matches(String regex) {
			where.put("$regex", regex);
			return this;
		}

		public Builder notContainedIn(Collection<? extends Object> values) {
			where.put("$nin", new ArrayList(values));
			return this;
		}

		public Builder notEqualTo(Object value) {
			where.put("$ne", value);
			return this;
		}

		public Builder prefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		public Builder startsWith(String prefix, boolean caseSensitive) {
			matches(new StringBuilder("^").append(caseSensitive ? "" : "(?i)").append(Pattern.quote(prefix))
					.toString());
			return this;
		}
	}

	public static String join(List<Clause> clauses) {
		JsonObject where = new JsonObject();
		for (Clause clause : clauses) {
			where.add(clause.getColumn(), clause.value());
		}
		return where.toString();
	}

	public static Clause or(Clause first, Clause second) {
		Builder builder = new Builder().prefix("").column("$or");

		JsonArray array = new JsonArray();
		array.add(first.toJson());
		array.add(second.toJson());

		builder.equalTo(array);
		return builder.build();
	}

	private String column;
	private JsonElement value;
	private JsonObject clause;

	private Clause() {
	}

	public String getColumn() {
		return column;
	}

	public JsonObject toJson() {
		if (clause == null) {
			clause = new JsonObject();
			clause.add(column, value);
		}
		return clause;
	}

	public JsonElement value() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
