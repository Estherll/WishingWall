package bit.model;

public class BGPicture {

	//����ͼƬ�����ݿ�id
	private Integer bgId;
	//����ͼƬ�ĵ�ַ
	private String bgAddress;
	//����ͼƬ������
	private String bgName;
	//�ϴ�����ͼƬ���û�
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
