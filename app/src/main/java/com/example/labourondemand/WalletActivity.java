package com.example.labourondemand;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WalletActivity extends AppCompatActivity {

    private Button checkBalance;
    private TextView balance, wallet;
    private Toolbar toolbar;
    private CustomerFinal customerFinal;
    private LabourerFinal labourerFinal;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        toolbar = findViewById(R.id.customer_wallet_tb);
        toolbar.setTitle("Wallet");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        checkBalance = findViewById(R.id.wallet_btn_check_balance);
        balance = findViewById(R.id.wallet_tv_balance);
        wallet = findViewById(R.id.tv);

        labourerFinal = (LabourerFinal) getIntent().getExtras().getSerializable("labourer");
        customerFinal = (CustomerFinal) getIntent().getExtras().getSerializable("customer");
        type = getIntent().getExtras().getString("type");

        checkBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                final View dialogView = inflater.inflate(R.layout.dialog_confirm_password, null);
                builder.setView(dialogView);

                builder.setTitle("Confirm Password");
                /*AlertDialog alertDialog = builder.create();
                alertDialog.show();*/
                //AlertDialog dialog = AlertDialog.
                builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText password = dialogView.findViewById(R.id.wallet_et_password);

                        if (type.equals("customer")) {
                            if (password.getText().toString().equals(customerFinal.getPassword())) {
                                balance.setVisibility(View.VISIBLE);
                                wallet.setVisibility(View.VISIBLE);
                                balance.setText(customerFinal.getWallet() + "");
                                //show balance
                            } else {
                                Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            if (password.getText().toString().equals(labourerFinal.getPassword())) {
                                balance.setVisibility(View.VISIBLE);
                                wallet.setVisibility(View.VISIBLE);

                                balance.setText(customerFinal.getWallet() + "");
                                //show balance
                            } else {
                                Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();

                            }
                        }
                        dialog.cancel();
                    }

                });
                builder.setView(dialogView);
                builder.show();


            }
        });
    }
}
