package com.study.board.service;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;

import jakarta.annotation.Resource;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	public void write(Board board, MultipartFile file) throws Exception {
		
		if (file.isEmpty()) {
			board.setFilename(board.getFilename());
			board.setFilepath(board.getFilepath());
		}else {
			
			String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
			
			UUID uuid = UUID.randomUUID();
			
			String fileName = uuid + "_" + file.getOriginalFilename();
			
			File saveFile = new File(projectPath, fileName);
			
			file.transferTo(saveFile);
			
			board.setFilename(fileName);
			board.setFilepath("/files/" + fileName);
		}
		
		boardRepository.save(board);
		
	}
	
	public Page<Board> boardList(Pageable pageable) {
		
		return boardRepository.findAll(pageable);
	}
	
	public Board boardView(Integer id) {
		
		return boardRepository.findById(id).get();
	}
	
	public void boardDelete(Integer id) {
		
		boardRepository.deleteById(id);
	}
	
	public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
		
		return boardRepository.findByTitleContaining(searchKeyword, pageable);
	}
	
}
