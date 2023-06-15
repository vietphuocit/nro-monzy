package com.monzy.models.transaction;

import java.util.UUID;

public class TranHisMsg {
    public UUID ID;
    public String user;
    public String commandInd;
    public long tranId;
    public long clientTime;
    public long ackTime;
    public long finishTime;
    public int tranType;
    public int io;
    public String partnerId;
    public String partnerCode;
    public String partnerName;
    public int amount;
    public String comment;
    public int status;
    public String ownerNumber;
    public String ownerName;
    public int moneySource;
    public String desc;
    public String serviceMode;
    public int originalAmount;
    public String serviceId;
    public int quantity;
    public long lastUpdate;
    public String share;
    public int receiverType;
    public String extras;
    public String channel;
    public String otpType;
    public String ipAddress;
    public EnableOptions enableOptions;
    public String _class;

    // Add getters and setters for each field

    public static class EnableOptions {
        public boolean voucher;
        public boolean discount;
        public boolean prepaid;
        public String desc;

        // Add getters and setters for each field
    }
}
