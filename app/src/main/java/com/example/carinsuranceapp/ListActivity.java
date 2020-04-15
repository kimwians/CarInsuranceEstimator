package com.example.carinsuranceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<Client> clients;
    boolean isDeleting = false;
    ClientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initNewClientButton();
        initItemClick();
        initDeleteButton();

        ClientDataSource ds = new ClientDataSource(this);

        try {
            ds.open();
            clients = ds.getClients();
            ds.close();
            ListView listView = findViewById(R.id.lvClients);
            adapter = new ClientAdapter(this, clients);
            listView.setAdapter(adapter);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving clients", Toast.LENGTH_LONG).show();
        }
    }

    private void initNewClientButton() {
        Button newClientButton = findViewById(R.id.buttonNewClient);
        newClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initItemClick() {
        ListView listView = findViewById(R.id.lvClients);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client selectedClient = clients.get(position);
                if (isDeleting) {
                    adapter.showDelete(position, view, ListActivity.this, selectedClient);
                }
                else {
                    Intent intent = new Intent(ListActivity.this, MainActivity.class);
                    intent.putExtra("clientid", selectedClient.getClientID());
                    startActivity(intent);
                }
            }
        });
    }

    private void initDeleteButton() {
        final Button deleteButton = findViewById(R.id.buttonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDeleting) {
                    deleteButton.setText("Delete");
                    isDeleting = false;
                    adapter.notifyDataSetChanged();
                }
                else {
                    deleteButton.setText("Done Deleting");
                    isDeleting = true;
                }
            }
        });
    }
}
