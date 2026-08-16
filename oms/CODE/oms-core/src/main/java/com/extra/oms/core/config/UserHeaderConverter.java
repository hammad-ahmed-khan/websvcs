/**
 * 
 */
package com.extra.oms.core.config;

import org.springframework.core.convert.converter.Converter;

import com.extra.oms.core.bean.UserInfo;

/**
 * @author aibrahim
 *
 */
public class UserHeaderConverter implements Converter<Object, UserInfo> {

	@Override
	public UserInfo convert(Object source) {
		return null;
	}
}
