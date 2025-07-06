package com.lksnext.parkingbercibengoa.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.lksnext.parkingbercibengoa.databinding.FragmentHistorialBinding;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModelFactory;


public class HistorialFragment extends BaseReservasFragment<ReservasViewModel> {

    private FragmentHistorialBinding binding;
    private ReservasViewModel viewModel;

    @Override
    protected View inflarLayout(LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentHistorialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected ViewGroup getContainerReservas() {
        return binding.containerReservas;
    }

    @Override
    protected ReservasViewModel getViewModel() {
        if (viewModel == null)
            viewModel = ReservasViewModelFactory.getSharedInstance(requireActivity().getApplication());
        return viewModel;
    }

    @Override
    protected void observarReservas() {
        getViewModel().getReservas().observe(getViewLifecycleOwner(),
                reservas -> mostrarReservas(reservas, false));
        getViewModel().cargarReservasDelUsuario();
    }

    @Override
    protected void onExtraViewReady() {}
}
