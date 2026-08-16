package com.extra.bds.bean;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Dates {

	@JsonFormat(pattern="yyyy-MM-dd")
	private Date date;

	private List<Window> windows;

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

	public List<Window> getWindows() {
		return windows;
	}

	public void setWindows(List<Window> windows) {
		this.windows = windows;
	}
}
