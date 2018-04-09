package org.semux.db;

import org.apache.commons.lang3.tuple.Pair;
import org.semux.crypto.Hex;
import org.semux.util.ClosableIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MysqlDb implements Db{

    private static final Logger logger = LoggerFactory.getLogger(MysqlDb.class);

    JdbcTemplate jdbcTemplate;
    String tableName;

    public MysqlDb(JdbcTemplate jdbcTemplate, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
    }


    @Override
    public byte[] get(byte[] key) {
        try {
            String id = Hex.encode(key);
            String sql = "SELECT value FROM " + tableName + " where id = ?";

            List<byte[]> values = jdbcTemplate.query(sql, new Object[]{id}, new RowMapper<byte[]>() {
                public byte[] mapRow(ResultSet result, int rowNum) throws SQLException {
                    Blob blob = result.getBlob("value");
                    byte[] v = blob.getBytes(1, (int) blob.length());
                    return v;
                }
            });
            if (values.size() <= 0) {
                return null;
            } else {
                return values.get(0);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void put(byte[] key, byte[] value) {
        try {
            String id = Hex.encode(key);
            String sql = "INSERT INTO " + tableName + " (id, value) values (?, ?) ON DUPLICATE KEY UPDATE value = ?";
            jdbcTemplate.update(sql, new Object[] {id, value, value});
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(byte[] key) {
        try {
            String id = Hex.encode(key);
            String sql = "DELETE FROM " + tableName + " WHERE id = ?";
            jdbcTemplate.update(sql, new Object[] {id});
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBatch(List<Pair<byte[], byte[]>> pairs) {
        try {
            for (Pair<byte[], byte[]> p : pairs) {
                if (p.getValue() == null) {
                    delete(p.getLeft());
                } else {
                    put(p.getLeft(), p.getRight());
                }
            }
        } catch (Exception e) {
            logger.error("Failed to update batch", e);
            e.printStackTrace();
        }
    }

    @Override
    public ClosableIterator<Map.Entry<byte[], byte[]>> iterator() {
        return iterator(null);
    }

    @Override
    public ClosableIterator<Map.Entry<byte[], byte[]>> iterator(byte[] prefix) {

        String sql = "SELECT id, value FROM " + tableName ;

        List<Map.Entry<byte[], byte[]>> values = jdbcTemplate.query(sql, new Object[]{}, new RowMapper<Map.Entry<byte[], byte[]>>() {
            public Map.Entry<byte[], byte[]> mapRow(ResultSet result, int rowNum) throws SQLException {
                String key = result.getString("id");
                Blob blob = result.getBlob("value");
                byte[] v = blob.getBytes(1, (int) blob.length());
                AbstractMap.SimpleImmutableEntry<byte[], byte[]> entry = new AbstractMap.SimpleImmutableEntry<>(Hex.decode(key), v);

                return entry;
            }
        });

        final Iterator<Map.Entry<byte[], byte[]>> it = values.iterator();
        return new ClosableIterator<Map.Entry<byte[], byte[]>>() {

            private ClosableIterator<Map.Entry<byte[], byte[]>> initialize() {
//                if (prefix != null) {
//                    it.seek(prefix);
//                } else {
//                    it.seekToFirst();
//                }
                return this;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Map.Entry<byte[], byte[]> next() {
                return it.next();
            }
        }.initialize();
    }

    @Override
    public void close() {

    }

    @Override
    public void destroy() {

    }


    public static class MysqlDbFactory implements DbFactory {

        DataSource dataSource;

        public MysqlDbFactory(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public Db getDB(DbName name) {
            return new MysqlDb(new JdbcTemplate(dataSource), "bit_"+name.name().toLowerCase());
        }

        @Override
        public void close() {

        }
    }


    public static void main(String[] args) {

        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        DataSource dataSource = (DataSource) context.getBean("dataSource");

        MysqlDbFactory factory = new MysqlDbFactory(dataSource);

        Db db = factory.getDB(DbName.INDEX);

        System.out.println("hello:"+ String.valueOf(db.get("hello".getBytes())));

        db.put("hello".getBytes(), "hello world 3".getBytes());

        context.close();
    }
}
