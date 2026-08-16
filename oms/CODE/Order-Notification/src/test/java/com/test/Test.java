package com.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.db.util.Constant;
import com.db.util.GetDBConnection;
import com.order.notification.mail.EmailNotification;
import com.order.notification.mail.bean.Email;
import com.order.notification.mail.bean.EmailInfo;
import com.order.notification.mail.bean.Metadata;
import com.order.notification.mail.bean.Recipients;
import com.order.notification.mail.bean.To;

public class Test {

	public static void main(String[] args) {
		Connection sim14Conn=null;
		GetDBConnection getDBConnection=new GetDBConnection();
		getDBConnection.loadProperties(Constant.Database.OMS);
		try {
			//sim14Conn=getDBConnection.getConnection();
			System.out.println("----------"+sim14Conn);
			//sim14Conn.close();
			
			EmailNotification en=new EmailNotification();
			Email email= new Email();
			
			EmailInfo emailInfo=new EmailInfo();
			email.setFrom("java85rabindra@gmail.com");
			email.setFromName("Rabindra Chodhary");
			
			Metadata md=new Metadata();
			md.setCampaignType("kjgdhag");
			email.setMetadata(md);
			Recipients recipients = new Recipients();
			To to = new To();
			to.setEmail("java85rabindra@gmail.com");
			List<To> toList= new ArrayList<To>();
			toList.add(to);
			recipients.setTo(toList);
			email.setRecipients(recipients);
			email.setSubject("mdsjagtstduya");
			email.setText("hjgsdgausydtau");
			emailInfo.setOrderNumber("87576576");
			emailInfo.setEmail(email);
			en.sendMail(emailInfo);
			} catch (Exception e) {			
			e.printStackTrace();
		}
	}

}
