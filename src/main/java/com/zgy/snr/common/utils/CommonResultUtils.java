package com.zgy.snr.common.utils;


import java.io.Serializable;

/**
 * 返回消息通用工具类
 */
public class CommonResultUtils implements Serializable{

    //响应业务状态 200:正常/400:错误/......可自定义
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    public static CommonResultUtils build(Integer status, String msg, Object data) {
        return new CommonResultUtils(status, msg, data);
    }

    public static CommonResultUtils build(Integer status, String msg) {
        return new CommonResultUtils(status, msg, null);
    }

    public static CommonResultUtils ok(Object data) {
        return new CommonResultUtils(data);
    }

    public static CommonResultUtils ok() {
        return new CommonResultUtils(null);
    }

    public static CommonResultUtils ok(String msg) {
        return new CommonResultUtils(200,msg);
    }

    public static CommonResultUtils err(String msg) {
        return new CommonResultUtils(400,msg);
    }

    public CommonResultUtils() {

    }

    public CommonResultUtils(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public CommonResultUtils(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public CommonResultUtils(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
