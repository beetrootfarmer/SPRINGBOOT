package com.springbook.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.springbook.common.FileUtils;
import com.springbook.service.BoardService;
import com.springbook.vo.BoardFileVO;
import com.springbook.vo.BoardVO;

@Controller
//board로 model 저장된 객체가 있으면 HttpSession 데이터 보관소에서 동일한 키값(board)로 저장
@SessionAttributes("board")
public class BoardController {
	@Autowired
	private BoardService boardService;

	// 검색 조건 목록 설정
	@ModelAttribute("conditionMap")
	public Map<String, String> searchConditionMap() {
		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("제목", "TITLE");
		conditionMap.put("내용", "CONTENT");
		return conditionMap;
	}

	// 글 등록
	@GetMapping(value = "/insertBoard.do")
	public String insertBoardView() {
		return "insertBoard";
	}
	@PostMapping(value = "/insertBoard.do")
	public String insertBoard(BoardVO vo, HttpServletRequest request, MultipartHttpServletRequest mhsr)
			throws IOException {
		System.out.println("title===============" + vo.getTitle());
		int seq = boardService.getBoardSeq();

		FileUtils fileUtils = new FileUtils();
		List<BoardFileVO> fileList = fileUtils.parseFileInfo(seq, request, mhsr);
		
		if(CollectionUtils.isEmpty(fileList) == false) {
			boardService.insertBoardFileList(fileList);
		}

		boardService.insertBoard(vo);

		return "redirect:getBoardList.do";
	}

	// 글 수정
	// @ModelAttribute로 세션에 board라는 이름으로 저장된 객체가 있는지 찾아서 Command 객체에 담아줌
	@RequestMapping(value="/updateBoard.do")
	public String updateBoard(@ModelAttribute("board") BoardVO vo, MultipartHttpServletRequest mhsr, HttpServletRequest request) throws IOException {
		System.out.println("글 수정 처리");
		System.out.println("번호 : " + vo.getSeq());
		System.out.println("제목 : " + vo.getTitle());
		System.out.println("작성자 이름 : " + vo.getWriter());
		System.out.println("내용 : " + vo.getContent());
		System.out.println("등록일 : " + vo.getRegDate());
		System.out.println("조회수 : " + vo.getCnt());

		//int seq = vo.getSeq();
		int seq = boardService.getBoardSeq();

		
		FileUtils fileUtils = new FileUtils();
		List<BoardFileVO> fileList = fileUtils.parseFileInfo(seq, request, mhsr);
	
		if(CollectionUtils.isEmpty(fileList) == false) {
			boardService.insertBoardFileList(fileList);
		}
		//boardService.updateBoard(vo);
		boardService.insertBoard(vo);


		return "redirect:getBoardList.do";
	}

	// 글 삭제
	@RequestMapping(value="/deleteBoard.do")
	public String delteBoard(BoardVO vo) {

		boardService.deleteBoard(vo);
		return "redirect:getBoardList.do";
	}

	// 글 상세 조회
	@RequestMapping("/getBoard.do")
	public String getBoard(BoardVO vo, Model model) {
		model.addAttribute("board", boardService.getBoard(vo)); // Model 정보 저장
		model.addAttribute("fileList", boardService.getBoardFileList(vo.getSeq()));		
		return "getBoard";
	}

	// 글 목록 검색
	@RequestMapping(value = "/getBoardList.do")
	public String getBoardList(BoardVO vo, Model model) {
		// Null Check
		if(vo.getSearchCondition() == null) {
			vo.setSearchCondition("TITLE");
		}
		if(vo.getSearchKeyword() == null) {
			vo.setSearchKeyword("");
		}
		model.addAttribute("boardList", boardService.getBoardList(vo));
		return "getBoardList"; // view이름 리턴
	}
	@RequestMapping(value="/deleteFile.do")
	@ResponseBody
	public void deleteFile(BoardFileVO vo) {
		boardService.deleteFileList(vo);
	}
	@RequestMapping(value="/fileDown.do")
	@ResponseBody
	public ResponseEntity<Resource> fileDown(@RequestParam("fileName")String fileName, HttpServletRequest request) throws Exception {
		// 업로드 파일 경로
		String path = request.getSession().getServletContext().getRealPath("/") + "/upload/";
		
		// 파일경로, 파일명으로 리소스 객체 생성
		Resource resource =  new FileSystemResource(path + fileName);
		
		// 리소스 파일명 다시 꺼내옴 
		String resourceName  = resource.getFilename();
		
		// Http 헤더에 옵션을 추가하기 위해서 헤더 변수 선언 
		HttpHeaders headers = new HttpHeaders();
		
		try {
			// 헤더에 파일 명으로 첨부파일 추가 
			headers.add("Content-Disposition", "attatchment; fileName=" + new String(resourceName.getBytes(
						"UTF-8"), "ISO-8859-1"));
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK) ;
		
	}
}
