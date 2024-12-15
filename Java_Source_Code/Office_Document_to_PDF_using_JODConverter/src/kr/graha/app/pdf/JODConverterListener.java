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

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

/**
 * Servlet Context 가 초기화 되면, JODConverterManager 를 시작시킨다.
 *
 * web.xml 에 다음을 추가한다.
 *
 * <pre>
 * <context-param>
 * 	<param-name>office.home</param-name>
 * 	<param-value>/opt/openoffice</param-value>
 * </context-param>
 * <context-param>
 * 	<param-name>office.port</param-name>
 * 	<param-value>2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008</param-value>
 * </context-param>
 * <context-param>
 * 	<param-name>office.timeout</param-name>
 * 	<param-value>1800000</param-value>
 * </context-param>
 * <context-param>
 * 	<param-name>office.virtualservername</param-name>
 * 	<param-value>Catalina/localhost</param-value>
 * </context-param>
 * <context-param>
 * 	<param-name>office.contextpath</param-name>
 * 	<param-value></param-value>
 * </context-param>
 * <listener>
 * <listener-class>kr.graha.app.pdf.JODConverterListener</listener-class>
 * </listener>
 * </pre>
 *
 * office.port 는 필수가 아니다.
 *
 * office.home 이 없는 경우 다음에서 찾는다.
 * 만약 찾을 수 없다면 logger.severe 로 로그를 남기고, JODConverterManager 를 시작하지 않는다.
 * - /opt/libreoffice
 * - /opt/openoffice
 * - /opt/libreoffice7.5
 * - /opt/openoffice4
 * - /usr/lib/openoffice
 * - /usr/lib/libreoffice
 *
 * JODConverterManager 는 여러 번 실행되면 안된다.
 * office.virtualservername 와 office.contextpath 파라미터로 중복 실행되지 않도록 한다.
 *
 * @author HeonJik, KIM
 * @version 0.1
 * @since 0.1
 */

public class JODConverterListener implements ServletContextListener {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private JODConverterManager converter = null;
	public void contextInitialized(ServletContextEvent sce) {
		if(JODConverterListenerServletVersionChecker.check(sce)) {
			if(JODConverterListenerVirtualServerNameChecker.check(sce, this.logger)) {
			} else {
				return;
			}
		}
		String officeHome = sce.getServletContext().getInitParameter("office.home");
		if(officeHome == null) {
			if(System.getProperty("office.home") != null) {
				officeHome = System.getProperty("office.home");
			}
		}
		if(officeHome == null) {
			if(JODConverterManager.searcheOfficeHome != null) {
				for(int i = 0; i < JODConverterManager.searcheOfficeHome.length; i++) {
					if((new File(JODConverterManager.searcheOfficeHome[i])).exists()) {
						officeHome = JODConverterManager.searcheOfficeHome[i];
						break;
					}
				}
			}
		}
		if(officeHome == null) {
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("require office.home!!!"); }
			
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("web.xml"); }
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("<context-param>"); }
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("	<param-name>office.home</param-name>"); }
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("	<param-value>${OpenOffice|LibreOffice Home Directory Path}</param-value>"); }
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("</context-param>"); }
			
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("or"); }
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("Symbolic link"); }
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("/opt/libreoffice"); }
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("/opt/openoffice"); }
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("/opt/libreoffice7.5"); }
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("/opt/openoffice4"); }
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("/usr/lib/openoffice"); }
			if(logger.isLoggable(Level.SEVERE)) { logger.severe("/usr/lib/libreoffice"); }
			return;
		}
/*
		int[] portNumbers = null;
		String ports = sce.getServletContext().getInitParameter("office.port");
		if(ports != null) {

			List list = new ArrayList();
			StringTokenizer st = new StringTokenizer(ports, ", ");
			while(st.hasMoreTokens()) {
				String s = st.nextToken();
				list.add(Integer.valueOf(s));
			}
			portNumbers = new int[list.size()];
			for(int i = 0 ; i < list.size(); i++)  {
				portNumbers[i] = ((Integer)list.get(i)).intValue();
			}
		}
*/
		long taskExecutionTimeout = 0;
		String timeout = sce.getServletContext().getInitParameter("office.timeout");
		if(timeout != null) {
			taskExecutionTimeout = Long.valueOf(timeout);
		}
		int maxTasksPerProcess = 0;
		String tasks = sce.getServletContext().getInitParameter("office.maxtasksperprocess");
		if(tasks != null) {
			maxTasksPerProcess = Integer.valueOf(tasks);
		}
		int[] portNumbers = null;
		try {
			this.converter = JODConverterManager.getInstance();
			String ports = sce.getServletContext().getInitParameter("office.port");
			if(ports != null) {
				portNumbers = this.converter.portNumbers(ports);
			}
			this.converter.init(
				officeHome,
				taskExecutionTimeout,
				maxTasksPerProcess,
				portNumbers
			);
		} catch (org.jodconverter.core.office.OfficeException e) {
			if(logger.isLoggable(Level.SEVERE)) { logger.severe(kr.graha.helper.LOG.toString(e)); }
		}
	}
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			this.converter.destory();
		} catch (org.jodconverter.core.office.OfficeException e) {
			if(logger.isLoggable(Level.SEVERE)) { logger.severe(kr.graha.helper.LOG.toString(e)); }
		}
	}
}