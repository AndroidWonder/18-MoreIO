/*
 * This is an example of binary I/O to a file.
 * The user enters integers only into the EditText widget.
 * Press Go button to write an integer to the file. 
 * Press Enter key after last integer or Press Go with empty EditText widget.
 * The written file is found in /data/data/com.course.example/files/MoreIO.dat
 * This example also uses some of Android's file and directory handling
 * methods.
 * \Run this example at API 23. There is a regression in APIs 24 and 25.
 */
package com.course.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.view.View.OnClickListener;
import android.util.Log;
import java.io.*;

public class MoreIO extends Activity implements OnClickListener {

	private final String file = "MoreIO.dat";
	private String line;
	private TextView text;
	private Button button;
	private EditText edit;
	private DataOutputStream out;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		text = (TextView) findViewById(R.id.TextView01);
		button = (Button) findViewById(R.id.Button01);
		edit = (EditText) findViewById(R.id.EditText01);
		button.setOnClickListener(this);

		// open stream for writing
		try {
			out = new DataOutputStream(openFileOutput(file, MODE_PRIVATE)); // Also try MODE_APPEND
		} catch (IOException e) {
		}

	}

	public void onClick(View v) {
		doThatIO();
	}
	
	@Override
	public boolean onKeyUp(int keycode, KeyEvent event){
		super.onKeyUp(keycode, event);
		if (keycode == KeyEvent.KEYCODE_ENTER) {
			doThatIO();
			return true;
		}
		return true;
	}

	public void doThatIO() {
		try {
			line = edit.getText().toString().trim();
			Log.i("MoreIO", line);
			if (!line.equals("")) { // empty string ends loop
					// convert to int
					int value = Integer.parseInt(line);
					out.writeInt(value);
					text.append(line + " written  \n");
				edit.setText("");
				return;
			}

			// end of input
			int byteNum = out.size(); // get number of bytes written
			Toast.makeText(this,
					new Integer(byteNum).toString() + " bytes written",
					Toast.LENGTH_LONG).show();
			Log.i("MoreIO", new Integer(byteNum).toString() + " bytes written");
			out.close();

			// open stream for reading
			FileInputStream fis = openFileInput(file);
			DataInputStream in = new DataInputStream(fis);
			int count = 0;

			// read until EOFException to end loop
			try {
				text.append("------read back--------  \n");
				while (true) {
					int value = in.readInt();
					count++; // count number of integers read
					text.append(new Integer(value).toString() + "\n");
				}
			} catch (EOFException e) {
			}

			// toast how many records read
			Toast.makeText(this,
					Integer.valueOf(count) + " integers read",
					Toast.LENGTH_LONG).show();
			in.close();

			// store absolute path to file system directory in log
			String path = getFilesDir().toString();
			Log.i("MoreIO", path);

			// store names of private files in the log
			String[] files = fileList();
			for (int i = 0; i < files.length; i++) {
				Log.i("MoreIO", files[i]);
			}

			// now delete them
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}

		} catch (IOException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			Log.e("MoreIO", e.getMessage());
			finish();
		}
	}

}