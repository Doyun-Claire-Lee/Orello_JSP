package com.test.orello.chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet("/chat/multichatlist.do")
public class MultiChatList extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		//정보 받아오기
		String pseq = req.getParameter("pseq");
		String mseq = "2";		//나중에 세션에서..
		
		//DB작업
		ChatDAO dao = new ChatDAO();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pseq", pseq);
		map.put("mseq", mseq);
		ArrayList<MessageDTO> list = dao.getMultiChatList(map);
		dao.close();
		
		JSONArray arr = new JSONArray();
		for (MessageDTO dto : list) {

			JSONObject obj = new JSONObject();
			obj.put("name", dto.getName());
			obj.put("regdate", dto.getRegdate());
			obj.put("content", dto.getContent());
			obj.put("myflag", dto.getMyflag());
			
			arr.add(obj);
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		writer.print(arr);
		
		writer.close();
 		
	}
	
	
}
