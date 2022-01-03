package com.example.sbs.demo.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.sbs.demo.dao.MemberDao2;
import com.example.sbs.demo.dto.Member;
import com.example.sbs.demo.service.MemberService;
import com.example.sbs.demo.util.CookieUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MemberController {
	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberDao2 memberDao2;

	@RequestMapping("/member/mybatisTest1")
	@ResponseBody
	public Member mybatisTest1() {
		Member member = memberDao2.test1();

		return member;
	}

	@RequestMapping("/member/mybatisTestFindByLoginId")
	@ResponseBody
	public Member mybatisTestFindByLoginId() {
		Member member = memberDao2.findByLoginId("user2");

		return member;
	}

	@RequestMapping("/member/mybatisTestFindByLoginIdAndLoginPasswd")
	@ResponseBody
	public Member mybatisTestFindByLoginIdAndLoginPasswd() {
		Member member = memberDao2.findByLoginIdAndLoginPasswd("user2", "user2");

		return member;
	}

	@RequestMapping("/member/mybatisTestAdd")
	@ResponseBody
	public Member mybatisTestAdd() {
		Member member = new Member(0, "2019-12-12 12:12:12", "user3", "user3", "임꺽정", "test@test.com", false);
		memberDao2.add(member);

		return member;
	}

	/*
	 * 
	 * @RequestMapping("/member/mybatisTestFindByLoginId")
	 * 
	 * @ResponseBody public Member mybatisTestFindByLoginId() { Member member =
	 * memberDao2.findByLoginId("user1");
	 * 
	 * return member; }
	 * 
	 * @RequestMapping("/member/mybatisTestAdd")
	 * 
	 * @ResponseBody public Member mybatisTestAdd() { Member member = new Member(0,
	 * "2019-12-12 12:12:12", "user2", "user2", "임꺽정", "test@test.com", false);
	 * 
	 * memberDao2.add(member);
	 * 
	 * return member; }
	 * 
	 */

	@RequestMapping("/member/testLogin")
	@ResponseBody
	public String testLogin(HttpServletRequest request) {
		String loginedMemberIdStr = CookieUtil.getAttribute(request, "loginedMemberId");

		int loginedMemberId = 0;

		if (loginedMemberIdStr != null) {
			loginedMemberId = Integer.parseInt(loginedMemberIdStr);

			Member loginedMember = memberService.findMemberById(loginedMemberId);

			return "당신은 " + loginedMember.getName() + " 입니다.";
		}

		return "당신은 로그아웃 상태입니다.";
	}

	@RequestMapping("/member/testCookie")
	@ResponseBody
	public String testCookie(HttpServletResponse response) {
		CookieUtil.setAttribute(response, "age", "11");

		return "쿠키에 나이가 11살이라고 적었다.";
	}

	@RequestMapping("/member/testCookie2")
	@ResponseBody
	public String testCookie2(HttpServletRequest request) {
		String ageStr = CookieUtil.getAttribute(request, "age");
		int age = 0;

		if (ageStr != null) {
			age = Integer.parseInt(ageStr);
		}

		return "쿠키에 적혀있는 나이는 " + age + "살 입니다.";
	}

	@RequestMapping("/member/findLoginPasswd")
	public String showFindLoginPasswd() {
		return "member/findLoginPasswd";
	}

	@RequestMapping("/member/doFindLoginPasswd")
	@ResponseBody
	public String doFindLoginPasswd(@RequestParam Map<String, Object> param) {
		Map<String, Object> findLoginIdRs = memberService.findLoginPasswd(param);

		return (String) findLoginIdRs.get("msg");
	}

	@RequestMapping("/member/findLoginId")
	public String showFindLoginId() {
		return "member/findLoginId";
	}

	@RequestMapping("/member/doFindLoginId")
	@ResponseBody
	public String doFindLoginId(@RequestParam Map<String, Object> param) {
		Map<String, Object> findLoginIdRs = memberService.findLoginId(param);

		return (String) findLoginIdRs.get("msg");
	}

	@RequestMapping("/member/login")
	public String showLogin() {
		return "member/login";
	}

	@RequestMapping("/member/doLogin")
	@ResponseBody
	public String doLogin(HttpServletResponse response, @RequestParam Map<String, Object> param) {
		Map<String, Object> rs = memberService.loginV2(param);

		String resultCode = (String) rs.get("resultCode");
		Member loginedMember = (Member) rs.get("member");

		if (resultCode.startsWith("S-")) {
			CookieUtil.setAttribute(response, "loginedMemberId", loginedMember.getId() + "");
		}

		return (String) rs.get("msg");
	}

	@RequestMapping("/member/join")
	public String showJoin() {
		return "member/join";
	}

	@RequestMapping("/member/doJoin")
	@ResponseBody
	public String doJoin(@RequestParam Map<String, Object> param) {
		Map<String, Object> joinRs = memberService.join(param);

		String resultCode = (String) joinRs.get("resultCode");

		if (resultCode.startsWith("S-")) {
			log.info("회원가입 성공!!");
		} else {
			log.info("회원가입 실패!!");
		}

		return (String) joinRs.get("msg");
	}
}