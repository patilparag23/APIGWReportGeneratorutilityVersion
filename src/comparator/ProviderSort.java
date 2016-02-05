package comparator;

import java.util.Comparator;

import models.FileObjects;

public class ProviderSort implements Comparator<FileObjects>{

	@Override
	public int compare(FileObjects o1, FileObjects o2) {
		 return o1.getProvider().compareTo(o2.getProvider());
	}

}
