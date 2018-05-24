package bit.model;

import java.sql.Timestamp;

public class Comment {
	// 评论的数据库ID
	private Integer cid;
	// 评论的用户的数据库ID
	private Integer uid;
	// 被评论的愿望的数据库ID
	private Integer cmid;
	// 被评论的用户的数据库ID
	private Integer cuid;
	// 评论内容
	private String comment;
	// 来自
	private String fromwho;
	// 是否匿名
	private Integer anonymity;
	// 这条评论收到的点赞次数
	private Integer liketimes;
	// 评论用户的微信头像地址
	private String headimgurl;
	// 发布这条评论的系统时间，数据库自动更新
	private Timestamp commenttime;
	//简化时间，不在数据库中，显示的是这个简化后的时间
	private String simpleTime;
    //判断当前用户是否为发表这条评论的用户，不在数据库中
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
