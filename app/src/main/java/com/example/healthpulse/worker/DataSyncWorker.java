package com.example.healthpulse.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import android.util.Log;

public class DataSyncWorker extends Worker {

    public DataSyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        boolean success = syncDataWithServer();
        return success ? Result.success() : Result.failure();
    }

    private boolean syncDataWithServer() {
        Log.d("DataSyncWorker", "Syncing data with server...");
        return true;
    }
}
