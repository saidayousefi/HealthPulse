package com.example.healthpulse.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.healthpulse.data.local.model.EducationalContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EducationalContentViewModel extends AndroidViewModel {

    private final MutableLiveData<List<EducationalContent>> educationalContentLiveData = new MutableLiveData<>();

    public EducationalContentViewModel(@NonNull Application application) {
        super(application);
        loadEducationalContent();
    }

    public LiveData<List<EducationalContent>> getEducationalContent() {
        return educationalContentLiveData;
    }

    private void loadEducationalContent() {
        List<EducationalContent> contentList = new ArrayList<>();
        try {
            InputStream is = getApplication().getAssets().open("educational_content.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String title = obj.getString("title");
                String content = obj.getString("content");
                contentList.add(new EducationalContent(title, content));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        educationalContentLiveData.setValue(contentList);
    }
}
