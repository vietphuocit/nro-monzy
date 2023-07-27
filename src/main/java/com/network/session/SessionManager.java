package com.network.session;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    private static SessionManager i;
    private final List<Session> sessions = new ArrayList<>();

    public SessionManager() {
    }

    public static SessionManager gI() {
        if (i == null) {
            i = new SessionManager();
        }
        return i;
    }

}
