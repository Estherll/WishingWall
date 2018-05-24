package bit.utils;

import java.util.ArrayList;



/**
 * 
 * @author Administrator
 * ��/ �����-- - һ�ֽṹ
 * �����ص�:
 * 1.��Ԫ�����Ǵ���(С��)��������������Ԫ��
 * 2.��һ������������,��ӽ�����������
 * 
 * ��ڵ�: 2 * i + 1
 * �ҽڵ�: 2 * i +2
 * ���ڵ�: ��ȡ�� ( i-1 )/2
 * 
 * ����µĽڵ�:  ���½ڵ���ӵ�listĩβ, �����Ϲ���              --- ʱ�临�Ӷ� logN
 * �Ƴ��׽ڵ�     :	  ������ĩβ��Ԫ�ض��浽ͷ��, �����¹���      --- ʱ�临�Ӷ�  logN
 * 
 * ���Ϲ���: filterUp(index)  --- logN
 * 		ֻҪ�ȸ�Ԫ��С(��)�ͺ͸�Ԫ�ؽ���
 * ���¹���: filterDown(index)  --- logN
 * 		��ͣ�ĺ�������Ԫ������С��Ԫ�ؽ���
 * 
 * ������: 
 * 		���--- NlogN
 * 		�--- 2NlogN
 * 		ƽ��--- 2NlogN
 * 
 * 
 */
public class Heap<E extends Comparable<E>> {
	private ArrayList<E> data=null;
	
	public Heap(){
		data=new ArrayList<E>();
	}
	
	// ����ѻ���ʱ�临�Ӷ�  NlogN
	public Heap(ArrayList<E> unsortedData){
		data=new ArrayList<E>();
		for(E item:unsortedData){
			data.add(item);
		}
		// ע��ѻ������λ��
		for (int i = ( data.size() /2 ) -1; i >= 0; i--) {
			filterDown(i);
		}
		
	}
	
	public static <E extends Comparable<E>> void heapSort(ArrayList<E> data){
		Heap<E> heap=new Heap<E>(data);
		for (int i = 0; i < data.size(); i++) {
			data.set(i, heap.remove());
		}
	}
	
	public void add(E item){
		data.add(item);
		filterUp(data.size()-1);
	}
	
	// �߽�, ��������list����Ϊ1
	public E remove(){ 
		E result=data.get(0);
		E lastElement=data.remove(data.size()-1);
		if(data.size()>0){
			data.set(0, lastElement);
			filterDown(0);
		}
		return result;
	}
	
	private void filterUp(int index) {
		int parent=parentIndex(index);
		while(parent>=0){
			if(data.get(index).compareTo(data.get(parent)) < 0){
				swap(parent,index);
				index=parent;
				parent=parentIndex(index);
			}else{
				return;
			}
		}
		
	}
	
	private void swap(int parent, int index) {
		E temp = data.get(parent);
		data.set(parent, data.get(index));
		data.set(index, temp);
		
		
	}
	// �߽�Ϊindex==data.length, index==smallest
	private void filterDown(int index) {
		while(index<data.size()){
			int left=leftChidIndex(index);
			int right=rightChildIndex(index);
			int smallest=index;
			if(left<data.size() && data.get(left).compareTo(data.get(smallest)) <0 ){
				smallest=left;
			}
			if(right< data.size() && data.get(right).compareTo(data.get(smallest) )<0){
				smallest=right;
			}
			if(smallest==index){
				return;
			}
			swap(index,smallest);
			index=smallest;
		}
		
	}
	private int parentIndex(int index){
		return (index-1)/2;
	}
	private int leftChidIndex(int index){
		return 2*index+1;
	}
	private int rightChildIndex(int index){
		return 2*index + 2 ;
	}
	public boolean isEmpty(){
		return data.isEmpty();
	}

}
