package com.test.orello.achart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.test.orello.DBUtil;
import com.test.orello.aproject.ProjectDTO;

public class ChartDAO {

	
	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	public ChartDAO() {
		DBUtil util = new DBUtil();
		conn = util.open();
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	//입력받은 기간동안 포인트를 사용한 회원 목록 
	public ArrayList<ChartDTO> pointlist(HashMap<String, String> map) {
		int cnt = 0;
		try {
	
			String sql = "select m.name as name, pc.regdate as regdate, pc.amount as amount, m.point as totalPoint from tbl_point_change pc inner join tbl_member m on pc.member_seq = m.seq where pc.regdate between ? and ? order by m.name, pc.regdate";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("start"));
			pstat.setString(2, map.get("end"));
			
			rs = pstat.executeQuery();
			
			ArrayList<ChartDTO> list = new ArrayList<ChartDTO>();
			
			while(rs.next()) {
				ChartDTO dto = new ChartDTO();
				
				dto.setSeq(++cnt);
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("regdate").substring(0,10));
				dto.setAmount(rs.getInt("amount"));
				dto.setTotalPoint(rs.getInt("totalPoint"));
				
				list.add(dto);
			}
			System.out.println(list + "$$$$");
			return list;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public int getTotalCount(HashMap<String, String> map) {
		try {
			
			  String where = "";
			  
			  if (map.get("search") != null) { // 이름 & 제목 & 내용 - 포괄 검색 
				  where = String.format("where (name like '%s')", map.get("search")); }
		

			  System.out.println(map.get("start"));
			  System.out.println(map.get("finish"));
			String sql = "select count(*) as cnt from tbl_point_change pc inner join tbl_member m on pc.member_seq = m.seq where pc.regdate between ? and ? order by m.name, pc.regdate";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1,map.get("start"));
			pstat.setString(2, map.get("finish"));
			
			rs = pstat.executeQuery();

			if (rs.next()) {
				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			System.out.println("BoardDAO.getTotalCount()");
			e.printStackTrace();
		}

		return 0;
	}

	
	
	public ArrayList<ChartDTO> memberlist(HashMap<String, String> map) {
		int cnt = 0;
		try {
			//목록 or 검색
			String where = "";
			
			if (map.get("search") != null) {
				//이름 & 제목 & 내용 - 포괄 검색
				where = String.format("where name like '%s'", map.get("search"));

			}
			
			String sql = String.format("select * from (select a.*, rownum as rnum from (select m.name as name, pc.regdate as regdate, pc.amount as amount, m.point as totalPoint from tbl_point_change pc inner join tbl_member m on pc.member_seq = m.seq where pc.regdate between '%s' and '%s' order by m.name, pc.regdate) a) where rnum >= '%s' and rnum <= '%s'", 
					map.get("start"), map.get("finish"), map.get("begin"), map.get("end"));
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			ArrayList<ChartDTO> list = new ArrayList<ChartDTO>();

			while (rs.next()) {
				ChartDTO dto = new ChartDTO();
				
				dto.setSeq(++cnt);
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("regdate").substring(0,10));
				dto.setAmount(rs.getInt("amount"));
				dto.setTotalPoint(rs.getInt("totalPoint"));

				list.add(dto);

			}
			System.out.println(list);
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	//PL count값 list 가져오기
	public ArrayList<PlDTO> pllist() {
		try {
			String sql = "select count(*) as cnt from TBL_CODE_BOARD group by type";
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			ArrayList<PlDTO> list = new ArrayList<>();
			
			while(rs.next()) {
				PlDTO dto = new PlDTO();
				
				dto.setCnt(rs.getInt("cnt"));
				list.add(dto);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<MonproDTO> plist() {
		try {
			String sql = "select count(*) as cnt from tbl_project group by to_char(startdate, 'mm') order by to_char(startdate, 'mm')";
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			ArrayList<MonproDTO> list = new ArrayList<>();
			while(rs.next()) {
				MonproDTO dto = new MonproDTO();
				
				dto.setCnt(rs.getInt("cnt"));
				list.add(dto);
			}
			return list;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public int getProjectCount(HashMap<String, String> map) {
		try {
			
			  String where = "";
			  
			  if (map.get("search") != null) { // 이름 & 제목 & 내용 - 포괄 검색 
				  where = String.format("where (name like '%s')", map.get("search")); }
		

			  System.out.println(map.get("start"));
			  System.out.println(map.get("finish"));
			String sql = "select count(*) as cnt from tbl_member where regdate between ? and ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1,map.get("start"));
			pstat.setString(2, map.get("finish"));
			
			rs = pstat.executeQuery();

			if (rs.next()) {
				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			System.out.println("BoardDAO.getTotalCount()");
			e.printStackTrace();
		}

		return 0;
	}

	public ArrayList<MonproDTO> joinlist(HashMap<String, String> map) {
		int cnt=1;
		try {
			String sql = String.format("select * from (select a.*, rownum as rnum from (select name, regdate from tbl_member where regdate between '%s' and '%s' ) a) where rnum >= '%s' and rnum <= '%s' order by regdate",
										map.get("start"), map.get("finish"), map.get("begin"), map.get("end"));
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			ArrayList<MonproDTO> list = new ArrayList<>();
			
			while(rs.next()) {
				MonproDTO dto = new MonproDTO();
				
				dto.setCnt(cnt++);
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("regdate").substring(0,10));
				list.add(dto);
			}
			return list;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	//로그인한 날짜 데이터 가져오기
	public ArrayList<ChartDTO> getLoginM() {
		Calendar cal = Calendar.getInstance();
		try {
			String sql = "select regdate from tbl_login group by regdate having regdate between ? and ? order by regdate asc";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(2, String.format("%tF", cal));
			cal.add(Calendar.DAY_OF_MONTH, -6);
			pstat.setString(1,String.format("%tF", cal));
			
			rs = pstat.executeQuery();
			ArrayList<ChartDTO> list = new ArrayList<>();
			while(rs.next()) {
				ChartDTO dto = new ChartDTO();
				
				dto.setRegdate(rs.getString("regdate").substring(5,10));
				list.add(dto);
			}
			return list;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	//날짜별로 로그인한 멤버 수 가져오기
	public ArrayList<ChartDTO> getCountM() {
		Calendar cal = Calendar.getInstance();
		try {
			String sql = "select count(*) as cnt from tbl_login group by regdate having regdate between ? and ? order by regdate asc";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(2, String.format("%tF", cal));
			cal.add(Calendar.DAY_OF_MONTH, -6);
			pstat.setString(1,String.format("%tF", cal));
			
			rs = pstat.executeQuery();
			
			ArrayList<ChartDTO> list = new ArrayList<>();
			while(rs.next()) {
				ChartDTO dto = new ChartDTO();
				
				dto.setLoginMemberCnt(rs.getInt("cnt"));
				list.add(dto);
			}
			return list;
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	//날짜별로 채팅빈도수 가져오기
	public ArrayList<ChartDTO> getCountCharting() {
		Calendar cal = Calendar.getInstance();
		try {
			String sql = "select count(*) as cnt from tbl_chat_log group by TO_CHAR(regdate, 'yyyy-mm-dd') having TO_CHAR(regdate, 'yyyy-mm-dd') between ? and ? order by TO_CHAR(regdate, 'yyyy-mm-dd')";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(2, String.format("%tF", cal));
			cal.add(Calendar.DAY_OF_MONTH, -6);
			pstat.setString(1,String.format("%tF", cal));
			
			rs = pstat.executeQuery();
			
			ArrayList<ChartDTO> list = new ArrayList<>();
			
			while(rs.next()) {
				ChartDTO dto = new ChartDTO();
				
				dto.setChatingCnt(rs.getInt("cnt"));
				list.add(dto);
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
