package bit.action;

import java.util.Date;

import com.opensymphony.xwork2.ActionContext;

import bit.dao.UserDao;
import bit.model.SNSUserInfo;
import bit.model.User;
import bit.model.WeixinOauth2Token;
import bit.utils.AdvancedUtil;
import bit.utils.PropertiesUtils;
import bit.utils.TextTransfer;

public class WXCode extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String code = "cjodeakdfja41sda5fasf1a";
    private String state;
    private User curUser;
    
    public String temp(){
    	User user = new User();
    	UserDao userDao = new UserDao();
    	user = userDao.checkUser("oXxykvyvy");
    	
    	ActionContext ctx = ActionContext.getContext();
    	ctx.getSession().put("curUser", user);
    	
    	return "";
    }
    
    /*
     * index.jsp重定向到该认证第一步，在通过Redirect_Uri重定向到该方法
     * 获取用户信息，然后重定向到该主页
     */
    public String gerUserinfo(){
    	//获取页面授权access_token
    	WeixinOauth2Token weixinOauth2Token;
    	try {
			weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(PropertiesUtils.getKeyValue("APPID"), PropertiesUtils.getKeyValue("APPSECRET"), code);
		} catch (Exception e) {
			//网页授权失败
			e.printStackTrace();
			return "error";
		}
    	//网页授权接口访问凭证，由于accesstoken请求的限制，要注意重复使用
    	String accessToken = weixinOauth2Token.getAccessToken();
    	
    	//用户标识
    	String openId = weixinOauth2Token.getOpenId();
    	
    	//获取用户信息
    	SNSUserInfo snsUserInfo = null;
    	
    	//根据userinfo查看用户是否存在，若不存在则插入新用户
    	UserDao userDao = new UserDao();
    	curUser = userDao.checkUser(openId);
    	
    	ActionContext ctx = ActionContext.getContext();
    	
    	//用户存在-->查数据库  ； 用户不存在-->获取个人info
    	if(curUser == null){//当前用户不存在
    		snsUserInfo = AdvancedUtil.getSNSuserInfo(accessToken, openId);
    		//如果不支持表情，要过滤
    		if(PropertiesUtils.getKeyValue("emoji").equals("0")){
    			snsUserInfo.setNickname(TextTransfer.filterEmoji(snsUserInfo.getNickname()));
    		}
    		int uid = userDao.insertUser(snsUserInfo).intValue();
    		curUser = userDao.getUser(uid);
    	}else{
    		Long now = new Date().getTime();
    		Long lastUpdate = curUser.getLastupdate().getTime();
    		long minute = 60 * 1000;//1minute
    		long hour = 60 * minute;//1hour
    		long twodays = 48 * hour;//2days
    		
    		if(now-lastUpdate > twodays){
    			snsUserInfo = AdvancedUtil.getSNSuserInfo(accessToken, openId);
    			userDao.updateUser(snsUserInfo);
    			curUser.setHeadimgurl(snsUserInfo.getHeadImgUrl());
    			curUser.setNickname(snsUserInfo.getNickname());
    		}
    	}
    	//将当前用户对象放到session中
    	ctx.getSession().put("curUser", curUser);
    	
    	return NONE;
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
	public User getCurUser() {
		return curUser;
	}
	public void setCurUser(User curUser) {
		this.curUser = curUser;
	}
    
    
	

}
