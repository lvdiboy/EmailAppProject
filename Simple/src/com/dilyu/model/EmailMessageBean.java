package com.dilyu.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import com.dilyu.model.table.AbstractTableItem;
import com.dilyu.model.table.FormatableInteger;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmailMessageBean extends AbstractTableItem{
	
	
	private SimpleStringProperty sender;
	private SimpleStringProperty subject;
	private SimpleObjectProperty<FormatableInteger> size;
	private Message messageReference;
	private SimpleObjectProperty<Date> date;
	
	//Attachments hanling:
	private List<MimeBodyPart> attachmentsList = new ArrayList<MimeBodyPart>();
	private StringBuffer attachmentsNames = new StringBuffer();
	
	
	
	
	public EmailMessageBean(String Subject, String Sender, int size, boolean isRead, Date date,Message messageReference){
		super(isRead);
		this.subject = new SimpleStringProperty(Subject);
		this.sender = new SimpleStringProperty(Sender);
		this.size = new SimpleObjectProperty<FormatableInteger>(new FormatableInteger(size));
		this.messageReference= messageReference;
		this.date = new SimpleObjectProperty<Date>(date);
	}
	
	@Override
	public String toString() {
		return "EmailMessageBean "
				+ "sender=" + sender.get() + 
				", subject=" + subject.get() +
				", size=" + size.get();
	}
	
	public String getSender(){
		return sender.get();
	}
	public String getSubject(){
		return subject.get();
	}
	public FormatableInteger getSize(){
		return size.get();
	}
	public Date getDate(){
		return date.get();
	}
	


	public Message getMessageReference() {
		return messageReference;
	}

	public List<MimeBodyPart> getAttachmentsList() {
		return attachmentsList;
	}

	public String getAttachmentsNames() {
		return attachmentsNames.toString();
	}

	public void addAttachment(MimeBodyPart mbp){
		attachmentsList.add(mbp);
		try {
			attachmentsNames.append(mbp.getFileName() + "; ");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	public boolean hasAttachments(){
		return attachmentsList.size() > 0;
	}
	//clear methods:
	public void clearAttachments(){
		attachmentsList.clear();
		attachmentsNames.setLength(0);
	}

}
