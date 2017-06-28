package com.dilyu;

import com.dilyu.model.EmailAccountBean;
import com.dilyu.model.EmailMessageBean;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Test {
	
	public static void main(String[] args) {
		final EmailAccountBean emailAccountBean = new EmailAccountBean("lvdiboy@gmail.com", "Shushan1992");

		ObservableList<EmailMessageBean> data = FXCollections.observableArrayList();
	}

}
