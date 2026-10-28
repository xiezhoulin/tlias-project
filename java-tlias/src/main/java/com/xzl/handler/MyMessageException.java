package com.xzl.handler;

/** 自定义异常消息类*/
public class MyMessageException extends RuntimeException{

    public MyMessageException(){}

    public MyMessageException(String message){super(message);}

}
