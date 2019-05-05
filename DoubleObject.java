package com.android.ang.seprocessmonitor;

public class DoubleObject {

    private Object object1, object2;
    private boolean notified;

    public DoubleObject(Object o1, Object o2) {
        object1 = o1;
        object2 = o2;
        notified = false;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public Object getObject1() {
        return object1;
    }

    public Object getObject2() {
        return object2;
    }

    public boolean isNotified() {
        return notified;
    }
}
