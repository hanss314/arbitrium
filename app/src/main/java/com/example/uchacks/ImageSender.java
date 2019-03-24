package com.example.uchacks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

import okhttp3.*;


public class ImageSender {
    private final String endpoint = "https://southcentralus.api.cognitive.microsoft.com/";
    private final String appid = "60aa32d4-414f-47ba-9293-34b3ef1c48f7";
    private final String token = "e39081d6b1bd4bcc8e6f643743aa9092";

    private final String url =  endpoint + "customvision/v2.0/Prediction/" +
            appid + "/image";

    private final File content;
    private final OkHttpClient client = new OkHttpClient();

    ImageSender(File content){
        this.content = content;
    }

    private String makeRequest() throws IOException{
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)

                .addFormDataPart("image", "pic.jpg",
                        RequestBody.create(MediaType
                                .parse("image/jpeg; charset=utf-8"), content))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Prediction-Key", token)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();
    }
    public boolean getOkay() throws IOException, JSONException {
        JSONObject json = new JSONObject(makeRequest());
        JSONArray predictions = json.getJSONArray("predictions");
        double fine=0.0;
        double notfine=0.0;
        for(int i=0; i<2; i++){
            JSONObject pred = predictions.getJSONObject(i);
            if(pred.getString("tagName").equals("p")){
                notfine = pred.getDouble("probability");
            } else {
                fine = pred.getDouble("probability");
            }
        }
        return fine > notfine;
    }
}
