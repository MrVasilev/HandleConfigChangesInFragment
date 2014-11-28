package com.example.handleconfigchangesinfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements TaskFragment.TaskCallbacks {

	private static final String KEY_CURRENT_PROGRESS = "current_progress";
	private static final String KEY_PERCENT_PROGRESS = "percent_progress";
	private static final String TAG_TASK_FRAGMENT = "task_fragment";

	private TextView progressTextView;
	private ProgressBar progressBar;
	private Button startStopButton;

	private TaskFragment taskFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progressTextView = (TextView) findViewById(R.id.progressTextView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		startStopButton = (Button) findViewById(R.id.startStopButton);

		if (savedInstanceState != null) {
			int currentProgress = savedInstanceState.getInt(KEY_CURRENT_PROGRESS);
			String percentProgress = savedInstanceState.getString(KEY_PERCENT_PROGRESS);

			progressBar.setProgress(currentProgress);
			progressTextView.setText(percentProgress);
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);

		if (taskFragment == null) {
			taskFragment = new TaskFragment();
			fragmentManager.beginTransaction().add(taskFragment, TAG_TASK_FRAGMENT).commit();
		}

		if (taskFragment.isBackgroundTaskRunning()) {
			startStopButton.setText(getString(R.string.stop_label));
		} else {
			startStopButton.setText(getString(R.string.start_label));
		}
	}

	public void onStartStopClick(View view) {

		if (view.getId() == R.id.startStopButton) {

			if (taskFragment.isBackgroundTaskRunning()) {
				taskFragment.stopBackgroundTask();
			} else {
				taskFragment.startBackgroundTask();
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(KEY_CURRENT_PROGRESS, progressBar.getProgress());
		outState.putString(KEY_PERCENT_PROGRESS, progressTextView.getText().toString().trim());
	}

	@Override
	public void onPreExecute() {
		startStopButton.setText(getString(R.string.stop_label));
		Toast.makeText(this, getString(R.string.start_task_message), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProgressUpdate(int progress) {
		progressBar.setProgress(progress * progressBar.getMax() / 100);
		progressTextView.setText(progress + " %");
	}

	@Override
	public void onCancelled() {
		startStopButton.setText(getString(R.string.start_label));
		progressBar.setProgress(0);
		progressTextView.setText("0 %");
		Toast.makeText(this, getString(R.string.stop_task_message), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPostExecute() {
		startStopButton.setText(getString(R.string.start_label));
		progressBar.setProgress(progressBar.getMax());
		progressTextView.setText("100 %");
		Toast.makeText(this, getString(R.string.complete_task_message), Toast.LENGTH_SHORT).show();
	}
}
