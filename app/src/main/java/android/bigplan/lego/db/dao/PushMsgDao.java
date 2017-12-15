package android.bigplan.lego.db.dao;

import android.bigplan.lego.db.DBInsideHelper;
import android.bigplan.lego.model.PushMsg;
import android.bigplan.lego.util.AbLogUtil;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import android.bigplan.lego.db.orm.dao.AbDBDaoImpl;

public class PushMsgDao extends AbDBDaoImpl<PushMsg>{
    public PushMsgDao(Context context) {
        super(new DBInsideHelper(context), PushMsg.class);
    }

    public List<PushMsg> getPushMsgListByMsgType(String memberId) {
        List<PushMsg> list = new ArrayList<PushMsg>();
        PushMsg pushMsg = null;
        Cursor cursor = null;
        try {
            startReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM PushMsg where MemberId='" +  memberId + "' order by Time desc", null);
            while (cursor.moveToNext())
            {
                pushMsg = new PushMsg();
                pushMsg.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                pushMsg.setAmount(cursor.getInt(cursor.getColumnIndex("Amount")));
                pushMsg.setId(cursor.getString(cursor.getColumnIndex("Id")));
                pushMsg.setObjId(cursor.getString(cursor.getColumnIndex("ObjId")));
                pushMsg.setPic(cursor.getString(cursor.getColumnIndex("Pic")));
                pushMsg.setSubtitle(cursor.getString(cursor.getColumnIndex("SubTitle")));
                pushMsg.setTime(cursor.getString(cursor.getColumnIndex("Time")));
                pushMsg.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                pushMsg.setType(cursor.getString(cursor.getColumnIndex("Type")));
                pushMsg.setMemberId(cursor.getString(cursor.getColumnIndex("MemberId")));
                pushMsg.setInitiatorId(cursor.getInt(cursor.getColumnIndex("InitiatorId")));
                list.add(pushMsg);
            }

        }
        catch (Exception e) {
            AbLogUtil.e(AbDBDaoImpl.class, "[rawQuery] from DB Exception.");
            e.printStackTrace();
        }
        finally {
            closeCursor(cursor);

        }
        return list;
    }

    public void updateMessageCount(String type, String memberId) {

        List<PushMsg> list = new ArrayList<PushMsg>();
        PushMsg pushMsg = null;
        Cursor cursor = null;
        try {
            startReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM PushMsg where Type='" + type + "' and MemberId='" +  memberId + "'", null);
            while (cursor.moveToNext())
            {
                pushMsg = new PushMsg();
                pushMsg.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                pushMsg.setAmount(cursor.getInt(cursor.getColumnIndex("Amount")));
                pushMsg.setId(cursor.getString(cursor.getColumnIndex("Id")));
                pushMsg.setObjId(cursor.getString(cursor.getColumnIndex("ObjId")));
                pushMsg.setPic(cursor.getString(cursor.getColumnIndex("Pic")));
                pushMsg.setSubtitle(cursor.getString(cursor.getColumnIndex("SubTitle")));
                pushMsg.setTime(cursor.getString(cursor.getColumnIndex("Time")));
                pushMsg.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                pushMsg.setType(cursor.getString(cursor.getColumnIndex("Type")));
                pushMsg.setMemberId(cursor.getString(cursor.getColumnIndex("MemberId")));
                pushMsg.setInitiatorId(cursor.getInt(cursor.getColumnIndex("InitiatorId")));
                list.add(pushMsg);
            }
            if (list.size() > 0){
                PushMsg updateMsg = list.get(0);
                if (updateMsg.getAmount() > 0) {
                    updateMsg.setAmount(updateMsg.getAmount() - 1);
                }
                update(updateMsg);
            }
        }
        catch (Exception e) {
            AbLogUtil.e(AbDBDaoImpl.class, "[rawQuery] from DB Exception.");
            e.printStackTrace();
        }
        finally {
            closeCursor(cursor);
        }
    }
}
