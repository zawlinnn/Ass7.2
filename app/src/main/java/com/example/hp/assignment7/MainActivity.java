package com.example.hp.assignment7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public RecyclerView mRecycle;
    public List<Student> studentList;
    public String URL = "http://192.168.88.236/sqlinsert/as7.php";
    EditText etId,etName,etTel,etEmail;
    Button btsave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycle = findViewById(R.id.Hello);
        mRecycle.setHasFixedSize(true);
        mRecycle.setLayoutManager(new LinearLayoutManager(this));
        studentList = new ArrayList<>();

        etId = findViewById(R.id.etid);
        etName = findViewById(R.id.etname);
        etTel = findViewById(R.id.ettel);
        etEmail = findViewById(R.id.etemail);
        btsave = findViewById(R.id.btadd);

        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Up();
            }
        });
        load();
    }
    public void load(){
        StringRequest stringRequest = new StringRequest(
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject posts = array.getJSONObject(i);
                        studentList.add(new Student(
                                posts.getString("id"),
                                posts.getString("name"),
                                posts.getString("tel"),
                                posts.getString("email")

                        ));
                        StudentAdapter pdpt = new StudentAdapter(getApplicationContext(),studentList);
                        mRecycle.setAdapter(pdpt);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void Up(){
        String url_up = "http://192.168.88.236/sqlinsert/up.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url_up, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            if(response.equalsIgnoreCase("success")){
                studentList.clear();
                load();
            }else{
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param =  new HashMap<>();
                param.put("id",etId.getText().toString());
                param.put("name",etName.getText().toString());
                param.put("tel",etTel.getText().toString());
                param.put("email",etEmail.getText().toString());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

