package com.dybcatering.chatwebmysql.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class RegisterRequest  extends StringRequest {

    private static final String REGISTER_URL = "http://192.168.1.101/loginapp/register.php";
    private Map<String, String> parameters;
    public RegisterRequest(String name, String email, String username, String password,  Response.Listener<String> listener) {
        super(Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();
		parameters.put("name", name);
		parameters.put("email", email);
		parameters.put("username", username);
		parameters.put("password", password);
        //parameters.put("image", photo);

    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
