package org.mansa.yumbeba;

/**
 * Created by mansa on 12/4/15.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Orders extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String KEY_DRIVER_PHONE = "phone";
    public static final String KEY_AUTH_TOKEN = "Authorization";
    ;

    // Orders json url
    private static final String ORDERS_URL = "http://private-77516-yumdriverappapi.apiary-mock.com/list_orders/";

    private ProgressDialog pDialog;
    private List<PopulateOrdersCard> ordersArrayList = new ArrayList<PopulateOrdersCard>();;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView)findViewById(R.id.orders_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        final Bundle bundle = getIntent().getExtras();
        final String phoneNumber = (String) bundle.get("phone");
        final String authToken = "Token " + bundle.get("auth_token");

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CardAdapter(this, ordersArrayList);
        mRecyclerView.setAdapter(mAdapter);


        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


// Creating volley request obj
        JsonArrayRequest ordersRequest = new JsonArrayRequest(Request.Method.POST,ORDERS_URL,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

// Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                PopulateOrdersCard orderCardItem = new PopulateOrdersCard();
                                orderCardItem.setRestaurant(obj.getJSONObject("restaurant").getString("name") + ",  " + obj.getJSONObject("restaurant").getJSONObject("address").getString("hood"));
                                orderCardItem.setDestination(obj.getJSONObject("restaurant").getString("name") + ",  " + obj.getJSONObject("restaurant").getJSONObject("address").getString("hood"));
// adding order to orders array cards
                                ordersArrayList.add(orderCardItem);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

// notifying list adapter about data changes
// so that it shows updated data in ListView
                        mAdapter.notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_DRIVER_PHONE, phoneNumber);
                return params;
            }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put(KEY_AUTH_TOKEN, authToken);
            //headers.put("Content-Type", "application/json");
            return headers;
        }


        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(ordersRequest);


        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it

                 Toast.makeText(Orders.this, position + "",Toast.LENGTH_LONG).show();
            }
        });
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