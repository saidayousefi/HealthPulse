package com.example.healthtracker.features;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthtracker.R;
import com.example.healthtracker.data.local.model.EducationalContent;
import com.example.healthtracker.databinding.FragmentEducationalContentBinding;
import com.example.healthtracker.ui.adapter.EducationalContentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EducationalContentFragment extends Fragment {

    private FragmentEducationalContentBinding binding;
    private List<EducationalContent> contentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEducationalContentBinding.inflate(inflater, container, false);
        contentList = loadEducationalContent();
        setupRecyclerView();
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recyclerViewEducationalContent;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        EducationalContentAdapter adapter = new EducationalContentAdapter(contentList);
        recyclerView.setAdapter(adapter);
    }

    private List<EducationalContent> loadEducationalContent() {
        List<EducationalContent> list = new ArrayList<>();
        try {
            InputStream is = getContext().getAssets().open("educational_content.json");
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
                list.add(new EducationalContent(title, content));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
