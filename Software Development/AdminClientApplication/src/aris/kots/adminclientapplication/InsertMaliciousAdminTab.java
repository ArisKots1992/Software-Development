package aris.kots.adminclientapplication;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InsertMaliciousAdminTab extends Fragment {
    //For KSOAP
    private static final String SOAP_ACTION_INSERT = "\"http://server/insertMaliciousPatterns\"" ;
    private static final String METHOD_NAME_INSERT = "insertMaliciousPatterns";
    private static final String SOAP_ACTION_RETRIEVE = "\"http://server/retrieveMaliciousPatterns\"" ;
    private static final String METHOD_NAME_RETRIEVE = "retrieveMaliciousPatterns";
    private static final String NAMESPACE = "http://server/";
    private static final String URL = "http://192.168.1.4:9999/CIService/CIService?WSDL";
@Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) {
          View malaris = inflater.inflate(R.layout.forth_tab, container, false);
          
  		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/COMICBD.TTF");
  		final Button addPatterns = (Button) malaris.findViewById(R.id.buttonpatterns);
  		final Button addIps = (Button) malaris.findViewById(R.id.buttonip);
  		final Button refresh = (Button) malaris.findViewById(R.id.buttonRefresh);

  		final EditText editTextPatterns = (EditText) malaris.findViewById(R.id.editTextpatterns);
  		final EditText editTextIps = (EditText) malaris.findViewById(R.id.editTextip);
  		final EditText multiline = (EditText) malaris.findViewById(R.id.edittextView);

  		addPatterns.setTypeface(tf);
  		addIps.setTypeface(tf);
  		editTextPatterns.setTypeface(tf);
  		editTextIps.setTypeface(tf);
  		multiline.setTypeface(tf);
  		refresh.setTypeface(tf);
  		
		addPatterns.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNetworkAvailable()) {
					new Thread() {
						@Override
						public void run() {
							SoapObject request = new SoapObject(NAMESPACE,
									METHOD_NAME_INSERT);
							request.addProperty("arg0", "admin");
							request.addProperty("arg1", "123");
							request.addProperty("arg2", null);
							request.addProperty("arg3", editTextPatterns.getText().toString());

							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
									SoapEnvelope.VER11);

							envelope.setOutputSoapObject(request);
							HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
							try {
								androidHttpTransport.call(SOAP_ACTION_INSERT,envelope);
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										editTextPatterns.setText("");
									}
								});

							} catch (Exception e) {
								Log.e(":D", "I got an error", e);
								Activity activity = getActivity();
								Toast.makeText(activity,"Exception in insert malicious",Toast.LENGTH_SHORT).show();
							}
						}
					}.start();

				} else {
					Toast.makeText(getActivity(), "No Internet connection! ",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		addIps.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNetworkAvailable()) {
					new Thread() {
						@Override
						public void run() {
							SoapObject request = new SoapObject(NAMESPACE,
									METHOD_NAME_INSERT);
							request.addProperty("arg0", "admin");
							request.addProperty("arg1", "123");
							request.addProperty("arg2", editTextIps.getText().toString());
							request.addProperty("arg3", null);

							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

							envelope.setOutputSoapObject(request);
							HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
							try {
								androidHttpTransport.call(SOAP_ACTION_INSERT,envelope);
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										editTextIps.setText("");
									}
								});
							} catch (Exception e) {
								Log.e(":D", "I got an error", e);
								Activity activity = getActivity();
								Toast.makeText(activity,"Excheption in insert ips",Toast.LENGTH_SHORT).show();
							}
						}
					}.start();

				} else {
					Toast.makeText(getActivity(), "No Internet connection! ",
							Toast.LENGTH_LONG).show();
				}
			}
		});
  		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNetworkAvailable()) {
					new Thread() {
						@Override
						public void run() {
							SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME_RETRIEVE);
							request.addProperty("arg0", "admin");
							request.addProperty("arg1", "123");

							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

							envelope.setOutputSoapObject(request);
							HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
							try {
								androidHttpTransport.call(SOAP_ACTION_RETRIEVE,envelope);
								final SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

								final String RetrievePatternsIps = response.toString();
								
								
								final String[] IPS;
								final String[] PATTERNS;
								String[] mem = RetrievePatternsIps.split("____");
									IPS= mem[0].split("--");
									PATTERNS = mem[1].split("--");
									
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										String temp = "*********\nMalicious IPS\n*********\n";
										for(int i=0; i<IPS.length; i++)
											temp +=IPS[i] + "\n";
										
										temp += "*********\nMalicious Patterns\n*********\n";
										
										for(int i=0; i<PATTERNS.length; i++)
											temp +=PATTERNS[i] + "\n";
										
										multiline.setText(temp);
									}
								});
							} catch (Exception e) {
								Log.e(":D", "I got an error", e);
								Activity activity = getActivity();
								Toast.makeText(activity,"Excheption in insert ips",Toast.LENGTH_SHORT).show();
							}
						}
					}.start();

				} else {
					Toast.makeText(getActivity(), "No Internet connection! ",
							Toast.LENGTH_LONG).show();
				}
			}
		});
          return malaris;
}
private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager 
          = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
}
}
