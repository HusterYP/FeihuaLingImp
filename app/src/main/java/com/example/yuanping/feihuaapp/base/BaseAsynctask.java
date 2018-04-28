package com.example.yuanping.feihuaapp.base;

import android.os.AsyncTask;

/**
 * Created by yuanping on 2/4/18.
 * 提供处理异常等
 */

public abstract class BaseAsynctask extends AsyncTask<Void, Void, Void> {
    Exception exception;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        doOnPrevious();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            doInBackgroud();
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        doPost(exception);
    }

    public abstract void doOnPrevious();

    public abstract void doInBackgroud() throws Exception;

    public abstract void doPost(Exception exception);

}
