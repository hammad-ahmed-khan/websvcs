package com.extra.sim.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.extra.sim.model.BaseLV;
import com.extra.sim.service.UtilService;

@RestController
@RequestMapping(path = "/util")
public class UtilController {

	@Autowired
	private UtilService utilService;

	@GetMapping(path = "/baselv")
	public Map<String, List<BaseLV>> getBaseLVs(@RequestParam(name = "listCode") String listCode) {
		return utilService.getBaseLVs(listCode);
	}
}
