package org.sundo.list.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Observatory {
	@Id
	@GeneratedValue
	private Long seq;
	
	//@ManyToOne
	private Long code; // MNUM - db 연결
	
	private String name; // 관측소명
	
	private String method; // 관측방식 
	
	private String org; // 운영기관
	
	private String type; // 수위, 강수량, 유량, cctv
	
	private boolean active; // 사용여부
	
	private double tenMAvg; // 10분
	
	private double hourAvg; // 1시간
	
	private double dateAvg; // 일
	
	private double monthAvg; // 월
	
	private double yearAvg; // 년
	

}
