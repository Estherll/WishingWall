package bit.model;

public class Liked {
	// 被点赞的愿望
	private Integer lid;
	// 被点赞的愿望
	private Integer mid;
	// 点赞的用户
	private Integer uid;
	// 判断当前用户是否为点赞过这条愿望的用户
	private Integer sstate;

	public Integer getLid() {
		return lid;
	}

	public void setLid(Integer Lid) {
		this.lid = Lid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getSstate() {
		return sstate;
	}

	public void setState(Integer sstate) {
		this.sstate = sstate;
	}

}
