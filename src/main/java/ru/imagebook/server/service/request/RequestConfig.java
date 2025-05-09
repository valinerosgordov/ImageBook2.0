package ru.imagebook.server.service.request;

public class RequestConfig {
    private int deliveryHour1;
    private int deliveryHour2;
	private String printRecipients;
	private String bookRecipients;
	private String urgentRecipients;
	
	public int getDeliveryHour1() {
        return deliveryHour1;
    }

    public void setDeliveryHour1(int deliveryHour1) {
        this.deliveryHour1 = deliveryHour1;
    }

    public int getDeliveryHour2() {
        return deliveryHour2;
    }

    public void setDeliveryHour2(int deliveryHour2) {
        this.deliveryHour2 = deliveryHour2;
    }

    public String getPrintRecipients() {
		return printRecipients;
	}

	public void setPrintRecipients(String printRecipients) {
		this.printRecipients = printRecipients;
	}

	public String getBookRecipients() {
		return bookRecipients;
	}

	public void setBookRecipients(String bookRecipients) {
		this.bookRecipients = bookRecipients;
	}

	public String getUrgentRecipients() {
		return urgentRecipients;
	}

	public void setUrgentRecipients(String urgentRecipients) {
		this.urgentRecipients = urgentRecipients;
	}
}
