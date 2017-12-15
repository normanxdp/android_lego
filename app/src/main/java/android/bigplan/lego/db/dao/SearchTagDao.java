package android.bigplan.lego.db.dao;

import android.bigplan.lego.db.DBInsideHelper;
import android.bigplan.lego.db.orm.dao.AbDBDaoImpl;
import android.bigplan.lego.model.SearchTag;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbStrUtil;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by gg on 17-9-16.
 */

public class SearchTagDao extends AbDBDaoImpl<SearchTag> {
    private final static String TAG = "SearchTagDao";

    public SearchTagDao(Context context) {
        super(new DBInsideHelper(context), SearchTag.class);

    }

    public void addSearchTag(SearchTag searchTag) {
        try {
            startWritableDatabase(true);
            insert(searchTag);
        } catch (Exception e) {
            AbLogUtil.e(AbDBDaoImpl.class, "[addSearchTag] from DB Exception.");
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public ArrayList<SearchTag> getAllSearchTag() {
        ArrayList<SearchTag> searchTagList = new ArrayList<>();
        SearchTag searchTag = null;
        Cursor cursor = null;
        try {
            startReadableDatabase();
            
            cursor = db.rawQuery("SELECT * FROM SearchHistoryTag", null);
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.moveToNext()) {
                    searchTag = new SearchTag();
                    searchTag.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                    searchTag.setKeyWords(cursor.getString(cursor.getColumnIndex("KeyWords")));
                    searchTagList.add(searchTag);

                    Log.d(TAG, "keyword: " + searchTag.getKeyWords());
                }
            }

        } catch (Exception e) {
            AbLogUtil.e(AbDBDaoImpl.class, "[getAllSearchTag] from DB Exception.");
            e.printStackTrace();
        } finally {
            closeCursor(cursor);

        }
        return searchTagList;
    }
}
