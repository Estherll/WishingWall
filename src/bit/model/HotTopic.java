package bit.model;

import java.sql.Timestamp;

public class HotTopic {

	//���Ż�������ݿ�id
	private Integer htId;
	//���Ż��������
	private String topic;
	//�������Ż����ϵͳʱ�䣬ϵͳ�Զ�����
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
