package com.example.sharearide;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class RecurOfferActivity extends AppCompatActivity {

    private Button submit_btn;
    private EditText departure, destination;
    private RecyclerView departure_list, destination_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recur_offer_page);

        // set toolbar format
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        AutoCompleteTextView exposedDropdown_1 = findViewById(R.id.frequency);
        String[] items_1 = new String[]{"Daily", "Weekly", "Monthly", "Every Weekday"};
        ArrayAdapter<String> adapter_1 = new ArrayAdapter<>(this, R.layout.dropdown_item, items_1);
        exposedDropdown_1.setAdapter(adapter_1);

        AutoCompleteTextView exposedDropdown_2 = findViewById(R.id.num_of_carpool);
        String[] items_2 = new String[]{"1", "2", "3", "4"};
        ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(this, R.layout.dropdown_item, items_2);
        exposedDropdown_2.setAdapter(adapter_2);

        submit_btn = (Button) findViewById(R.id.submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecurOfferActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
