/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.bigplan.lego.global;

import java.io.File;

import android.bigplan.lego.app.AppApplication;
import android.os.Environment;

/**
 * 名称：AbConstant.java 描述：常量.
 */
public class AbConstant {
	/**
	 * 0 : 测试
	 * 1 : 生产
	 */
	public static int environment = 1;

	/** 服务地址 */
	public static String REQUEST_URL;
	/**  分享地址 */
	public static String SHARE_URL;

	/**
	 * Crash 提交地址
	 */
	public static String CRASH_URL;

	/** 移动设备操作系统类型,0代表IOS,1代表Android */
	public static final String REQUEST_OSTYPE = "1";

	/**
	 * 0 通用版
	 * 10000 清华版
	 * > 10000 定制版
	 */
	public static final int CUSTOM_VERSION = 10000;

	public static String Chamber = "云联乐购";
	
	/** 调用接口的APP类型,0代表会员APP,1代表秘书APP */
	public static final String REQUEST_APPTYPE = "0";

	/** 加密KEY */
	public static final String CRYPT_KEY = "DP!YUN#HJQ%TGB%UJM67OL5@WSX$RFV^YHN*IK4X";

	/** 通知ID */
	public static final int NOTIFY_ID = 0x9999;// 通知ID

	/** 挑选人物广播 */
	public static final String SELECT_PEOPLE = "select_people";

	/** 更换头像广播 */
	public static final String UPDATE_PICTURE = "update_picture";

	/** 发布分享广播 */
	public static final String PUBLISH_SHARE = "publish_share";

	public static final String ACTION_LOCALTION = "lego_location";
	public static final String ACTION_HOME_REFRESH = "lego_home_refresh";

	/** 二维码 --活动 */
	public static final String DECODE_ACT = "http://dwzhi.cn/act/item?id";

	/** 二维码 --需求 */
	public static final String DECODE_DEMAND = "http://dwzhi.cn/demand/item?id";

	/** 二维码 --商会 */
	public static final String DECODE_ALLY = "http://dwzhi.cn/ally/item?id";

	/** 二维码 --课程 */
	public static final String DECODE_COURSE = "courseid";

	/** 系统通知 */
	public static final String PUSHMSG_TYPE_SYS  = "1";
	public static final String PUSHMSG_TYPE_WALLET = "2";
	public static final String PUSHMSG_TYPE_STATE = "3";
	/** 积分通知 */
	public static final String PUSHMSG_TYPE_MY_PUBLISH_SCORE = "22";
	public static final String PUSHMSG_TYPE_AT_MY_DEMAND = "33";
	public static final String PUSHMSG_TYPE_MY_PUBLISH_DEMAND = "44";


	/** 聊天消息的广播 */
	public static final String CHAT_NEW_MESSAGE = "cn.tszjh.member.action.NEW_MESSAGE";
	/** 会客厅 */
	public static final String RECEPTIONROOM_NEW_MESSAGE = "cn.tszjh.member.action.receptionroom.NEW_MESSAGE";

	public static final String KEY_CAPTURE_FLAG  = "/member/register";
	// SharePreferences KEY
	public static final String KEY_SP_TELPHONE = "telphone";
	public static final String KEY_FROM_HOME = "lego_from_home";
	public static final String KEY_SP_PASSWORD = "password";
	public static final String KEY_WAIMAI_DES = "waimaidian_des";

	public static final String KEY_QUICK_USERNAME = "Quick_User_Name";
	public static final String KEY_QUICK_USER_AVATAR = "quick_user_avatar";
	public static final String KEY_QUICK_USER_TYPE = "quick_user_type";
	public static final String KEY_QUICK_USER_NICKNAME = "quick_user_nickname";


	public static final String DEFAULT_LONGITUDE = "113.03716"; //经纬
	public static final String DEFAULT_LATITUDE = "23.71326"; //纬度

	public static final double DEFAULT_DOUBLE_LONGITUDE = 113.03716; //经纬
	public static final double DEFAULT_DOUBLE_LATITUDE = 23.71326; //纬度

	//验证码业务类型
	public static final String REGISTER_USER  = "2";
	public static final String FORGET_PASSWORD = "1";
	public static final String BOUND_MOBILE = "3";

	// 程序目录结构
	public static final String DIR_SD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	public static final String DIR_PACKAGE = AppApplication.getInstance().getPackageName() + File.separator;
	public static final String DIR_CACHE = "cache" + File.separator;
	public static final String DIR_DB = "db" + File.separator;
	public static final String DIR_files = "files" + File.separator;
	public static final String DIR_IMAGES = "images" + File.separator;
	public static final String DIR_CAMERA_PIC = "cam_pic" + File.separator;
	public static final String DIR_LOGS = "logs" + File.separator;

	public static final String DIR_COMP_IMAGES = DIR_SD + "lazy" + DIR_IMAGES;
	public static final String DIR_COMP_CAMPIC = DIR_SD + "." + DIR_PACKAGE + DIR_CAMERA_PIC;
	public static final String DIR_COMP_LOGS = DIR_SD + "." + DIR_PACKAGE + DIR_LOGS;

	static {
		switch (environment) {
			case 1: // 生产环境
				REQUEST_URL = "http://api.yunlianlegou.com";
				SHARE_URL = "http://api.yunlianlegou.com";
				CRASH_URL = "http://api.yunlianlegou.com";
				break;

			default: // 测试环境
				REQUEST_URL = "http://api2.yunlianlegou.com";
				SHARE_URL = "http://api2.yunlianlegou.com";
				CRASH_URL = "http://api2.yunlianlegou.com";
				break;
		}
	}
}
