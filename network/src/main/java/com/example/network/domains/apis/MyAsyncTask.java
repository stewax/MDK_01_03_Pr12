package com.example.network.domains.apis;


import androidx.loader.content.AsyncTaskLoader;

import com.example.network.domains.callbacks.MyResponseCallbacks;
import android.os.AsyncTask;

public class MyAsyncTask extends AsyncTask<Void, Void, String> {
    protected MyResponseCallbacks callback;

    public  MyAsyncTask(MyResponseCallbacks callback){
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... voids){
        return "";
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        if (callback != null) {
            if (result != null && !result.startsWith("Error"))
                callback.onCompile(result);
            else
                callback.onError(result);
        }
    }
}
