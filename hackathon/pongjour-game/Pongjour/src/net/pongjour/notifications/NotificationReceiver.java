package net.pongjour.notifications;




/**
 * The interface an object must implement to be able to receive notifications.
 * Usually it will be used to create an anonymous inner classes implementing
 * this interface and having a callback to the receiver method.
 * 
 * @area Receive receiving the notification
 **/

public interface NotificationReceiver {

    /**
     * The method will be invoked when a notification center delivers the
     * notification to the receiver.
     * 
     * @param aNotification
     *            that is delivered.
     * @area Receive
     **/
    public void receive(Notification aNotification);

}