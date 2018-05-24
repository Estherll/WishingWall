package bit.model;

/**
 * 网页授权信息
 * 
 * @author liufeng
 * @date 2013-11-09
 */
public class WeixinOauth2Token {
	// 网页授权接口调用凭证
	private String accessToken;
	// 凭证有效时长
	private int expiresIn;
	// 用于刷新凭证
	private String refreshToken;
	// 用户标识
	private String openId;
	// 用户授权作用�?
	private String scope;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
