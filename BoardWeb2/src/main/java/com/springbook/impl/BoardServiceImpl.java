package com.springbook.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbook.mapper.BoardMapper;
import com.springbook.service.BoardService;
import com.springbook.vo.BoardFileVO;
import com.springbook.vo.BoardVO;

@Service("boardService")

public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardMapper boardMapper;

	public void insertBoard(BoardVO vo) {

		boardMapper.insertBoard(vo);
		// boardDAO.insertBoard(vo);
	}

	public void updateBoard(BoardVO vo) {
		boardMapper.updateBoard(vo);
	}

	public void deleteBoard(BoardVO vo) {
		boardMapper.deleteBoard(vo);
	}

	public BoardVO getBoard(BoardVO vo) {
		return boardMapper.getBoard(vo);
	}

	public List<BoardVO> getBoardList(BoardVO vo) {
		return boardMapper.getBoardList(vo);
	}

	public int getBoardSeq() {
		return boardMapper.getBoardSeq();
	}

	public void insertBoardFileList(List<BoardFileVO> fileList) {
		boardMapper.insertBoardFileList(fileList);
	}
	
	public List<BoardFileVO> getBoardFileList(int seq) {
		return boardMapper.getBoardFileList(seq);
	}
	
	public void deleteFile(BoardFileVO vo) {
		boardMapper.deleteFile(vo);
	}
	public void deleteFileList(int seq) {
		boardMapper.deleteFileList(seq);
	}

	


}
