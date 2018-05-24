package bit.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Hacker News �����㷨
 * �㷨���ʽ��r=(p-1)/(t+2)^g
 * �����������أ�
 * p��ʾ��Ʊ����������������ȥ1��Ϊ�˺��Է����ߵ�ͶƱ�����ޣ���
 * t��ʾ���뷢����ʱ�䣨��λΪСʱ������2��Ϊ�˷�ֹ���·������·�ĸ��С��
 * g��ʾ�������ӣ���������������������������gֵԽ�������½���Խ�죬�����а�ĸ����ٶ�Խ�죩
 * 
 * G=1.5ʱ
 * 1 week----1vote---0.00045
 * 48hours---1vote---0.002828
 * 24hours---1vote---0.007543
 * 0 hours---1vote---0.287175
 * 
 * re�շ��� = re(һ��ǰ39Ʊ) = re(����102Ʊ)
 *
 * @author 
 *
 */
public class HackerNewsSort {
	
	//���ݵ�ǰʱ������ؾ��뵱ǰʱ�̵�Сʱ�������ý�һ��
	public static double hoursPast(Timestamp start, Timestamp end){
		Long ms = end.getTime() - start.getTime();
		Long tt = ms / 1000L;
		return tt.doubleValue()/60.0;
	}
	//�ݵ�ǰһ�ܵ�timestamp
	public static Timestamp getLastWeek(){
		java.sql.Timestamp start = new Timestamp(new Date().getTime());
		return new Timestamp(start.getTime()-604800000L);//1000*60*60*24*7
	}
	
	/**
	 * �����ȶ�
	 * @param point ͶƱ��
	 * @param start ����ʱ��
	 * @param end	��ǰʱ��
	 * @param gravity ��������
	 * @param accuracy ���ռ���ֵ�ľ���
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
