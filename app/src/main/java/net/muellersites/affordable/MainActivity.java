package net.muellersites.affordable;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.muellersites.affordable.Objects.Balance;
import net.muellersites.affordable.Utils.DBHelper;
import net.muellersites.affordable.Utils.SimpleTwoFingerDoubleTapDetector;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    private DBHelper dbHelper;
    private Balance balance;
    private TextView balance_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getApplicationContext());
        try {
            balance = dbHelper.getBalance();
        } catch (Exception e) {
            balance = new Balance();
            dbHelper.insertBalance(balance);
        }

        balance_view = (TextView) findViewById(R.id.balance_view);
        Log.d("Dev", "Curr Val: " + balance.getCurrent_value());
        Log.d("Dev", balance_view.getText().toString());
        balance_view.setText(String.valueOf(balance.getCurrent_value()));

        FloatingActionButton fab_plus = (FloatingActionButton) findViewById(R.id.fab_plus);
        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(true);
            }
        });

        FloatingActionButton fab_minus = (FloatingActionButton) findViewById(R.id.fab_minus);
        fab_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(false);
            }
        });
    }

    private void openDialog(final Boolean positive) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(false);
        Button accept = (Button) dialog.findViewById(R.id.form_accept);
        String button_text;
        Integer button_color;
        if (positive) {
            button_text = getResources().getString(R.string.add);
            button_color = ContextCompat.getColor(getApplicationContext(), R.color.colorPositive);
        }else {
            button_text = getResources().getString(R.string.subtract);
            button_color = ContextCompat.getColor(getApplicationContext(), R.color.colorNegative);
        }
        accept.setText(button_text);
        accept.setBackgroundColor(button_color);
        ImageButton exit = (ImageButton) dialog.findViewById(R.id.closeDialog);

        final EditText int_field = (EditText) dialog.findViewById(R.id.form_integer);
        dialog.show();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = Integer.parseInt(int_field.getText().toString());
                if (positive){
                    balance.setCurrent_value(balance.getCurrent_value() + value);
                }else {
                    balance.setCurrent_value(balance.getCurrent_value() - value);
                }
                dbHelper.updateBalance(balance);
                balance_view.setText(String.valueOf(balance.getCurrent_value()));
                dialog.dismiss();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void clearBalance() {
        dbHelper.clearBalances();
        balance = new Balance();
        dbHelper.insertBalance(balance);
        balance_view.setText("0");
    }

    SimpleTwoFingerDoubleTapDetector multiTouchListener = new SimpleTwoFingerDoubleTapDetector() {
        @Override
        public void onTwoFingerDoubleTap() {
            clearBalance();
            Toast.makeText(MainActivity.this, "Cleared Balance", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return multiTouchListener.onTouchEvent(event) || super.onTouchEvent(event);
    }
}
