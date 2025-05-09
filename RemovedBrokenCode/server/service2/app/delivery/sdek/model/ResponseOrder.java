package ru.imagebook.server.service2.app.delivery.sdek.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Order")
public class ResponseOrder implements Serializable {
    @XmlAttribute(name = "DispatchNumber")
    private Integer dispatchNumber;
    @XmlAttribute(name = "Number")
    private String number;
    @XmlAttribute(name = "ErrorCode")
    private String errCode;
    @XmlAttribute(name = "Msg")
    private String msg;

    public Integer getDispatchNumber() {
        return dispatchNumber;
    }

    public String getNumber() {
        return number;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ResponseOrder{" +
                "dispatchNumber=" + dispatchNumber +
                ", number='" + number + '\'' +
                ", errCode='" + errCode + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}