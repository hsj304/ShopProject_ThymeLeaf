package com.sist.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sist.web.dao.BoardDAO;
import com.sist.web.entity.BoardEntity;

@Controller
@RequestMapping("board/")
public class BoardController {
	@Autowired
	private BoardDAO dao;
	
	@RequestMapping("list")
	public String list(Model model, String fd, String page) {
		if(fd==null) {
			fd="";
		}
		if(page==null) {
			page="1";
		}
		int curpage=Integer.parseInt(page);
		
		int rowsize=10;
		int start=rowsize*curpage-rowsize;
		
		List<BoardEntity> list=dao.boardListData(fd, start);
		
		for(BoardEntity vo:list) {
			vo.setRegdate(vo.getRegdate().split(" ")[0]);
		}
		
		int totalpage=dao.boardTotalPage(fd);
		
		final int BLOCK=5;
		int startpage=(curpage-1)/BLOCK*BLOCK+1;
		int endpage=(curpage-1)/BLOCK*BLOCK+BLOCK;
		if(endpage>totalpage)
			endpage=totalpage;
		
		model.addAttribute("curpage", curpage);
		model.addAttribute("startpage", startpage);
		model.addAttribute("endpage", endpage);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("list", list);
		model.addAttribute("fd", fd);
		model.addAttribute("main_html", "board/list");
		return "main";
	}
	
	@GetMapping("insert")
	public String insert(Model model) {

		model.addAttribute("main_html", "board/insert");
		return "main";
	}
	
	@PostMapping("insert_ok")
	public String insert_ok(BoardEntity vo) {
		dao.save(vo);
		
		return "redirect:/board/list";
	}
	
	@GetMapping("detail")
	public String detail(int no,Model model) {
		BoardEntity vo=dao.findByNo(no);
		
		vo.setHit(vo.getHit()+1);
		dao.save(vo);
		vo.setRegdate(vo.getRegdate().split(" ")[0]);
		
		model.addAttribute("vo", vo);
		model.addAttribute("main_html", "board/detail");
		return "main";
	}
	
	@GetMapping("update")
	public String update(int no, Model model) {
		BoardEntity vo=dao.findByNo(no);
		
		model.addAttribute("vo", vo);
		model.addAttribute("main_html", "board/update");
		return "main";
	}
	
	@PostMapping("update_ok")
	public @ResponseBody String update_ok(BoardEntity vo) {
		BoardEntity e=dao.findByNo(vo.getNo());
		if(e.getPwd().equals(vo.getPwd())) {
			vo.setHit(e.getHit());
			dao.save(vo);
			return "YES";
		} else {
			return "NO";
		}
	}
	
	@PostMapping("delete_ok")
	@ResponseBody
	public String delete(String pwd, String no) {
		BoardEntity vo=dao.findByNo(Integer.parseInt(no));
		if(pwd.equals(vo.getPwd())) {
			dao.delete(vo);
			return "YES";
		} else {
			return "NO";
		}
		
	}
	
}
