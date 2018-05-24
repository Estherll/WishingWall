package bit.model;

public class Liked_comm {

	private Integer lid;
	// 被点赞的评论
	private Integer cid;
	// 点赞的用户
	private Integer uid;
	// 判断当前用户是否为点赞过这条评论的用户
	private Integer sstate;

	public Integer getLid() {
		return lid;
	}

	public void setLid(Integer lid) {
		this.lid = lid;
	}

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

	public Integer getSstate() {
		return sstate;
	}

	public void setSstate(Integer sstate) {
		this.sstate = sstate;
	}

}
