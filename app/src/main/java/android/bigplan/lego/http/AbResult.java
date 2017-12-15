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
package android.bigplan.lego.http;

import java.util.List;

import android.bigplan.lego.util.AbJsonUtil;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn 名称：AbResult.java 描述：结果
 * 
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-13 上午9:00:52
 */
public class AbResult{
    
    /** 返回码：成功. */
    public static final int RESULT_OK = 0;
    
    /** 返回码：失败. */
    public static final int RESULT_ERROR = 1;
    
    /** 返回码：0 成功. 1 失败 */
    private int code;
    
    /** 结果 message. */
    private String msg;
    
    /** 数据. */
    private List<?> data;
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public List<?> getData() {
        return data;
    }
    
    public void setData(List<?> data) {
        this.data = data;
    }
    
    /**
     * 
     * 描述：转换成json.
     * 
     * @return
     */
    public String toJson() {
        return AbJsonUtil.toJson(this);
    }
    
}
