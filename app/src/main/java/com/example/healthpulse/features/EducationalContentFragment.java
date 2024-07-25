package com.example.healthpulse.features;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.healthpulse.data.local.model.EducationalContent;
import com.example.healthpulse.databinding.FragmentEducationalContentBinding;
import com.example.healthpulse.ui.adapter.EducationalContentAdapter;
import com.example.healthpulse.ui.viewmodel.EducationalContentViewModel;

import java.util.ArrayList;
import java.util.List;

public class EducationalContentFragment extends Fragment {

    private FragmentEducationalContentBinding binding;
    private EducationalContentViewModel viewModel;
    private EducationalContentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEducationalContentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(EducationalContentViewModel.class);

        setupRecyclerView();

        viewModel.getEducationalContent().observe(getViewLifecycleOwner(), this::updateContentList);
    }

    private void setupRecyclerView() {
        binding.recyclerViewEducationalContent.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EducationalContentAdapter(new ArrayList<>());
        binding.recyclerViewEducationalContent.setAdapter(adapter);
    }

    private void updateContentList(List<EducationalContent> contentList) {
        adapter.updateData(contentList);
    }
}
