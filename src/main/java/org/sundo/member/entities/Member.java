package org.sundo.member.entities;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class Member {
	@Id @GeneratedValue
	private Long seq;
	
	@Column(length=40, unique=true)
	private String userId;
}
