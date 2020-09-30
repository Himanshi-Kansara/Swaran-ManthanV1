package com.jwellery.swaran_manthan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText email, password, name;
    private TextView backArrow, forgetPswd, register;
    private RelativeLayout signin;
    RequestQueue MyRequestQueuereg;
    private String LoginUrl  = "http://13.233.16.255:8082/customer/login";
    public String loginToken;
    public SharedPreferences sharedPreferences;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = Login.this.getSharedPreferences("TokenAgent", Context.MODE_PRIVATE);
        initViews();
        loginToken = randomAlphaNumeric(4);
        MyRequestQueuereg = Volley.newRequestQueue(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Signup.class));
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Pressed", "");
                Toast.makeText( Login.this,"Pressed", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Login.this,MainActivity.class));
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Token", loginToken);
                String firstPassword = md5(password.getText().toString());
                Log.e("Password",firstPassword + " " +loginToken);
                String secondPassword = md5(firstPassword+loginToken);
                customerLogin(secondPassword);
            }
        });
        forgetPswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void initViews() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registerText);
        signin = findViewById(R.id.login);
        name = findViewById(R.id.name);
        backArrow = findViewById(R.id.backlogin);
        forgetPswd = findViewById(R.id.forgetPswd);
    }
    public static String md5(final String s) {
        final String MD5 = "MD5";

        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private void customerLogin(String pswd) {
        final Dialog dialog = ProgressDialog.show(Login.this, "", "Loading...",
                true);
        dialog.show();
        JSONObject jsonobject_one = new JSONObject();
        try {
            jsonobject_one.put("emailId", email.getText().toString());
            jsonobject_one.put("password",pswd);
            jsonobject_one.put("loginToken",loginToken);
            jsonobject_one.put("userName",name.getText().toString());
            JSONObject jsonobject_TWO = new JSONObject();
            jsonobject_TWO.put("request",jsonobject_one );
            Log.e("Data", jsonobject_TWO.toString());
        }catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,LoginUrl, jsonobject_one,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Message",response.toString());
                        dialog.dismiss();
                        try {
                             String status = response.getString("code");
                            Log.e("Dataccc",status);
                            if(Integer.parseInt(status)== 200) {
                                DynamicToast.makeSuccess(Login.this,"Successfully Login").show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", loginToken);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                dialog.dismiss();
                DynamicToast.makeError(Login.this,"User not found").show();
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

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
//                headers.put("Accept", "application/json");
                headers.put("Authorization", "XXXX");
                return headers;
            }
        };
        MyRequestQueuereg.add(jsonObjReq);
    }
}
