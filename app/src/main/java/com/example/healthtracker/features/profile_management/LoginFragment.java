package com.example.healthtracker.features.profile_management;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.healthtracker.R;
import com.example.healthtracker.databinding.FragmentLoginBinding;
import com.example.healthtracker.ui.viewmodel.AuthViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.loginButton.setOnClickListener(v -> {
            if (validateInputs()) {
                login();
            }
        });

        binding.signUpButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_userSignUpFragment);
        });
    }

    private boolean validateInputs() {
        boolean valid = true;

        if (TextUtils.isEmpty(binding.emailEditText.getText())) {
            binding.emailInputLayout.setError("Email is required.");
            valid = false;
        } else {
            binding.emailInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(binding.passwordEditText.getText())) {
            binding.passwordInputLayout.setError("Password is required.");
            valid = false;
        } else {
            binding.passwordInputLayout.setError(null);
        }

        return valid;
    }

    private void login() {
        String email = binding.emailEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();

        authViewModel.login(email, password).observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_mainMenuFragment);
            } else {
                Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
