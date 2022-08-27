package springboot.project3.service;

import java.util.Date;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import spring.project3.model.MessageDTO;
import springboot.project3.repository.BillRepo;

// co the add them @service hoac @component
@Component
public class JobSchedule {
	
	@Autowired
	BillRepo billRepo;
	@Autowired
	MailService mailService;
	
	@Scheduled(fixedDelay = 5 * 60 * 1000)
	public void notification() {
		
		MessageDTO messageDTO = new MessageDTO();
		
		Date now = new Date();
		long date1 = now.getTime() - ((long) 5 * 1000 * 60);
		Date date = new Date(date1);
		
		int size = billRepo.searchByDate(date).size();
		messageDTO.setContent(String.valueOf(size));
		
		try {
			mailService.sendEmailReport(messageDTO);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Sent email REPORT. Time: " + now);
//	public void hello() {
//		int size = categoryRepo.findAll().size();
//		System.out.println("hello" + size);
//	}
		 // from billdate >= currentDate - 5 minutes
//dung JPQL		 select from bill where date >= current -5 
}

}