package aris.kots.adminclientapplication;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.crypto.spec.DESKeySpec;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Register  extends Activity{
    ActionBar actionBar;
    int Number = 0;
    int real_devs = 0;
    DBAdapter db = new DBAdapter(this);
    private static final String SOAP_ACTION = "\"http://server/register_smartphone\"" ;
    private static final String METHOD_NAME = "register_smartphone";
    private static final String NAMESPACE = "http://server/";
    private static final String URL = "http://192.168.1.4:9999/CIService/CIService?WSDL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Number = 0;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        // set title and Fonts
		int titleId = getResources().getIdentifier("action_bar_title", "id","android");
        TextView yourTextView = (TextView) findViewById(titleId);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/COMICBD.TTF");
		TextView txt = (TextView) findViewById(R.id.textView1);
		final EditText edit1 = (EditText) findViewById(R.id.username);
		final EditText edit2 = (EditText) findViewById(R.id.password);
		final EditText edit3 = (EditText) findViewById(R.id.repeat_password);
		txt.setTypeface(tf);
		edit1.setTypeface(tf);
		edit2.setTypeface(tf);
		edit3.setTypeface(tf);
		yourTextView.setTypeface(tf);
		yourTextView.setTextColor(Color.WHITE);
		actionBar = getActionBar();////
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));///

		////////TEST/////////////
        final Button addBtn = (Button)findViewById(R.id.Add_Device);
        addBtn.setTypeface(tf);
        addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				add_device();
			}
		});
        //Register!
        final Button register = (Button)findViewById(R.id.Register);
        register.setTypeface(tf);
        register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String username = edit1.getText().toString();
				final String password = edit2.getText().toString();
				String re_password = edit3.getText().toString();
				if(password.equals(re_password)){
					if(isNetworkAvailable()){
						new Thread() {
							@Override
							public void run() {
								real_devs = 0;
								for(int i=0; i<Number; i++){
									LinearLayout linear = (LinearLayout) findViewById(R.id.Device);
									EditText edit = (EditText) linear.findViewById(i);
									if (edit.getText().toString().length() != 0)
										real_devs++;
								}
								
								final String[] Devices = new String[real_devs];
								int j = 0;
								for(int i=0; i<Number; i++){
									LinearLayout linear = (LinearLayout) findViewById(R.id.Device);
									EditText edit = (EditText) linear.findViewById(i);
									if (edit.getText().toString().length() != 0) {
										Devices[j] = edit.getText().toString();
										j++;
									}
								}
								
								SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
								request.addProperty("arg0", username);
								request.addProperty("arg1",password);

								AvailableNodes avail_nodes = new AvailableNodes(Devices);

								PropertyInfo pi = new PropertyInfo();
								pi.setName("arg2");
								pi.setValue(avail_nodes);
								pi.setType(avail_nodes.getClass());
								request.addProperty(pi);
								SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

								envelope.setOutputSoapObject(request);
								//envelope.addMapping(NAMESPACE,"AvailableNodes",avail_nodes.getClass());
								HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

								try {
									androidHttpTransport.call(SOAP_ACTION, envelope);
									final SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
									
									final int result = Integer.valueOf(response.toString());
									if(result == -2){
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
													Toast.makeText(Register.this, "Username already Exists!",Toast.LENGTH_SHORT).show();
											}
										});
									}else if(result != -1){
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(Register.this,Devices[result]+ " is not a registered device.",Toast.LENGTH_SHORT).show();
											}
										});
									}else{
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(Register.this, "Registration Succesful.",Toast.LENGTH_SHORT).show();
											}
										});
										for(int i=0; i<real_devs; i++){
											db.open();
											db.insertTableUSERSDEVICES(username,Devices[i]);
											db.close();
										}
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Register.this.finish();
											}
										});
										return;
									}

								} catch (Exception e) {
									Log.e(":D", "I got an error", e);
									Toast.makeText(Register.this, "Excheption in Register",Toast.LENGTH_SHORT).show();
								}

							}
						}.start();
						
						}else{
							Toast.makeText(Register.this, "No Internet connection!",Toast.LENGTH_LONG).show();
						}

					
				}else{
					Toast.makeText(Register.this, "Password does not match the confirm password",Toast.LENGTH_SHORT).show();
				}
			}
		});
    }
  public void add_device(){
		LinearLayout main_layout = (LinearLayout) findViewById(R.id.Device);
		final EditText editText = new EditText(this);
		editText.setId(Number);
		Number++;
		editText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		editText.setEms(10);
		editText.setHint("Enter Device ID");
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/COMICBD.TTF");
		editText.setTypeface(tf);
		main_layout.addView(editText);
  }
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}

class AvailableNodes implements KvmSerializable{
	
	String ids;
	
	public AvailableNodes(String[] Devices){
		ids = "";
		if (Devices.length == 0)
			return;
		for (int i=0; i<Devices.length-1; i++)
			ids += Devices[i] + ",,";

		ids += Devices[Devices.length-1];
		//Log.w("myApp", ids);
	}

	@Override
	public Object getProperty(int arg0) {
		if(arg0 == 0){
			return ids;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 1;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		if(arg0 == 0){
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "ids";
		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		if(arg0 == 0){
			ids = arg1.toString();
		}
	}

	@Override
	public String getInnerText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInnerText(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
}