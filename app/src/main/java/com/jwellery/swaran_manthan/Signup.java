package com.jwellery.swaran_manthan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    private EditText name,email,mobileNo, password;
    private TextView login;
    private RelativeLayout register;
    RequestQueue MyRequestQueuereg;
    private String RegisterUrl;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initViews();
        RegisterUrl = "http://13.233.16.255:8082/customer/customerRegistration";
        MyRequestQueuereg = Volley.newRequestQueue(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this,Login.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerRegister();
            }
        });
    }
    private void initViews() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobileNo = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
    }

    private void customerRegister() {
        final Dialog dialog = ProgressDialog.show(Signup.this, "", "Loading...",
                true);
        dialog.show();
        JSONObject jsonobject_one = new JSONObject();
        try {
            jsonobject_one.put("customerId", id);
            jsonobject_one.put("emailId", email.getText().toString());
            jsonobject_one.put("mobileNo", mobileNo.getText().toString());
            jsonobject_one.put("password", password.getText().toString());
            jsonobject_one.put("token", "bvbvb");
            jsonobject_one.put("userName", name.getText().toString());

            JSONObject jsonobject_TWO = new JSONObject();
            jsonobject_TWO.put("customerRegistrationRequest",jsonobject_one );
            JSONObject jsonobject = new JSONObject();
            jsonobject.put("data",jsonobject_one );
            Log.e("Data", jsonobject_TWO.toString());
        }catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,RegisterUrl, jsonobject_one,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        try {
                            String status = response.getString("code");
                            Log.e("Dataccc",status);
                            if(Integer.parseInt(status)== 200) {
                                DynamicToast.makeSuccess(Signup.this,"Successfully Registered").show();
                            }
                            else
                                DynamicToast.makeError(Signup.this, response.getString("respMsg")).show();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject obj = new JSONObject(res);
                        Log.e("obj","");
                    } catch (UnsupportedEncodingException e1) {
                        Log.e("obj4","");
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        Log.e("obj","");
                        e2.printStackTrace();
                    }
                }
                VolleyLog.d("Error: " + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "XXXX");
                return headers;
            }
        };
        MyRequestQueuereg.add(jsonObjReq);
    }
}

