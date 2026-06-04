package com.warehouse.common.constant;

public final class MqConstants {

    private MqConstants() {
    }

    public static final String TOPIC_STOCK_UPDATE = "warehouse-stock-update-topic";

    public static final String CONSUMER_GROUP_STOCK = "warehouse-stock-consumer-group";

    public static final String TAG_STOCK_IN = "stock-in";

    public static final String TAG_STOCK_OUT = "stock-out";

    public static final String TAG_STOCK_CHECK = "stock-check";
}
