package bit.model;

import java.sql.Timestamp;
import java.util.List;

public class Message {

	//message�����ݿ�id,����messageʱ���ݿ��Զ�����
	private Integer mid;
	//д����Ը�����û����ݿ�id
	private Integer uid;
	//to
	private String towho;
	//��������
	private String content;
	//from��ѡ���������û�����ǳ�
	private String fromwho;
	//�û�΢����
	private String nickname;
	//�ж��Ƿ�����״̬�������������������������message
	private Integer anonymity;
	//Ը��������
	private String messType;
	//�û���΢��ͷ���ַ
	private String headimgurl;
	//����Ը���յ��ĵ��������״�Ĭ��Ϊ0
	private Integer liketimes;
	//��������message��ϵͳʱ�䣬���ݿ��Զ�����
	private Timestamp createTime;
	//����message�յ�����������
	private Integer commentNum;
	//����message���������ۣ���ҳ��ֻ��ʾһ����������
	private Comment hotComment;
	//����message���������ۣ�����Ը������ҳʱ�������
	private List<Comment> comments;
	//�жϴ�����Ϣ�Ƿ񱻵�ǰ�û����޹�
	private Integer sstate;
	//���ڽ�timestamp�������������message���У�ֻ��ʾ����򻯵�ʱ��
	private String simpleTime;
	//�û���ѯչʾ��Ϣ��ʹ�õ����жϱ���Ϣ�ǲ��ǵ�ǰ�û���������������message����
	private Integer state;
	//�Ƿ�μ����Ż��⣬�����㼴Ϊ�������Ż��⣬�Ҵ�1��ʼ��Ӧÿ�����Ż����id����joinTopic==6���ʾ����message������idΪ6�����Ż��⣩
	private Integer joinTopic;
	//�Ƿ��������ۣ������㼴Ϊ����������
	private Integer notComm;
	

	public Integer getMid() {
		return mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getTowho() {
		return towho;
	}

	public void setTowho(String towho) {
		this.towho = towho;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFromwho() {
		return fromwho;
	}

	public void setFromwho(String fromwho) {
		this.fromwho = fromwho;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getAnonymity() {
		return anonymity;
	}

	public void setAnonymity(Integer anonymity) {
		this.anonymity = anonymity;
	}

	public String getMessType() {
		return messType;
	}

	public void setMessType(String messType) {
		this.messType = messType;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public Integer getLiketimes() {
		return liketimes;
	}

	public void setLiketimes(Integer liketimes) {
		this.liketimes = liketimes;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getSstate() {
		return sstate;
	}

	public void setSstate(Integer sstate) {
		this.sstate = sstate;
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

	public Comment getHotComment() {
		return hotComment;
	}

	public void setHotComment(Comment hotComment) {
		this.hotComment = hotComment;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Integer getJoinTopic() {
		return joinTopic;
	}

	public void setJoinTopic(Integer joinTopic) {
		this.joinTopic = joinTopic;
	}

	public Integer getNotComm() {
		return notComm;
	}

	public void setNotComm(Integer notComm) {
		this.notComm = notComm;
	}
	

}
