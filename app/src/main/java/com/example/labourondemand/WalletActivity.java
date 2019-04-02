package com.example.labourondemand;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalletActivity extends AppCompatActivity {

    private Button checkBalance, updateBalance;
    private TextView balance, wallet;
    private Toolbar toolbar;
    private CustomerFinal customerFinal;
    private LabourerFinal labourerFinal;
    private String type;
    private FirebaseFirestore firebaseFirestore;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        firebaseFirestore = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(getApplicationContext());

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
        updateBalance = findViewById(R.id.wallet_btn_update_balance);
        balance = findViewById(R.id.wallet_tv_balance);
        wallet = findViewById(R.id.tv);

        labourerFinal = (LabourerFinal) getIntent().getExtras().getSerializable("labourer");
        customerFinal = (CustomerFinal) getIntent().getExtras().getSerializable("customer");
        type = getIntent().getExtras().getString("type");

        updateBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert(v, false);
            }
        });

        checkBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert(v, true);
            }
        });
    }

    public void alert(View v, Boolean b) {
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
                TextInputEditText password = dialogView.findViewById(R.id.wallet_tiet_password);
                TextInputEditText money = dialogView.findViewById(R.id.wallet_tiet_add_money);

                if (b) {
                    money.setVisibility(View.GONE);
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
                } else {
                    if (type.equals("customer")) {
                        if (money.getText().toString().length() == 0) {
                            money.setError("enter amount");
                        }
                        else if (password.getText().toString().equals(customerFinal.getPassword())) {

                            customerFinal.setWallet(customerFinal.getWallet()+Long.valueOf(money.getText().toString()));
                            firebaseFirestore.collection("customer").document(customerFinal.getId())
                                    .update("wallet",customerFinal.getWallet())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            sessionManager.saveCustomer(customerFinal);
                                            Toast.makeText(getApplicationContext(),"Wallet Updated",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("error at walletcustomer",e.toString());
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (password.getText().toString().equals(labourerFinal.getPassword())) {
                            labourerFinal.setWallet(labourerFinal.getWallet()+Long.valueOf(money.getText().toString()));
                            firebaseFirestore.collection("labourer").document(labourerFinal.getId())
                                    .update("wallet",labourerFinal.getWallet())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            sessionManager.saveLabourer(labourerFinal);
                                            Toast.makeText(getApplicationContext(),"Wallet Updated",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("error at walletlabourer",e.toString());
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();

                        }
                    }
                }

                dialog.cancel();
            }

        });

        builder.setView(dialogView);
        builder.show();

    }
}
