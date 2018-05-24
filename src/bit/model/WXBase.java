package bit.model;
/**
 * 微信用户认证�?�?要的琳琳碎碎的东�?
 * 
 * @throws Exception
 * @author ware E-mail:
 * @version create time: 20172017�?3�?9日下�?3:51:03
 */
public class WXBase {
	
	private String code;
	private String state;
	private User curUser;
	
	public User getCurUser() {
		return curUser;
	}
	public void setCurUser(User curUser) {
		this.curUser = curUser;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	

}
