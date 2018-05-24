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
	 * ɾ��message
	 */
	public String deleteMessage(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		List<Integer> hotMessMids = (List<Integer>) ctx.getApplication().get("hotMessMids");
		
		MessageDao messageDao = new MessageDao();
		Integer rows = messageDao.deleteMessage(targetMid, curUser.getUid());
		if(rows > 0 && hotMessMids != null){//ɾ���ɹ�������hotMessMid
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
	 * ����message
	 */
	public String publishMessage(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		
		/******ʶ�������,һ�������ǻ����˳���5��,��ʮ���Ӻ�������ٴη���Ϣ; ��ÿ�λ�����Ϊ�����ᱻ����********/
		Long now = new Date().getTime();
		/**
		 *  �жϴ˴���Ϊ�Ƿ��ǻ�����
		 * |---Yes, ���»�������Ϊ����,����������
		 * |---NO , �ж� ��ǰ�Ƿ��й���������Ϊ |--- ��, ������˶��⴦����(��Ϊ��������Ϊ�ﵽ5�βŻ����ӳٴ���),���û�������Ϊ,�� ��������(���·���Ϣ��ʱ��); ����, ������
		 * |--- �� , ��������(���·���Ϣ��ʱ��)
		 */
		/**���ݼ�¼,�жϴ˴���Ϊ�Ƿ��ǻ�����**/
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
		/**���ݼ�¼,�ж��Ƿ��ǻ�����**/
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
			if(robot == null || robot == 0){//��ǰ�޻�������Ϊ
				robot = 0;
				ctx.getSession().put("robot", robot);
			}else{//��ǰ�л�������Ϊ
				Long lastPublish = (Long) ctx.getSession().get("lastPublish");
				long s = 1000L;
				long min = s*60;
				if(robot > 5){
					if(now - lastPublish > min*20){
						//���˴����ڣ����û�������Ϊ��������
						robot = 0;
						ctx.getSession().put("robot", robot);
					}else{
						return SUCCESS;
					}
				}
			}
		}
		/******ʶ�������********/
		
		//��������
		Long lastPublish = (Long) ctx.getSession().get("lastPublish");
		if(lastPublish == null){
			lastPublish = now;
			ctx.getSession().put("lastPulish", lastPublish);
		}
		//��������������anonymity==1&&�ǳ�Ϊ��
		if(message.getAnonymity() == 0){
			if(!message.getFromwho().trim().equals(curUser.getNickname().trim())){
				message.setAnonymity(1);
			}
		}
		message.setUid(curUser.getUid());
		message.setCreateTime(new Timestamp(now));
		//�����֧�ֱ��飬Ҫ����from��to��content
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
	 * ����һ�������Ϣ��ҡһҡ��
	 */
	public String getRandomMessage(){
		ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curUser");
		Message message = null;
		
		/******��ֹ����ҡһҡ******/
		Message lastMess = (Message) ctx.getSession().get("lastMess");
		Long lastRandom = (Long) ctx.getSession().get("lastRandom");
		Long now = new Date().getTime();
		// ���֮ǰû��ҡһҡ, ҡ ; ����, �ж��Ƿ����Ƶ��
		if(lastMess != null && lastRandom !=null){
			if(now-lastRandom < 500L){//����Ƶ�����ṩ�ϴε�����
				message = lastMess;
			}else{//����Ƶ��
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
		/******��ֹ����ҡһҡ******/
		
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
