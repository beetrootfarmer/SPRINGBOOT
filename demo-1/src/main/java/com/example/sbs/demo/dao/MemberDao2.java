package com.example.sbs.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.sbs.demo.dto.Member;



@Mapper
public interface MemberDao2 {
	public Member test1();

	public Member findByLoginId(@Param("loginId") String loginId);

	public Member findByLoginIdAndLoginPasswd(@Param("loginId") String loginId,
			@Param("loginPasswd") String loginPasswd);

	public void add(Member member);
}