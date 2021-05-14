package itrf.doctor.handlers;

import android.util.Log;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ConnectionHandler {
    public String sendGetRequest(String uri) {
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");  //  In absence of this statement, GET method is used by default

            //  Following two lines are very important otherwise the connection may get closed during uploading data/reading server response and this error is very hard to detect
            conn.setReadTimeout(300000);
            conn.setConnectTimeout(300000);

            //	In case we want to ignore data received as response from server then you can set doInput to false.
            //	But if we wish to use getInputStream() then we need to set it true
            conn.setDoInput(true);
            //	If we wish to append data to the request (by calling getOutputStream()) then we must set doOutput to true.
            conn.setDoOutput(false);

            String response = "";

            int responseCode = conn.getResponseCode();
            Log.e("Status Code", "" + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line = "";
                InputStreamReader ISR = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(ISR);
                while (true) {
                    line = bufferedReader.readLine();
                    if (line != null) {
                        response += line.trim();
                    } else {
                        bufferedReader.close();
                        Log.e("RESPONSE", response);
                        return response;
                    }
                }
            } else {
                return "illegal response";
            }
        } catch (MalformedURLException e) {
            Log.e("Exception","Malformed URL");
            return "{}";
        } catch (UnsupportedEncodingException e) {
            Log.e("Exception","Unsupported encoding");
            return "{}";
        } catch (ProtocolException e) {
            Log.e("Protocol Exception", e.getStackTrace().toString());
            return "{}";
        } catch (IOException e) {
            Log.e("IOException", e.getStackTrace().toString());
            return "{}";
        }
    }

    public String sendPostJsonRequest(JSONObject jsondata, String uri) {
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");  //  In absence of this statement, GET method is used by default
            conn.setRequestProperty("Content-Type", "application/json");

            //  Following two lines are very important otherwise the connection may get closed during uploading data/reading server response and this error is very hard to detect
            conn.setReadTimeout(300000);
            conn.setConnectTimeout(300000);

            //	In case we want to ignore data received as response from server then you can set doInput to false.
            //	But if we wish to use getInputStream() then we need to set it true
            conn.setDoInput(true);
            //	If we wish to append data to the request (by calling getOutputStream()) then we must set doOutput to true.
            conn.setDoOutput(true);

            OutputStream OS = conn.getOutputStream();
            OutputStreamWriter OSW = new OutputStreamWriter(OS, "UTF-8");
            BufferedWriter writer = new BufferedWriter(OSW);
//            Log.e("Sending Json", jsondata.toString());
            writer.write(jsondata.toString());
            writer.flush();

            String response = "";

            int responseCode = conn.getResponseCode();
            Log.e("Status Code", "" + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line = "";
                InputStreamReader ISR = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(ISR);
                while (true) {
                    line = bufferedReader.readLine();
                    if (line != null) {
                        response += line.trim();
                    } else {
                        writer.close();
                        OS.close();
                        bufferedReader.close();
                        Log.e("RESPONSE", response);
                        return response;
                    }
                }
            } else {
                return "illegal response";
            }
        } catch (MalformedURLException e) {
            Log.e("Malformed URL Exception","In Connection Handler");
            e.printStackTrace();
            return "{}";
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding","In Connection Handler");
            e.printStackTrace();
            return "{}";
        } catch (ProtocolException e) {
            Log.e("Protocol Exception", "In Connection Handler");
            e.printStackTrace();
            return "{}";
        } catch (IOException e) {
            Log.e("IOException", "In Connection Handler");
            e.printStackTrace();
            return "{}";
        }
    }
}