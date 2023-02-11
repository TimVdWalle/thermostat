package mobi.vandewalle.thermostat;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private final static int refreshInterval = 900000;
	
	public static double currentTemperature = 0.0;
	public static double previousTemperature = 0.0;
	private Timer myTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new LongOperation().execute("");
        
        myTimer = new Timer();
        myTimer.schedule(new TimerTask(){
        	@Override
        	public void run(){
        		TimerMethod();
        		//new LongOperation().execute("");
        	}
        }, 0, refreshInterval);
    }

    private void TimerMethod(){
    	this.runOnUiThread(Timer_Tick);
    }
    
    private Runnable Timer_Tick = new Runnable(){
    	public void run(){
    		new LongOperation().execute("");
    	}
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void createNotification(View view){
    	// om te kunnen oproepen met button click (debugging only?)
    	createNotification();
    }
    
    public void createNotification(){
    	int ntf_id = 1;
    	Context context = MainActivity.this.getApplicationContext();
    	NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
    	Notification blurring_ntf = new Notification();
    	blurring_ntf.icon = android.R.drawable.stat_notify_sync;
    	String message = String.valueOf(previousTemperature) + "¡C to " + String.valueOf(currentTemperature) + "¡C temperature change.";
    	//blurring_ntf.tickerText = message;
    	blurring_ntf.tickerText = "Temperature change!";
    	blurring_ntf.when = System.currentTimeMillis();
    	
    	//this is correct
    	Intent notificationIntent = new Intent(this, MainActivity.class);
    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
    	blurring_ntf.setLatestEventInfo(context, getText(R.string.app_name), message, pendingIntent);
    	
    	if(previousTemperature != 0){
    		notificationManager.notify(ntf_id, blurring_ntf);
    	}
    }
    
    public void fetchTemperature(View view){
    	new LongOperation().execute("");
    }



	private class LongOperation extends AsyncTask<String, Void, String> {
	
	    @Override
	    protected String doInBackground(String... params) {
	    	String fetchedTemperature = HttpUtils.getContents("http://thermostat.vandewalle.mobi/temperature.php");
	    	try {
	    		fetchedTemperature = fetchedTemperature.replace(",", ".");
	    		currentTemperature = Double.valueOf(fetchedTemperature);
	    		fetchedTemperature = fetchedTemperature.replace(".", ",");
	    		
	    		if(Math.abs(currentTemperature - previousTemperature) > 1 || Math.abs(previousTemperature - currentTemperature) > 1){
		    		previousTemperature = currentTemperature;
	    			if(previousTemperature != 0){
	    				createNotification();	    				
	    			}
	    		}
	    	} catch(Exception e){
	    		// nothing; wrong conversion ; fix later
	    	}
	    	return fetchedTemperature;
	    }      
	
	    @Override
	    protected void onPostExecute(String result) {
	          TextView txt = (TextView) findViewById(R.id.lblTemperature);
	          txt.setTextSize(50);
	          txt.setText(result + "¡C");
	    }
	
	    @Override
	    protected void onPreExecute() {
	    }
	
	    @Override
	    protected void onProgressUpdate(Void... values) {
	    }
	}  
}
