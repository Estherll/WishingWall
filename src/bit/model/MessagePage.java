package bit.model;

import java.util.List;

public class MessagePage {

	private int yema; // 当前已经展示消息中索引值最小的记录的索引
	private List<Message> messageList;
	private int yechang;// 消息页面长度
	private int zongshu;// 总记录数
	private int havemore;// 是否还有剩余消息

	// 考虑背景设置的字段
	private String bgPic;

	public int getYema() {
		return yema;
	}

	public void setYema(int yema) {
		this.yema = yema;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	public int getYechang() {
		return yechang;
	}

	public void setYechang(int yechang) {
		this.yechang = yechang;
	}

	public int getZongshu() {
		return zongshu;
	}

	public void setZongshu(int zongshu) {
		this.zongshu = zongshu;
	}

	public int getHavemore() {
		return havemore;
	}

	public void setHavemore(int havemore) {
		this.havemore = havemore;
	}

	public String getBgPic() {
		return bgPic;
	}

	public void setBgPic(String bgPic) {
		this.bgPic = bgPic;
	}

}
