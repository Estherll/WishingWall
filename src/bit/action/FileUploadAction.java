package bit.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

import bit.dao.UserDao;
import bit.model.User;

public class FileUploadAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private String bgname;
    
    //ע�⣬file������ָǰ��jsp�ϴ��������ļ����������ļ��ϴ������������ʱ�ļ���������ļ�
    private File file;
    
    //�ύ������file������
    private String fileFileName;
    
    //�ύ������file��MIME����
    private String fileContentType;

	private UserDao dao;
    
    // �ļ��ϴ� 
    public String fileUpload() throws Exception {

    	String root = ServletActionContext.getServletContext().getRealPath("/img");
    	String rmstr= UUID.randomUUID().toString()+fileFileName.substring(fileFileName.lastIndexOf("."));
    	
        InputStream is = new FileInputStream(file);
        
        OutputStream os = new FileOutputStream(new File(root, rmstr));
        
        byte[] buffer = new byte[1024];
        int length = 0;
        
        while((length = is.read(buffer))>0)
        {
            os.write(buffer,0,length);
        }
        
        os.close();
        is.close();
        
        
        // ��Ϣ�������ݿ���
        ActionContext ctx = ActionContext.getContext();
		User curUser = (User) ctx.getSession().get("curuser");
        dao = new UserDao();
        int rows = dao.provideBg(bgname, rmstr, curUser.getUid());
        
        return "bgs";
    }
    
    //	-----------------------------------------

    public File getFile()
    {
        return file;
    }

    public String getBgname() {
		return bgname;
	}

	public void setBgname(String bgname) {
		this.bgname = bgname;
	}

	public void setFile(File file)
    {
        this.file = file;
    }

    public String getFileFileName()
    {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName)
    {
        this.fileFileName = fileFileName;
    }

    public String getFileContentType()
    {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType)
    {
        this.fileContentType = fileContentType;
    }
    

}
