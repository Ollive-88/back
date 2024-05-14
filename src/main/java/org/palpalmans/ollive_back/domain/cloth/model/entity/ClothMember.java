package org.palpalmans.ollive_back.domain.cloth.model.entity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import org.palpalmans.ollive_back.common.BaseTimeEntity;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cloth_member")
@NoArgsConstructor(access = PROTECTED)
public class ClothMember extends BaseTimeEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "cloth_id")
	private Cloth cloth;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public ClothMember(Cloth cloth, Member member){
		this.cloth = cloth;
		this.member = member;
	}
}