package comparator;

import java.util.Comparator;

import models.FileObjects;
public class ClientSort implements Comparator<FileObjects>{

	@Override
	public int compare(FileObjects o1, FileObjects o2) {
		 return o1.getClient().compareTo(o2.getClient());
	}


}
