package bit.model;

import java.sql.Timestamp;

public class Comment {
	// ���۵����ݿ�ID
	private Integer cid;
	// ���۵��û������ݿ�ID
	private Integer uid;
	// �����۵�Ը�������ݿ�ID
	private Integer cmid;
	// �����۵��û������ݿ�ID
	private Integer cuid;
	// ��������
	private String comment;
	// ����
	private String fromwho;
	// �Ƿ�����
	private Integer anonymity;
	// ���������յ��ĵ��޴���
	private Integer liketimes;
	// �����û���΢��ͷ���ַ
	private String headimgurl;
	// �����������۵�ϵͳʱ�䣬���ݿ��Զ�����
	private Timestamp commenttime;
	//��ʱ�䣬�������ݿ��У���ʾ��������򻯺��ʱ��
	private String simpleTime;
    //�жϵ�ǰ�û��Ƿ�Ϊ�����������۵��û����������ݿ���
	private Integer state;

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getCmid() {
		return cmid;
	}

	public void setCmid(Integer cmid) {
		this.cmid = cmid;
	}

	public Integer getCuid() {
		return cuid;
	}

	public void setCuid(Integer cuid) {
		this.cuid = cuid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFromwho() {
		return fromwho;
	}

	public void setFromwho(String fromwho) {
		this.fromwho = fromwho;
	}

	public Integer getAnonymity() {
		return anonymity;
	}

	public void setAnonymity(Integer anonymity) {
		this.anonymity = anonymity;
	}

	public Integer getLikeTimes() {
		return liketimes;
	}

	public void setLikeTimes(Integer liketimes) {
		this.liketimes = liketimes;
	}

	public String getHandimgurl() {
		return headimgurl;
	}

	public void setHandimgurl(String handimgurl) {
		this.headimgurl = handimgurl;
	}

	public Timestamp getCommentTime() {
		return commenttime;
	}

	public void setCommentTime(Timestamp commentTime) {
		this.commenttime = commentTime;
	}

	public String getSimpleTime() {
		return simpleTime;
	}

	public void setSimpleTime(String simpleTime) {
		this.simpleTime = simpleTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	

}
