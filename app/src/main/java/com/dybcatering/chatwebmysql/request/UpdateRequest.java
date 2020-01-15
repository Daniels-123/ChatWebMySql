package com.dybcatering.chatwebmysql.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/22/17.
 */

public class UpdateRequest extends StringRequest {

    private static final String REGISTER_URL = "http://192.168.1.101.com/updateuser.php";
    private Map<String, String> parameters;
    public UpdateRequest(String name, String mobile, String email, String newemail, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("newemail", newemail);
        parameters.put("mobile", mobile);
        parameters.put("email", email);

    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}