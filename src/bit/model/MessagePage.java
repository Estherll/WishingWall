package bit.model;

import java.util.List;

public class MessagePage {

	private int yema; // ��ǰ�Ѿ�չʾ��Ϣ������ֵ��С�ļ�¼������
	private List<Message> messageList;
	private int yechang;// ��Ϣҳ�泤��
	private int zongshu;// �ܼ�¼��
	private int havemore;// �Ƿ���ʣ����Ϣ

	// ���Ǳ������õ��ֶ�
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
