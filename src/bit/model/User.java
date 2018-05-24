package bit.model;

import java.sql.Timestamp;

public class User {

	private Integer uid;
	private String openid;
	private String nickname;
	private String headimgurl;
	private String bground;
	private Integer sysnotify;
	private Integer usenotify;
	private Timestamp lastupdate;
	private String signature;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getBground() {
		return bground;
	}

	public void setBground(String bground) {
		this.bground = bground;
	}

	public Integer getSysnotify() {
		return sysnotify;
	}

	public void setSysnotify(Integer sysnotify) {
		this.sysnotify = sysnotify;
	}

	public Integer getUsenotify() {
		return usenotify;
	}

	public void setUsenotify(Integer usenotify) {
		this.usenotify = usenotify;
	}

	public Timestamp getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(Timestamp lastupdate) {
		this.lastupdate = lastupdate;
	}

	public final String getSignature() {
		return signature;
	}

	public final void setSignature(String signature) {
		this.signature = signature;
	}
	
	

}
