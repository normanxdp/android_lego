package android.bigplan.lego.view.sort;

import android.bigplan.lego.model.ProvinceCity;

import java.util.Comparator;

/**
 * @author xiaanming
 */
public class PinyinComparator implements Comparator<ProvinceCity> {

	public int compare(ProvinceCity o1, ProvinceCity o2) {
	/*	if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}*/
		
		if ("@".equals(o1.getFirstLetter())
				|| "#".equals(o2.getFirstLetter())) {
			return 1;
		} else if ("#".equals(o1.getFirstLetter())
				|| "@".equals(o2.getFirstLetter())) {
			return 1;
		} else {
			return o1.getFirstLetter().compareTo(o2.getFirstLetter());
		}
	}

}
