package net.pongjour.notifications;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

public class Notification  {

    private String _name;

    private Object _sender;

    private Map _userInfoMap;

    /**
     * Factory object for NotificationObjects.
     * 
     * @noarea
     **/
    public static class Factory {
        public static Notification create(String aName, Object aSender, Map aUserInfoMap) {
            return new Notification(aName, aSender, aUserInfoMap);
        }

        public static Notification create(String aName, Object aSender) {
            return new Notification(aName, aSender);
        }
    }

    private Notification(String aName, Object aSender, Map aUserInfoMap) {
        _name = aName;
        _sender = aSender;
        if (aUserInfoMap == null) {
            aUserInfoMap = Collections.EMPTY_MAP;
        }
        _userInfoMap = aUserInfoMap;
    }

    private Notification(String aName, Object aSender) {
        this(aName, aSender, null);
    }

    public String getName() {
        return _name;
    }

    public Object getSender() {
        return _sender;
    }

    public boolean hasUserInfo(String aKey) {
        return _userInfoMap.containsKey(aKey);
    }

    public Object getUserInfo(String aKey) {
        if (_userInfoMap.containsKey(aKey)) {
            return _userInfoMap.get(aKey);
        } else {
            throw new NoSuchElementException("Key " + aKey + " not found in user info map");
        }
    }

    public Map getUserInfoMap() {
        return _userInfoMap;
    }

    /**
     * @noverify
     * @area Implementation
     **/
    public boolean equals(Object anObject) {
        if (anObject == this) {
            return true;
        }

        if (anObject instanceof Notification) {
            Notification aNotification = (Notification) anObject;
            if (getName().equals(aNotification.getName()) && getSender() == aNotification.getSender() && getUserInfoMap().equals(aNotification.getUserInfoMap())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @noverify
     * @area Implementation
     **/
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * @noverify
     * @area Implementation
     **/
    public String toString() {
        return getName() + " from " + getSender().toString();
    }

}