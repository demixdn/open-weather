package com.github.demixdn.weather.data;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created on 13.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public class Observable {
    private final Map<Integer, WeakReference<Observer>> observers;

    public Observable() {
        this.observers = new HashMap<>();
    }

    public synchronized void addObserver(Observer o) {
        if (o == null) {
            throw new NullPointerException();
        }
        observers.put(o.hashCode(), new WeakReference<>(o));
    }

    public void notifyObservers() {
        Set<Map.Entry<Integer, WeakReference<Observer>>> entries = observers.entrySet();
        Iterator<Map.Entry<Integer, WeakReference<Observer>>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, WeakReference<Observer>> next = iterator.next();
            WeakReference<Observer> observerReference = next.getValue();
            if (observerReference != null) {
                Observer observer = observerReference.get();
                if (observer == null) {
                    iterator.remove();
                } else {
                    observer.update();
                }
            }
        }
    }
}
