package john.sensor.ilstu;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SensorPlayActivity extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
	private TextView tview;
	private long lastUpdate;
	private int count;
	private int sensitivty;
	SeekBar seekbar;
	TextView value;

	
/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.tview = (TextView)this.findViewById(R.id.counter);
		value = (TextView) findViewById(R.id.textView1);
		seekbar = (SeekBar) findViewById(R.id.seekBar1);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		lastUpdate = System.currentTimeMillis();
			seekbar.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
			{
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
				{
					// TODO Auto-generated method stub
		            value.setText("Sensitivity "+(1+progress));
		            sensitivty=progress;
		        }

		        public void onStartTrackingTouch(SeekBar seekBar)
		        {
		        	// TODO Auto-generated method stub
		        }
		        public void onStopTrackingTouch(SeekBar seekBar)
		        {
		            // TODO Auto-generated method stub
		        }
		     });
		
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float[] values = event.values;
			// Movement
			float x = values[0];
			float y = values[1];
			float z = values[2];

			float accelationSquareRoot = (x * x + y * y + z * z)
					/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
			long actualTime = System.currentTimeMillis();
			if (accelationSquareRoot >= (((double)sensitivty/10)+1)) //
			{
				if (actualTime - lastUpdate < 200) {
					return;
				}
				lastUpdate = actualTime;
				count++;
				tview.setText(String.valueOf(count));
			}

		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
		// register this class as a listener for the orientation and
		// accelerometer sensors
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// unregister listener
		sensorManager.unregisterListener(this);
		super.onStop();
	}
	public void resetCount(View view)
	{
		count=0;
		tview.setText(String.valueOf(count));
	}
}