package it.alessangiorgi.ndt7clientandroidjava;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import speedtest.Ndt7ServerSearch;
import speedtest.model.Results;
import speedtest.model.Server;

public class ServerAndISPHelper {

    private static final String NDT7_SERVER_LOCATE = "https://locate.measurementlab.net/v2/nearest/ndt/ndt7";

    private static OnServerListener callback;

    public ServerAndISPHelper() {
    }

    public void getInfo() {
        new ISPTask().execute();
    }

    public void setOnServerListener(OnServerListener listener) {
        callback = listener;
    }

    public interface OnServerListener {
        void onServerSearchStarted();

        void onServerFound(List<Server> server);

        void onServerError();
    }

    private static class ISPTask extends AsyncTask<Void, Void, List<Server>> {
        private String TAG = getClass().getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "onPreExecute: start");
            if (callback != null) callback.onServerSearchStarted();
        }

        @Override
        protected List<Server> doInBackground(Void... voids) {
            List<Server> server = new ArrayList<>();
            Results results;
            try {
                Ndt7ServerSearch ndt7ServerSearch = new Ndt7ServerSearch(new URL(NDT7_SERVER_LOCATE));
                results = new Gson().fromJson(ndt7ServerSearch.getServer(), Results.class);
                for(int i = 0; i < results.results.size(); i++) {
                    server.add(new Gson().fromJson(results.results.get(i), Server.class));
                }

            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }

            return server;
        }

        @Override
        protected void onPostExecute(List<Server> server) {
            super.onPostExecute(server);
            Log.e(TAG, "onPostExecute: end");

            if (callback != null) {
                if (server == null) {
                    callback.onServerError();
                } else {
                    callback.onServerFound(server);
                }
            }

        }
    }
}
