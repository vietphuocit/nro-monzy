package com.network.server;

import com.network.session.ISession;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MonzySessionManager {

    private static MonzySessionManager i;
    private final List<ISession> sessions = new ArrayList<>();

    public MonzySessionManager() {
    }

    public static MonzySessionManager gI() {
        if (i == null) {
            i = new MonzySessionManager();
        }
        return i;
    }

    public void putSession(ISession session) {
        this.sessions.add(session);
    }

    public void removeSession(ISession session) {
        this.sessions.remove(session);
    }

    public List<ISession> getSessions() {
        return this.sessions;
    }

    public ISession findByID(long id) throws Exception {
        if (this.sessions.isEmpty()) {
            throw new Exception("Session " + id + " không tồn tại");
        } else {
            Iterator var3 = this.sessions.iterator();
            ISession session;
            do {
                if (!var3.hasNext()) {
                    throw new Exception("Session " + id + " không tồn tại");
                }
                session = (ISession) var3.next();
                if (session.getID() > id) {
                    throw new Exception("Session " + id + " không tồn tại");
                }
            } while (session.getID() != id);
            return session;
        }
    }

    public int getNumSession() {
        return this.sessions.size();
    }

}
