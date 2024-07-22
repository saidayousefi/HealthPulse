package com.example.healthtracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthtracker.databinding.FragmentMainMenuBinding;
import com.example.healthtracker.ui.viewmodel.MainMenuViewModel;

public class MainMenuFragment extends Fragment {

    private FragmentMainMenuBinding binding;
    private MainMenuViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MainMenuViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }
}
