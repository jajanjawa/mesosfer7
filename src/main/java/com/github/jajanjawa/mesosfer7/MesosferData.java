package com.github.jajanjawa.mesosfer7;

import com.github.jajanjawa.mesosfer7.response.PostResponse;
import com.github.jajanjawa.mesosfer7.response.UpdateResponse;
import com.github.jajanjawa.mesosfer7.util.RequestCallback;
import com.google.gson.JsonObject;
import okhttp3.RequestBody;

public class MesosferData {

    private final String bucket;
    private final MesosferRider rider;

    public MesosferData(String bucket) {
        this(Mesosfer.getApplication(), Mesosfer.getToken(), bucket);
    }

    public MesosferData(Application application, String token, String bucket) {
        if (bucket.length() > 30) {
            throw new IllegalArgumentException("nama bucket terlalu panjang. maksimal 30");
        }
        this.bucket = bucket;
        rider = new MesosferRider(application).authorize(token);
    }

    private RequestBody constructMetadataBody(JsonObject data) {
        JsonObject metadata = new JsonObject();
        metadata.add("metadata", data);

        return RequestBody.create(MesosferRider.JSON_MIME, metadata.toString());
    }

    public PostResponse push(MesosferObject data) throws MesosferException {
        data.reset();
        JsonObject metadata = Mesosfer.getGson().toJsonTree(data).getAsJsonObject();

        PostResponse response = rider.path("data/bucket/" + bucket).post(constructMetadataBody(metadata)).execute().fromResultAs(PostResponse.class);
        data.objectId = response.getObjectId();
        data.createdAt = response.getCreatedAt();
        return response;
    }

    public MesosferCall push(MesosferObject data, MesosferCallback<PostResponse> callback) {
        data.reset();
        JsonObject metadata = Mesosfer.getGson().toJsonTree(data).getAsJsonObject();

        return rider.path("data/bucket/" + bucket).post(constructMetadataBody(metadata)).enqueue(new RequestCallback() {
            @Override
            public void handle(MesosferResponse response, MesosferException e) {
                PostResponse postResponse = response != null ? response.fromResultAs(PostResponse.class) : null;
                if (postResponse != null) {
                    data.objectId = postResponse.getObjectId();
                    data.createdAt = postResponse.getCreatedAt();
                }
                callback.handle(postResponse, e);
            }
        });
    }

    public <T extends MesosferObject> T fetch(String id, Class<T> type) throws MesosferException {
        JsonObject result = rider.path("data/bucket").addPathSegments(bucket).addPathSegments(id)
                .get().execute().result();
        JsonObject metadata = result.get("metadata").getAsJsonObject();

        T data = Mesosfer.getGson().fromJson(metadata, type);
        data.setObjectId(result);
        data.setCreatedAt(result);
        data.setUpdatedAt(result);
        return data;
    }

    public <T extends MesosferObject> MesosferCall fetch(String id, Class<T> type, MesosferCallback<T> callback) {
        return rider.path("data/bucket").addPathSegments(bucket).addPathSegments(id)
                .get().enqueue(new RequestCallback() {
                    @Override
                    public void handle(MesosferResponse response, MesosferException e) {
                        JsonObject result = response != null ? response.result() : null;
                        JsonObject metadata = result.get("metadata").getAsJsonObject();
                        T data = Mesosfer.getGson().fromJson(metadata, type);
                        data.setObjectId(result);
                        data.setCreatedAt(result);
                        data.setUpdatedAt(result);

                        callback.handle(data, e);
                    }
                });
    }

    public void delete(String id) throws MesosferException {
        rider.path("data/bucket").addPathSegments(bucket).addPathSegments(id)
                .delete().execute();
    }

    public MesosferCall delete(String id, MesosferCallback<Void> callback) {
        return rider.path("data/bucket").addPathSegments(bucket).addPathSegments(id)
                .delete().enqueue(new RequestCallback() {
                    @Override
                    public void handle(MesosferResponse response, MesosferException e) {
                        callback.handle(null, e);
                    }
                });
    }

    public UpdateResponse update(MesosferObject object) throws MesosferException {
        String objectId = object.objectId;
        object.reset();

        JsonObject metadata = Mesosfer.getGson().toJsonTree(object).getAsJsonObject();

        UpdateResponse response = rider.path("data/bucket").addPathSegments(bucket).addPathSegments(objectId)
                .put(constructMetadataBody(metadata)).execute().fromResultAs(UpdateResponse.class);
        object.setUpdatedAt(response.getUpdatedAt());
        return response;
    }

    public MesosferCall update(MesosferObject object, MesosferCallback<UpdateResponse> callback) {
        String objectId = object.objectId;
        object.reset();

        JsonObject metadata = Mesosfer.getGson().toJsonTree(object).getAsJsonObject();

        return rider.path("data/bucket").addPathSegments(bucket).addPathSegments(objectId)
                .put(constructMetadataBody(metadata)).enqueue(new RequestCallback() {
                    @Override
                    public void handle(MesosferResponse response, MesosferException e) {
                        UpdateResponse updateResponse = response != null ? response.fromResultAs(UpdateResponse.class) : null;
                        object.setUpdatedAt(updateResponse.getUpdatedAt());
                        callback.handle(updateResponse, e);
                    }
                });
    }

    /**
     * belum bisa ubah secara parsial
     *
     * @param id   objek id
     * @param data data yang dirubah
     * @return UpdateResponse
     * @throws MesosferException terjadi kesalahan
     */
    public UpdateResponse update(String id, Object data) throws MesosferException {
        JsonObject metadata = Mesosfer.getGson().toJsonTree(data).getAsJsonObject();

        return rider.path("data/bucket").addPathSegments(bucket).addPathSegments(id)
                .put(constructMetadataBody(metadata)).execute().fromResultAs(UpdateResponse.class);
    }

    public MesosferCall update(String id, Object data, MesosferCallback<UpdateResponse> callback) {
        JsonObject metadata = Mesosfer.getGson().toJsonTree(data).getAsJsonObject();

        return rider.path("data/bucket").addPathSegments(bucket).addPathSegments(id)
                .put(constructMetadataBody(metadata)).enqueue(new RequestCallback() {
                    @Override
                    public void handle(MesosferResponse response, MesosferException e) {
                        UpdateResponse updateResponse = response != null ? response.fromResultAs(UpdateResponse.class) : null;
                        callback.handle(updateResponse, e);
                    }
                });
    }

}
