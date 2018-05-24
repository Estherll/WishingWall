package bit.utils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

/**
 * ���ֶ��󵽸�ʽ�ı���ת��
 * @author ware E-mail:
 * @version create time: 20172017��3��9������3:45:42
 */
public class TextTransfer {
	
	private final static long minute = 60 * 1000;//1minute
	private final static long hour = 60 * minute;//1hour
	private final static long day = 24 * hour;//1day
	private final static long month = 31 * day;//1month
	private final static long year = 12 * month;//1year
	
	public static Object jsonStr2Pojo(String jsonStr, Class targer){
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		return JSONObject.toBean(jsonObject, targer);
	}
	
	//�����ʱ��
	public static String getTimeFormatText(Timestamp date){
		if(date == null){
			return null;
		}
		long diff = new Timestamp(new Date().getTime()).getTime() - date.getTime();
		Long r = 0L;
		if(diff > year){
			r = diff/year;
			return r + "��ǰ";
		}
		if(diff > month){
			r = diff/month;
			return r + "����ǰ";
		}
		if(diff > day){
		    r = diff/day;
			if(r.intValue() == 1){
				return "����";
			}else if(r.intValue() == 2){
				return "ǰ��";
			}
			return r + "��ǰ";
		}
		if (diff > hour){
			r = diff/hour;
			return r + "Сʱǰ";
		}
		if(diff > minute){
			r = diff/minute;
			return r + "����ǰ";
		}
		return "�ո�";
	}
	
	public static boolean isRobot(Queue<Long> queue, Long limit){
		//�������С��5�� ֱ��ͨ��
		if(queue.size() < 5){
			return false;
		}
		Iterator<Long> iterator = queue.iterator();
		Long temp = iterator.next();
		Long sum = 0L;
		while (iterator.hasNext()){
			Long tt = iterator.next();
			sum = sum + Math.abs(tt-temp);
			temp = tt;
		}
		return sum < (limit * (queue.size()-1));
	}
	
	public static String filterEmoji(String source){
		if(source != null){
			Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
			Matcher emojiMatcher = emoji.matcher(source);
			if(emojiMatcher.find()){
				source = emojiMatcher.replaceAll("[����]");
				return source;
			}
			return source;
		}
		return source;
	}
}

