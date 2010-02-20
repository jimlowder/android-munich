package net.pongjour.notifications;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import net.pongjour.notifications.Notification;
import net.pongjour.notifications.NotificationReceiver;

import android.util.Log;

/**
 * Implementation of the NotificationCenter interface. This class has to be
 * initialized. <h3>Initialization</h3> Use the {@link #createFactory
 * createFactory} method to create a factory object which can be used as the
 * Factory in the {@link org.cosada.foundation.NotificationCenter
 * NotificationCenter}. E.g.
 * <code>NotificationCenter.Factory.setFactory(new NotificationCenterObject().createFactory())</code>
 * . <br>
 * <b>Note</b>: Initialization is automatically performed in the startup
 * sequence.
 * 
 * @area Implementation implementation
 **/
public class NotificationCenter extends Object {

	private static final boolean _usesWeakReferences = true;

	protected static boolean usesWeakReferences() {
		return _usesWeakReferences;
	}

	private Map _receiverMap;
	
	public static final NotificationCenter CENTER = new NotificationCenter();

	{
		_receiverMap = new HashMap();
		
	}

	private static void debug(String msg) {
		Log.d("NotificationCenter", msg);
	}

	protected void _addReceiver(NotificationReceiver aReceiver, String aNotificationName, Object aSender) {
		initSetIfNeccessary(aNotificationName, aSender);
		Object aR = aReceiver;
		if (usesWeakReferences()) {
			aR = new WeakReference(aReceiver);
		}
		((Set) ((Map) getReceiverMap().get(aNotificationName)).get(aSender)).add(aR);
		debug("Adding new receiver " + aReceiver.toString() + " for " + aNotificationName);
	}

	protected void _removeReceiver(NotificationReceiver aReceiver) {
		Iterator nameIterator = getReceiverMap().values().iterator();
		while (nameIterator.hasNext()) {
			Iterator senderIterator = ((Map) nameIterator.next()).values().iterator();
			while (senderIterator.hasNext()) {
				Set receiverSet = ((Set) senderIterator.next());
				if (usesWeakReferences()) {
					Iterator receiverIterator = receiverSet.iterator();
					while (receiverIterator.hasNext()) {
						WeakReference currentReceiverWR = (WeakReference) receiverIterator.next();
						if (aReceiver.equals(currentReceiverWR.get())) {
							receiverIterator.remove();
							debug("Removed receiver");
						}
					}
				} else {
					receiverSet.remove(aReceiver);
				}
			}
		}
	}

	protected void _postNotification(Notification aNotification) {
		Set receivers = getReceivers(aNotification);
		debug("Posting notification " + aNotification + "\nto: " + receivers);
		if (receivers != null) {
			Iterator receiverIterator = receivers.iterator();
			while (receiverIterator.hasNext()) {
				Object aReceiver = receiverIterator.next();
				if (usesWeakReferences()) {
					aReceiver = ((WeakReference) aReceiver).get();
					if (aReceiver == null) {
						debug("a receiver which subscribed to Notification " + aNotification.getName() + " did get gced without unsubscribing. Removing reference");
						receiverIterator.remove();
						continue;
					}
				}
				((NotificationReceiver) aReceiver).receive(aNotification);
			}
		}
	}

	// end NotificationCenter Interface

	protected Set getOmniscientReceivers() {
		return getReceivers(null, null);
	}

	protected Set getReceiversBySender(Object aSender) {
		return getReceivers(null, aSender);
	}

	protected Set getReceiversByName(String aName) {
		return getReceivers(aName, null);
	}

	protected Set getReceivers(String aName, Object aSender) {

		if (!getReceiverMap().containsKey(aName))
			return Collections.EMPTY_SET;

		Map nameMap = (Map) getReceiverMap().get(aName);
		// debug("Name Map for " + aName + " : " + nameMap);

		if (!nameMap.containsKey(aSender))
			return Collections.EMPTY_SET;

		return (Set) nameMap.get(aSender);
	}

	protected Set getReceivers(Notification aNotification) {
		Set resultSet = new HashSet(getOmniscientReceivers());
		resultSet.addAll(getReceiversByName(aNotification.getName()));
		resultSet.addAll(getReceiversBySender(aNotification.getSender()));
		resultSet.addAll(getReceivers(aNotification.getName(), aNotification.getSender()));

		debug("Receivers for " + aNotification.getName() + " :" + resultSet.size());

		return resultSet;
	}

	private Map getReceiverMap() {
		return _receiverMap;
	}

	private void initSetIfNeccessary(String aName, Object aSender) {
		if (!getReceiverMap().containsKey(aName)) {
			getReceiverMap().put(aName, new WeakHashMap());
			debug("Creating new Name Map for " + aName);
		}
		Map nameMap = ((Map) getReceiverMap().get(aName));
		if (!nameMap.containsKey(aSender)) {
			nameMap.put(aSender, new HashSet());
			debug("Creating new Sender Map for " + aSender);
		}
	}

	
	public static void postNotification(Notification aNotification) {
		CENTER._postNotification(aNotification);
	}
	
	public static void postNotification(String aNotificationName, Object aSender, Map aMap) {
		postNotification(Notification.Factory.create(aNotificationName, aSender, aMap));
	}

	public static void postNotification(String aNotificationName, Object aSender) {
		postNotification(Notification.Factory.create(aNotificationName, aSender, null));
	}
	
	public static void addReceiver(NotificationReceiver aReceiver) {
		addReceiver(aReceiver, null, null);
	}

	public static void addReceiver(NotificationReceiver aReceiver, String aNotificationName, Object aSender) {
		CENTER._addReceiver(aReceiver, aNotificationName, aSender);
	}
	
	public static void removeReceiver(NotificationReceiver aReceiver) {
		CENTER._removeReceiver(aReceiver);
	}

}
