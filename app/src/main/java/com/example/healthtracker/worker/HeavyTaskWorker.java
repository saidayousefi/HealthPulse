package com.example.healthtracker.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class HeavyTaskWorker extends Worker {

    public HeavyTaskWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {

            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Result.failure();
        }
        return Result.success();
    }
}
