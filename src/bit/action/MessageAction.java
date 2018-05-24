package bit.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import com.opensymphony.xwork2.ActionContext;

import bit.dao.MessageDao;
import bit.dao.UserDao;
import bit.model.Message;
import bit.model.User;
import bit.utils.PropertiesUtils;
import bit.utils.TextTransfer;

public class MessageAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int targetMid;
	private Message message;
	
	/*
	 * 删除message
	 */
	public String deleteMessage(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		List<Integer> hotMessMids = (List<Integer>) ctx.getApplication().get("hotMessMids");
		
		MessageDao messageDao = new MessageDao();
		Integer rows = messageDao.deleteMessage(targetMid, curUser.getUid());
		if(rows > 0 && hotMessMids != null){//删除成功，更新hotMessMid
			hotMessMids.remove(targetMid);
		}
		try {
			writerList2json(rows, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 发布message
	 */
	public String publishMessage(){
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
		
		//正常受理
		Long lastPublish = (Long) ctx.getSession().get("lastPublish");
		if(lastPublish == null){
			lastPublish = now;
			ctx.getSession().put("lastPulish", lastPublish);
		}
		//不匿名的条件：anonymity==1&&昵称为真
		if(message.getAnonymity() == 0){
			if(!message.getFromwho().trim().equals(curUser.getNickname().trim())){
				message.setAnonymity(1);
			}
		}
		message.setUid(curUser.getUid());
		message.setCreateTime(new Timestamp(now));
		//如果不支持表情，要过滤from，to，content
		/*
		if(PropertiesUtils.getKeyValue("emoji").equals("0")){
			message.setFromwho(TextTransfer.filterEmoji(message.getFromwho()));
			message.setTowho(TextTransfer.filterEmoji(message.getTowho()));
			message.setContent(TextTransfer.filterEmoji(message.getContent()));
		}*/
		MessageDao messageDao = new MessageDao();
		messageDao.insertMess(message);
		if(curUser.getUsenotify() != null && curUser.getUsenotify() > 0){
			UserDao userDao = new UserDao();
			userDao.dealNotify(0, -1, curUser.getUid());
			curUser.setUsenotify(curUser.getUsenotify()-1);
		}
		
		return SUCCESS;
	}
	
	/*
	 * 产生一条随机信息（摇一摇）
	 */
	public String getRandomMessage(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		Message message = null;
		
		/******防止过度摇一摇******/
		Message lastMess = (Message) ctx.getSession().get("lastMess");
		Long lastRandom = (Long) ctx.getSession().get("lastRandom");
		Long now = new Date().getTime();
		// 如果之前没有摇一摇, 摇 ; 否则, 判断是否过度频繁
		if(lastMess != null && lastRandom !=null){
			if(now-lastRandom < 500L){//过渡频繁，提供上次的数据
				message = lastMess;
			}else{//正常频率
				MessageDao messageDao = new MessageDao();
				message = messageDao.getRandomMessage(curUser.getUid());
				ctx.getSession().put("lastMess", message);
				ctx.getSession().put("lastRandom", now);
			}
		}else{
			MessageDao messageDao = new MessageDao();
			message = messageDao.getRandomMessage(curUser.getUid());
			ctx.getSession().put("lastMess", message);
			ctx.getSession().put("lastRandom", now);
		}
		/******防止过度摇一摇******/
		
		try {
			writerList2json(message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}

	public int getTargetMid() {
		return targetMid;
	}

	public void setTargetMid(int targetMid) {
		this.targetMid = targetMid;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	
	
	

}
