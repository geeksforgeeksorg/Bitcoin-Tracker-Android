package com.example.bitcointracker;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "http://api.coinlayer.com/live?access_key=";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = findViewById(R.id.priceLabel);
        Spinner spinner = findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String publicKey = "cd9ebbd0c5c20340b9d638e409f41fb1";
                String finalUrl = BASE_URL + publicKey + "&TARGET=" + adapterView.getItemAtPosition(i) + "&symbols=BTC";
                try {
                    letsDoSomeNetworking(finalUrl);
                } catch (IOException | JSONException e) {
                    System.out.println(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) throws IOException, JSONException {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Handle the response
                    System.out.println(response);
                    try {
                        JSONObject responseObj = new JSONObject(response);
                        JSONObject price = responseObj.getJSONObject("rates");
                        String object = price.getString("BTC");
                        mPriceTextView.setText(object);
                    } catch (JSONException E) {
                        System.out.println(E);
                    }

                },
                error -> {
                    // Handle the error
                    System.out.println(error.toString());
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                });

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(stringRequest);

    }
}