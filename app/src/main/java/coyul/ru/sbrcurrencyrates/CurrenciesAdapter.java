package coyul.ru.sbrcurrencyrates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.sberbank.learning.rates.R;

/*
Adapter to currencyList, adjust data on each currency to relative view
 */

public class CurrenciesAdapter extends BaseAdapter {

    private List<Currency> mCurrencyList;

    public CurrenciesAdapter(List<Currency> mCurrencyList) {
        this.mCurrencyList = mCurrencyList;
    }

    @Override
    public int getCount() {
        return mCurrencyList.size();
    }

    @Override
    public Currency getItem(int position) {
        return mCurrencyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCurrencyList.get(position).getId().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.currency_list_item, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.mCode = (TextView) view.findViewById(R.id.code);
            holder.mName = (TextView) view.findViewById(R.id.name);
            holder.mNominal = (TextView) view.findViewById(R.id.nominal);
            holder.mValue = (TextView) view.findViewById(R.id.value);

            view.setTag(holder);

        }

        ViewHolder holder = (ViewHolder) view.getTag();
        Currency currency = getItem(position);

        if (currency != null) {
            holder.mName.setText(currency.getName());
            //string format is taken from parent resources
            holder.mValue.setText(parent.getResources().getString(R.string.two_digits_number_format, currency.getValue()));
            holder.mNominal.setText(parent.getResources().getString(R.string.nominal_format, currency.getNominal()));
            holder.mCode.setText(parent.getResources().getString(R.string.code_format, currency.getCharCode(), currency.getNumCode()));

        }

        return view;
    }


    private static class ViewHolder {
        private TextView mNominal;
        private TextView mName;
        private TextView mCode;
        private TextView mValue;
    }
}
