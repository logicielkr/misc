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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JODConverterListener 에서 VirtualServerName 을 확인한다.
 *
 * getVirtualServerName 메소드는 Servlet API 3.1 부터 지원하므로,
 * Apache Tomcat 7.x 에서 실행할 때 에러가 나지 않도록,
 * 별도의 Class 로 분리하였다.
 *
 * @author HeonJik, KIM
 * @version 0.1
 * @since 0.1
 */

public class JODConverterListenerVirtualServerNameChecker {
	public static boolean check(ServletContextEvent sce, Logger logger) {
		String virtualServerName = sce.getServletContext().getInitParameter("office.virtualservername");
		if(
			virtualServerName != null &&
			!virtualServerName.equals(sce.getServletContext().getVirtualServerName())
		) {
			if(logger.isLoggable(Level.FINER)) { logger.finer("contextInitialized(ServletContextEvent sce) skip : office.virtualservername = " + virtualServerName + "but sce.getServletContext().getVirtualServerName() = " + sce.getServletContext().getVirtualServerName()); }
			return false;
		}
		String contextPath = sce.getServletContext().getInitParameter("office.contextpath");
		if(
			contextPath != null &&
			!contextPath.equals(sce.getServletContext().getContextPath())
		) {
			if(logger.isLoggable(Level.FINER)) { logger.finer("contextInitialized(ServletContextEvent sce) skip : office.contextpath = " + contextPath + "but sce.getServletContext().getContextPath() = " + sce.getServletContext().getContextPath()); }
			return false;
		}
		if(logger.isLoggable(Level.FINER)) { logger.finer("contextInitialized(ServletContextEvent sce) with " + sce.getServletContext().getVirtualServerName() + " and " + sce.getServletContext().getContextPath());}
		return true;
	}
}