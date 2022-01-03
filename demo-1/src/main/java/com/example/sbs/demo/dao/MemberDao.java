package com.example.sbs.demo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.sbs.demo.dto.Member;


@Component
public class MemberDao {
	private List<Member> members;
	private int lastId;

	MemberDao() {
		members = new ArrayList<>();
		lastId = 0;
	}

	public int add(Member member) {
		members.add(member);

		lastId = member.getId();

		return member.getId();
	}

	public int getNewId() {
		return lastId + 1;
	}

	public Member findByLoginId(String loginId) {
		for (Member member : members) {
			if (member.getLoginId().equals(loginId)) {
				return member;
			}
		}

		return null;
	}

	public Member findByLoginIdAndLoginPasswd(String loginId, String loginPasswd) {
		for (Member member : members) {
			if (member.getLoginId().equals(loginId) && member.getLoginPasswd().equals(loginPasswd)) {
				return member;
			}
		}

		return null;
	}

	public Member findMemberById(int id) {
		for (Member member : members) {
			if (member.getId() == id) {
				return member;
			}
		}

		return null;
	}

	public Member findByNameAndEmail(String name, String email) {
		for (Member member : members) {
			if (member.getName().equals(name) && member.getEmail().equals(email)) {
				return member;
			}
		}

		return null;
	}

	public Member findByLoginIdAndNameAndEmail(String loginId, String name, String email) {
		for (Member member : members) {
			if (member.getLoginId().equals(loginId) && member.getName().equals(name) && member.getEmail().equals(email)) {
				return member;
			}
		}

		return null;
	}
}