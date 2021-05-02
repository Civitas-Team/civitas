package usjt.project.civitas.civitas.helper;

import java.util.Collections;
import java.util.List;

public abstract class PaginationHelper {

	public PaginationHelper() {
	}
	
	public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
	    if(pageSize <= 0 || page <= 0) {
	        throw new IllegalArgumentException("Invalid page size: " + pageSize);
	    }

	    int fromIndex = (page - 1) * pageSize;
	    if(sourceList == null || sourceList.size() < fromIndex){
	        return Collections.emptyList();
	    }

	    // toIndex exclusive
	    return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
	}

}