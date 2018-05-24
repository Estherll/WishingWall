package bit.action;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * ActionSupport �ṩ��:
 * 			1.addActionError(),��Ӵ�����Ϣ��ҳ��
 * 			2.getText(),��ȡ���ʻ��ļ�
 * ModelDriven 
 * 			1.���е����඼��һ��model����(����ͨ�����������Ϳ��Խ��������ƥ�䵽model��,ע:ware����ȫȷ���Ƿ������������ķ�ʽ)
 * 			2.��һ��model��ӵ�ֵջ��valueStack
 * ͨ��Actionʵ��
 * @author zhaoqx
 *
 * @param <T>
 */

public class BaseAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//��listת��Ϊjson������
	public void writerList2json(Object object, String excludes[]) throws IOException{
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		JSONArray jsonArray = JSONArray.fromObject(object, jsonConfig);
		//test
		System.out.println("jsonArray" + jsonArray.toString());
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().write(jsonArray.toString());
		
	}

	//������תΪjson������
	public void writerObject2json(Object object, String excludes[]) throws IOException{
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		JSONObject jsonObject = JSONObject.fromObject(object, jsonConfig);
		
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().write(jsonObject.toString());
	}

}
