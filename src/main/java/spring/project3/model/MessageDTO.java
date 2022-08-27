package spring.project3.model;

import lombok.Data;

@Data
public class MessageDTO {
	final String from = "ducphuong170498@gmail.com";
	private String to;
	private String toName;
	final String subject = "Thông báo đơn hàng";
	private String content;
}
