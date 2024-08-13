package com.example.btlptudddn14.ui.planning;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlptudddn14.Adapter.PriorityLevelAdapter;
import com.example.btlptudddn14.Adapter.TargetAdapter;
import com.example.btlptudddn14.Adapter.TypeTargetAdapter;
import com.example.btlptudddn14.databinding.FragmentPlanningBinding;
import com.example.btlptudddn14.models.DBHandler;
import com.example.btlptudddn14.models.ListTypeTarget;
import com.example.btlptudddn14.models.Target;
import com.example.btlptudddn14.ui.AddExpenseActivity;
import com.example.btlptudddn14.ui.AddNewTarget.AddNewTargetActivity;
import com.example.btlptudddn14.ui.TargetDetail.TargetDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlanningFragment extends Fragment implements TargetAdapter.OnItemClickListener, TypeTargetAdapter.OnItemClickListener {
    public static ArrayList<Target> items = new ArrayList<>();
    private TypeTargetAdapter typeAdapter;
    private PriorityLevelAdapter priorityLevelAdapter;
    private TargetAdapter listTargetAdapter;
    private RecyclerView recyclerViewType, recyclerViewListTarget, recyclerViewLevel;
    private ImageView ic_plus;
    private FragmentPlanningBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPlanningBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ic_plus = binding.icPlus;
        icPlusOnclick();
        initRecyclerviewType();
        initRecyclerviewTarget();
        initRecyclerviewPriority();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void icPlusOnclick() {
        ic_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNewTargetActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initRecyclerviewPriority() {
        items = DBHandler.getInstance(getContext()).fetchAllTarget();
        Collections.sort(items, new Comparator<Target>() {
            @Override
            public int compare(Target t1, Target t2) {
                return Integer.compare(t2.getPriorityLevel(), t1.getPriorityLevel());
            }
        });
        recyclerViewLevel = binding.listviewLevelTarget;
        recyclerViewLevel.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        priorityLevelAdapter = new PriorityLevelAdapter(items);
        recyclerViewLevel.setAdapter(priorityLevelAdapter);
    }

    private void initRecyclerviewTarget() {
        items = DBHandler.getInstance(getContext()).fetchAllTarget();
        recyclerViewListTarget = binding.listviewTarget;
        recyclerViewListTarget.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listTargetAdapter = new TargetAdapter(getContext(), items, this);
        recyclerViewListTarget.setAdapter(listTargetAdapter);

    }

    private void initRecyclerviewType() {
        ArrayList<ListTypeTarget> items = new ArrayList<>();
        items.add(new ListTypeTarget("piggy_bank", "Small"));
        items.add(new ListTypeTarget("piggy_bank", "Middle"));
        items.add(new ListTypeTarget("piggy_bank", "Big"));
        items.add(new ListTypeTarget("piggy_bank", "Short-term"));
        items.add(new ListTypeTarget("piggy_bank", "Long-term"));
        recyclerViewType = binding.listviewType;
        recyclerViewType.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        typeAdapter = new TypeTargetAdapter(getContext(), items, this);
        recyclerViewType.setAdapter(typeAdapter);
    }


    @Override
    public void onItemClick(Target item) {
        Intent intent = new Intent(getContext(), TargetDetailActivity.class);
        intent.putExtra("Target", item);
        startActivity(intent);
    }

    @Override
    public void onItemClick(ListTypeTarget item) {
        String type = item.getTitle();
        TextView txtName = binding.planName;
        TextView txtListName = binding.textViewListTarget;
        txtName.setText("Target " + type);
        txtListName.setText("List " + type + " target");
        items = DBHandler.getInstance(getContext()).fetchTargetByType(type);
        listTargetAdapter.setTypeTarget(items);
        priorityLevelAdapter.saveChangedTarget(items);
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerviewType();
        initRecyclerviewTarget();
        initRecyclerviewPriority();
    }
}