package coyul.ru.sbrcurrencyrates;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import ru.sberbank.learning.rates.R;


public class CalcActivity extends Activity {

    private EditText currencyNumberEdit;
    private EditText rubNumberEdit;
    private TextView currencyNameView;
    private TextView currencyResultNameView;
    private TextView resultInRub;
    private TextView resultInCurrency;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        currencyNumberEdit = (EditText) findViewById(R.id.number_in_currency);
        rubNumberEdit = (EditText) findViewById(R.id.number_in_rub);
        currencyNameView = (TextView) findViewById(R.id.currency_name);
        currencyResultNameView = (TextView) findViewById(R.id.currency_result_name);
        resultInRub = (TextView) findViewById(R.id.result_in_rub);
        resultInCurrency = (TextView) findViewById(R.id.result_in_currency);

        //set currency name on views
        String currencyName = getIntent().getStringExtra("code");
        currencyNameView.setText(currencyName);
        currencyResultNameView.setText(currencyName);

        final double exchangeRate = getIntent().getDoubleExtra("exchangeRate", 0);

        //calculate from currency to rubles, result is calculating immediately by onTextChanged
        currencyNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.equals("")) {
                    Double result = 0.0;
                    try {
                        result = Double.parseDouble(s.toString()) * exchangeRate;
                    } catch (NumberFormatException e) {
                        Log.d(this.getClass().getName(), "NumberFormatException", e);
                    }
                    resultInRub.setText(getString(R.string.two_digits_number_format, result));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) resultInRub.setText("");
            }
        });

        //calculate from rubles to currency, result is calculating immediately by onTextChanged
        rubNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.equals("")) {
                    Double result = 0.0;
                    try {
                        result = Double.parseDouble(s.toString()) / exchangeRate;
                    } catch (NumberFormatException e) {
                        Log.d(this.getClass().getName(), "NumberFormatException", e);
                    }
                    resultInCurrency.setText(getString(R.string.two_digits_number_format, result));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) resultInCurrency.setText("");
            }
        });


    }
}
