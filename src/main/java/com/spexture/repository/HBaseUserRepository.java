package com.spexture.repository;

import com.spexture.model.User;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

@Repository
public class HBaseUserRepository {

    private static final byte[] TABLE_NAME = Bytes.toBytes("users");
    private static final byte[] CF_INFO = Bytes.toBytes("info");
    private static final byte[] Q_EMAIL = Bytes.toBytes("email");
    private static final byte[] Q_PASSWORD = Bytes.toBytes("password");

    private final Connection connection;

    public HBaseUserRepository() throws IOException {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.rootdir", "file:///path/to/your/hbase/data");
        config.set("hbase.zookeeper.property.dataDir", "/path/to/zookeeper/data");
        config.set("hbase.zookeeper.quorum", "localhost");
        config.set("hbase.cluster.distributed", "false");
        
        this.connection = ConnectionFactory.createConnection(config);
        initializeTable();
    }

    private void initializeTable() throws IOException {
        Admin admin = connection.getAdmin();
        if (!admin.tableExists(TableName.valueOf(TABLE_NAME))) {
            TableDescriptorBuilder builder = TableDescriptorBuilder.newBuilder(TableName.valueOf(TABLE_NAME));
            builder.setColumnFamily(ColumnFamilyDescriptorBuilder.of(CF_INFO));
            admin.createTable(builder.build());
        }
        admin.close();
    }

    public User saveUser(User user) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(TABLE_NAME))) {
            Put put = new Put(user.getIdAsBytes());
            put.addColumn(CF_INFO, Q_EMAIL, Bytes.toBytes(user.getEmail()));
            put.addColumn(CF_INFO, Q_PASSWORD, Bytes.toBytes(user.getPassword()));
            table.put(put);
            return user;
        }
    }

    public User getUser(String id) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(TABLE_NAME))) {
            Get get = new Get(Bytes.toBytes(id));
            Result result = table.get(get);
            if (result.isEmpty()) return null;
            return User.fromBytes(
                Bytes.toBytes(id),
                result.getValue(CF_INFO, Q_EMAIL),
                result.getValue(CF_INFO, Q_PASSWORD)
            );
        }
    }

    // Additional methods like update or delete can be added here
}
