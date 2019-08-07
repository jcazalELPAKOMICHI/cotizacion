package com.example.cotizaciondolar;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;



public class NumberTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private DecimalFormat dfNd;
    private boolean hasFractionalPart;

    private EditText et;

    public NumberTextWatcher(EditText et) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        df = createDecimalFormat();
        dfNd = new DecimalFormat("#,###", symbols);
        this.et = et;
        hasFractionalPart = false;
    }

    public static DecimalFormat createDecimalFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat df = new DecimalFormat("#,###.##", symbols);
        df.setDecimalSeparatorAlwaysShown(true);
        return df;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        try {
            int iniLen, endLen;
            iniLen = et.getText().length();

            if (s.toString().startsWith(",") || s.toString().startsWith(".")) {
                et.setText(s.subSequence(1, s.length()));
            }

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");

            Number n = df.parse(v);
            if (maxNumberOverpassed(v)) {
                //prevent change, remove last char
                String newValue = et.getText().toString();
                newValue = newValue.substring(0, newValue.length() - 1);
                et.setText(newValue);

                //select at the end
                et.setSelection(et.getText().length());
                et.addTextChangedListener(this);
                return;
            }
            int cp = et.getSelectionStart();
            if (hasFractionalPart) {
                String format = df.format(n);
                et.setText(format);
            } else {
                String format = dfNd.format(n);
                et.setText(format);
            }
            endLen = et.getText().length();
            int sel = (cp + (endLen - iniLen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }
        } catch (NumberFormatException nfe) {
        } catch (ParseException e) {
        }

        et.addTextChangedListener(this);
    }

    int maxReal = 13;
    int maxDecimal = 2;

    private boolean maxNumberOverpassed(String s) {

        String[] parts = s.split("//" + df.getDecimalFormatSymbols().getDecimalSeparator());
        return parts[0].length() > maxReal || parts.length > 1 && parts[1].length() > maxDecimal;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        hasFractionalPart = s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
    }

}