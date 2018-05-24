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
     * index.jsp�ض��򵽸���֤��һ������ͨ��Redirect_Uri�ض��򵽸÷���
     * ��ȡ�û���Ϣ��Ȼ���ض��򵽸���ҳ
     */
    public String gerUserinfo(){
    	//��ȡҳ����Ȩaccess_token
    	WeixinOauth2Token weixinOauth2Token;
    	try {
			weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(PropertiesUtils.getKeyValue("APPID"), PropertiesUtils.getKeyValue("APPSECRET"), code);
		} catch (Exception e) {
			//��ҳ��Ȩʧ��
			e.printStackTrace();
			return "error";
		}
    	//��ҳ��Ȩ�ӿڷ���ƾ֤������accesstoken��������ƣ�Ҫע���ظ�ʹ��
    	String accessToken = weixinOauth2Token.getAccessToken();
    	
    	//�û���ʶ
    	String openId = weixinOauth2Token.getOpenId();
    	
    	//��ȡ�û���Ϣ
    	SNSUserInfo snsUserInfo = null;
    	
    	//����userinfo�鿴�û��Ƿ���ڣ�����������������û�
    	UserDao userDao = new UserDao();
    	curUser = userDao.checkUser(openId);
    	
    	ActionContext ctx = ActionContext.getContext();
    	
    	//�û�����-->�����ݿ�  �� �û�������-->��ȡ����info
    	if(curUser == null){//��ǰ�û�������
    		snsUserInfo = AdvancedUtil.getSNSuserInfo(accessToken, openId);
    		//�����֧�ֱ��飬Ҫ����
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
    	//����ǰ�û�����ŵ�session��
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
