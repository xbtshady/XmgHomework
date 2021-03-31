package org.geektimes.rest.demo;

import org.geektimes.rest.util.Maps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.*;

public class RestClientPostDemo {
    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        User user = new User();
        user.setId(1);
        MultivaluedMap header = new MultivaluedHashMap<String, List<String>>();
        List<String> list = new ArrayList<>();
        list.add("application/json;charset=UTF-8");
        header.put("Content-Type", list);
        Response response = client
                .target("http://127.0.0.1:8080/hello/world")      // WebTarget
                .request() // Invocation.Builder
                .headers(header)
                .post(Entity.json(user));                                     //  Response

        String content = response.readEntity(String.class);

        System.out.println(content);

    }

    static class User{
        public User() {
        }

        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
