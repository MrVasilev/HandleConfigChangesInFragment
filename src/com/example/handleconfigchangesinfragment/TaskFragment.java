package com.example.handleconfigchangesinfragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

public class TaskFragment extends Fragment {

	/**
	 * Callback interface through which the fragment can report the task's
	 * progress and results back to the Activity.
	 */
	protected static interface TaskCallbacks {

		void onPreExecute();

		void onProgressUpdate(int progress);

		void onCancelled();

		void onPostExecute();
	}

	private TaskCallbacks taskCallbacks;
	private DummyAsyncTask dummyAsyncTask;
	private boolean isRunning;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof TaskCallbacks)) {
			throw new IllegalStateException("Activity must implement the TaskCallbacks interface.");
		}

		if (activity != null) {
			taskCallbacks = (TaskCallbacks) activity;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}

	/**
	 * Start the background task.
	 */
	public void startBackgroundTask() {
		if (!isRunning) {
			dummyAsyncTask = new DummyAsyncTask();
			dummyAsyncTask.execute();
			isRunning = true;
		}
	}

	/**
	 * Stop the background task.
	 */
	public void stopBackgroundTask() {
		if (isRunning) {
			dummyAsyncTask.cancel(false);
			dummyAsyncTask = null;
			isRunning = false;
		}
	}

	/**
	 * Returns the current state of the background task.
	 */
	public boolean isBackgroundTaskRunning() {
		return isRunning;
	}

	/**
	 * A dummy task that performs some (dumb) background work and proxies
	 * progress updates and results back to the Activity.
	 */
	private class DummyAsyncTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			taskCallbacks.onPreExecute();
			isRunning = true;
		}

		@Override
		protected Void doInBackground(Void... params) {

			for (int i = 0; i <= 100; i++) {

				/**
				 * {@link #sleep SystemClock.sleep(millis)} is a utility
				 * function very similar to {@link Thread#sleep(long)
				 * Thread.sleep(millis)}, but it ignores
				 * {@link InterruptedException}.
				 */
				SystemClock.sleep(100);

				publishProgress(i);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			taskCallbacks.onPostExecute();
			isRunning = false;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			taskCallbacks.onProgressUpdate(values[0]);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			taskCallbacks.onCancelled();
			isRunning = false;
		}
	}
}
