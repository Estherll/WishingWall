package bit.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionContext;

import bit.dao.HotTopicDao;
import bit.dao.MessageDao;
import bit.dao.UserDao;
import bit.model.HotTopic;
import bit.model.Message;
import bit.model.MessagePage;
import bit.model.User;

public class AdministorAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String openid;
	private MessagePage messagePage;
	private List<Message> hotMessages;
	private HotTopic hotTopic;
	private int targetHtid;
	private List<Message> topicMessages;

	public String getAdminHotMess() {
		UserDao userDao = new UserDao();
		User administor = userDao.checkUser(openid);
		if (administor == null) {
			return "error";
		}
		ActionContext ctx = ActionContext.getContext();
		ctx.getSession().put("curUser", administor);

		long minute = 60 * 1000;// 1minute
		long hour = 60 * minute;// 1hour
		long day = 24 * hour;// 1day
		Long lastTime = (Long) ctx.getApplication().get("lastTime");
		Long now = new Date().getTime();

		MessageDao messageDao = new MessageDao();
		List<Integer> hotMessMids = (List<Integer>) ctx.getApplication().get("hotMessMids");
		// 若没有统计过hot message 或者统计过期（一天）则重新统计
		if (hotMessMids == null || hotMessMids.size() == 0 || lastTime == null || (now - lastTime >= day)) {
			hotMessMids = messageDao.getHotMessMids();
			ctx.getApplication().put("hotMessMids", hotMessMids);
			ctx.getApplication().put("lastTime", now);
		}
		hotMessages = messageDao.getHotMessList(hotMessMids, administor.getUid());
		try {
			writerList2json(hotMessages, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}

	public String getAdminNewestMess() {
		UserDao userDao = new UserDao();
		User administor = userDao.checkUser(openid);
		if (administor == null) {
			return "error";
		}
		ActionContext ctx = ActionContext.getContext();
		ctx.getSession().put("curUser", administor);
		
		long minute = 60 * 1000;// 1minute
		long hour = 60 * minute;// 1hour
		long day = 24 * hour;// 1day
		Long lastTime = (Long) ctx.getApplication().get("lastTime");
		Long now = new Date().getTime();

		MessageDao messageDao = new MessageDao();
		List<Integer> hotMessMids = (List<Integer>) ctx.getApplication().get("hotMessMids");
		// 若没有统计过hot message 或者统计过期（一天）则重新统计
		if (hotMessMids == null || hotMessMids.size() == 0 || lastTime == null || (now - lastTime >= day)) {
			hotMessMids = messageDao.getHotMessMids();
			ctx.getApplication().put("hotMessMids", hotMessMids);
			ctx.getApplication().put("lastTime", now);
		}
		messagePage = messageDao.getNewestMessagePage(administor.getUid(), hotMessMids);
		try {
			writerList2json(messagePage, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 管理员界面
	 */
	public String getAdministorHome(){
		UserDao userDao = new UserDao();
		User administor = userDao.checkUser(openid);
		if(administor == null){
			return "error";
		}
		ActionContext ctx = ActionContext.getContext();
		ctx.getSession().put("curUser", administor);
		
		long minute = 60 * 1000;//1minute
		long hour = 60 * minute;//1hour
		long day = 24 * hour;//1day
		Long lastTime = (Long) ctx.getApplication().get("lastTime");
		Long now = new Date().getTime();
		
		MessageDao messageDao = new MessageDao();
		List<Integer> hotMessMids = (List<Integer>) ctx.getApplication().get("hotMessMids");
		//若没有统计过hot message 或者统计过期（超过一天）则重新统计
		if(hotMessMids == null || hotMessMids.size() == 0 || lastTime == null || (now-lastTime >= day)){
			hotMessMids = messageDao.getHotMessMids();
			ctx.getApplication().put("hotMessMids", hotMessMids);
			ctx.getApplication().put("lastTime", now);
		}
		
		hotMessages = messageDao.getHotMessList(hotMessMids, administor.getUid());
		messagePage = messageDao.getNewestMessagePage(administor.getUid(), hotMessMids);
		topicMessages = messageDao.getTopicMessListByHtid(targetHtid, administor.getUid());
		
		return "";
	}
	
	/*
	 * 获取最新一条热门话题的信息
	 */
	public String getHotTopicInfo(){
		HotTopicDao hotTopicDao = new HotTopicDao();
		hotTopic = hotTopicDao.getNewestTopic();
		try {
			writerObject2json(hotTopic, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 获取话题消息
	 */
	public String getAdminTopicMess(){
		UserDao userDao = new UserDao();
		User administor = userDao.checkUser(openid);
		if (administor == null) {
			return "error";
		}
		ActionContext ctx = ActionContext.getContext();
		ctx.getSession().put("curUser", administor);
		MessageDao messageDao = new MessageDao();
		topicMessages = messageDao.getTopicMessListByHtid(hotTopic.getHtId(), administor.getUid());
		try {
			writerList2json(topicMessages, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 管理员删除热门话题
	 */
	public String deleteHotTopic(){
		HotTopicDao hotTopicDao = new HotTopicDao();
		Integer rows = hotTopicDao.deleteHotTopic(targetHtid);
		try {
			writerList2json(rows, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 管理员发布新的热门话题
	 */
	public String pulishNewTopic(){
		HotTopicDao hotTopicDao = new HotTopicDao();
		hotTopicDao.insertHotTopic(hotTopic);
		return NONE;
	}


	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public MessagePage getMessagePage() {
		return messagePage;
	}

	public void setMessagePage(MessagePage messagePage) {
		this.messagePage = messagePage;
	}

	public List<Message> getHotMessages() {
		return hotMessages;
	}

	public void setHotMessages(List<Message> hotMessages) {
		this.hotMessages = hotMessages;
	}

	public HotTopic getHotTopic() {
		return hotTopic;
	}

	public void setHotTopic(HotTopic hotTopic) {
		this.hotTopic = hotTopic;
	}

	public int getTargetHtid() {
		return targetHtid;
	}

	public void setTargetHtid(int targetHtid) {
		this.targetHtid = targetHtid;
	}

	public List<Message> getTopicMessages() {
		return topicMessages;
	}

	public void setTopicMessages(List<Message> topicMessages) {
		this.topicMessages = topicMessages;
	}
	

}
