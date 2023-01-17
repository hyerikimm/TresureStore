package com.kh.tresure.sell.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kh.tresure.chat.model.vo.ChatRoom;
import com.kh.tresure.sell.model.service.SellService;

@Controller
public class SellController {
		
	private Logger logger = LoggerFactory.getLogger(SellController.class);
	private	SellService sellService;
	
	// 기본생성자
	public SellController() {
		
	}
	
	@Autowired
	public SellController( SellService sellService){
		this.sellService = sellService;
	}
	
	// 상품등록 페이지
	@RequestMapping(value = "sell/sellInsert", method = RequestMethod.GET)
	public String insertSell(Model model) {
		
		
		return "sell/sellInsert";
	}
}