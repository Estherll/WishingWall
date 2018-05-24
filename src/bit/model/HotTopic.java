package bit.model;

import java.sql.Timestamp;

public class HotTopic {

	//热门话题的数据库id
	private Integer htId;
	//热门话题的内容
	private String topic;
	//发布热门话题的系统时间，系统自动更新
	private Timestamp createTime;

	public Integer getHtId() {
		return htId;
	}

	public void setHtId(Integer htId) {
		this.htId = htId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
