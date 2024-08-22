package com.qatapultt.tryscannerndk;

import androidx.lifecycle.ViewModel;

import com.qatapultt.tryscannerndk.models.QCode;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MainViewModel extends ViewModel {
    public final ConcurrentLinkedQueue<QCode> detectedQueue = new ConcurrentLinkedQueue<>();
    public final HashMap<String, QCode> uniqueResponses = new HashMap<>();

    public void processResponses() {
        QCode qCode = null;
        while (!detectedQueue.isEmpty()) {
            qCode = detectedQueue.poll();
            if (qCode != null) {
                uniqueResponses.put(qCode.id, qCode);
            }
        }
    }
}
