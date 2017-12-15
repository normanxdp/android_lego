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

import android.os.Environment;

/**
 * 名称：AbAppConfig.java
 * 描述：初始设置类.
 */
public class AbAppConfig {

    public static final String ROOT = Environment.getExternalStorageDirectory().getPath() + "/bnplus/";
    public static final String CAMERA = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
    public static final String CACHE_IMG = ROOT + "cache/images/";
    public static final String CACHE_SHARE = ROOT + "cache/share/";
    /**
     * 应用日志目录文件
     */
    public static String APP_LOG_PATH = ROOT + "log/";

    /**
     * 上传文件目录
     */
    public static String APP_IMAGE_UPLOAD = ROOT + "upload/";

    /**
     * 日志文件路径
     */
    public static String LOGFILE = APP_LOG_PATH + "log.txt";

    /**
     * UI设计的基准宽度.
     */
    public static int UI_WIDTH = 720;

    /**
     * UI设计的基准高度.
     */
    public static int UI_HEIGHT = 1280;

    /**
     * UI设计的密度.
     */
    public static int UI_DENSITY = 2;

    /**
     * 默认 SharePreferences文件名.
     */
    public static String SHARED_PATH = "app_share";

    /**
     * 默认下载文件地址.
     */
    public static String DOWNLOAD_ROOT_DIR = "download/";

    /**
     * 默认下载图片文件地址.
     */
    public static String DOWNLOAD_IMAGE_DIR = "images";

    /**
     * 默认下载文件地址.
     */
    public static String DOWNLOAD_FILE_DIR = "files";

    /**
     * APP缓存目录.
     */
    public static String CACHE_DIR = "cache";

    /**
     * DB目录.
     */
    public static String DB_DIR = "db";

    /**
     * 默认缓存超时时间设置，毫秒.
     */
    public static int IMAGE_CACHE_EXPIRES_TIME = 60 * 10000 * 24 * 5;

    /**
     * 内存缓存大小  单位10M.
     */
    public static int MAX_CACHE_SIZE_INBYTES = 10 * 1024 * 1024;

    /**
     * 磁盘缓存大小  单位10M.
     */
    public static int MAX_DISK_USAGE_INBYTES = 10 * 1024 * 1024;

    /**
     * The Constant CONNECTEXCEPTION.
     */
    public static String CONNECT_EXCEPTION = "无法连接到网络";

    /**
     * The Constant UNKNOWNHOSTEXCEPTION.
     */
    public static String UNKNOWN_HOST_EXCEPTION = "连接远程地址失败";

    /**
     * The Constant SOCKETEXCEPTION.
     */
    public static String SOCKET_EXCEPTION = "网络连接出错，请重试";

    /**
     * The Constant SOCKETTIMEOUTEXCEPTION.
     */
    public static String SOCKET_TIMEOUT_EXCEPTION = "连接超时，请重试";

    /**
     * The Constant NULLPOINTEREXCEPTION.
     */
    public static String NULL_POINTER_EXCEPTION = "操作失败，请重试";

    /**
     * The Constant NULLMESSAGEEXCEPTION.
     */
    public static String NULL_MESSAGE_EXCEPTION = "操作失败，请重试";

    /**
     * The Constant CLIENTPROTOCOLEXCEPTION.
     */
    public static String CLIENT_PROTOCOL_EXCEPTION = "Http请求参数错误";

    /**
     * 参数个数不够.
     */
    public static String MISSING_PARAMETERS = "参数没有包含足够的值";

    /**
     * The Constant REMOTESERVICEEXCEPTION.
     */
    public static String REMOTE_SERVICE_EXCEPTION = "操作失败，请重试";

    /**
     * 资源未找到.
     */
    public static String NOT_FOUND_EXCEPTION = "页面未找到";

    /**
     * 没有权限访问资源.
     */
    public static String FORBIDDEN_EXCEPTION = "没有权限访问资源";

    /**
     * 其他异常.
     */
    public static String UNTREATED_EXCEPTION = "未处理的异常";


}
