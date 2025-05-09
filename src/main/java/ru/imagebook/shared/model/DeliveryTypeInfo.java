package ru.imagebook.shared.model;

import ru.imagebook.client.common.service.delivery.MultishipType;
import ru.imagebook.client.common.service.delivery.PostHouseType;

import java.io.Serializable;

public class DeliveryTypeInfo implements Serializable {
    private Integer deliveryType;
    private PostHouseType postHouseType;
    private MultishipType multishipType;
    private int cost;
    private String label;
    private String info;
    private String infoComment;
    private String comment;

    public DeliveryTypeInfo() {
    }

    public DeliveryTypeInfo(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public DeliveryTypeInfo(Integer deliveryType, PostHouseType postHouseType, int cost) {
        this(deliveryType);
        this.postHouseType = postHouseType;
        this.cost = cost;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }
    
    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public PostHouseType getPostHouseType() {
        return postHouseType;
    }

    public void setPostHouseType(PostHouseType postHouseType) {
        this.postHouseType = postHouseType;
    }

    public MultishipType getMultishipType() {
        return multishipType;
    }

    public void setMultishipType(MultishipType multishipType) {
        this.multishipType = multishipType;
    }

    public void setMultishipType(String multishipType) {
        this.multishipType = (multishipType != null) ? MultishipType.valueOf(multishipType) : null;
    }

    public int getCost() {
        return cost;
    }
    
    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfoComment() {
        return infoComment;
    }

    public void setInfoComment(String infoComment) {
        this.infoComment = infoComment;
    }

    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeliveryTypeInfo that = (DeliveryTypeInfo) o;

        return deliveryType.equals(that.deliveryType)
                && multishipType == that.multishipType
                && postHouseType == that.postHouseType;

    }

    @Override
    public int hashCode() {
        int result = deliveryType.hashCode();
        result = 31 * result + (postHouseType != null ? postHouseType.hashCode() : 0);
        result = 31 * result + (multishipType != null ? multishipType.hashCode() : 0);
        return result;
    }
}
