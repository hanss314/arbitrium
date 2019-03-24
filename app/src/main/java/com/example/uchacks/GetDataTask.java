package com.example.uchacks;

import android.media.Image;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;


public class GetDataTask extends AsyncTask<File,Integer,Boolean> {

    private final TextView textView;

    GetDataTask(TextView textView){
        this.textView = textView;
    }

    @Override
    protected Boolean doInBackground(File... files) {
        boolean okay;
        try {
            okay = new ImageSender(files[0]).getOkay();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return true;
        }
        return okay;
    }

    protected void onPostExecute(Boolean result){
        if (result){
            textView.setText("You are okay!");
        } else {
            textView.setText("You are not okay");
        }
        textView.setVisibility(View.VISIBLE);
    }
}
