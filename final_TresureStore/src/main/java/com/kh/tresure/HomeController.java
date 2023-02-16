package com.kh.tresure;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kh.tresure.member.model.service.KakaoAPI;
import com.kh.tresure.member.model.service.MemberService;
import com.kh.tresure.member.model.service.NaverLoginBO;
import com.kh.tresure.member.model.vo.Member;
import com.kh.tresure.review.model.vo.Review;
import com.kh.tresure.sell.model.service.SellService;
import com.kh.tresure.sell.model.vo.Sell;

@Controller
public class HomeController {
	private int count = 0; 
	public static final String HOME = "redirect:/";
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private SellService sellService;
	private KakaoAPI kakao;
	private MemberService memberservice;
	private NaverLoginBO naverLoginBo;
	
	public HomeController() {}
	
	@Autowired
	public HomeController(SellService sellService, KakaoAPI kakao,MemberService memberservice, NaverLoginBO naverLoginBo) {
		this.sellService = sellService;
		this.kakao = kakao;
		this.memberservice = memberservice;
		this.naverLoginBo = naverLoginBo;
	}

	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model,HttpSession session) {

		List<Sell> sList = sellService.sellListselect();
		logger.info("sList ? "+sList);
		for(int i=0; i<sList.size(); i++) {
			sList.get(i).setTimeago(sList.get(i).getCreateDate());
		}
		
		String access_Token = (String)session.getAttribute("access_Token");
		OAuth2AccessToken oauthToken = (OAuth2AccessToken)session.getAttribute("oauthToken");
		
		
		if(access_Token != null) { //kakao 로그인 했던 경우
			
			Member member = kakao.getUserInfo(access_Token);
			member = memberservice.loginAndMemberEnroll(member);
			
			session.setAttribute("loginUser", member);
	    	session.setAttribute("access_Token", access_Token);
	    	
	    	if(count == 0) {
	    		session.setAttribute("alertMsg", member.getUserName()+"님 환영합니다");
	    		count++;
	    	}
	    	
		}else if(oauthToken != null) { // naver 로그인 했던 경우
			Member member = naverLoginBo.getNavUserInfo(oauthToken);
			member = memberservice.loginAndMemberEnroll(member);
			
			session.setAttribute("loginUser", member);
			session.setAttribute("oauthToken", oauthToken);
			
			if(count == 0) {
	    		session.setAttribute("alertMsg", member.getUserName()+"님 환영합니다");
	    		count++;
	    	}
			
			
		}
		
		int finishSellNo = sellService.finishSellNo();
		
		model.addAttribute("sellList", sList);
		model.addAttribute("finishSellNo", finishSellNo);
		
		return "home";
	}
	
	
	
	
	
	@RequestMapping(value = "/errors/error500")
	public String errors500() {
		
		return "/errors/error500";
	}
	
	@RequestMapping(value = "/errors/error405")
	public String errors405() {
		
		return "/errors/error405";
	}
	
	@RequestMapping(value = "/errors/error404")
	public String errors404() {
		
		return "/errors/error404";
	}
	
}
