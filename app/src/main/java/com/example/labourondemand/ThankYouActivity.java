package com.example.labourondemand;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThankYouActivity extends AppCompatActivity {

    private ServicesFinal servicesFinal;
    private CustomerFinal customerFinal;
    private Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        servicesFinal = (ServicesFinal) getIntent().getExtras().getSerializable("service");
        customerFinal = (CustomerFinal) getIntent().getExtras().getSerializable("customer");
        proceed = findViewById(R.id.proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThankYouActivity.this, ReviewActivity2.class);
                intent.putExtra("customer", customerFinal);
                intent.putExtra("services", servicesFinal);
                startActivity(intent);
                finish();
            }
        });


    }
}
