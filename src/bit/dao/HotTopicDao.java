package bit.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bit.model.HotTopic;
import bit.utils.C3p0Utils;

public class HotTopicDao {
	
	/*
	 * 添加热门话题，仅提供管理员使用
	 */
	public int insertHotTopic(HotTopic h){
		Connection conn = C3p0Utils.getConnection();
		String sql = "INSERT INTO hottopic (topic, createtime) VALUES (?,?)";
		PreparedStatement ptmt = null;
		int rows = 0;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, h.getTopic());
			ptmt.setTimestamp(2, h.getCreateTime());
			rows = ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ptmt!=null) ptmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rows;
	}
	
	/*
	 * 删除热门话题，仅提供管理员使用
	 */
	public int deleteHotTopic(int targetHtid){
		Connection conn = C3p0Utils.getConnection();
		String sql = "DELETE FROM hottopic WHERE htid=?";
		PreparedStatement ptmt = null;
		int affect = 0;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, targetHtid);
			affect = ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ptmt!=null) ptmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return affect;
	}
	
	/*
	 * 获取最新的热门话题
	 */
	public HotTopic getNewestTopic(){
		HotTopic ht = null;
		Connection conn = C3p0Utils.getConnection();
		String sql = "SELECT * FROM hottopic ORDER BY htid DESC LIMIT 1";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			rs = ptmt.executeQuery();
			while(rs.next()){
				ht = new HotTopic();
				ht.setHtId(rs.getInt("htid"));
				ht.setTopic(rs.getString("topic"));
				ht.setCreateTime(rs.getTimestamp("createtime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ptmt!=null) ptmt.close();
				if(rs!=null) rs.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ht;
	}
	
	/*
	public static void main(String[] args) {
		HotTopic ht = new HotTopic();
		ht.setTopic("热门话题测试test");
		
		HotTopicDao hotTopicDao = new HotTopicDao();
		hotTopicDao.insertHotTopic(ht);
	}*/

}
