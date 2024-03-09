package com.springboot.binding;

import lombok.Data;

@Data
public class CoResponse {
	private Long totalTrigger;
	private Long succTrigger;
	private Long failedTrigger;
}
