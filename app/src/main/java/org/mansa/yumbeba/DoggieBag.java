package org.mansa.yumbeba;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by mansa on 12/8/15.
 */
public class DoggieBag  extends AppCompatActivity {

    TextView mRestaurantHoodTxt;
    TextView mRestaurantStreetTxt;
    TextView mRestaurantApartmentTxt;
    TextView mRestaurantInfoTxt;
    TextView mRestaurantCallTxt;
    TextView mRestaurantMapTxt;
    TextView mcustomerHoodTxt;
    TextView mCustomerNameTxt;
    TextView mCustomerApartmentTxt;
    TextView mCustomerInfoTxt;
    TextView mCustomerCallTxt;
    TextView mCustomerMapTxt;
    TextView mOrderTotalTxt;
    Button mDeliveryStatusBtn;

    // Log tag
    private static final String TAG = DoggieBag.class.getSimpleName();

    public static final String KEY_DRIVER_PHONE = "phone";
    public static final String KEY_AUTH_TOKEN = "Authorization";

    // Orders json url
    private static final String ORDER_URL = "http://private-77516-yumdriverappapi.apiary-mock.com/order/";

    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doggie_bag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        final Bundle bundle = getIntent().getExtras();


        // initialize restaurant txtviews
        mRestaurantHoodTxt = (TextView)findViewById(R.id.restaurant_hood_txt);
        mRestaurantStreetTxt = (TextView)findViewById(R.id.restaurant_street_txt);
        mRestaurantApartmentTxt = (TextView)findViewById(R.id.restaurant_apartment_name_txt);
        mRestaurantInfoTxt = (TextView)findViewById(R.id.restaurant_other_info_txt);
        mRestaurantCallTxt = (TextView)findViewById(R.id.restaurant_call_txt);
        mRestaurantMapTxt = (TextView)findViewById(R.id.restaurant_map_txt);

        // initialize customer txtviews
        mcustomerHoodTxt = (TextView)findViewById(R.id.customer_hood_txt);
        mCustomerNameTxt = (TextView)findViewById(R.id.customer_name_txt);
        mCustomerApartmentTxt = (TextView)findViewById(R.id.customer_apartment_name_txt);
        mCustomerInfoTxt = (TextView)findViewById(R.id.customer_other_info_txt);
        mCustomerCallTxt = (TextView)findViewById(R.id.customer_call_txt);
        mCustomerMapTxt = (TextView)findViewById(R.id.customer_map_txt);

        mOrderTotalTxt = (TextView) findViewById(R.id.items_total_price_txt);

        mOrderTotalTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("");
                builder.setTitle("Order Details");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //to close the dialog
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });





       /* pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();*/


/*
// Creating volley request obj
        JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.POST, ORDER_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

// Parsing json

                        try {

                            mRestaurantHoodTxt.setText(response.getJSONObject("restaurant").getString("name") + ",  " + response.getJSONObject("restaurant").getJSONObject("address").getString("hood"));
                            mRestaurantStreetTxt.setText("Street : " + response.getJSONObject("restaurant").getJSONObject("address").getString("street_name"));
                            mRestaurantApartmentTxt.setText("Apartment : " + response.getJSONObject("restaurant").getJSONObject("address").getString("apartment_name") +  ", " + response.getJSONObject("restaurant").getJSONObject("address").getString("unit_number"));
                            mRestaurantInfoTxt.setText("Info : " + response.getJSONObject("restaurant").getJSONObject("address").getString("other_info"));
                            String mRestaurantPhoneNumber =  response.getJSONObject("restaurant").getString("phone");
                            String mRestaurantLatitude =  response.getJSONObject("restaurant").getJSONObject("address").getJSONObject("position").getString("longitude");
                            String mRestaurantLongitude =  response.getJSONObject("restaurant").getJSONObject("address").getJSONObject("position").getString("latitude");

                            mcustomerHoodTxt.setText(response.getJSONObject("client").getJSONObject("address").getString("hood") + ", " + response.getJSONObject("client").getJSONObject("address").getString("street_name"));
                            mCustomerNameTxt.setText("Customer : " + response.getJSONObject("client").getString("name"));
                            mCustomerApartmentTxt.setText("Apartment : " + response.getJSONObject("client").getJSONObject("address").getString("apartment_name") +  ", " + response.getJSONObject("client").getJSONObject("address").getString("unit_number"));
                            mCustomerInfoTxt.setText("Info : " + response.getJSONObject("client").getJSONObject("address").getString("other_info"));
                            String mCustomerPhoneNumber = (String) response.getJSONObject("client").getString("phone");
                            String mCustomerLatitude = (String) response.getJSONObject("client").getJSONObject("address").getJSONObject("position").getString("latitude");
                            String mCustomerLongitude = (String) response.getJSONObject("client").getJSONObject("address").getJSONObject("position").getString("longitude");

                            mOrderTotalTxt.setText("Total : " + response.getString("order_total"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
                Toast.makeText(DoggieBag.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put(KEY_DRIVER_PHONE, bundle.getString("phone"));
                params.put("order_id", "930");
                return params;
            }

        *//*@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put(KEY_AUTH_TOKEN, "Token " + (String) bundle.get("auth_token"));
            //headers.put("Content-Type", "application/json");
            return headers;
        }*//*

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(orderRequest);*/
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
