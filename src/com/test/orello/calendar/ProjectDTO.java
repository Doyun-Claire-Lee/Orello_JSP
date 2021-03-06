package com.test.orello.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter@Setter
public class ProjectDTO {

	private String seq;
	private String title;
	private String content;
	private String startdate;
	private String enddate;
	private String colorcode;
	
	private int num;
	
}
