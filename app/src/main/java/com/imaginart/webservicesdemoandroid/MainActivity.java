package com.imaginart.webservicesdemoandroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends Activity {

    public static final String TAG="MainActivity";
    public static final Log log = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void EnviarOnClick(View v) {

        log.d(TAG, "Ingreso a variables");

        Thread nt = new Thread() {
            String resultado;
            EditText et_numero1 = (EditText) findViewById(R.id.et_num1);
            EditText et_numero2 = (EditText) findViewById(R.id.et_num2);

            @Override
            public void run() {
                String NAMESPACE = "http://demo.android.org/";
                String URL = "http://192.168.1.106/WebServiceDemoAndroid/WebServiceDemoAndroid.asmx";
                String METHOD_NAME = "suma";
                String SOAP_ACTION = "http://demo.android.org/suma";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numero1", Integer.parseInt(et_numero1.getText().toString()));
                request.addProperty("numero2", Integer.parseInt(et_numero2.getText().toString()));

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);
                log.d(TAG, "Antes del llamado transporte");
                HttpTransportSE transporte = new HttpTransportSE(URL);

                try {
                    log.d(TAG, "Despues del Try");
                    transporte.call(SOAP_ACTION, envelope);
                    SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
                    resultado = resultado_xml.toString();
                    log.d(TAG, "Despues del resultado " + resultado.toString());
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    log.d(TAG, "Excepcion XmlPull" + e.toString());
                } catch (HttpResponseException e) {
                    e.printStackTrace();
                    log.d(TAG, "StackTrace " + e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    log.d(TAG, "IOException " + e.toString());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, resultado, Toast.LENGTH_LONG).show();
                        TextView txtResult = (TextView)findViewById(R.id.txtResultado);
                        txtResult.setText(resultado);
                    }
                });
            }


        };
        nt.start();


    }
}
