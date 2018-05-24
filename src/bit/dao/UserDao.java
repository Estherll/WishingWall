package bit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bit.model.BGPicture;
import bit.model.SNSUserInfo;
import bit.model.User;
import bit.utils.C3p0Utils;

public class UserDao {
	
	/*
	 * 查询用户是否在数据库中，若不在返回null
	 */
	public User checkUser(String openid){
		User user = null;
		Connection conn = C3p0Utils.getConnection();
		String sql = "SELECT * FROM user WHERE openid=?";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, openid);
			rs = ptmt.executeQuery();
			if(rs.next()){
				user = new User();
				user.setUid(rs.getInt("uid"));
				user.setBground(rs.getString("bground"));
				user.setHeadimgurl(rs.getString("headimgurl"));
				user.setLastupdate(rs.getTimestamp("lastupdate"));
				user.setNickname(rs.getString("nickname"));
				user.setOpenid(rs.getString("openid"));
				user.setSysnotify(rs.getInt("sysnotify"));
				user.setUsenotify(rs.getInt("usenotify"));
				user.setSignature(rs.getString("signature"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null) rs.close();
				if(ptmt!=null) ptmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}
	
	/*
	 * 添加新用户
	 */
	public Long insertUser(SNSUserInfo userinfo){
		Long uid = null;
		Connection conn = C3p0Utils.getConnection();
		String sql = "INSERT INTO user(openid, nickname, headimgurl, signature, bground, lastupdate, sysnotify, usenotify)"
				+ "VALUES(?,?,?,?,?,?,1,1)";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ptmt.setString(1, userinfo.getOpenId());
			ptmt.setString(2, userinfo.getNickname());
			ptmt.setString(3, userinfo.getHeadImgUrl());
			ptmt.setString(4, "......");
			ptmt.setString(5, "xxxxxxx.png");
			ptmt.setTimestamp(6, new Timestamp(new Date().getTime()));
			ptmt.executeUpdate();
			rs = ptmt.getGeneratedKeys();
			if(rs.next()){
				uid = rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null) rs.close();
				if(ptmt!=null) ptmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return uid;
	}
	
	/*
	 * 更新用户信息
	 */
	public int updateUser(SNSUserInfo userInfo){
		int executeUpdate = 0;
		
		Connection conn = C3p0Utils.getConnection();
		String sql = "UPDATE user SET headimgurl=?, nickname=?, lastupdate=? WHERE openid=?";
		PreparedStatement ptmt = null;
		try {
			ptmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ptmt.setString(1, userInfo.getHeadImgUrl());
			ptmt.setString(2, userInfo.getNickname());
			ptmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			ptmt.setString(3, userInfo.getOpenId());
			executeUpdate = ptmt.executeUpdate();
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
		return executeUpdate;
	}
	
	/*
	 * 根据用户的UID查询用户的信息
	 */
	public User getUser(int targetUid){
		User user = null;
		Connection conn = C3p0Utils.getConnection();
		String sql = "SELECT * FROM user WHERE uid=?";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, targetUid);
			rs = ptmt.executeQuery();
			if(rs.next()){
				user = new User();
				user.setBground(rs.getString("bground"));
				user.setHeadimgurl(rs.getString("headimgurl"));
				user.setLastupdate(rs.getTimestamp("lastupdate"));
				user.setNickname(rs.getString("nickname"));
				user.setOpenid(rs.getString("openid"));
				user.setSignature(rs.getString("signature"));
				user.setSysnotify(rs.getInt("sysnotify"));
				user.setUid(rs.getInt("uid"));
				user.setUsenotify(rs.getInt("usenotify"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null) rs.close();
				if(ptmt!=null) ptmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}
	
	/*
	 * 设置背景
	 */
	public int setBg(String bground, int uid){
		Connection conn = C3p0Utils.getConnection();
		String sql = "UPDATE user SET bground=? WHERE uid=?";
		PreparedStatement ptmt = null;
		int rs = -1;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, bground);
			ptmt.setInt(2, uid);
			rs = ptmt.executeUpdate();
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
		return rs;
	}
	
	/*
	 * 插入上传的额图片到数据库
	 */
	public int provideBg(String bgname, String path, int uid){
		Connection conn = C3p0Utils.getConnection();
		String sql = "INSERT INTO bgs(bgname, bgaddress, provider) VALUES (?,?,?)";
		PreparedStatement ptmt = null;
		int rs = -1;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, bgname);
			ptmt.setString(2, path);
			ptmt.setInt(3, uid);
			rs = ptmt.executeUpdate();
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
		return rs;
	}
	
	/*
	 * 展示多有背景图片信息
	 */
	public List<BGPicture> showAllbgs(){
		List<BGPicture> bgs = new ArrayList<BGPicture>();
		
		Connection conn = C3p0Utils.getConnection();
		String sql = "SELECT * FROM bgs";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			rs = ptmt.executeQuery();
			while(rs.next()){
				BGPicture bg = new BGPicture();
				bg.setBgAddress(rs.getString("bgaddress"));
				bg.setBgId(rs.getInt("bgid"));
				bg.setBgName(rs.getString("bgname"));
				bg.setProvider(rs.getInt("provider"));
				
				bgs.add(bg);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null) rs.close();
				if(ptmt!=null) ptmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bgs;
	}
	
	/**
	 * 设置或重置通知
	 * @param sysnotify 为负数，减一；为零，不变；为正数，加一
	 * @param usenotify 为负数，减一；为零，不变；为正数，加一
	 * @param uid
	 */
	public int dealNotify(int sysnotify, int usenotify, int uid){
		Connection conn = C3p0Utils.getConnection();
		String sql = "";
		if(sysnotify < 0){
			sql = "UPDATE user SET sysnotify=sysnotify-1 WHERE uid=? AND sysnotify>0";
		}else if(sysnotify>0){
			sql = "UPDATE user SET sysnotify=sysnotify+1 WHERE uid=? AND sysnotify>0";
		}
		if(usenotify<0){
			sql = "UPDATE user SET usenotify=usenotify-1 WHERE uid=? AND usenotify>0";
		}else if(usenotify>0){
			sql = "UPDATE user SET usenotify=usenotify+1 WHERE uid=? AND usenotify>0";
		}
		PreparedStatement ptmt = null;
		int rs = -1;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, uid);
			rs = ptmt.executeUpdate();
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
		return rs;
	}
	
	
	public static void main(String[] args) {
		SNSUserInfo u = new SNSUserInfo();
		u.setCity("city");
		u.setCountry("country");
		u.setHeadImgUrl("headImgUrl");
		u.setNickname("nickname");
		u.setOpenId("openid");
        u.setProvince("province");
        u.setSex(1);
        
		UserDao userDao = new UserDao();
		userDao.insertUser(u);
	}

}
