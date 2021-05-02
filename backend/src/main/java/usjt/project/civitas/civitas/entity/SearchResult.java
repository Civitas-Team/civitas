package usjt.project.civitas.civitas.entity;

import java.util.List;

public class SearchResult {
	
	private List<?> data;
	private int totalPages;
	private int totalOfResults;
	private int currentPage;
	
	public SearchResult() {
	}

	public SearchResult(List<?> data, int totalPages, int totalOfResults, int currentPage) {
		super();
		this.data = data;
		this.totalPages = totalPages;
		this.totalOfResults = totalOfResults;
		this.currentPage = currentPage;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalOfResults() {
		return totalOfResults;
	}

	public void setTotalOfResults(int totalOfResults) {
		this.totalOfResults = totalOfResults;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}