package bit.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionContext;

import bit.dao.CommentDao;
import bit.dao.MessageDao;
import bit.dao.UserDao;
import bit.model.BGPicture;
import bit.model.Message;
import bit.model.MessagePage;
import bit.model.User;

public class HomeAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Message> hotMessages;
	private MessagePage messagePage;
	private List<Message> topicMessages;

	private int targetMid;
	private int targetHtid;//当前最新的话题id
	private String curNickname;//当前用户的昵称，发送消息时使用

	private String openid;//供管理员使用

	private List<BGPicture> bgPic;

	private int usenotify = 0;
	
	private int targetCid;
	
	/*
	 * 本周热门页面
	 */
	public String getHotMessPage(){
//		ActionContext ctx = ActionContext.getContext();
//		User curUser = (User) ctx.getSession().get("curUser");
		
		long minute = 60 * 1000;//1minute
		long hour = 60 * minute;//1hour
		long day = 24 * hour;//1day
//		Long lastTime = (Long)ctx.getApplication().get("lastTime");
		Long now = new Date().getTime();
		
		MessageDao messageDao = new MessageDao();
//		List<Integer> hotMessMids = (List<Integer>) ctx.getApplication().get("hotMessMids");
		
		//若没有统计过热门message或统计过期（超过一天）则重新统计
//		if(hotMessMids == null || hotMessMids.size() == 0 ||(lastTime == null) || (now-lastTime >= day)){
		List<Integer>	hotMessMids = messageDao.getHotMessMids();
		System.out.println("actionHotMids:::" + hotMessMids);
//			ctx.getApplication().put("hotMessMids", hotMessMids);
//			ctx.getApplication().put("lastTime", now);
//		}
//		hotMessages = messageDao.getHotMessage(hotMessMids, curUser.getUid());
		hotMessages = messageDao.getHotMessList(hotMessMids, 1);
		System.out.println("action:::" + hotMessages.toString());
		try {
			writerList2json(hotMessages, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return NONE;
	}
	
	/*
	 * 本周最新页面
	 */
	public String getNewestMessPage(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		
		long minute = 60 * 1000;//1minute
		long hour = 60 * minute;//1hour
		long day = 24 * hour;//1day
		Long lastTime = (Long)ctx.getApplication().get("lastTime");
		Long now = new Date().getTime();
		
		MessageDao messageDao = new MessageDao();
		List<Integer> hotMessMids = (List<Integer>) ctx.getApplication().get("hotMessMids");
		
		//若没有统计过热门message或统计过期（超过一天）则重新统计
		if(hotMessMids == null || hotMessMids.size() == 0 ||(lastTime == null) || (now-lastTime >= day)){
			hotMessMids = messageDao.getHotMessMids();
			ctx.getApplication().put("hotMessMids", hotMessMids);
			ctx.getApplication().put("lastTime", now);
		}
		messagePage = messageDao.getNewestMessagePage(curUser.getUid(), hotMessMids);
		try {
			writerList2json(messagePage, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 热门话题界面
	 */
	public String getTopicMessPage(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		
		long minute = 60 * 1000;//1minute
		long hour = 60 * minute;//1hour
		long day = 24 * hour;//1day
		Long lastTime = (Long) ctx.getApplication().get("lastTime");
		Long now = new Date().getTime();
		
		MessageDao messageDao = new MessageDao();
		List<Integer> hotMessMids = (List<Integer>) ctx.getApplication().get("hotMessMids");
		//若没有统计过热门message或统计过期（超过一天）则重新统计
		if(hotMessMids == null || hotMessMids.size() == 0 || lastTime == null || now-lastTime >= day){
			hotMessMids = messageDao.getHotMessMids();
			ctx.getApplication().put("hotMessMids", hotMessMids);
			ctx.getApplication().put("lastTime", now);
		}
		topicMessages = messageDao.getTopicMessListByHtid(targetHtid, curUser.getUid());
		try {
			writerList2json(topicMessages, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 主页面
	 */
	public String getHomePage(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		
		long minute = 60 * 1000;//1minute
		long hour = 60 * minute;//1hour
		long day = 24 * hour;//1day
		Long lastTime = (Long) ctx.getApplication().get("lastTime");
		Long now = new Date().getTime();
		
		MessageDao messageDao = new MessageDao();
		List<Integer> hotMessMids = (List<Integer>) ctx.getApplication().get("hotMessMids");
		//若没有统计过热门message或统计过期（超过一天）则重新统计
		if(hotMessMids == null || hotMessMids.size() == 0 || lastTime == null || now-lastTime >= day){
			hotMessMids = messageDao.getHotMessMids();
			ctx.getApplication().put("hotMessMids", hotMessMids);
    		ctx.getApplication().put("lastTime", now);
    	}
		hotMessages = messageDao.getHotMessList(hotMessMids, curUser.getUid());
		messagePage = messageDao.getNewestMessagePage(curUser.getUid(), hotMessMids);
		topicMessages = messageDao.getTopicMessListByHtid(targetHtid, curUser.getUid());

		return "";
	}
	
	/*
	 * 加载更多message
	 */
	public String getMoreMessage(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curuser");
		
		List<Integer> hotMessMids = (List<Integer>) ctx.getApplication().get("hotMessMids");
		
		MessageDao messageDao = new MessageDao();
		
		messagePage = messageDao.getMessagePage(messagePage.getZongshu(),
				messagePage.getYema(),
				messagePage.getYechang(),
				curUser.getUid(),
				hotMessMids);
		//设置背景字段
		messagePage.setBgPic(curUser.getBground());
		
		try {
			writerObject2json(messagePage,null);
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
		
		return NONE;
	}
	
	
	/*
	 * 点赞message
	 */
	public String likeMess(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		
		MessageDao messageDao = new MessageDao();
		Integer rows = messageDao.likeMessage(targetMid, curUser.getUid());
//		Integer rows = messageDao.likeMessage(1, 1);
		try {
			writerList2json(rows, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 点赞comment
	 */
	public String likeComm(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		
		CommentDao commentDao = new CommentDao();
		Integer rows = commentDao.likeComment(targetCid, curUser.getUid());
		try {
			writerList2json(rows, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 进入许愿界面
	 */
	public String toPray(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		curNickname = curUser.getNickname();
		usenotify = curUser.getUsenotify();
		try {
			writerList2json(curNickname, null);
			writerList2json(usenotify, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return NONE;
	}
	
	/*
	 * 展示所有可选的背景图
	 */
	public String showBgs(){
		UserDao userDao = new UserDao();
		bgPic = userDao.showAllbgs();
		
		//去掉系统提示信息
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		if(curUser.getSysnotify() != null && curUser.getSysnotify()>0){
			userDao.dealNotify(-1, 0, curUser.getUid());
			curUser.setSysnotify(curUser.getSysnotify()-1);
		}
		try {
			writerList2json(bgPic, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}

	public List<Message> getHotMessages() {
		return hotMessages;
	}

	public void setHotMessages(List<Message> hotMessages) {
		this.hotMessages = hotMessages;
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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public List<BGPicture> getBgPic() {
		return bgPic;
	}

	public void setBgPic(List<BGPicture> bgPic) {
		this.bgPic = bgPic;
	}

	public int getUsenotify() {
		return usenotify;
	}

	public void setUsenotify(int usenotify) {
		this.usenotify = usenotify;
	}

	public int getTargetCid() {
		return targetCid;
	}

	public void setTargetCid(int targetCid) {
		this.targetCid = targetCid;
	}

	public MessagePage getMessagePage() {
		return messagePage;
	}

	public void setMessagePage(MessagePage messagePage) {
		this.messagePage = messagePage;
	}

	public List<Message> getTopicMessages() {
		return topicMessages;
	}

	public void setTopicMessages(List<Message> topicMessages) {
		this.topicMessages = topicMessages;
	}
	

}
