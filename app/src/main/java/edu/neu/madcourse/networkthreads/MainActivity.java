package edu.neu.madcourse.networkthreads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The MainActivity class is the main activity of the Android application. It contains a text view
 * to display the result of the web service API call and an edit text to input the URL of the
 * API endpoint.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The resultTextView is a text view to display the result of the web service API call.
     */
    private TextView resultTextView;

    /**
     * The mURLEditText is an edit text to input the URL of the API endpoint.
     */
    private EditText mURLEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the resultTextView and mURLEditText
        resultTextView = findViewById(R.id.result_textview);
        mURLEditText = findViewById(R.id.URL_editText);
    }

    /**
     * This method is called when the callWebserviceButton is clicked. It starts a new thread to make the API call and update the resultTextView with the response.
     *
     * @param view the view that was clicked.
     */
    public void callWebserviceButtonHandler(View view) {
        // Start a new thread to make the API call
        new Thread(() -> {
            try {
                // Create a URL object for the API endpoint
                URL url = new URL(mURLEditText.getText().toString());

                // Open a connection to the API endpoint
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set request method to GET
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Get the response stream
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    // Read the response into a StringBuilder
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Convert the response to a JSON object
                    JSONObject jObject = new JSONObject(response.toString());

                    // Extract the title from the JSON object
                    String jTitle = jObject.getString("title");

                    // Close the input stream and connection
                    reader.close();
                    inputStream.close();
                    connection.disconnect();

                    // Update the UI with the API response
                    runOnUiThread(() -> resultTextView.setText(jTitle));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
