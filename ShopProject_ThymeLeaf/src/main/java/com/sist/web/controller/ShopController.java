package com.sist.web.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sist.web.dao.CategoryDAO;
import com.sist.web.dao.ShopDAO;
import com.sist.web.entity.ShopEntity;
import com.sist.web.entity.categoryEntity;

@Controller
@RequestMapping("shop/")
public class ShopController {
	@Autowired
	private ShopDAO dao;
	@Autowired
	private CategoryDAO cdao;
	
	@RequestMapping("list")
	public String list(Model model, String fd, String cno, String page, HttpServletRequest request) {
		Cookie[] cookies=request.getCookies();
		List<ShopEntity> ckList=new ArrayList<ShopEntity>();
		if(cookies!=null) {
			for(int i=cookies.length-1;i>=0;i--) {
				String key=cookies[i].getName();
				if(key.startsWith("shop_")) {
					String data=cookies[i].getValue();
					ShopEntity vo=new ShopEntity();
					vo=dao.findByGno(Integer.parseInt(data));
					String name=vo.getGoods_name();
					if(name.length()>7) {
						vo.setGoods_name(name.substring(0,7)+"..");
					}
					ckList.add(vo);
					if(ckList.size()==3) {
						break;
					}
				}
			}
		}
		
		if(fd==null) {
			fd="";
		}
		if(cno==null) {
			cno="1";
		}
		if(page==null) {
			page="1";
		}
		int cateno=Integer.parseInt(cno);
		int curpage=Integer.parseInt(page);
		
		int rowsize=9;
		int start=rowsize*curpage-rowsize;
		
		List<ShopEntity> list=dao.shopListData(fd, cateno, start);
		
		int totalpage=dao.shopFindTotalPage(fd, cateno);
		
		final int BLOCK=10;
		int startpage=(curpage-1)/BLOCK*BLOCK+1;
		int endpage=(curpage-1)/BLOCK*BLOCK+BLOCK;
		if(endpage>totalpage)
			endpage=totalpage;
		
		List<categoryEntity> cList=cdao.findAll();
		
		model.addAttribute("ckList", ckList);
		model.addAttribute("cList", cList);
		model.addAttribute("curpage", curpage);
		model.addAttribute("startpage", startpage);
		model.addAttribute("endpage", endpage);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("list", list);
		model.addAttribute("fd", fd);
		model.addAttribute("cno", cateno);
		model.addAttribute("main_html", "shop/list");
		return "main";
	}
	
	@GetMapping("detail")
	public String detail(Model model, int gno,HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies=request.getCookies();
		if(cookies!=null) {
			for(int i=cookies.length-1;i>=0;i--) {
				String key=cookies[i].getName();
				if(key.equals("shop_"+gno)) {
					cookies[i].setMaxAge(0); // 쿠키를 삭제하기 위해 만료시간을 0으로 설정
	                cookies[i].setPath("/");
	                response.addCookie(cookies[i]);
				}
			}
		}
		
		Cookie cookie=new Cookie("shop_"+gno, String.valueOf(gno));
		cookie.setPath("/");
		cookie.setMaxAge(60*60*24);
		response.addCookie(cookie);
		
		ShopEntity vo=dao.findByGno(gno);
		vo.setHit(vo.getHit()+1);
		dao.save(vo);
		
		model.addAttribute("vo", vo);
		model.addAttribute("main_html", "shop/detail");
		return "main";
	}
}
