package com.mmj.emtool;

/**
 * Created by mmj on 18-2-11.
 */

public class EmMessage {
    private String name;
    private String msg;
    private int backimage;
    public EmMessage(String name,String msg,int imageId){
        this.name=name;
        this.backimage=imageId;
        this.msg=msg;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMsg(String msg){
        this.msg=msg;
    }
    public String getMsg() {
        return msg;
    }
    public int getImageId(){
        return backimage;
    }
}
