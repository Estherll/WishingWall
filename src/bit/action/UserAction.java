package bit.action;

import java.io.IOException;
import java.util.List;

import com.opensymphony.xwork2.ActionContext;

import bit.dao.CommentDao;
import bit.dao.MessageDao;
import bit.dao.UserDao;
import bit.model.Comment;
import bit.model.Message;
import bit.model.User;

public class UserAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int targetUid;
	private User targetUser;
	private List<Message> targetMess;
	private List<Comment> targetComm;
	private int messageSize = 0;
	private int commentSize = 0;
	private boolean self = false;//判断是否当前用户就是目标用户
	private String bground = "bg_default.jpg";
	
	/*
	 * 根据目标用户的uid查询对应用户的信息
	 */
	public String getTargetUserInfo(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		
		
		UserDao userDao = new UserDao();
		targetUser = userDao.getUser(targetUid);
//		targetUser = userDao.getUser(1);
		
		self = (targetUid == curUser.getUid());
		
		try {
			writerList2json(targetUser, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return NONE;
	}
	
	/*
	 * 进入用户的个人界面
	 */
	public String getPersonalHome(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		
		UserDao userDao = new UserDao();
		targetUser = userDao.getUser(targetUid);
		
		MessageDao messageDao = new MessageDao();
		targetMess = messageDao.getMessListByUid(curUser.getUid(), targetUid);
		if(targetMess != null){
			messageSize = targetMess.size();
		}
		
		CommentDao commentDao = new CommentDao();
		targetComm = commentDao.getCommListByUid(curUser.getUid(), targetUid);
		if(targetComm != null){
			commentSize = targetComm.size();
		}
		
		self = (targetUid == curUser.getUid());
		
		return "";
	}
	
	/*
	 * 获取对应用户的message
	 */
	public String getTargetUserMess(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		MessageDao messageDao = new MessageDao();
		targetMess = messageDao.getMessListByUid(curUser.getUid(), targetUid);
//		targetMess = messageDao.getMessListByUid(1, 1);
		if(targetMess != null){
			messageSize = targetMess.size();
		}
		try {
			writerList2json(targetMess, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 把messageSize写回前端
	 */
	public String getMessageSizeInfo(){
		try {
			writerObject2json(messageSize, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 把commentSize写回前端
	 */
	public String getCommentSizeInfo(){
		try {
			writerObject2json(commentSize, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 获取对应用户的comment
	 */
	public String getTargerUserComm(){
		CommentDao commentDao = new CommentDao();
//		targetComm = commentDao.getCommByUid(curUser.getUid(), targetUid);
		targetComm = commentDao.getCommListByUid(1, 1);
		if(targetComm != null){
			commentSize = targetComm.size();
		}
		try {
			writerList2json(targetComm, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 设置用户背景
	 */
	public String setBground(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		
		UserDao userDao = new UserDao();
		int affect = userDao.setBg(bground, curUser.getUid());
		//修改成功
		if(affect > 0){
			curUser.setBground(bground);
		}
		return NONE;
	}

	public int getTargetUid() {
		return targetUid;
	}

	public void setTargetUid(int targetUid) {
		this.targetUid = targetUid;
	}

	public User getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}

	public List<Message> getTargetMess() {
		return targetMess;
	}

	public void setTargetMess(List<Message> targetMess) {
		this.targetMess = targetMess;
	}

	public int getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}

	public int getCommentSize() {
		return commentSize;
	}

	public void setCommentSize(int commentSize) {
		this.commentSize = commentSize;
	}

	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean self) {
		this.self = self;
	}

	public String getBground() {
		return bground;
	}

	public void setBground(String bground) {
		this.bground = bground;
	}

	public List<Comment> getTargetComm() {
		return targetComm;
	}

	public void setTargetComm(List<Comment> targetComm) {
		this.targetComm = targetComm;
	}
	
	

}
