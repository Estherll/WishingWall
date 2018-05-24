package bit.action;

import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;

import com.opensymphony.xwork2.ActionContext;

import bit.dao.CommentDao;
import bit.model.Comment;
import bit.model.User;
import bit.utils.PropertiesUtils;
import bit.utils.TextTransfer;

public class CommentAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int targetMid;//被评论愿望的ID
	private String curNickname;//当前用户的昵称
	private Comment comment;
	
	public String PublishComm(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		
		/******识别机器人,一旦发现是机器人超过5次,二十分钟后才允许再次发消息; 且每次机器行为都不会被受理********/
		Long now = new Date().getTime();
		/**
		 *  判断此次行为是否是机器人
		 * |---Yes, 更新机器人行为次数,不受理请求
		 * |---NO , 判断 以前是否有过机器人行为 |--- 有, 如果过了恶意处罚期(认为机器人行为达到5次才会有延迟处罚),重置机器人行为,并 正常受理(更新发消息的时间); 否则, 不受理
		 * |--- 无 , 正常受理(更新发消息的时间)
		 */
		/**根据记录,判断此次行为是否是机器人**/
		Queue<Long> publishMess = (Queue<Long>) ctx.getSession().get("publishMess");
		if(publishMess == null){
			publishMess = new ArrayDeque<Long>();
			ctx.getSession().put("pubishMess", publishMess);
		}
		publishMess.add(now);
		while(publishMess.size() > 5){
			publishMess.poll();
		}
		boolean isRobot = TextTransfer.isRobot(publishMess, 1000L);
		/**根据记录,判断是否是机器人**/
		Integer robot = (Integer) ctx.getSession().get("robot");
		if(isRobot){
			if(robot == null){
				robot = 0;
				ctx.getSession().put("robot", robot);
			}
			robot = robot + 1;
			ctx.getSession().put("robot", robot);
			return SUCCESS;
		}else{
			if(robot == null || robot == 0){//以前无机器人行为
				robot = 0;
				ctx.getSession().put("robot", robot);
			}else{//以前有机器人行为
				Long lastPublish = (Long) ctx.getSession().get("lastPublish");
				long s = 1000L;
				long min = s*60;
				if(robot > 5){
					if(now - lastPublish > min*20){
						//过了处罚期，重置机器人行为，并受理
						robot = 0;
						ctx.getSession().put("robot", robot);
					}else{
						return SUCCESS;
					}
				}
			}
		}
		/******识别机器人********/
		
		//正常受理请求
		Long lastPublish = (Long) ctx.getSession().get("lastPublish");
		if(lastPublish == null){
			lastPublish = now;
			ctx.getSession().put("lastPublish", lastPublish);
		}
		
		//不匿名的条件：有1，且昵称为真
		if(comment.getAnonymity() == 0){
			if(!comment.getFromwho().trim().equals(curUser.getNickname().trim())){
				comment.setAnonymity(1);
			}
		}
		comment.setUid(curUser.getUid());
		comment.setCommentTime(new Timestamp(now));
		// 如果不支持表情,要过滤from,content
		if(PropertiesUtils.getKeyValue("emoji").equals("0")){
			comment.setFromwho(TextTransfer.filterEmoji(comment.getFromwho()));
			comment.setComment(TextTransfer.filterEmoji(comment.getComment()));
		}
		CommentDao commentDao = new CommentDao();
		commentDao.insertComment(comment);
		
		return SUCCESS;
	}
	
	
	public int getTargetMid() {
		return targetMid;
	}

	public void setTargetMid(int targetMid) {
		this.targetMid = targetMid;
	}

	public String getCurNickname() {
		return curNickname;
	}

	public void setCurNickname(String curNickname) {
		this.curNickname = curNickname;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
	
	

}
