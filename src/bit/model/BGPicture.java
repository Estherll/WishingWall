package bit.model;

public class BGPicture {

	//背景图片的数据库id
	private Integer bgId;
	//背景图片的地址
	private String bgAddress;
	//背景图片的名字
	private String bgName;
	//上传背景图片的用户
	private Integer provider;

	public Integer getBgId() {
		return bgId;
	}

	public void setBgId(Integer bgId) {
		this.bgId = bgId;
	}

	public String getBgAddress() {
		return bgAddress;
	}

	public void setBgAddress(String bgAddress) {
		this.bgAddress = bgAddress;
	}

	public String getBgName() {
		return bgName;
	}

	public void setBgName(String bgName) {
		this.bgName = bgName;
	}

	public final Integer getProvider() {
		return provider;
	}

	public final void setProvider(Integer provider) {
		this.provider = provider;
	}


}
