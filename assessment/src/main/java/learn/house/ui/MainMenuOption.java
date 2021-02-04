package learn.house.ui;

import com.sun.tools.javac.Main;

public enum MainMenuOption {

    EXIT(0, "Exit"),
    VIEW_RESERVATIONS_HOST(1, "View Reservations by Host"),
    VIEW_RESERVATIONS_GUEST(2, "View Reservations by Guest"),
    VIEW_RESERVATIONS_STATE(3, "View Reservations by State"),
    CREATE_RESERVATION(4, "Create a Reservation"),
    UPDATE_RESERVATION(5, "Edit an Existing Reservation"),
    DELETE_RESERVATION(6, "Cancel a Reservation");

    private int value;
    private String message;

    private MainMenuOption(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
