package bit.model;

import java.sql.Timestamp;

public class HotTopicMessage {

	private Integer htmId;
	private Integer userId;
	private String to;
	private String content;
	private String from;
	private String nickname;
	private String headimaurl;
	private Integer anonymity;
	private Integer likeTimes;
	private Integer commNum;
	private Timestamp createTime;
	//´ý¶¨
	private String hotTopic;

	public Integer getHtId() {
		return htmId;
	}

	public void setHtId(Integer htmId) {
		this.htmId = htmId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimaurl() {
		return headimaurl;
	}

	public void setHeadimaurl(String headimaurl) {
		this.headimaurl = headimaurl;
	}

	public Integer getAnonymity() {
		return anonymity;
	}

	public void setAnonymity(Integer anonymity) {
		this.anonymity = anonymity;
	}

	public Integer getLikeTimes() {
		return likeTimes;
	}

	public void setLikeTimes(Integer likeTimes) {
		this.likeTimes = likeTimes;
	}

	public Integer getCommNum() {
		return commNum;
	}

	public void setCommNum(Integer commNum) {
		this.commNum = commNum;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
