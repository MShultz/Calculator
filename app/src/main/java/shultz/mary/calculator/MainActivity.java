package shultz.mary.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String history = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void addToHistory(String num, String operator) {
        history += num + operator;
    }

    private void clearHistory() {
        history = "";
    }

    public void addDigit(View view) {
        TextView currentInput = (TextView) findViewById(R.id.input_view);
        int currentLength = currentInput.length();
        Button currentButton = (Button) view;
        String currentButtonText = currentButton.getText().toString();
        if (isDigit(currentButtonText)) {
            int currentDigit = Integer.parseInt(currentButtonText);
            if ((currentDigit == 0 && currentLength >= 1) || currentDigit != 0) {
                setText(currentDigit + "", currentInput);
            }
        } else {
            setText(currentButtonText, currentInput);
        }
    }

    private boolean isDigit(String text) {
        return !text.equals(".");
    }

    private void setText(String text, TextView currentView) {
        currentView.setText(currentView.getText().toString() + text);
    }
}
