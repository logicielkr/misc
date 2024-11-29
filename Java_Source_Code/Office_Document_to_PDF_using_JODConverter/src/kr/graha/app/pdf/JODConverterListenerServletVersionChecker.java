/*
 *
 * Copyright (C) HeonJik, KIM
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package kr.graha.app.pdf;

import javax.servlet.ServletContextEvent;

/**
 * JODConverterListener 에서 Servlet API 버전을 확인한다.
 *
 * Servlet API 버전 3.1 부터는 true 를 리턴하고,
 * Servlet API 버전이 3.0 이하라면, false 를 리턴한다.
 *
 * @author HeonJik, KIM
 * @version 0.1
 * @since 0.1
 */

public class JODConverterListenerServletVersionChecker {
	public static boolean check(ServletContextEvent sce) {
		if(
			sce.getServletContext().getMajorVersion() < 3
			|| (
				sce.getServletContext().getMajorVersion() == 3
				&& sce.getServletContext().getMinorVersion() == 0
			)
		) {
			return false;
		} {
			return true;
		}
	}
}