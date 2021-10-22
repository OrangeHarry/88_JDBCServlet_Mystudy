package spms.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MemberUpdateServlet
 */
@WebServlet("/member/update")
public class MemberUpdateServlet extends HttpServlet {
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String id = request.getParameter("ID");
		String sqlSelect = "SELECT * FROM MEMBER WHERE ID = " + "'" + id + "'";
		

		//서블릿 컨텍스트 객체 생성
		ServletContext sc = this.getServletContext();
		
		//컨텍스트 초기화 매개변수 꺼내 쓰기 -> sc.getIninParameter메소드로 접근
		String driver = sc.getInitParameter("driver");
		String url = sc.getInitParameter("url");
		String id_v = sc.getInitParameter("username");
		String pwd = sc.getInitParameter("password");
		
		try {
			//mysql 제어 객체 로딩
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id_v, pwd);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlSelect);
			rs.next();
			
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>회원정보</title></head>");
			out.println("<body><h1>회원정보</h1>");
			out.println("<form action='update' method='post'>");
			out.println("아이디 : <input type='text' name='id' value='" + id + "' readonly> <br>");
			out.println("비밀번호 : <input type='password' name='password' value='" + rs.getNString("PWD") + "'> <br>");
			out.println("이름 : <input type='text' name='name' value='" + rs.getNString("name") + "'> <br>");
			out.println("성별 : <input type='text' name='gender' value='" + rs.getNString("gender") + "'> <br>");
			out.println("<input type='submit' value='저장'>");
			out.println("<input type='button' value='취소' onclick='location.href=\"list\"'>");
			out.println("</form></body></html>");
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			try {
				if(stmt != null) {
					stmt.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			try {
				if(conn != null) {
					conn.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		String sqlUpdate = "UPDATE MEMBER SET PWD=?, NAME=?, GENDER=? WHERE ID=?";
		
		//서블릿 컨텍스트 객체 생성
		ServletContext sc = this.getServletContext();
		
		String driver = sc.getInitParameter("driver");
		String url = sc.getInitParameter("url");
		String id_v = sc.getInitParameter("username");
		String pwd = sc.getInitParameter("password");
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id_v, pwd);
			stmt = conn.prepareStatement(sqlUpdate);
			stmt.setString(1, request.getParameter("password"));
			stmt.setString(2, request.getParameter("name"));
			stmt.setString(3, request.getParameter("gender"));
			
			stmt.setString(4, request.getParameter("id"));
			
			stmt.executeUpdate();
			
			response.sendRedirect("list");
		} catch(Exception e) {
			e.printStackTrace();
		} finally {			
			try {
				if(stmt != null) {
					stmt.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			try {
				if(conn != null) {
					conn.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
