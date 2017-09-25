package cn.peng.pxun.modle.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MESSAGE".
*/
public class MessageDao extends AbstractDao<Message, Long> {

    public static final String TABLENAME = "MESSAGE";

    /**
     * Properties of entity Message.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Message = new Property(1, String.class, "message", false, "MESSAGE");
        public final static Property Date = new Property(2, String.class, "date", false, "DATE");
        public final static Property FromUserID = new Property(3, String.class, "fromUserID", false, "FROM_USER_ID");
        public final static Property ToUserID = new Property(4, String.class, "toUserID", false, "TO_USER_ID");
        public final static Property MessageType = new Property(5, Integer.class, "messageType", false, "MESSAGE_TYPE");
        public final static Property IsTuring = new Property(6, Boolean.class, "isTuring", false, "IS_TURING");
        public final static Property PicURL = new Property(7, String.class, "picURL", false, "PIC_URL");
    }


    public MessageDao(DaoConfig config) {
        super(config);
    }
    
    public MessageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MESSAGE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"MESSAGE\" TEXT NOT NULL ," + // 1: message
                "\"DATE\" TEXT," + // 2: date
                "\"FROM_USER_ID\" TEXT," + // 3: fromUserID
                "\"TO_USER_ID\" TEXT," + // 4: toUserID
                "\"MESSAGE_TYPE\" INTEGER," + // 5: messageType
                "\"IS_TURING\" INTEGER," + // 6: isTuring
                "\"PIC_URL\" TEXT);"); // 7: picURL
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MESSAGE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Message entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getMessage());
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(3, date);
        }
 
        String fromUserID = entity.getFromUserID();
        if (fromUserID != null) {
            stmt.bindString(4, fromUserID);
        }
 
        String toUserID = entity.getToUserID();
        if (toUserID != null) {
            stmt.bindString(5, toUserID);
        }
 
        Integer messageType = entity.getMessageType();
        if (messageType != null) {
            stmt.bindLong(6, messageType);
        }
 
        Boolean isTuring = entity.getIsTuring();
        if (isTuring != null) {
            stmt.bindLong(7, isTuring ? 1L: 0L);
        }
 
        String picURL = entity.getPicURL();
        if (picURL != null) {
            stmt.bindString(8, picURL);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Message entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getMessage());
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(3, date);
        }
 
        String fromUserID = entity.getFromUserID();
        if (fromUserID != null) {
            stmt.bindString(4, fromUserID);
        }
 
        String toUserID = entity.getToUserID();
        if (toUserID != null) {
            stmt.bindString(5, toUserID);
        }
 
        Integer messageType = entity.getMessageType();
        if (messageType != null) {
            stmt.bindLong(6, messageType);
        }
 
        Boolean isTuring = entity.getIsTuring();
        if (isTuring != null) {
            stmt.bindLong(7, isTuring ? 1L: 0L);
        }
 
        String picURL = entity.getPicURL();
        if (picURL != null) {
            stmt.bindString(8, picURL);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Message readEntity(Cursor cursor, int offset) {
        Message entity = new Message( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // message
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // date
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // fromUserID
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // toUserID
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // messageType
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0, // isTuring
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // picURL
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Message entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMessage(cursor.getString(offset + 1));
        entity.setDate(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFromUserID(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setToUserID(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMessageType(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setIsTuring(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
        entity.setPicURL(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Message entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Message entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Message entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
