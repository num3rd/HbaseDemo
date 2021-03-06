package com.num3rd.hbase.scheduler.put;

import com.num3rd.hbase.utils.Contants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.logging.Logger;

public class ScheduledPut implements Runnable {
    Logger LOG = Logger.getLogger(getClass().getName());
    private Configuration configuration;

    public ScheduledPut(Configuration configuration) {
        this.configuration = configuration;
    }

    public void run() {
        LOG.info(getClass().getName());
        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(configuration);
            Table table = connection.getTable(TableName.valueOf(Contants.TABLE_NAME));

            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            String reverseCurrentTimeMillis = new StringBuffer(currentTimeMillis).reverse().toString();
            Put put = new Put(Bytes.toBytes(reverseCurrentTimeMillis.concat("-r")));
            put.addColumn(
                    Bytes.toBytes(Contants.CF_DEFAULT),
                    Bytes.toBytes(currentTimeMillis.concat("-q")),
                    Bytes.toBytes(currentTimeMillis.concat("-v"))
            );

            table.put(put);

            System.out.println(reverseCurrentTimeMillis.concat("-r"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
