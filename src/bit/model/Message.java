package bit.model;

import java.sql.Timestamp;
import java.util.List;

public class Message {

	//message的数据库id,创建message时数据库自动递增
	private Integer mid;
	//写这条愿望的用户数据库id
	private Integer uid;
	//to
	private String towho;
	//留言内容
	private String content;
	//from，选择匿名后用户填的昵称
	private String fromwho;
	//用户微信名
	private String nickname;
	//判断是否匿名状态，大于零就是匿名发布的这条message
	private Integer anonymity;
	//愿望的类型
	private String messType;
	//用户的微信头像地址
	private String headimgurl;
	//这条愿望收到的点赞数，首次默认为0
	private Integer liketimes;
	//发布这条message的系统时间，数据库自动更新
	private Timestamp createTime;
	//这条message收到的评论条数
	private Integer commentNum;
	//这条message的热门评论，主页面只显示一条热门评论
	private Comment hotComment;
	//这条message的所有评论，进入愿望详情页时遍历输出
	private List<Comment> comments;
	//判断此条消息是否被当前用户点赞过
	private Integer sstate;
	//用于将timestamp简化输出，并不在message表中，只显示这个简化的时间
	private String simpleTime;
	//用户查询展示消息会使用到（判断本消息是不是当前用户所发），并不在message表中
	private Integer state;
	//是否参加热门话题，大于零即为参与热门话题，且从1开始对应每条热门话题的id（如joinTopic==6则表示这条message参与了id为6的热门话题）
	private Integer joinTopic;
	//是否允许评论，大于零即为不允许评论
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
