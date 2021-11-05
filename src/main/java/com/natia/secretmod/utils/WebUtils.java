package com.natia.secretmod.utils;

import com.natia.secretmod.SecretUtils;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtils {

    public static void fetch(String urlstring, FetchRunnable fetchRunnable) {
        try {
            URL url = new URL(urlstring);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String input;
                StringBuilder response = new StringBuilder();

                while ((input = in.readLine()) != null) {
                    response.append(input);
                }
                in.close();

                fetchRunnable.run(new FetchResponse(response.toString()));
            } else {
                fetchRunnable.run(null);
            }
        } catch (IOException ex) {
            System.out.println("Unable to GET data from " + urlstring);
            ex.printStackTrace();
        }
    }

}
