package com.gmail.psyh2409;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Jackson {

    public static Report getReport(String yyyymmdd) throws IOException {
        String sUrl = Parser.getUrlForRequest(Config.getMyProp().getProperty("src"));
        String request = sUrl.concat(String.format("?valcode=USD&date=%s&json", yyyymmdd));
        try {
            URLConnection urlConnection = new URL(request).openConnection();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            urlConnection
                                    .getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String temp;
                while ((temp = br.readLine()) != null) sb.append(temp);
                String json = sb.substring(1, sb.length()-1).trim();
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(json, Report.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
