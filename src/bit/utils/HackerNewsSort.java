package bit.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Hacker News 排序算法
 * 算法表达式：r=(p-1)/(t+2)^g
 * 三个决定因素：
 * p表示得票数（点赞数）（减去1是为了忽略发布者的投票（点赞））
 * t表示距离发布的时间（单位为小时，加上2是为了防止最新发布导致分母过小）
 * g表示重力因子，即将帖子排名往下拉的力量（g值越大排名下降得越快，即排行榜的更新速度越快）
 * 
 * G=1.5时
 * 1 week----1vote---0.00045
 * 48hours---1vote---0.002828
 * 24hours---1vote---0.007543
 * 0 hours---1vote---0.287175
 * 
 * re刚发送 = re(一天前39票) = re(两天102票)
 *
 * @author 
 *
 */
public class HackerNewsSort {
	
	//根据当前时间戳返回距离当前时刻的小时数，采用进一法
	public static double hoursPast(Timestamp start, Timestamp end){
		Long ms = end.getTime() - start.getTime();
		Long tt = ms / 1000L;
		return tt.doubleValue()/60.0;
	}
	//据当前一周的timestamp
	public static Timestamp getLastWeek(){
		java.sql.Timestamp start = new Timestamp(new Date().getTime());
		return new Timestamp(start.getTime()-604800000L);//1000*60*60*24*7
	}
	
	/**
	 * 计算热度
	 * @param point 投票数
	 * @param start 发表时间
	 * @param end	当前时间
	 * @param gravity 重力参数
	 * @param accuracy 最终计算值的精度
	 * @return
	 */
	
	public static double hackerScore(int point, Timestamp start, Timestamp end, 
			double gravity, DecimalFormat accuracy){
		double hours = Math.abs(hoursPast(start, end));
		double a = 0;
		if(point > 0){
			a = point;
		}
		double b = Math.pow(hours+2, gravity);
		return Double.parseDouble((accuracy.format(a/b)).trim());
	}

}
