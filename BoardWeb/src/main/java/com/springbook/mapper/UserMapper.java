package com.springbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.springbook.vo.MemberVO;
//사용자 정보를 찾는 메소드 

@Mapper
public interface UserMapper {
	MemberVO findById(String username);

	void join(MemberVO vo);
}
