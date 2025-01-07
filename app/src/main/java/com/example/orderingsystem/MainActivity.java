package com.example.orderingsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private HashMap<Integer, List<String>> tableOrders = new HashMap<>();
    private Spinner tableSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> orderAdapter;
    private List<String> currentOrders = new ArrayList<>();
    private List<String> tableList = new ArrayList<>();
    private int selectedTable = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText tableNumberInput = findViewById(R.id.tableNumberInput);
        EditText dishInput = findViewById(R.id.dishInput);
        Button addDishButton = findViewById(R.id.addDishButton);
        Button deleteTableButton = findViewById(R.id.deleteTableButton);
        ListView orderListView = findViewById(R.id.orderListView);
        tableSpinner = findViewById(R.id.tableSpinner);

        // Spinner setup
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tableList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableSpinner.setAdapter(spinnerAdapter);

        // ListView setup
        orderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currentOrders);
        orderListView.setAdapter(orderAdapter);

        // Add dish button logic
        addDishButton.setOnClickListener(v -> {
            String tableNumberStr = tableNumberInput.getText().toString();
            String dishName = dishInput.getText().toString();

            if (tableNumberStr.isEmpty() || dishName.isEmpty()) {
                Toast.makeText(this, "请输入桌号和餐点", Toast.LENGTH_SHORT).show();
                return;
            }

            int tableNumber = Integer.parseInt(tableNumberStr);
            tableOrders.putIfAbsent(tableNumber, new ArrayList<>());
            tableOrders.get(tableNumber).add(dishName);

            if (!tableList.contains(String.valueOf(tableNumber))) {
                tableList.add(String.valueOf(tableNumber));
                spinnerAdapter.notifyDataSetChanged();
            }

            if (selectedTable == tableNumber) {
                updateCurrentOrders(tableNumber);
            }
        });

        // Spinner item selection logic
        tableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTable = Integer.parseInt(tableList.get(position));
                updateCurrentOrders(selectedTable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTable = -1;
                currentOrders.clear();
                orderAdapter.notifyDataSetChanged();
            }
        });

        // Delete table button logic
        deleteTableButton.setOnClickListener(v -> {
            if (selectedTable == -1) {
                Toast.makeText(this, "请选择桌号", Toast.LENGTH_SHORT).show();
                return;
            }

            tableOrders.remove(selectedTable);
            tableList.remove(String.valueOf(selectedTable));
            spinnerAdapter.notifyDataSetChanged();

            currentOrders.clear();
            orderAdapter.notifyDataSetChanged();

            selectedTable = -1;
        });
    }

    private void updateCurrentOrders(int tableNumber) {
        currentOrders.clear();
        if (tableOrders.containsKey(tableNumber)) {
            currentOrders.addAll(tableOrders.get(tableNumber));
        }
        orderAdapter.notifyDataSetChanged();
    }
}
