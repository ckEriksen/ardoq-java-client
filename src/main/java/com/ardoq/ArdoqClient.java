package com.ardoq;

import com.ardoq.service.WorkspaceService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import java.util.Date;

public class ArdoqClient {
    private RestAdapter restAdapter;

    public ArdoqClient(final String endpoint, final String username, final String password) {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade requestFacade) {
                String pwd = Base64.encode((username + ":" + password).getBytes());
                requestFacade.addHeader("Authorization", "Basic " + pwd);
            }
        };

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new Iso8601Adapter())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(endpoint)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(requestInterceptor)
                .build();

        this.restAdapter = restAdapter;
    }

    public WorkspaceService workspace() {
        return restAdapter.create(WorkspaceService.class);
    }

}