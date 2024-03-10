package fr.internetcartographier.controller.topmenubar.contextmenu;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;

import java.util.concurrent.atomic.AtomicLong;

public class Notification {

    private static final AtomicLong idGenerator = new AtomicLong(0);
    private final long id;

    private final NotificationType type;

    private final String text;

    private final long date;

    private final BooleanProperty isRead;

    public Notification(String message, NotificationType type) {
        id = idGenerator.incrementAndGet();
        this.text = message;
        this.type = type;
        date = System.currentTimeMillis();
        isRead = new SimpleBooleanProperty(false);
    }

    public long getId() {
        return id;
    }

    public NotificationType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public long getDate() {
        return date;
    }

    public BooleanProperty getReadProperty() {
        return isRead;
    }

    public void read() {
        isRead.set(true);
    }

    public enum NotificationType {

        INFORMATION("notification.png");

        private final Image icon;

        NotificationType(String iconPath) {
            icon = new Image(iconPath);
        }

        public Image getIcon() {
            return icon;
        }

    }

}
