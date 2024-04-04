package org.palpalmans.ollive_back.domain.board.service;

import org.palpalmans.ollive_back.domain.board.model.entity.Board;
import org.palpalmans.ollive_back.domain.board.model.entity.View;
import org.palpalmans.ollive_back.domain.board.repository.ViewRepository;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewService {
	private final ViewRepository viewRepository;

	public void saveView(Board board, Member member) {
		viewRepository.save(new View(board, member)); // TODO 로그인 구현시 수정 예정
	}
}
