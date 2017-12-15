package android.bigplan.lego.db.dao;

import android.bigplan.lego.db.DBInsideHelper;
import android.bigplan.lego.db.orm.dao.AbDBDaoImpl;
import android.bigplan.lego.model.Skill;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbStrUtil;
import android.content.Context;
import android.database.Cursor;


/**
 * Created by gg on 16-5-13.
 */
public class SkillDao extends AbDBDaoImpl<Skill> {
    public SkillDao(Context context) {
        super(new DBInsideHelper(context), Skill.class);

    }

    public void addPublicSkill(Skill skill) {
        try {
            startReadableDatabase();
            insert(skill);
        } catch (Exception e) {
            AbLogUtil.e(AbDBDaoImpl.class, "[addSkill] from DB Exception.");
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public int getSkillIdByName(String name) {
        int id = -1;
        Cursor cursor = null;
        try {
            startReadableDatabase();

            if (!AbStrUtil.isEmpty(name)) {
                cursor = db.rawQuery("SELECT * FROM Skill where Name=?",
                        new String[] { name });
            } else {
                return -1;
            }

            if (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("Id"));
            }
        } catch (Exception e) {
            AbLogUtil.e(AbDBDaoImpl.class, "[getSkillIdByName] from DB Exception.");
            e.printStackTrace();
        } finally {
            closeCursor(cursor);

        }
        return id;
    }
}
