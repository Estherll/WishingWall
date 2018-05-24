package bit.utils;

import antlr.collections.List;
import bit.model.SNSUserInfo;
import bit.model.WeixinOauth2Token;
import bit.model.WeixinUserInfo;
import bit.utils.CommonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * �߼��ӿڹ�����
 * 
 * @author liufeng
 * @date 2013-11-9
 */
public class AdvancedUtil {
	
	/**
	 * ��ȡ��ҳ��Ȩƾ֤
	 * 
	 * @param appId �����˺ŵ�Ψһ��ʶ
	 * @param appSecret �����˺ŵ���Կ
	 * @param code
	 * @return WeixinAouth2Token
	 * @throws Exception 
	 */
	
	public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) throws Exception{
		WeixinOauth2Token wat = null;
		//ƴ�������ַ
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		requestUrl = requestUrl.replace("APPID", appId);
		requestUrl = requestUrl.replace("SECRET", appSecret);
		requestUrl = requestUrl.replace("CODE", code);
		//��ȡ��ҳ��Ȩƾ֤
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
		if(null != jsonObject){
			try{
				wat = new WeixinOauth2Token();
				wat.setAccessToken(jsonObject.getString("access_token"));
				wat.setExpiresIn(jsonObject.getInt("expires_in"));
				wat.setRefreshToken(jsonObject.getString("refresh_token"));
				wat.setOpenId(jsonObject.getString("openid"));
				wat.setScope(jsonObject.getString("scope"));
			}catch(Exception e){
				wat = null;
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				System.out.println("��ȡaccesstokenʧ��");
				System.out.println("errorCode"+errorCode);
				System.out.println("errorMsg"+errorMsg);
				
				//��ȡ��ҳ��Ȩƾ֤ʧ��
				throw new Exception("��ȡ��ҳ��Ȩƾ֤ʧ��");
			}
			
		}
		return wat;
	}
	
	/**
	 * ˢ����ҳ��Ȩƾ֤
	 * 
	 * @param appId �����˺ŵ�Ψһ��ʶ
	 * @param refreshToken
	 * @return WeixinAouth2Token
	 */
	public static WeixinOauth2Token refreshOauth2AccessToken(String appId, String refreshToken){
		WeixinOauth2Token wat = null;
		//ƴ�������ַ
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
		requestUrl = requestUrl.replace("APPID", appId);
		requestUrl = requestUrl.replace("REFRESH_TOKEN", refreshToken);
		//ˢ����ҳ��Ȩƾ֤
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
		if(null != jsonObject){
			try{
				wat = new WeixinOauth2Token();
				wat.setAccessToken(jsonObject.getString("access_token"));
				wat.setExpiresIn(jsonObject.getInt("expires_in"));
				wat.setRefreshToken(jsonObject.getString("refesh_token"));
				wat.setOpenId(jsonObject.getString("openid"));
				wat.setScope(jsonObject.getString("scope"));
			}catch(Exception e){
				wat = null;
				e.printStackTrace();
			}
		}
		return wat;
	}
	
	/**
	 * ͨ����ҳ��Ȩ��ȡ�û���Ϣ
	 * @param accessToken ��ҳ��Ȩ�ӿڵ���ƾ֤
	 * @param openId �û���ʶ
	 * @return SNSUserInfo
	 */
	@SuppressWarnings("unchecked")
	public static SNSUserInfo getSNSuserInfo(String accessToken, String openId){
		SNSUserInfo snsUserInfo = null;
		//ƴ�������ַ
		String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		//ͨ����ҳ��Ȩ��ȡ�û���Ϣ
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
		if(null != jsonObject){
			try{
				snsUserInfo = new SNSUserInfo();
				//�û��ı�ʶ
				snsUserInfo.setOpenId(jsonObject.getString("openid"));
				//�ǳ�
				snsUserInfo.setNickname(jsonObject.getString("nickname"));
				//�Ա�1�����ԣ�2��Ů�ԣ�0��δ֪��
				snsUserInfo.setSex(jsonObject.getInt("sex"));
				//�û����ڹ���
				snsUserInfo.setCountry(jsonObject.getString("country"));
				//�û�����ʡ��
				snsUserInfo.setProvince(jsonObject.getString("province"));
				//�û����ڳ���
				snsUserInfo.setCity(jsonObject.getString("city"));
				//�û�ͷ��
				snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
				//�û���Ȩ��Ϣ
				snsUserInfo.setPrivilegeList(JSONArray.toList(jsonObject.getJSONArray("privilege"), List.class));
			}catch(Exception e){
				snsUserInfo = null;
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				System.out.println("��ȡ�û���Ϣʧ��");
				System.out.println("errorCode"+errorCode);
				System.out.println("errorMsg"+errorMsg);
				//��ȡ�û���Ϣʧ��
				e.printStackTrace();
			}
		}
		return snsUserInfo;
	}
	
	/**
	 * ��ȡ�û���Ϣ
	 * 
	 * @param accessToken �ӿڷ���ƾ֤
	 * @param openId �û���ʶ
	 * @return WeixinUserInfo
	 */
	public static WeixinUserInfo getUserInfo(String accessToken, String openId) {
		WeixinUserInfo weixinUserInfo = null;
		// ƴ�������ַ
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		// ��ȡ�û���Ϣ
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);

		if (null != jsonObject) {
			try {
				weixinUserInfo = new WeixinUserInfo();
				// �û��ı�ʶ
				weixinUserInfo.setOpenId(jsonObject.getString("openid"));
				// ��ע״̬��1�ǹ�ע��0��δ��ע����δ��עʱ��ȡ����������Ϣ
				weixinUserInfo.setSubscribe(jsonObject.getInt("subscribe"));
				// �û���עʱ��
				weixinUserInfo.setSubscribeTime(jsonObject.getString("subscribe_time"));
				// �ǳ�
				weixinUserInfo.setNickname(jsonObject.getString("nickname"));
				// �û����Ա�1�����ԣ�2��Ů�ԣ�0��δ֪��
				weixinUserInfo.setSex(jsonObject.getInt("sex"));
				// �û����ڹ���
				weixinUserInfo.setCountry(jsonObject.getString("country"));
				// �û�����ʡ��
				weixinUserInfo.setProvince(jsonObject.getString("province"));
				// �û����ڳ���
				weixinUserInfo.setCity(jsonObject.getString("city"));
				// �û������ԣ���������Ϊzh_CN
				weixinUserInfo.setLanguage(jsonObject.getString("language"));
				// �û�ͷ��
				weixinUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
			} catch (Exception e) {
				if (0 == weixinUserInfo.getSubscribe()) {
					// �û�{}��ȡ����ע
					e.printStackTrace();
				} else {
//					int errorCode = jsonObject.getInt("errcode");
//					String errorMsg = jsonObject.getString("errmsg");
					//��ȡ�û���Ϣʧ�� 
					e.printStackTrace();
				}
			}
		}
		return weixinUserInfo;
	}

}
