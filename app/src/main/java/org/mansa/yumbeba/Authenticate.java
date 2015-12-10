package org.mansa.yumbeba;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mansa on 12/4/15.
 */
public class Authenticate extends AppCompatActivity {

    private Button loginBtn;
    private TextInputLayout mPhoneNumberLayout;
    private TextInputLayout mPasswordLayout;
    private EditText mPasswordTxt;
    private EditText mPhoneNumberTxt;


    private static final String REGISTER_URL = "http://private-77516-yumdriverappapi.apiary-mock.com/authenticate/";

    public static final String KEY_PHONENUMBER = "phone";
    public static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mPhoneNumberLayout = (TextInputLayout) findViewById(R.id.phone_number_layout);
        mPhoneNumberTxt = (EditText) findViewById(R.id.phone_number_txt);
        mPasswordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        mPasswordTxt = (EditText) findViewById(R.id.password_txt);


        loginBtn = (Button) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateDriver();
            }
        });
    }

    public void authenticateDriver(){

        boolean isPhoneNumberEmpty = phoneNumberEmpty();
        boolean isPasswordEmpty = passwordEmpty();

        if (isPhoneNumberEmpty && isPasswordEmpty){

            Context context = getApplicationContext();
            CharSequence text = "Fill All Slots To Proceed!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (isPasswordEmpty && !isPhoneNumberEmpty) {

            mPasswordLayout.setError("Fill this to proceed");
            mPhoneNumberLayout.setError(null);
        }
        else if (!isPasswordEmpty && isPhoneNumberEmpty) {

            mPhoneNumberLayout.setError("Fill this to proceed");
            mPasswordLayout.setError(null);
        }
        else {
            registerUser();

        }



    }

    private boolean phoneNumberEmpty(){

        return mPhoneNumberTxt.getText() == null || mPhoneNumberTxt.getText().toString() == null || mPhoneNumberTxt.getText().toString().isEmpty();
    }


    private boolean passwordEmpty(){

        return mPasswordTxt.getText() == null || mPasswordTxt.getText().toString() == null || mPasswordTxt.getText().toString().isEmpty();
    }

    private void registerUser(){

        final String phoneNumber = mPhoneNumberTxt.getText().toString().trim();
        final String password = mPasswordTxt.getText().toString().trim();

        JsonObjectRequest authRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            final Intent intent = new Intent(Authenticate.this,
                                    Orders.class);
                            intent.putExtra("auth_token", (String) response.get("token"));
                            intent.putExtra("phone_number", phoneNumber);
                            startActivity(intent);
                        }catch (Exception e){}

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Authenticate.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_PHONENUMBER,phoneNumber);
                params.put(KEY_PASSWORD,password);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(authRequest);

    }


}
