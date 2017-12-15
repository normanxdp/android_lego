package android.bigplan.lego.model;

import java.io.Serializable;

import android.bigplan.lego.db.orm.annotation.Column;
import android.bigplan.lego.db.orm.annotation.Id;
import android.bigplan.lego.db.orm.annotation.Table;

/**
 * 首页按钮
 * @author FingerArt FingerArt.me
 * @date 2015年11月25日 下午12:07:21
 */
@Table(name = "indexIcon")
public class IndexIcon implements Serializable {

	@Id
	@Column(name = "_id")
	private int _id;

}
