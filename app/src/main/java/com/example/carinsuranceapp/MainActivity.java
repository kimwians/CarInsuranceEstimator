package com.example.carinsuranceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Client currentClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListButton();
        initSaveButton();
        initTextChangedEvents();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initClient(extras.getInt("clientid"));
        }
        else {
            currentClient = new Client();
        }
    }

    private void initListButton() {
        Button btnList = findViewById(R.id.buttonClientList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                boolean wasSuccessful = false;
                ClientDataSource ds = new ClientDataSource(MainActivity.this);
                try {
                    ds.open();

                    if (currentClient.getClientID() == -1) {
                        wasSuccessful = ds.insertClient(currentClient);
                        if (wasSuccessful) {
                            int newId = ds.getLastClientId();
                            currentClient.setClientID(newId);
                        }
                    }
                    else {
                        wasSuccessful = ds.updateClient(currentClient);
                    }

                    ds.close();
                }
                catch (Exception e) {
                    Log.w(MainActivity.class.getName(), "Failed to execute query");
                    wasSuccessful = false;
                }

                if (wasSuccessful) {
                    Toast.makeText(MainActivity.this, "Successfully updated database", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initClient(int id) {
        ClientDataSource ds = new ClientDataSource(MainActivity.this);
        try {
            ds.open();
            currentClient = ds.getSpecificClient(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Load Client Failed", Toast.LENGTH_LONG).show();
        }

        EditText editName = findViewById(R.id.editName);
        EditText editAge = findViewById(R.id.editAge);
        RadioButton rbMale = findViewById(R.id.rbMale);
        RadioButton rbFemale = findViewById(R.id.rbFemale);
        RadioButton rbSingle = findViewById(R.id.rbSingle);
        RadioButton rbMarried = findViewById(R.id.rbMarried);
        RadioButton rbDivorced = findViewById(R.id.rbDivorced);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZip = findViewById(R.id.editZip);
        RadioButton rbLow = findViewById(R.id.rbValueLow);
        RadioButton rbMid = findViewById(R.id.rbValueMid);
        RadioButton rbHigh = findViewById(R.id.rbValueHigh);
        TextView textMonthlyRate = findViewById(R.id.textEstimate);

        editName.setText(currentClient.getClientName());
        editAge.setText(currentClient.getAge());
        if (currentClient.getGender().equalsIgnoreCase("M")) {
            rbMale.setChecked(true);
        }
        else {
            rbFemale.setChecked(true);
        }

        if (currentClient.getMarriageStatus().equalsIgnoreCase("single")) {
            rbSingle.setChecked(true);
        }
        else if (currentClient.getMarriageStatus().equalsIgnoreCase("married")) {
            rbMarried.setChecked(true);
        }
        else {
            rbDivorced.setChecked(true);
        }

        editAddress.setText(currentClient.getAddress());
        editCity.setText(currentClient.getCity());
        editState.setText(currentClient.getState());
        editZip.setText(currentClient.getZip());
        if (currentClient.getValue().equalsIgnoreCase("low")) {
            rbLow.setChecked(true);
        }
        else if (currentClient.getValue().equalsIgnoreCase("mid")) {
            rbMid.setChecked(true);
        }
        else {
            rbHigh.setChecked(true);
        }

        textMonthlyRate.setText(currentClient.getMonthlyRate());
    }

    private void initTextChangedEvents() {
        final EditText etClientName = findViewById(R.id.editName);
        etClientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentClient.setClientName(etClientName.getText().toString());
            }
        });

        final EditText etAge = findViewById(R.id.editAge);
        etAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentClient.setAge(etAge.getText().toString());
            }
        });

        final RadioGroup rgGender = findViewById(R.id.rgGender);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbMale = findViewById(R.id.rbMale);
                if (rbMale.isChecked()) {
                    currentClient.setGender("M");
                }
                else {
                    currentClient.setGender("F");
                }
            }
        });

        final RadioGroup rgMarriageStatus = findViewById(R.id.rgMarriageStatus);
        rgMarriageStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbSingle = findViewById(R.id.rbSingle);
                RadioButton rbMarried = findViewById(R.id.rbMarried);
                if (rbSingle.isChecked()) {
                    currentClient.setMarriageStatus("single");
                }
                else if (rbMarried.isChecked()) {
                    currentClient.setMarriageStatus("married");
                }
                else {
                    currentClient.setMarriageStatus("divorced");
                }
            }
        });

        final EditText etAddress = findViewById(R.id.editAddress);
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentClient.setAddress(etAddress.getText().toString());
            }
        });

        final EditText etCity = findViewById(R.id.editCity);
        etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentClient.setCity(etCity.getText().toString());
            }
        });

        final EditText etState = findViewById(R.id.editState);
        etState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentClient.setState(etState.getText().toString());
            }
        });

        final EditText etZip = findViewById(R.id.editZip);
        etZip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentClient.setZip(etZip.getText().toString());
            }
        });

        final RadioGroup rgValue = findViewById(R.id.rgValue);
        rgValue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbLow = findViewById(R.id.rbValueLow);
                RadioButton rbMid = findViewById(R.id.rbValueMid);
                if (rbLow.isChecked()) {
                    currentClient.setValue("low");
                }
                else if (rbMid.isChecked()) {
                    currentClient.setValue("mid");
                }
                else {
                    currentClient.setValue("high");
                }
            }
        });

        final TextView textMonthlyRate = findViewById(R.id.textEstimate);
        textMonthlyRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentClient.setMonthlyRate(textMonthlyRate.getText().toString());
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editName = findViewById(R.id.editName);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
        EditText editAge = findViewById(R.id.editAge);
        imm.hideSoftInputFromWindow(editAge.getWindowToken(), 0);
        EditText editAddress = findViewById(R.id.editAddress);
        imm.hideSoftInputFromWindow(editAddress.getWindowToken(), 0);
        EditText editCity = findViewById(R.id.editCity);
        imm.hideSoftInputFromWindow(editCity.getWindowToken(), 0);
        EditText editState = findViewById(R.id.editState);
        imm.hideSoftInputFromWindow(editState.getWindowToken(), 0);
        EditText editZip = findViewById(R.id.editZip);
        imm.hideSoftInputFromWindow(editZip.getWindowToken(), 0);
    }
}
