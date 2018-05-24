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
import java.util.List;

import bit.model.Comment;
import bit.utils.C3p0Utils;
import bit.utils.HackerNewsSort;
import bit.utils.Heap;
import bit.utils.HotNode;
import bit.utils.TextTransfer;

public class CommentDao {
	
	/*
	 * �������
	 */
	public int insertComment(Comment c){
		int rows = 0;
		Connection conn = C3p0Utils.getConnection();
		
		String sql = "INSERT INTO comment(comment, uid, cmid, cuid, fromwho, anonymity, liketimes, headimgurl, commenttime)"
				+ "VALUES(?,?,?,?,?,?,0,?,?)";
		PreparedStatement ptmt = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, c.getComment());
			ptmt.setInt(2, c.getUid());
			ptmt.setInt(3, c.getCmid());
			ptmt.setInt(4, c.getCuid());
			ptmt.setString(5, c.getFromwho());
			ptmt.setInt(6, c.getAnonymity());
			ptmt.setString(7, c.getHandimgurl());
			ptmt.setTimestamp(8, c.getCommentTime());
			
			rows = ptmt.executeUpdate();
			
			//�����۵�message������+1
			sql = "UPDATE message SET commentnum=commentnum+1 WHERE mid=?";
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, c.getCmid());
			
			rows = ptmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(ptmt != null) { ptmt.close();}
				if(conn != null) { conn.close();}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		return rows;
	}
	
	/*
	 * ��ȡ����ȶȵ�comment
	 * message��commentNum>1�ŵ����������
	 */
	public int getHotCommCid(int cmid){
		int hotCid = 0;
		//��ȡ��ǰһ�ܵ�ʱ��
		Timestamp lastWeek = HackerNewsSort.getLastWeek();
		//test
		System.out.println("lastWeek:" + lastWeek);
		Connection conn = C3p0Utils.getConnection();
		String sql = "SELECT cid, commenttime, liketimes FROM comment WHERE cmid=? AND liketimes>0 AND commenttime>?";
		
		ArrayList<HotNode> nodes = new ArrayList<HotNode>();
		Timestamp end = new Timestamp(new Date().getTime());
		DecimalFormat accuracy = new DecimalFormat("0.00000");
		
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, cmid);
			ptmt.setString(2, lastWeek.toString());
			rs = ptmt.executeQuery();
			while(rs.next()){
				HotNode node = new HotNode();
				node.setMid(rs.getInt("cid"));
				node.setScore(HackerNewsSort.hackerScore(rs.getInt("liketimes"), rs.getTimestamp("commenttime"), end, 1.5, accuracy));
				nodes.add(node);
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
		
		if(nodes.size() > 0){
			//����valueֵ����
			Heap.heapSort(nodes);
			
			List<Integer> copyed = new ArrayList<Integer>();
			for(int i=0; i<nodes.size(); i++){
				copyed.add(nodes.get(i).getMid());
			}
			
			hotCid = copyed.get(0);
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		java.util.Date date=new java.util.Date();  
		String str=sdf.format(date);  
		
		System.out.println("hotcomment ͳ����...."+str);
		//test
		System.out.println("hotCid:" + hotCid);
		
		return hotCid;
	}
	
	/*
	 * ����hotcid��ȡ��Ӧ��comment
	 */
	public Comment getHotComment(int hotCid, int curUid, int cmid){
		Comment c = null;
		if(hotCid > 0){
			String sql = "SELECT c.*, lc.sstate FROM "
					+ "( ( SELECT co.*, us.nickname, us.headimgurl FROM"
					+ "( SELECT * FROM comment WHERE cid= ? ) AS co, user us WHERE co.uid=us.uid ) AS c ) "
					+ "LEFT JOIN ( ( SELECT * FROM liked_comm WHERE uid=?) AS lc ) ON c.cid=lc.cid";
			Connection conn = C3p0Utils.getConnection();
			PreparedStatement ptmt = null;
			ResultSet rs = null;
			try {
				ptmt = conn.prepareStatement(sql);
				ptmt.setInt(1, hotCid);
				ptmt.setInt(2, curUid);
				rs = ptmt.executeQuery();
				if(rs.next()){
					c = new Comment();
					c.setAnonymity(rs.getInt("anonymity"));
					c.setCid(rs.getInt("cid"));
					c.setCmid(rs.getInt("cmid"));
					c.setComment(rs.getString("comment"));
					c.setCommentTime(rs.getTimestamp("commenttime"));
					c.setCuid(rs.getInt("cuid"));
					c.setFromwho(rs.getString("fromwho"));
					c.setHandimgurl(rs.getString("headimgurl"));
					c.setLikeTimes(rs.getInt("liketimes"));
					c.setUid(rs.getInt("uid"));
					c.setSimpleTime(TextTransfer.getTimeFormatText(rs.getTimestamp("commenttime")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}finally{
				try {
					if(rs!=null) rs.close();
					if(ptmt!=null) ptmt.close();
					if(conn!=null) conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else{
			c = getCommListByCmid(curUid,cmid).get(0);
		}
		//teset
		System.out.println("hotCommrnt:" + c.getComment());
		return c;
	}
	
	/*
	 * ����message��mid��ȡ���е�������ۣ�Ը������ҳ��
	 */
	public List<Comment> getCommListByCmid(int curUid, int cmid){
		List<Comment> commentList = new ArrayList<Comment>();
		
		String sql = "( SELECT c.*, lc.sstate FROM "
				+ "( (SELECT co.*, us.nickname, us.headimgurl FROM comment co, user us WHERE co.cmid=? AND co.uid=us.uid ORDER BY co.cid ASC) AS c ) "
				+ "LEFT JOIN ( ( SELECT * FROM liked_comm WHERE uid=?) AS lc ) ON c.cid=lc.cid ) ORDER BY cid DESC";
		Connection conn = C3p0Utils.getConnection();
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, cmid);
			ptmt.setInt(2, curUid);
			rs = ptmt.executeQuery();
			while(rs.next()){
				Comment c = new Comment();
				c.setAnonymity(rs.getInt("anonymity"));
				c.setCid(rs.getInt("cid"));
				c.setCmid(rs.getInt("cmid"));
				c.setComment(rs.getString("comment"));
				c.setCommentTime(rs.getTimestamp("commenttime"));
				c.setCuid(rs.getInt("cuid"));
				c.setFromwho(rs.getString("fromwho"));
				c.setHandimgurl(rs.getString("headimgurl"));
				c.setLikeTimes(rs.getInt("liketimes"));
				c.setUid(rs.getInt("uid"));
				c.setSimpleTime(TextTransfer.getTimeFormatText(rs.getTimestamp("commenttime")));
				
				commentList.add(c);
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
		//test
		System.out.println("commentList:" + commentList.toString());
		return commentList;
	}
	
	/*
	 * ����comment��UID��ȡ�û������ۣ�����ҳ�棩
	 */
	public List<Comment> getCommListByUid(int curUid, int targetUid){
		List<Comment> commList = new ArrayList<Comment>();
		String sql = "( SELECT c.*, lc.sstate FROM "
				+ "( ( SELECT co.*, us.nickname, us.headimgurl FROM comment co, user us WHERE co.uid=? AND co.anonymity<? AND co.uid=us.uid ) AS c ) "
				+ "LEFT JOIN ( SELECT cid, sstate FROM liked_comm WHERE uid=? ) AS lc ON c.cid=lc.cid ) ORDER BY cid DESC";
		Connection conn = C3p0Utils.getConnection();
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, targetUid);
			if(targetUid == curUid){
				ptmt.setInt(2, 2);
			}else{
				ptmt.setInt(2, 1);
			}
			ptmt.setInt(3, curUid);
			rs = ptmt.executeQuery();
			while(rs.next()){
				Comment c = new Comment();
				c.setAnonymity(rs.getInt("anonymity"));
				c.setCid(rs.getInt("cid"));
				c.setCmid(rs.getInt("cmid"));
				c.setComment(rs.getString("comment"));
				c.setCommentTime(rs.getTimestamp("commenttime"));
				c.setCuid(rs.getInt("cuid"));
				c.setFromwho(rs.getString("fromwho"));
				c.setHandimgurl(rs.getString("headimgurl"));
				c.setLikeTimes(rs.getInt("liketimes"));
				c.setSimpleTime(TextTransfer.getTimeFormatText(rs.getTimestamp("commenttime")));
				c.setUid(rs.getInt("uid"));
				
				c.setState(curUid == targetUid ? 1 : 0);
				
				commList.add(c);
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
		return commList;
	}

	public int deleteComment(int targetCid, int curUid){
		Connection conn = C3p0Utils.getConnection();
		String sql = "SELECT cuid, cmid FROM comment WHERE cid=?";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		int affect = 0;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, targetCid);
			rs = ptmt.executeQuery();
			rs.next();
			int cuid = rs.getInt("cuid");
			int cmid = rs.getInt("cmid");
			if(curUid == 1 || curUid == cuid){//����Ա�����۵��û�ɾ������
				sql = "DELETE FROM comment WHERE cid=?";
			}else{//�������۵��û�ɾ������
				sql = "DELECT FROM comment WHERE cid=? AND uid=?";
			}
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, targetCid);
			if(curUid > 1 || curUid != cuid){
				ptmt.setInt(2, curUid);
			}
			affect = ptmt.executeUpdate();
			
			//ɾ����ص��޼�¼��message��commentnum-1
			if(affect > 0){
				sql = "DELETE FROM liked_comm WHERE cid=?";
				ptmt = conn.prepareStatement(sql);
				ptmt.setInt(1, targetCid);
				affect = ptmt.executeUpdate();
				
				sql = "UPDATE message SET commentnum=commentnum-1 WHERE cmid=? AND commentnum>0";
				ptmt = conn.prepareStatement(sql);
				ptmt.setInt(1, cmid);
				affect = ptmt.executeUpdate();
			}
			
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
	 * ��message��ɾ��ʱ��֮�����������Ҳ��ɾ��
	 */
	public int deleteCommentByCmid(int cmid){
		Connection conn = C3p0Utils.getConnection();
		//��¼��Ҫ��ɾ����comment��cid�Ա�֮��ɾ��������ĵ��޼�¼
		List<Integer> deleteCids = new ArrayList<Integer>();
		String sql = "SELECT cid FROM comment WHERE cmid=?";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		int affect = 0;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, cmid);
			rs = ptmt.executeQuery();
			while(rs.next()){
				deleteCids.add(rs.getInt("cid"));
			}
			
			sql = "DELETE FROM comment WHERE cmid=?";
			ptmt.setInt(1, cmid);
			affect = ptmt.executeUpdate();
			//ɾ�����������
			if(affect > 0){
				sql="DELETE FROM liked_comm WHERE cid IN";
				if(deleteCids == null || deleteCids.size() == 0){
					sql += -1;
				}else{
					for(Integer cid : deleteCids){
						sql = sql + cid +",";
					}
					sql = sql.substring(0, sql.length()-1);
				}
				ptmt = conn.prepareStatement(sql);
				ptmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ptmt!=null) ptmt.close();
				if(rs != null) rs.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return affect;
	}
	
	public int likeComment(int targetCid, int curUid){
		Connection conn = C3p0Utils.getConnection();
		String sql = "SELECT * FROM like WHERE cid=? AND uid=?";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		int affect = 0;
		try {
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, targetCid);
			ptmt.setInt(2, curUid);
			rs = ptmt.executeQuery();
			if(rs.first()){//����Ѿ��޹���message��liketimes-1
				sql = "UPDATE comment SET liketimes=liketime-1 WHERE cid=? AND liketimes>0";
				ptmt.setInt(1, targetCid);
				affect = ptmt.executeUpdate();
				if(affect >= 1){
					//ɾ��liked_comm���е���ؼ�¼
					sql = "DELETE FROM liked_comm WHERE cid=? AND uid=?";
					ptmt = conn.prepareStatement(sql);
					ptmt.setInt(1, targetCid);
					ptmt.setInt(2, curUid);
					affect = ptmt.executeUpdate();
				}
			}else{//û���޹�
				sql = "UPDATE comment SET liketimes=liketimes+1 WHERE cid=?";
				ptmt = conn.prepareStatement(sql);
				ptmt.setInt(1, targetCid);
				affect = ptmt.executeUpdate();
				if(affect >= 1){
					//liked_comm����������ؼ�¼
					sql = "INSERT INTO liked_comm(cid,uid,sstate) VALUES (?,?,1)";
					ptmt = conn.prepareStatement(sql);
					ptmt.setInt(1, targetCid);
					ptmt.setInt(2, curUid);
					affect = ptmt.executeUpdate();
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
		return affect;
	}
	
	public static void main(String[] args) {
		Comment c = new Comment();
		c.setAnonymity(0);
		c.setCmid(1);
		c.setComment("commentcommentcomment������������");
		c.setCuid(1);
		c.setFromwho("fromwho");
		c.setUid(1);
		
		CommentDao commentDao = new CommentDao();
		commentDao.insertComment(c);
	}

}
