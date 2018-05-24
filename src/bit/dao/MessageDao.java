package bit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import bit.model.Comment;
import bit.model.HotTopic;
import bit.model.Message;
import bit.model.MessagePage;
import bit.utils.C3p0Utils;
import bit.utils.HackerNewsSort;
import bit.utils.Heap;
import bit.utils.HotNode;
import bit.utils.TextTransfer;

public class MessageDao {
	
	public int insertMess(Message m){
		Connection conn = C3p0Utils.getConnection();

		String sql = "INSERT INTO message"
				+ "(uid, towho, content, fromwho, anonymity, messtype, createtime, liketimes, jointopic, notcomm, commentnum)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?, ?, 0)";
		PreparedStatement ptmt = null;
		int rows = 0;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, m.getUid());
			ptmt.setString(2, m.getTowho());
			ptmt.setString(3, m.getContent());
			ptmt.setString(4, m.getFromwho());
			ptmt.setInt(5, m.getAnonymity());
			ptmt.setString(6, m.getMessType());
			ptmt.setTimestamp(7, m.getCreateTime());
			ptmt.setInt(8, m.getJoinTopic());
			ptmt.setInt(9, m.getNotComm());
			
			rows = ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ptmt != null) {ptmt.close();}
				if(conn != null) {conn.close();}
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rows;
	}
	
	public int deleteMessage(int targetMid, int curUid){
		Connection conn = C3p0Utils.getConnection();
		String sql = "";
		//当前用户为管理员
		if(curUid == 1){
			sql = "DELETE FROM message WHERE mid=?";
		}else{
			sql = "DELETE FROM message WHERE mid=? AND uid=?";
		}
		PreparedStatement ptmt = null;
		int affect = 0;
		
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, targetMid);
			if(curUid > 1){
				ptmt.setInt(2, curUid);
			}
			affect = ptmt.executeUpdate();
			
			//删除相关联的点赞以及评论
			if(affect > 0){
				sql = "DELETE FROM liked WHERE mid=?";
				ptmt = conn.prepareStatement(sql);
				ptmt.setInt(1, targetMid);
				ptmt.executeUpdate();
				
				CommentDao commentDao = new CommentDao();
				commentDao.deleteCommentByCmid(targetMid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if( ptmt != null){ ptmt.close();}
				if( conn != null){ conn.close();}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		return affect;
	}
	
	public int likeMessage(int targetMid, int curUid){
		Connection conn = C3p0Utils.getConnection();
		String sql = "SELECT * FROM liked WHERE mid=? AND uid=?";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		int affect = 0;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, targetMid);
			ptmt.setInt(2, curUid);
			rs = ptmt.executeQuery();
			
			if(rs.first()){//如果已经赞过，message的liketimes-1
				sql = "UPDATE message SET liketimes=liketimes-1 WHERE mid=? AND liketimes>0";
				ptmt = conn.prepareStatement(sql);
				ptmt.setInt(1, targetMid);
				affect = ptmt.executeUpdate();
				if(affect >= 1){
					//删除liked表中的相关记录
					sql = "DELETE FROM liked WHERE mid=? AND uid=?";
					ptmt = conn.prepareStatement(sql);
					ptmt.setInt(1, targetMid);
					ptmt.setInt(2, curUid);
					affect = ptmt.executeUpdate();
				}
			}else{//没有赞过
				sql = "UPDATE message SET liketimes=liketimes+1 WHERE mid=?";
				ptmt = conn.prepareStatement(sql);
				ptmt.setInt(1, targetMid);
				affect = ptmt.executeUpdate();
				if(affect >= 1){
					//liked表中增加相关记录
					sql = "INSERT INTO liked (uid, mid, sstate) VALUES (?,?,1)";
					ptmt = conn.prepareStatement(sql);
					ptmt.setInt(1, curUid);
					ptmt.setInt(2, targetMid);
					affect = ptmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(rs != null){ rs.close();}
				if(ptmt != null){ ptmt.close();}
				if(conn != null){ conn.close();}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return affect;
	}
	
	/*
	 * 根据mid的map获取对应的最热消息
	 */
	public ArrayList<Message> getHotMessList(List<Integer> mids, int curUid){
		ArrayList<Message> hotMessages = new ArrayList<Message>();
		
		String sql = "SELECT m.*, l.sstate FROM ( "
				+ "SELECT me.*, us.nickname, us.headimgurl FROM ( "
				+ "SELECT * FROM message WHERE mid IN ( ";
		if(mids == null || mids.size() == 0){
			sql += "-1";
		}else{
			for(Integer mid : mids){
				sql = sql + mid + ",";
			}
			sql = sql.substring(0, sql.length()-1);
		}
		sql += ") ) AS me, user us WHERE me.uid=us.uid ) AS m "
				+ "LEFT JOIN ( SELECT * FROM liked WHERE uid=? ) AS l ON m.mid=l.mid";
		
		Connection conn = C3p0Utils.getConnection();
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, curUid);
			rs = ptmt.executeQuery();
			while(rs.next()){
				//test
				System.out.println("sstate:" + rs.getInt("sstate"));
				Message m = new Message();
				m.setMid(rs.getInt("mid"));
				m.setUid(rs.getInt("uid"));
				m.setAnonymity(rs.getInt("anonymity"));
				m.setContent(rs.getString("content"));
				m.setCreateTime(rs.getTimestamp("createtime"));
				m.setFromwho(rs.getString("fromwho"));
				m.setTowho(rs.getString("towho"));
				m.setHeadimgurl(rs.getString("headimgurl"));
				m.setNickname(rs.getString("nickname"));
				m.setLiketimes(rs.getInt("liketimes"));
				m.setMessType(rs.getString("messtype"));
				m.setNotComm(rs.getInt("notcomm"));
				m.setSstate(rs.getInt("sstate"));
				m.setSimpleTime(TextTransfer.getTimeFormatText(rs.getTimestamp("createtime")));
				//test
				System.out.println(m.getSimpleTime());
				//获取评论，主页面的只显示一条评论的情况
				if(rs.getInt("notcomm") < 1 && rs.getInt("commentnum")>0){
					CommentDao commentDao = new CommentDao();
					m.setHotComment(commentDao.getHotComment(commentDao.getHotCommCid(rs.getInt("mid")), curUid, rs.getInt("mid")));
				}
				//test
				System.out.println("cotent:" + m.getContent());
				System.out.println("commentConten:" + m.getHotComment().getComment());
				hotMessages.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(rs != null){ rs.close(); }
				if(ptmt != null){ ptmt.close(); }
				if(conn != null){ conn.close(); }
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		//调整顺序，把新的放前面
		for(Integer mid : mids){
			for(int i=0; i<hotMessages.size(); i++){
				if(hotMessages.get(i).getMid() == mid){
					hotMessages.add(hotMessages.remove(i));
				}
			}
		}
		//test
		System.out.println("hotMessages:" + hotMessages);
		return hotMessages;
	}
	
	/*
	 * 获得前一周的liketimes超过0的message并打分排序
	 */
	public List<Integer> getHotMessMids(){
		//获取当前一周的时间
		Timestamp lastWeek = HackerNewsSort.getLastWeek();
		
		Connection conn = C3p0Utils.getConnection();
		String sql = "SELECT mid, createtime, liketimes FROM message WHERE liketimes>0 AND createtime>? AND jointopic<1";
		
		//计算热度
		ArrayList<HotNode> nodes = new ArrayList<HotNode>();
		Timestamp end = new Timestamp(new Date().getTime());
		DecimalFormat accuracy = new DecimalFormat("0.00000");
		
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, lastWeek.toString());
			rs = ptmt.executeQuery();
			
			while(rs.next()){
				HotNode node = new HotNode();
				node.setMid(rs.getInt("mid"));
				node.setScore(HackerNewsSort.hackerScore(rs.getInt("liketimes"), rs.getTimestamp("createtime"), end, 1.5, accuracy));
				nodes.add(node);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if( rs != null){ rs.close(); }
				if( ptmt != null){ ptmt.close(); }
				if( conn != null){ conn.close(); }
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		//根据value值对map排序
		Heap.heapSort(nodes);
		
		List<Integer> copyed = new ArrayList<Integer>();
		int len = nodes.size() < 6 ? nodes.size() : 6;
		for(int i=0; i<len; i++){
			copyed.add(nodes.get(i).getMid());
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = new java.util.Date();
		String str = sdf.format(date);
		System.out.println("Hot message统计中..."+str);
		System.out.println(copyed.toString());
		
		return copyed;
	}
	
	/*
	 * 根据偏移量获取最新的几条数据
	 */
	public MessagePage getMessagePage(int zongshu, int yema, int yechang, int curUid, List<Integer> hotMessMids){
		//防止空指针异常
		if(hotMessMids == null){
			hotMessMids = new ArrayList<Integer>();
		}
		
		MessagePage messagePage = new MessagePage();
		int offset = zongshu-(yema+1)*yechang;
		int amount = yechang;
		
		if(offset<0){
			amount = offset+yechang;
			offset = 0;
		}
		if(amount<0){
			return messagePage;
		}
		List<Message> news = new ArrayList<Message>();
		
		String sql = "( SELECT m.*, l.sstate FROM "
				+ "( ( SELECT me.*, us.nickname, us.headimgurl FROM message me, user us WHERE me.jointopic<1 AND me.uid=us.uid ORDER BY me.mid ASC LIMIT ?,?) AS m ) "
				+ "LEFT JOIN ( ( SELECT * FROM liked WHERE uid=? ) AS l ) "
				+ "ON m.mid=l.mid ) ORDER BY mid DESC";
		Connection conn = C3p0Utils.getConnection();
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, offset);
			ptmt.setInt(2, amount);
			ptmt.setInt(3, curUid);
			rs = ptmt.executeQuery();
			//test
			System.out.println("test1");
			while(rs.next()){
				//test
				System.out.println("test2");
				System.out.println("message:"+rs.getInt("mid"));
				//在热门消息中出现就不会出现在最新消息中
				if(!hotMessMids.contains(rs.getInt("mid"))){
					//test
					System.out.println("message...");
					Message m = new Message();
					m.setMid(rs.getInt("mid"));
					m.setContent(rs.getString("content"));
					m.setCreateTime(rs.getTimestamp("createTime"));
					m.setAnonymity(rs.getInt("anonymity"));
					if(rs.getInt("anonymity") == 0){
						m.setUid(rs.getInt("uid"));
						m.setHeadimgurl(rs.getString("headimgurl"));
						m.setNickname(rs.getString("nickname"));
					}else{
						m.setUid(0);
						m.setHeadimgurl("../img/ni_img.png");
						m.setNickname("匿名");
					}
					m.setLiketimes(rs.getInt("liketimes"));
					m.setMessType(rs.getString("messtype"));
					m.setTowho(rs.getString("towho"));
					m.setFromwho(rs.getString("fromwho"));
					m.setNotComm(rs.getInt("notcomm"));
					m.setSstate(rs.getInt("sstate"));
					m.setSimpleTime(TextTransfer.getTimeFormatText(rs.getTimestamp("createtime")));
					
					//获取评论，message只显示一条评论的情况
					if(rs.getInt("notComm") < 1 && rs.getInt("commentnum")>0){
						CommentDao commentDao = new CommentDao();
						m.setHotComment(commentDao.getHotComment(commentDao.getHotCommCid(rs.getInt("mid")), curUid, rs.getInt("mid")));
						//test
						System.out.println("commentTest" + m.getHotComment());
					}
					//测试
					System.out.println("content" + m.getContent());
					news.add(m);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(rs != null){ rs.close(); }
				if(ptmt != null){ ptmt.close(); }
				if(conn != null){ conn.close(); }
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		System.out.println(news.toString());
		messagePage.setYechang(yechang);
		messagePage.setYema(yema+1);
		messagePage.setZongshu(zongshu);
		messagePage.setMessageList(news);
		messagePage.setHavemore(zongshu-(yema+1)*yechang);
		
		return messagePage;
		
	}
	
	//统计消息
	public int getCount(){
		String sql = "SELECT COUNT(*) FROM message";
		Connection conn = C3p0Utils.getConnection();
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		int zongshu = 0;
		try {
			ptmt = conn.prepareStatement(sql);
			rs = ptmt.executeQuery();
			rs.next();
			zongshu = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(rs != null){ rs.close(); }
				if(ptmt != null){ ptmt.close(); }
				if(conn != null){ conn.close(); }
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return zongshu;
	}
	
	public MessagePage getNewestMessagePage(int curUid, List<Integer> hotMessMids){
		int zongshu = getCount();
		return getMessagePage(zongshu, 0, 10, curUid, hotMessMids);
	}
	
	/*
	 * 根据UID查询对应用户的所有消息（点击头像进去个人页面）
	 */
	public List<Message> getMessListByUid(int curUid, int targetUid){
		List<Message> messageList = new ArrayList<Message>();
		
		String sql = "(SELECT m.*, l.sstate FROM "
				+ "((SELECT me.*, us.nickname, us.headimgurl FROM message me, user us WHERE me.uid=? AND me.anonymity<? AND me.uid=us.uid ) AS m)"
				+ "LEFT JOIN ((SELECT mid, sstate FROM liked WHERE uid=?) AS l) ON m.mid=l.mid) ORDER BY mid DESC";
		Connection conn = C3p0Utils.getConnection();
		
		//第一个UID为被查看用户的UID，第二个UID为当前用户的UID
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, targetUid);
			if(targetUid == curUid){//当前用户即为被查看用户，显示全部message包括匿名message
				ptmt.setInt(2, 2);
			}else{
				ptmt.setInt(2, 1);//否则只显示被查看用户非匿名发布的message
			}
			ptmt.setInt(3, curUid);
			rs = ptmt.executeQuery();
			while(rs.next()){
				Message m = new Message();
				m.setMid(rs.getInt("mid"));
				m.setUid(rs.getInt("uid"));
				m.setContent(rs.getString("content"));
				m.setMessType(rs.getString("messtype"));
				m.setSimpleTime(TextTransfer.getTimeFormatText(rs.getTimestamp("createtime")));
				m.setCreateTime(rs.getTimestamp("createtime"));
				m.setFromwho(rs.getString("fromwho"));
				m.setHeadimgurl(rs.getString("headimgurl"));
				m.setLiketimes(rs.getInt("liketimes"));
				m.setNickname(rs.getString("nickname"));
				m.setSstate(rs.getInt("sstate"));
				m.setTowho(rs.getString("towho"));
				m.setAnonymity(rs.getInt("anonymity"));
				m.setNotComm(rs.getInt("notcomm"));
				
				m.setState(curUid==targetUid ? 1 : 0);
				
				messageList.add(m);
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
		return messageList;		
	}
	
	/*
	 * 根据message的mid获取message(点击进入愿望详情页）
	 */
	public Message getMessByMid(int targetMid, int curUid){
		Message m = null;
		
		String sql = "( SELECT m.* l.sstate FROM"
				+ "( SELECT me.*, us.nickname, us.headimgurl FROM message me, user us WHERE me.uid=us.uid AND me.mid=? ) AS m"
				+ "LEFT JOIN (SELECT mid, sstate FROM liked WHERE uid=?) AS l ON m.mid = l.mid) ORDER BY mid DESC";
		Connection conn = C3p0Utils.getConnection();
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, targetMid);
			ptmt.setInt(2, curUid);
			rs = ptmt.executeQuery();
			if(rs.next()){
				m = new Message();
				m.setMid(rs.getInt("mid"));
				m.setUid(rs.getInt("uid"));
				m.setContent(rs.getString("content"));
				m.setMessType(rs.getString("messtype"));
				m.setSimpleTime(TextTransfer.getTimeFormatText(rs.getTimestamp("createtime")));
				m.setCreateTime(rs.getTimestamp("createtime"));
				m.setFromwho(rs.getString("fromwho"));
				m.setHeadimgurl(rs.getString("headimgurl"));
				m.setLiketimes(rs.getInt("liketimes"));
				m.setNickname(rs.getString("nickname"));
				m.setSstate(rs.getInt("sstate"));
				m.setTowho(rs.getString("towho"));
				m.setAnonymity(rs.getInt("anonymity"));
				m.setNotComm(rs.getInt("notcomm"));
				//获取message的所有评论
				if(rs.getInt("notcomm") < 1){
					CommentDao commentDao = new CommentDao();
					m.setComments(commentDao.getCommListByCmid(curUid, targetMid));
				}
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
		return m;
	}
	
	/*
	 * 获取一条随机消息（摇一摇）
	 */
	public Message getRandomMessage(int curUid){
		Message m = null;
		
		int zongshu = getCount();
		int random = (int)(Math.random()*zongshu);
		
		String sql = "SELECT m.*, l.sstate FROM"
				+ "(SELECT me.*, us.nickname, us.headimgurl FROM message me, user us WHERE me.uid=us.uid ORDER BY me.mid ASC LIMIT ?,1) AS m"
				+ "LEFT JOIN (SELECT * FROM liked WHERE uid=?) AS l ON m.mid=l.mid ORDER BY mid DESC";
		
		Connection conn = C3p0Utils.getConnection();
		//找到UID=1的用户的所有消息，降序排序，第一个UID为被查看用户UID，第二个UID为当前用户
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, random);
			ptmt.setInt(2, curUid);
			rs = ptmt.executeQuery();
			
			if(rs.next()){
				m = new Message();
				m.setMid(rs.getInt("mid"));
				m.setUid(rs.getInt("uid"));
				m.setContent(rs.getString("content"));
				m.setMessType(rs.getString("messtype"));
				m.setSimpleTime(TextTransfer.getTimeFormatText(rs.getTimestamp("createtime")));
				m.setCreateTime(rs.getTimestamp("createtime"));
				m.setFromwho(rs.getString("fromwho"));
				m.setHeadimgurl(rs.getString("headimgurl"));
				m.setLiketimes(rs.getInt("liketimes"));
				m.setNickname(rs.getString("nickname"));
				m.setSstate(rs.getInt("sstate"));
				m.setTowho(rs.getString("towho"));
				m.setAnonymity(rs.getInt("anonymity"));
				m.setNotComm(rs.getInt("notcomm"));
				//获取本条message的hot comment
				if(rs.getInt("notcomm") < 1 && rs.getInt("commentnum") > 0){
					CommentDao commentDao = new CommentDao();
					m.setHotComment(commentDao.getHotComment(commentDao.getHotCommCid(rs.getInt("mid")), curUid, rs.getInt("mid")));
				}
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
		return m;
	}
	
	/*
	 * 根据comment的cid获取message（在个人页面中点击comment进入message详情页）
	 */
	public Message getMessByCid(int cid, int curUid){
		Message m = null;
		int targetMid = 0;
		//获取对应的被评论的message的mid
		String sql = "SELECT cmid FROM comment WHERE cid=?";
		Connection conn = C3p0Utils.getConnection();
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, cid);
			rs = ptmt.executeQuery();
			rs.next();
			targetMid = rs.getInt("cmid");
			m = getMessByMid(targetMid, curUid);
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
		return m;
	}
	
	/*
	 * 获取参与热门话题的message,必须传入最新的话题的id以便获取对应的热门话题message
	 */
	public List<Message> getTopicMessListByHtid(int htid, int curUid){
		List<Message> topicMessList = new ArrayList<Message>();
		
		String sql = "SELECT m.*, l.sstate FROM"
				+ "(SELECT me.*, us.nickname, us.headimgurl FROM message me, user us WHERE me.uid=us.uid AND me.jointopic=? ORDER BY me.mid AS me.mid ASC )AS m"
				+ "LEFT JOIN ( SELECT * FROM liked WHERE uid=? ) AS l"
				+ "ON m.mid=l.mid ORDER BY mid DESC";
		Connection conn = C3p0Utils.getConnection();
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, htid);
			ptmt.setInt(2, curUid);
			rs = ptmt.executeQuery();
			while(rs.next()){
				Message m = new Message();
				m.setAnonymity(rs.getInt("anonymity"));
				m.setCommentNum(rs.getInt("commentnum"));
				m.setContent(rs.getString("content"));
				m.setCreateTime(rs.getTimestamp("createtime"));
				m.setFromwho(rs.getString("fromwho"));
				m.setHeadimgurl(rs.getString("headimgurl"));
				m.setJoinTopic(rs.getInt("jointopic"));
				m.setLiketimes(rs.getInt("liketimes"));
				m.setMessType(rs.getString("messtype"));
				m.setMid(rs.getInt("mid"));
				m.setNickname(rs.getString("nickname"));
				m.setSimpleTime(TextTransfer.getTimeFormatText(rs.getTimestamp("createtime")));
				m.setSstate(rs.getInt("sstate"));
				m.setTowho(rs.getString("towho"));
				m.setUid(rs.getInt("uid"));
                
				
				//获取评论，每条message只显示一条评论的情况
				if(rs.getInt("commentnum")>0){
					CommentDao commentDao = new CommentDao();
					m.setHotComment(commentDao.getHotComment(commentDao.getHotCommCid(rs.getInt("mid")), curUid, rs.getInt("mid")));
				}
				
				topicMessList.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return topicMessList;
	}
	
	
	/*
	public static void main(String[] args) {
		
		Message m = new Message();
		m.setUid(1);
		m.setAnonymity(0);
		m.setContent("😁😁😁测试测试测试contencontenconten");
		m.setTowho("!!!test测试");
		m.setFromwho("!!!testtest测试");
        m.setMessType("wish");
        m.setJoinTopic(1);
        
        MessageDao messageDao = new MessageDao();
        System.out.println(messageDao.insertMess(m));
        
	}*/

}
