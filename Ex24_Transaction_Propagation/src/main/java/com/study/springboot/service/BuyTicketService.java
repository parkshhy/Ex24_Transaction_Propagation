package com.study.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.study.springboot.dao.ITransaction1Dao;
import com.study.springboot.dao.ITransaction2Dao;

//@Service 어노테이션을 지정하면 이 클래스를 빈으로 사용하겠다는 의미이다.
@Service
public class BuyTicketService{

	//Transaction1Dao 클래스의 객체를 자동 주입받아 변수를 만든다.
	@Autowired
	ITransaction1Dao transaction1;
	
	//Transaction2Dao 클래스의 객체를 자동 주입 받아 변수를 만든다.
	@Autowired
	ITransaction2Dao transaction2;
	
	
	@Autowired
	TransactionTemplate transactionTemplate;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int buy(String consumerId,int amount,String error) {
	
		try {
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus arg0) {
						
						transaction1.pay(consumerId,amount);
						
						if(error.equals("1")) {int n = 10 / 0;}
						
						transaction2.pay(consumerId,amount);
					}
				});
		
			return 1;
		
		}catch(Exception e){
			System.out.println("[TransactionTemplate] Rollback");
			return 0;
		}
	}
}
