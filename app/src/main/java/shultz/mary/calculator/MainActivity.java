package shultz.mary.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean hasSecond, hasFirst = false;
    private TextView currentInput;
    private TextView calculationView;
    private double firstNum, secondNum = 0;
    private String calculationType = "";
    private String nextOperand = "";
    private String conversionType = "Dec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentInput = (TextView) findViewById(R.id.input_view);
        calculationView = (TextView) findViewById(R.id.calculation_view);
    }

    private void addToHistory(Double num) {
        calculationView.setText(calculationView.getText().toString() + num);
    }

    private void addToHistory(String operation) {
        calculationView.setText(calculationView.getText().toString() + operation);
    }

    private void clearHistory() {
        calculationView.setText("");
    }

    public void addDigit(View view) {
        if (currentInput.length() > 15)
            Toast.makeText(MainActivity.this, "Maximum number of digits met.", Toast.LENGTH_SHORT).show();
        else if (isConverted())
            Toast.makeText(MainActivity.this, "Cannot calculate while converted. Swap to decimal", Toast.LENGTH_SHORT).show();
        else
            updateCurrentDigitInput(view);
    }

    private boolean isDigit(String text) {
        return !text.equals(".");
    }

    private void setText(String text, TextView currentView) {
        currentView.setText(currentView.getText().toString() + text);
    }

    public void backspace(View view) {
        if (currentInput.length() > 0) {
            currentInput.setText(currentInput.getText().toString().substring(0, currentInput.length() - 1));
        }
    }

    public void clear(View view) {
        resetValues();
        clearHistory();
    }

    private void updateCurrentDigitInput(View view) {
        int currentLength = currentInput.length();
        Button currentButton = (Button) view;
        String currentButtonText = currentButton.getText().toString();
        if (isDigit(currentButtonText)) {
            int currentDigit = Integer.parseInt(currentButtonText);
            if (currentLength == 1 && currentInput.getText().toString().equals("0")) {
                currentInput.setText(currentDigit + "");
            } else {
                setText(currentDigit + "", currentInput);
            }
        } else {
            if (currentInput.getText().toString().contains(".")) {
                Toast.makeText(MainActivity.this, "Cannot have multiple decimals.", Toast.LENGTH_SHORT).show();
            } else {
                setText(currentButtonText, currentInput);
            }
        }
    }

    private void setValue(String currentNum) {
        double completedNumber = Double.parseDouble(currentNum);
        if (!hasFirst) {
            clearHistory();
            firstNum = completedNumber;
            hasFirst = true;
        } else {
            secondNum = completedNumber;
            hasSecond = true;
        }
        addToHistory(completedNumber);
    }

    public void addEvaluation(View view) {
        Button pressedButton = (Button) view;
        if (!isConverted()) {
            if (currentInput.length() > 0) {
                setValue(currentInput.getText().toString());
                nextOperand = pressedButton.getText().toString();
                if (calculationType.equals(""))
                    calculationType = nextOperand;
                addToHistory(calculationType);
                currentInput.setText("");
                evaluate();
            } else if (pressedButton.getText().toString().equals("-")) {
                currentInput.setText("-");
            } else {
                Toast.makeText(MainActivity.this, "Cannot operate without a number", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(MainActivity.this, "Cannot calculate while converted. Swap to decimal", Toast.LENGTH_SHORT).show();
    }

    private void evaluate() {
        if (hasSecond) {
            switch (calculationType) {
                case "+":
                    firstNum = firstNum + secondNum;
                    break;
                case "-":
                    firstNum = firstNum - secondNum;
                    break;
                case "/":
                    if (secondNum != 0)
                        firstNum = firstNum / secondNum;
                    else {
                        Toast.makeText(MainActivity.this, "You cannot divide by zero", Toast.LENGTH_SHORT).show();
                        clearHistory();
                        resetValues();
                    }
                    break;
                case "x":
                    firstNum = firstNum * secondNum;
                    break;
            }
            clearHistory();
            addToHistory(firstNum);
            calculationType = nextOperand;
            addToHistory(calculationType);
            hasSecond = false;
        }
    }


    public void equals(View view) {
        setValue(currentInput.getText().toString());
        evaluate();
        clearHistory();
        addToHistory(firstNum);
        resetValues();

    }

    private void resetValues() {
        currentInput.setText("");
        firstNum = 0;
        secondNum = 0;
        hasFirst = hasSecond = false;
        calculationType = nextOperand = "";
        conversionType = "Dec";
    }

    public void hexConversion(View view) {
        if (!conversionType.equals("Hex")) {
            String conversion = decimalConversion(currentInput.getText().toString());
            conversion = hexConversion(conversion);
            currentInput.setText(conversion);
            conversionType = "Hex";
        }
    }

    public String hexConversion(String currentNum) {
        return Integer.toHexString(Integer.parseInt(currentNum));
    }

    public String decimalConversion(String currentNum) {
        int base = conversionType.equals("Dec") ? 10 : conversionType.equals("Bin") ? 2 : 16;
        Integer convertedNum = Integer.parseInt(currentNum, base);
        return convertedNum.toString();
    }

    public void decimalConversion(View view) {
        currentInput.setText(decimalConversion(currentInput.getText().toString()));
        conversionType = "Dec";
    }

    public String binaryConversion(String currentNum) {
        return Integer.toBinaryString(Integer.parseInt(currentNum));
    }


    public void binaryConversion(View view) {
        if (!conversionType.equals("Bin")) {
            String conversion = decimalConversion(currentInput.getText().toString());
            conversion = binaryConversion(conversion);
            conversionType = "Bin";
            currentInput.setText(conversion);
        }
    }


    private boolean isConverted() {
        return !conversionType.equals("Dec");
    }
}
