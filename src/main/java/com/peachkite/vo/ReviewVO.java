package com.peachkite.vo;

import com.peachkite.enums.MartialStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewVO{
	private MartialStatus martialStatus;
	private Date date;
	private String comment;
}
