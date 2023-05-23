package com.database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log implements Runnable {

    private static final Logger logger = Logger.getLogger(Log.class.getName());
    private static final Log INSTANCE = new Log();
    private final ConcurrentLinkedQueue<String> queries;
    private BufferedWriter writer;

    private Log() {
        queries = new ConcurrentLinkedQueue<>();
        try {
            writer = new BufferedWriter(new FileWriter("monzy_log/sql_" + System.currentTimeMillis() + ".txt"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error creating log file", e);
        }
    }

    public static Log getInstance() {
        return INSTANCE;
    }

    public void log(String query) {
        queries.add(query);
    }

    @Override
    public void run() {
        while (Database.LOG_QUERY) {
            String query = queries.poll();
            if (query != null) {
                try {
                    writer.write(query + "\n");
                    writer.flush();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error writing to log file", e);
                }
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "Log thread interrupted", e);
            }
        }
        dispose();
    }

    private void dispose() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error closing log file", e);
            }
        }
        queries.clear();
    }

}
