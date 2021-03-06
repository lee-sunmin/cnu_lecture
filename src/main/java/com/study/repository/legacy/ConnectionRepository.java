package com.study.repository.legacy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.study.domain.jpa.JpaBoard;

@Repository
public class ConnectionRepository {
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	
	private Connection conn = null;
	
	
	@PostConstruct
	public void init() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");                       // 데이터베이스와 연동하기 위해 DriverManager에 등록한다.
		conn = DriverManager.getConnection(url, username, password);   
	}

	public List<JpaBoard> selectBoardList() throws SQLException {
		String sql = "SELECT id, name, create_at FROM jpa_board";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();

		List<JpaBoard> jpaBoardList = new ArrayList<>();
		
		while(rs.next()) {
			JpaBoard jpaBoard = new JpaBoard();
			jpaBoard.setId(rs.getLong("id"));
			jpaBoard.setName(rs.getString("name"));
			jpaBoard.setCreateAt(rs.getDate("create_at"));
			
			jpaBoardList.add(jpaBoard);
		}
		
		return jpaBoardList;
	}

	
	public List<JpaBoard> selectBoard(String boardName) throws SQLException {
		String sql = "SELECT id, name, create_at FROM jpa_board WHERE name = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, boardName);
		ResultSet rs = pstmt.executeQuery();

		List<JpaBoard> jpaBoardList = new ArrayList<>();
		
		while(rs.next()) {
			JpaBoard jpaBoard = new JpaBoard();
			jpaBoard.setId(rs.getLong("id"));
			jpaBoard.setName(rs.getString("name"));
			jpaBoard.setCreateAt(new Date(rs.getLong("create_at")));

			jpaBoardList.add(jpaBoard);
		}
		
		return jpaBoardList;
	}
}
