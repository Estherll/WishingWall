package bit.action;

import java.io.IOException;

import bit.dao.HotTopicDao;
import bit.model.HotTopic;

public class HotTopicAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HotTopic hotTopic;
	private int targetHtid;
	
	public String getHotTopicInfo(){
		HotTopicDao hotTopicDao = new HotTopicDao();
		hotTopic = hotTopicDao.getNewestTopic();
		try {
			writerObject2json(hotTopic, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 管理员删除热门话题
	 */
	public String deleteHotTopic(){
		HotTopicDao hotTopicDao = new HotTopicDao();
		Integer rows = hotTopicDao.deleteHotTopic(targetHtid);
		try {
			writerList2json(rows, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/*
	 * 管理员发布新的热门话题
	 */
	public String pulishNewTopic(){
		HotTopicDao hotTopicDao = new HotTopicDao();
		hotTopicDao.insertHotTopic(hotTopic);
		return NONE;
	}

	public HotTopic getHotTopic() {
		return hotTopic;
	}

	public void setHotTopic(HotTopic hotTopic) {
		this.hotTopic = hotTopic;
	}

	public int getTargetHtid() {
		return targetHtid;
	}

	public void setTargetHtid(int targetHtid) {
		this.targetHtid = targetHtid;
	}
	
	
}
