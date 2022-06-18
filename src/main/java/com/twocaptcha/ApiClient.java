package com.twocaptcha;

import java.util.Map;
import java.io.*;
import java.net.*;

public class ApiClient {

    /**
     * API server
     */
    private final String host = "2captcha.com";

    /**
     * Network client
     */
    public ApiClient(){
    }

    /**
     * Sends captcha to /in.php
     *
     * @param params
     * @return
     * @throws Exception
     */
    public String in(Map<String, String> params, Map<String, File> files) throws Exception {
        String protocol = "https";
        String path = "/in.php";
        URL url = new URL(protocol, host, path);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "text/html");
        http.setConnectTimeout(5000);
        http.setReadTimeout(5000);

        try(OutputStream os = http.getOutputStream()){
            byte[] input = ParameterStringBuilder.getParamsString(params).getBytes();
            os.write(input,0,input.length);
        }

        int code = http.getResponseCode();
//        System.out.println(code);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"))){
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
//            System.out.println(response.toString());
            return response.toString();
        }
    }

    /**
     * Does request to /res.php
     *
     * @param params
     * @return
     * @throws Exception
     */
    public String res(Map<String, String> params) throws Exception {
        String protocol = "https";
        String path = "/res.php";
        URL url = new URL(protocol, host, path);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("GET"); // PUT is another valid option
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "text/html");
        http.setConnectTimeout(5000);
        http.setReadTimeout(5000);

        try(OutputStream os = http.getOutputStream()){
            byte[] input = ParameterStringBuilder.getParamsString(params).getBytes();
            os.write(input,0,input.length);
        }

        int code = http.getResponseCode();
//        System.out.println(code);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"))){
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
//            System.out.println(response.toString());
            return response.toString();
        }
    }

    public static class ParameterStringBuilder {
        public static String getParamsString(Map<String, String> params)
                throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        }
    }

    /**
     * Executes http request to api
     *
     * @param request
     * @return
     * @throws Exception
     */

}