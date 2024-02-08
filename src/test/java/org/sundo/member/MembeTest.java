package org.sundo.member;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.sundo.configs.MvcConfig;
import org.sundo.member.entities.Member;
import org.sundo.member.repositories.MemberRepository;
import org.sundo.member.entities.*;
import org.sundo.member.*;
;
@SpringJUnitWebConfig
@ContextConfiguration(classes = MvcConfig.class)
class MembeTest {
	
	@Autowired
	private MemberRepository repository;
	
	@Test
	void test() {
		//long cnt = repository.count();
		//System.out.println(cnt);
		
		//Member member = new Member();
		//member.setUserId("user01");
		//repository.saveAndFlush(member);
		
		//repository.count(QMember.member.userId.eq("user01"));
	
	}

}
