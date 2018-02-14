package com.github.jajanjawa.mesosfer7;

import com.github.jajanjawa.mesosfer7.util.Clause;
import com.github.jajanjawa.mesosfer7.util.RequestCallback;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MesosferQuery {

    private final MesosferRider rider;
    private int limit;
    private int skip;
    private int count;
    private String order;
    private List<Clause> clauses;
    private Clause.Builder clauseBuilder;
    private boolean caseSensitive;
    private boolean extractMetadata;


    public MesosferQuery(Application application, String token) {
        rider = new MesosferRider(application).authorize(token);
        limit = 20;
        clauses = new ArrayList<>();
        clauseBuilder = new Clause.Builder();
    }

    public static MesosferQuery users(Application application, String token) {
        MesosferQuery query = new MesosferQuery(application, token);
        query.rider.addPathSegments("users");
        return query;
    }

    public static MesosferQuery files(Application application, String token) {
        MesosferQuery query = new MesosferQuery(application, token);
        query.rider.addPathSegments("files");
        return query;
    }

    public static MesosferQuery data(Application application, String token, String bucket) {
        MesosferQuery query = new MesosferQuery(application, token);
        query.keyPrefix("metadata.");
        query.extractMetadata = true;

        query.rider.addPathSegments("data/bucket").addPathSegments(bucket);
        return query;
    }

    private void addQuery(HttpUrl.Builder builder) {
        if (order != null) {
            builder.addQueryParameter("order", order);
        }
        if (count > 0) {
            builder.addQueryParameter("count", Integer.toString(count));
        }
        if (limit >= 0) {
            builder.addQueryParameter("limit", Integer.toString(limit));
        }
        if (skip > 0) {
            builder.addQueryParameter("skip", Integer.toString(skip));
        }
        if (clauses.size() > 0) {
            builder.addQueryParameter("where", Clause.join(clauses));
        }
    }

    public MesosferQuery addWhereClause(Clause clause) {
        clauses.add(clause);
        return this;
    }

    public MesosferQuery clamp(String key, int min, int max) {
        Clause clause = clauseBuilder.clear().column(key).clamp(min, max).build();
        addWhereClause(clause);
        return this;
    }

    public MesosferQuery lessThan(String key, Number value) {
        Clause clause = clauseBuilder.clear().column(key).lessThan(value).build();
        addWhereClause(clause);
        return this;
    }

    public MesosferQuery lessThanOrEqualTo(String key, Number value) {
        Clause clause = clauseBuilder.clear().column(key).lessThanOrEqualTo(value).build();
        addWhereClause(clause);
        return this;
    }

    public MesosferQuery limit(int limit) {
        this.limit = limit;
        return this;
    }

    public MesosferQuery matches(String key, String regex) {
        Clause clause = clauseBuilder.clear().column(key).matches(regex).build();
        addWhereClause(clause);
        return this;
    }

    public <T> List<T> find(Class<T> clazz) throws MesosferException {
        addQuery(rider.getUrlBuilder());

        JsonArray result = rider.get().execute().resultArray();
        Type type = TypeToken.getParameterized(ArrayList.class, clazz).getType();

        if (extractMetadata) {
            extractMetadata(result);
        }
        return Mesosfer.getGson().fromJson(result, type);
    }

    private JsonArray extractMetadata(JsonArray elements) {
        for (JsonElement element : elements) {
            JsonObject object = element.getAsJsonObject();
            // tanpa dicek, seharusnya punya metadata
            JsonObject metadata = object.get("metadata").getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : metadata.entrySet()) {
                object.add(entry.getKey(), entry.getValue());
            }
            object.remove("metadata");
        }
        return elements;
    }

    public <T> void find(Class<T> clazz, MesosferCallback<List<T>> callback) {
        addQuery(rider.getUrlBuilder());

        rider.get().enqueue(new RequestCallback() {
            @Override
            public void handle(MesosferResponse response, MesosferException e) {
                Type type = TypeToken.getParameterized(ArrayList.class, clazz).getType();
                JsonArray resultArray = response != null ? response.resultArray() : null;
                if (resultArray != null) {
                    if (extractMetadata) {
                        extractMetadata(resultArray);
                    }
                    List<T> result = Mesosfer.getGson().fromJson(resultArray, type);
                    callback.handle(result, e);
                } else {
                    callback.handle(new ArrayList<>(), e);
                }
            }
        });
    }

    public MesosferQuery contains(String key, String substring) {
        matches(key, new StringBuilder(caseSensitive ? "" : "(?i)").append(Pattern.quote(substring)).toString());
        return this;
    }

    public long count() throws MesosferException {
        count = 1;
        limit = 0;
        addQuery(rider.getUrlBuilder());

        return rider.get().execute().count();
    }

    public MesosferCall count(MesosferCallback<Long> callback) {
        count = 1;
        limit = 0;
        addQuery(rider.getUrlBuilder());

        return rider.get().enqueue(new RequestCallback() {
            @Override
            public void handle(MesosferResponse response, MesosferException e) {
                Long count = response != null ? response.count() : 0;
                callback.handle(count, e);
            }
        });
    }

    public MesosferQuery equalTo(String key, Object value) {
        Clause clause = clauseBuilder.clear().column(key).equalTo(value).build();
        addWhereClause(clause);
        return this;
    }

    public MesosferQuery endsWith(String key, String suffix) {
        matches(key,
                new StringBuilder(caseSensitive ? "" : "(?i)").append(Pattern.quote(suffix)).append("$").toString());
        return this;
    }

    public MesosferQuery startsWith(String key, String prefix) {
        matches(key,
                new StringBuilder("^").append(caseSensitive ? "" : "(?i)").append(Pattern.quote(prefix)).toString());
        return this;
    }

    public MesosferQuery greaterThan(String key, Number value) {
        Clause clause = clauseBuilder.clear().column(key).greaterThan(value).build();
        addWhereClause(clause);
        return this;
    }

    /**
     * untuk pencarian pada bucket, data ada di metadata objek.
     *
     * @param prefix diisi "metadata."
     * @return MesosferQuery
     */
    public MesosferQuery keyPrefix(String prefix) {
        clauseBuilder.prefix(prefix);
        return this;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public MesosferQuery greaterThanOrEqualTo(String key, Number value) {
        Clause clause = clauseBuilder.clear().column(key).greaterThanOrEqualTo(value).build();
        addWhereClause(clause);
        return this;
    }

    public MesosferQuery notEqualTo(String key, Object value) {
        Clause clause = clauseBuilder.clear().column(key).notEqualTo(value).build();
        addWhereClause(clause);
        return this;
    }

    public MesosferQuery notContainedIn(String key, Collection<? extends Object> values) {
        Clause clause = clauseBuilder.clear().column(key).notContainedIn(values).build();
        addWhereClause(clause);
        return this;
    }

    public MesosferQuery containedIn(String key, Collection<? extends Object> values) {
        Clause clause = clauseBuilder.clear().column(key).containedIn(values).build();
        addWhereClause(clause);
        return this;
    }

    public MesosferQuery orderByAscending(String key) {
        order = key;
        return this;
    }

    public MesosferQuery orderByDescending(String key) {
        order = "-".concat(key);
        return this;
    }

    public MesosferQuery skip(int skip) {
        this.skip = skip;
        return this;
    }
}
