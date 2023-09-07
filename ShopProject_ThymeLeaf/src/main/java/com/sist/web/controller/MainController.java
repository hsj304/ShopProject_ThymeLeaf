package com.sist.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;
import com.sist.web.entity.*;
import com.sist.web.dao.*;


@Controller
public class MainController {
	@Autowired
	private ShopDAO dao;
	@Autowired
	private CategoryDAO cdao;
	
	@GetMapping("/")
	public String main(Model model){
		List<ShopEntity> tList=dao.shopTop3Data();
		List<ShopEntity> rList=dao.shopRandData();
		List<categoryEntity> cList=cdao.findAll();
		
		model.addAttribute("cList", cList);
		model.addAttribute("tList", tList);
		model.addAttribute("rList", rList);
		model.addAttribute("main_html","main/home");
		return "main";
	}
}
