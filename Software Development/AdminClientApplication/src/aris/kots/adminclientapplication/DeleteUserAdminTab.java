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

public class DeleteUserAdminTab extends Fragment {
    //For KSOAP
    private static final String SOAP_ACTION = "\"http://server/delete\"" ;
    private static final String METHOD_NAME = "delete";
    private static final String NAMESPACE = "http://server/";
    private static final String URL = "http://192.168.1.4:9999/CIService/CIService?WSDL";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View arisAdmin = inflater.inflate(R.layout.third_tab, container, false);

		// Activity activity = getActivity();
		// Toast.makeText(activity,"MPIKA",Toast.LENGTH_SHORT).show();
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/COMICBD.TTF");
		final Button addBtn = (Button) arisAdmin.findViewById(R.id.delete_button);
		final EditText MacAddr = (EditText) arisAdmin.findViewById(R.id.editTextMac);
		addBtn.setTypeface(tf);
		MacAddr.setTypeface(tf);
		addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isNetworkAvailable()){
				new Thread() {
					@Override
					public void run() {

						SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
						request.addProperty("arg0", "admin");
						request.addProperty("arg1","123");
						request.addProperty("arg2", MacAddr.getText().toString());

						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

						envelope.setOutputSoapObject(request);
						HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
						try {
							androidHttpTransport.call(SOAP_ACTION, envelope);
							final SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

							try {
								// code runs in a thread
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										if(Boolean.valueOf(response.toString())){
											Toast.makeText(getActivity(), "Deleted Succesfully!",Toast.LENGTH_SHORT).show();
											MacAddr.setText("");
										}
										else
											Toast.makeText(getActivity(), "Error!",Toast.LENGTH_SHORT).show();
									}
								});
							} catch (final Exception ex) {
								Log.e(":D", "Exxeee", ex);
							}
						} catch (Exception e) {
							Log.e(":D", "I got an error", e);
							Activity activity = getActivity();
							Toast.makeText(activity, "Excheption in Response",Toast.LENGTH_SHORT).show();
						}

					}
				}.start();
				
				}else{
					Toast.makeText(getActivity(), "No connection! Action will be performed when connection is reestablished.",Toast.LENGTH_LONG).show();
				}
			}
		});
		return arisAdmin;
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
