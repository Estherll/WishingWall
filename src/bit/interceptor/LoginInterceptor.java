package bit.interceptor;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @param
 * @return
 * @throws Exception
 * @author ware E-mail:
 * @version create time: 20162016��12��24������1:24:56
 */
public class LoginInterceptor extends AbstractInterceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String actionname = invocation.getInvocationContext().getName();
		
		//���session����current user����Ϣ����������Ϊlogin��ͷ�����
		if(ServletActionContext.getRequest().getSession().getAttribute("curUser") == null){
			if(actionname.indexOf("login") == 0 || actionname.indexOf("wx") == 0 || actionname.indexOf("administor") >0){
				return invocation.invoke();
			}
			return "error";
		}
		return invocation.invoke();
	}

}
