package domain;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

/**
 * Class to find the median in a list of Integer-WorkingDay pairs.
 * @author Thomas Vochten
 *
 */
public class MedianSelector {
	
	/**
	 * Finds the median in the specified list of Integer-WorkingDay pairs.
	 * @param data
	 * 		A list of Integer-WorkingDay pairs.
	 * @return The median.
	 */
	public double findMedian(List<Pair<Integer, WorkingDay>> data) {
		if (data.size() % 2 == 0) {
			double leftMedian = this.quickSelect(new ArrayList<Pair<Integer, WorkingDay>>(data),
					0, data.size() - 1, (data.size() / 2) - 1);
			double rightMedian = this.quickSelect(new ArrayList<Pair<Integer, WorkingDay>>(data),
					0, data.size() - 1, data.size() / 2);
			return (leftMedian + rightMedian) / 2;
		}
		else return this.quickSelect(new ArrayList<Pair<Integer, WorkingDay>> (data), 
				0, data.size() - 1, data.size() / 2);
	}
	
	/**
	 * Finds the kth smallest element in the specified list.
	 * @param data
	 * 		A list of Integer-WorkingDay pairs.
	 * @param k
	 * 		The index of the element of interest.
	 * @return The kth smallest element.
	 */
	private double quickSelect(List<Pair<Integer, WorkingDay>> data, int left, int right,
			int k) {
		if (left == right) {
			return data.get(left).getValue0();
		}
		while (left < right) {
			int r = left;
			int w = right;
			int midValue = data.get(left
					+ (int) Math.floor(Math.random() * (right - left + 1))).getValue0();
			while (r < w) {
				if (data.get(r).getValue0() >= midValue) {
					this.swap(data, r, w);
					w--;
				} else {
					r++;
				}
			}
			if (data.get(r).getValue0() > midValue) {
				r--;
			}
			if (k <= r) {
				right = r;
			} else {
				left = r + 1;
			}
		}
		return data.get(k).getValue0();
	}
	
	/**
	 * Swaps the element at the first specified index with the element at the
	 * second specified index in the specified list.
	 * @param data
	 * 		The list to perform a swap in
	 * @param indexOne
	 * 		Index of the first element of interest
	 * @param indexTwo
	 * 		Index of the second element of interest
	 */
	private void swap(List<Pair<Integer, WorkingDay>> data, int indexOne, int indexTwo) {
		Pair<Integer, WorkingDay> temp = data.get(indexOne);
		data.set(indexOne, data.get(indexTwo));
		data.set(indexTwo, temp);
	}

}
