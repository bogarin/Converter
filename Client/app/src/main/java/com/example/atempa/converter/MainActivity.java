package com.example.atempa.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

public class MainActivity extends Activity {
    EditText txtValor;
    RadioGroup unidadesGroup;
    RadioButton unidad;
    Button btnConvertidor;
    TextView tvResultado;
    JSONObject jsonObject;
    JSONArray data;
    HttpAsyncTask httpGetTask;
    // URL to get data json
    String url = "http://192.168.1.101/index.php?data=", urlData = null;
    int selectedId;
    float valor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Gets a reference to our graphic components
        txtValor = (EditText) findViewById(R.id.txtValor);
        btnConvertidor = (Button) findViewById(R.id.btnConvertidor);
        tvResultado = (TextView) findViewById(R.id.tvResultado);
        unidadesGroup = (RadioGroup) findViewById(R.id.unidadesGroup);

        btnConvertidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Returns an integer which represents the selected radio button's ID
                selectedId = unidadesGroup.getCheckedRadioButtonId();
                // Gets a reference to our "selected" radio button
                unidad = (RadioButton) findViewById(selectedId);
                valor = Float.parseFloat(txtValor.getText().toString());

                jsonObject = new JSONObject();
                data = new JSONArray();
                httpGetTask = new HttpAsyncTask();
               
                try {
                    jsonObject.put("unidad", unidad.getText());
                    jsonObject.put("valor", valor);
                    data.put(jsonObject);
                    urlData = url + URLEncoder.encode(data.toString(), "UTF-8");
                    // call AsynTask to perform network operation on separate thread
                    httpGetTask.execute(urlData);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";

        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            tvResultado.setText(result);
            Toast.makeText(getBaseContext(), "Received !!!", Toast.LENGTH_LONG).show();
        }
    }
}