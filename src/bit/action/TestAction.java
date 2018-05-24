package bit.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import bit.model.Comment;
import bit.model.Message;

public class TestAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String test(){
		List<Message> ml = new ArrayList<Message>();
		Message m = new Message();
		m.setAnonymity(0);
		m.setCommentNum(1);
		m.setContent("😁😁😁测试啦啦啦啦");
		m.setFromwho("fromme");
		m.setJoinTopic(1);
		m.setLiketimes(0);
		m.setMessType("wish");
		m.setMid(1);
		m.setTowho("toyou");
		m.setUid(9);
		Comment c = new Comment();
		c.setAnonymity(0);
		c.setCid(1);
		c.setCmid(1);
		c.setComment("commentlll");
		c.setCuid(1);
		c.setLikeTimes(0);
		c.setUid(9);
		m.setHotComment(c);
		ml.add(m);
		System.out.println("ml:" + ml.toString());
		try {
			writerList2json(ml, null);
			System.out.println("hello...");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}

}
