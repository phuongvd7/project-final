package springboot.project3.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import spring.project3.model.MessageDTO;

@Service
public class MailService {
	@Autowired
    private JavaMailSender javaMailSender;
    
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	
	public void sendEmail(String to,String subject, String body) {
		try {

		    MimeMessage message = javaMailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message, 
		    		StandardCharsets.UTF_8.name());
		    
		    //load template email with content
//		    Context context = new Context();
//		    context.setVariable("name", messageDTO.getToName());
//		    context.setVariable("content", messageDTO.getContent());
//		    String html = templateE  ngine.process("welcome-email", context);
		    
		    
		    //load template email with content
//		    Context context = new Context();
//		    context.setVariable("data", body); // phi giao dien vao key "data"
			//		    context.setVariable("content", messageDTO.getContent());
		//    String html = templateEngine.process("hi.html", context);
		    // muc dich tra ve giao dien view themleaf sau khi phi du lieu vao 
		    
		    ///send email
		    helper.setTo(to);
		    helper.setText(body, true);
		    helper.setSubject(subject);
		    helper.setFrom("ducphuong170498@gmail.com");
		    javaMailSender.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
//		    logger.error("Email sent with error: " + e.getMessage());
		}
	}
	public void sendEmailReport(MessageDTO messageDTO) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

		// load template email with content
		Context context = new Context();
		context.setVariable("toName", "Sếp");
		context.setVariable("content", messageDTO.getContent());
		String html = templateEngine.process("report.html", context);

		/// send mail
		helper.setTo("ducphuong170498@gmail.com");
		helper.setText(html, true);
		helper.setSubject("Báo cáo tình trạng hoá đơn");
		helper.setFrom(messageDTO.getFrom());
		javaMailSender.send(message);

	}
	
	public void sendEmailPassword(MessageDTO messageDTO) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

		// load template email with content
		Context context = new Context();
		context.setVariable("toName", messageDTO.getToName());
		context.setVariable("content", messageDTO.getContent());
		String html = templateEngine.process("password-customer.html", context);

		/// send mail
		helper.setTo(messageDTO.getTo());
		helper.setText(html, true);
		helper.setSubject("THAY ĐỔI MẬT KHẨU THÀNH CÔNG");
		helper.setFrom(messageDTO.getFrom());
		javaMailSender.send(message);

	}
}
