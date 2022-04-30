package ru.job4j.todo.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@ThreadSafe
public class ItemDbStore {

    private final BasicDataSource pool;

    public ItemDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    private LocalDateTime getLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    private Timestamp getTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM item")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    items.add(new Item(it.getInt("id"),
                            it.getString("description"),
                            getLocalDateTime(it.getTimestamp("created")),
                            it.getBoolean("done"))
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public Item add(Item item) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO item(description,"
                             + " created,"
                             + " done) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, item.getDescription());
            ps.setTimestamp(2, getTimestamp(item.getCreated()));
            ps.setBoolean(3, item.isDone());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    item.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public void update(Item item) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("UPDATE item SET description = ?, done = ? WHERE id = ?")
        ) {
            ps.setString(1, item.getDescription());
            ps.setBoolean(2, item.isDone());
            ps.setInt(3, item.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Item findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM item WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Item(it.getInt("id"),
                            it.getString("description"),
                            getLocalDateTime(it.getTimestamp("created")),
                            it.getBoolean("done")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Item> newItems() {
        List<Item> items = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM item ORDER BY created desc LIMIT 3")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    items.add(new Item(it.getInt("id"),
                            it.getString("description"),
                            getLocalDateTime(it.getTimestamp("created")),
                            it.getBoolean("done"))
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public Item done(int id) {
        Item item = findById(id);
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE item SET done = ? WHERE id = ?")
        ) {
            ps.setBoolean(1, true);
            ps.setInt(2, item.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public List<Item> findAlldone() {
        List<Item> items = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM item WHERE done is true")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    items.add(new Item(it.getInt("id"),
                            it.getString("description"),
                            getLocalDateTime(it.getTimestamp("created")),
                            it.getBoolean("done"))
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;

    }
}
