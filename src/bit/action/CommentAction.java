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
	private int targetMid;//������Ը����ID
	private String curNickname;//��ǰ�û����ǳ�
	private Comment comment;
	
	public String PublishComm(){
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
		
		//������������
		Long lastPublish = (Long) ctx.getSession().get("lastPublish");
		if(lastPublish == null){
			lastPublish = now;
			ctx.getSession().put("lastPublish", lastPublish);
		}
		
		//����������������1�����ǳ�Ϊ��
		if(comment.getAnonymity() == 0){
			if(!comment.getFromwho().trim().equals(curUser.getNickname().trim())){
				comment.setAnonymity(1);
			}
		}
		comment.setUid(curUser.getUid());
		comment.setCommentTime(new Timestamp(now));
		// �����֧�ֱ���,Ҫ����from,content
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
