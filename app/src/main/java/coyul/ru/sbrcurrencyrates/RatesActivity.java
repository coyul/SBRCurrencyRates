package coyul.ru.sbrcurrencyrates;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import ru.sberbank.learning.rates.R;


public class RatesActivity extends Activity {

    //list view
    private ListView mCurrenciesListView;
    //screen with "downloading" text
    private TextView mWaitingView;
    //custom list adapter
    private CurrenciesAdapter mCurrenciesAdapter;
    //currency storage
    private CurrenciesStorage mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        mCurrenciesListView = (ListView) findViewById(R.id.currency_list);
        mWaitingView = (TextView) findViewById(R.id.waiting_screen);

        //storage is keeping in simple custom application class (instead of singleton)
        CurrenciesApplication application = (CurrenciesApplication) getApplication();
        mStorage = application.getCurrenciesStorage();

        //if storage is empty, try to get it by custom class in another thread (async task)
        if (!mStorage.isReady()) {
            mWaitingView.setVisibility(View.VISIBLE);
            mCurrenciesListView.setVisibility(View.GONE);

            HelpCurrencyTask task = new HelpCurrencyTask(mStorage, this);
            task.execute();
        }


        else {
            setAdapter();
        }

        //on click go to calculator of this currency
        mCurrenciesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Currency currency = mCurrenciesAdapter.getItem(position);
                Intent intent  = new Intent(getApplicationContext(), CalcActivity.class);
                intent.putExtra("code", currency.getCharCode());
                intent.putExtra("exchangeRate", currency.getValue()/currency.getNominal());

                startActivity(intent);

            }
        });


    }

    //method gets list from storage and set adapter
    public void setAdapter() {
        List<Currency> mCurrencyList = mStorage.getLoadedList().getCurrencies();
        mCurrenciesAdapter = new CurrenciesAdapter(mCurrencyList);
        mCurrenciesListView.setAdapter(mCurrenciesAdapter);

        mWaitingView.setVisibility(View.GONE);
        mCurrenciesListView.setVisibility(View.VISIBLE);
    }


    /*
    class extends AsyncTask (to get data from URL)
    has two weak reference on storage and parent activity
     */
    public class HelpCurrencyTask extends AsyncTask<Void, Void, CurrenciesList> {

        private static final String URL_PATH = "http://www.cbr.ru/scripts/XML_daily.asp";

        private WeakReference<CurrenciesStorage> mStorageWeak;
        private WeakReference<Activity> mActivityTargetWeak;

        public HelpCurrencyTask(CurrenciesStorage mStorage, Activity mActivityTarget) {
            this.mStorageWeak = new WeakReference<CurrenciesStorage>(mStorage);
            this.mActivityTargetWeak = new WeakReference<Activity>(mActivityTarget);
        }

        @Override
        protected CurrenciesList doInBackground(Void... params) {

            CurrenciesList resultList = null;
            try {

                URL url = new URL(URL_PATH);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                resultList = CurrenciesList.readFromStream(connection.getInputStream());


            } catch (MalformedURLException e1) {
                Log.d(this.getClass().getName(), "MalformedURLException", e1);
            } catch (IOException e2) {
                Log.d(this.getClass().getName(), "IOException", e2);
            }

            return resultList;
        }

        @Override
        protected void onPostExecute(CurrenciesList currenciesList) {
            CurrenciesStorage currenciesStoragePost = mStorageWeak.get();
            Activity activityPost = mActivityTargetWeak.get();

            if (currenciesStoragePost != null) {
                currenciesStoragePost.setLoadedList(currenciesList);
            }

            if (activityPost != null) {
                setAdapter();
            }

            super.onPostExecute(currenciesList);


        }
    }

}
