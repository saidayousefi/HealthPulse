package com.example.healthpulse.features.view_health_records;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.healthpulse.ui.Navigator;
import com.example.healthpulse.R;
import com.example.healthpulse.ui.adapter.ViewHealthRecordsAdapter;
import com.example.healthpulse.MyDividerItemDecoration;
import com.example.healthpulse.databinding.FragmentViewHealthRecordsBinding;
import com.example.healthpulse.data.local.model.RecordData;
import com.example.healthpulse.ui.viewmodel.HealthRecordsViewModel;
import java.util.Collections;
import java.util.Comparator;

public class ViewHealthRecordsFragment extends Fragment implements Navigator {

    private HealthRecordsViewModel healthRecordsViewModel;
    private FragmentViewHealthRecordsBinding binding;
    private ViewHealthRecordsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_health_records, container, false);
        healthRecordsViewModel = new ViewModelProvider(this).get(HealthRecordsViewModel.class);
        healthRecordsViewModel.setNavigator(this);
        binding.setViewModel(healthRecordsViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // Initializing the adapter
        adapter = new ViewHealthRecordsAdapter(recordData -> {
            // Navigate to EditRecordFragment
            Bundle bundle = new Bundle();
            bundle.putInt("recordId", recordData.getId());
            Navigation.findNavController(getView()).navigate(R.id.action_viewHealthRecordsFragment_to_editHealthRecordsFragment, bundle);
        });

        binding.recyclerViewRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewRecords.setAdapter(adapter);

        // Item decoration for dividers
        MyDividerItemDecoration dividerItemDecoration = new MyDividerItemDecoration(getContext());
        binding.recyclerViewRecords.addItemDecoration(dividerItemDecoration);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        healthRecordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
            if (records.isEmpty()) {
                binding.recyclerViewRecords.setVisibility(View.GONE);
                binding.emptyStateView.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerViewRecords.setVisibility(View.VISIBLE);
                binding.emptyStateView.setVisibility(View.GONE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(records, Comparator.comparing(RecordData::getDate).thenComparing(RecordData::getTime).reversed());
            }
            adapter.submitList(records);
        });
    }

    @Override
    public void onBackClicked() {
        Navigation.findNavController(requireView()).navigateUp();
    }
}
