package coyul.ru.sbrcurrencyrates;

import android.app.Application;


public class CurrenciesApplication extends Application {

    private CurrenciesStorage mCurrenciesStorage = new CurrenciesStorage();

    public CurrenciesStorage getCurrenciesStorage() {
        return mCurrenciesStorage;
    }
}
