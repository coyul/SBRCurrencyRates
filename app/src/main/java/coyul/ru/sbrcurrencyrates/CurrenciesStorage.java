package coyul.ru.sbrcurrencyrates;

import android.support.annotation.Nullable;

public final class CurrenciesStorage {

    private CurrenciesList mLoadedList;

    public synchronized boolean isReady() {
        return mLoadedList != null;
    }

    public synchronized CurrenciesList getLoadedList() {
        return mLoadedList;
    }

    public synchronized void setLoadedList(CurrenciesList loadedList) {
        mLoadedList = loadedList;
    }

    @Nullable
    public synchronized Currency findByCode(@Nullable String code) {
        if (mLoadedList != null && code != null) {
            for (Currency currency: mLoadedList.getCurrencies()) {
                if (currency.getCharCode().equals(code)) {
                    return currency;
                }
            }
        }

        return null;
    }
}
