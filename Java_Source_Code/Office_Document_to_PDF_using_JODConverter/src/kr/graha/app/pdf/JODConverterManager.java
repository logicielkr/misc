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

import org.jodconverter.local.office.LocalOfficeManager;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.local.LocalConverter;
import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * JODConverter 관리자
 * java -classpath ./bin/:lib/JODConverter/jodconverter-local-4.4.6.jar:lib/JODConverter/jodconverter-core-4.4.6.jar kr.graha.app.pdf.JODConverterManager
 * 
 * 
 * @author HeonJik, KIM
 * @version 0.1
 * @since 0.1
 */

public class JODConverterManager {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private LocalOfficeManager.Builder builder = null;
	private LocalOfficeManager localOfficeManager = null;
	private DocumentConverter documentConverter = null;
	private boolean init = false;
	protected static String[] searcheOfficeHome = new String[]{
		"/opt/libreoffice",
		"/opt/openoffice",
		"/opt/libreoffice7.5",
		"/opt/openoffice4",
		"/usr/lib/openoffice",
		"/usr/lib/libreoffice"
	};
	private JODConverterManager() {
	}
	static class JODConverterManagerHolder {
		static JODConverterManager instance = new JODConverterManager();
	}
	public static JODConverterManager getInstance() {
		return JODConverterManagerHolder.instance;
	}
	private synchronized void initInternal(
		String officeHome,
		long taskExecutionTimeout,
		int maxTasksPerProcess,
		int[] portNumbers
	) throws org.jodconverter.core.office.OfficeException {
		if(this.init) {
			return;
		}
		this.init = true;
		if(this.builder == null) {
			this.builder = LocalOfficeManager.builder();
			if(officeHome != null) {
				this.builder.officeHome(officeHome);
				if(logger.isLoggable(Level.FINER)) { logger.finer("officeHome = " + officeHome); }
			}
			if(taskExecutionTimeout > 0) {
				this.builder.taskExecutionTimeout(taskExecutionTimeout);
				if(logger.isLoggable(Level.FINER)) { logger.finer("taskExecutionTimeout = " + taskExecutionTimeout); }
			}
			if(maxTasksPerProcess > 0) {
				this.builder.maxTasksPerProcess(maxTasksPerProcess);
				if(logger.isLoggable(Level.FINER)) { logger.finer("maxTasksPerProcess = " + maxTasksPerProcess); }
			}
			if(portNumbers != null && portNumbers.length > 0) {
				this.builder.portNumbers(portNumbers);
				if(logger.isLoggable(Level.FINER)) { logger.finer("portNumbers = " + portNumbers); }
			}
			if(logger.isLoggable(Level.FINER)) { logger.finer("LocalOfficeManager.Builder"); }
		}
		if(this.localOfficeManager == null) {
			this.localOfficeManager = this.builder.build();
			if(logger.isLoggable(Level.FINER)) { logger.finer("LocalOfficeManager.Builder.build() => LocalOfficeManager"); }
		}
		if(!this.localOfficeManager.isRunning()) {
			this.localOfficeManager.start();
			if(logger.isLoggable(Level.FINER)) { logger.finer("LocalOfficeManager.start()"); }
		}
		if(this.documentConverter == null) {
			this.documentConverter = LocalConverter.make(this.localOfficeManager);
			if(logger.isLoggable(Level.FINER)) { logger.finer("LocalConverter.make(LocalOfficeManager) => DocumentConverter"); }
		}
	}
	protected void init(
		String officeHome,
		long taskExecutionTimeout,
		int maxTasksPerProcess,
		int[] portNumbers
	) throws org.jodconverter.core.office.OfficeException {
		this.initInternal(
			officeHome,
			taskExecutionTimeout,
			maxTasksPerProcess,
			portNumbers
		);
	}
	protected void init(
		String officeHome
	) throws org.jodconverter.core.office.OfficeException {
		this.initInternal(
			officeHome,
			0,
			0,
			null
		);
	}
	protected void init(
		String officeHome,
		int[] portNumbers
	) throws org.jodconverter.core.office.OfficeException {
		this.initInternal(
			officeHome,
			0,
			0,
			portNumbers
		);
	}
	protected void init() throws org.jodconverter.core.office.OfficeException {
		this.initInternal(
			null,
			0,
			0,
			null
		);
	}
	protected void init(
		int[] portNumbers
	) throws org.jodconverter.core.office.OfficeException {
		this.initInternal(
			null,
			0,
			0,
			portNumbers
		);
	}
	private synchronized void checkInternal() throws org.jodconverter.core.office.OfficeException {
		if(this.localOfficeManager != null && !this.localOfficeManager.isRunning()) {
			this.localOfficeManager.start();
			if(logger.isLoggable(Level.FINER)) { logger.finer("LocalOfficeManager.start()"); }
		}
	}
	private synchronized void destoryInternal() throws org.jodconverter.core.office.OfficeException {
		if(this.localOfficeManager != null && this.localOfficeManager.isRunning()) {
			this.localOfficeManager.stop();
			if(logger.isLoggable(Level.FINER)) { logger.finer("LocalOfficeManager.stop()"); }
		}
		this.documentConverter = null;
		this.localOfficeManager = null;
		this.builder = null;
	}
	protected void destory() throws org.jodconverter.core.office.OfficeException {
		this.destoryInternal();
	}
	public void convert(File file) throws org.jodconverter.core.office.OfficeException {
		if(file.exists()) {
			if(file.isDirectory()) {
				this.convert(file.listFiles());
			} else {
				convert(new File[]{file});
			}
		} else {
			if(logger.isLoggable(Level.WARNING)) { logger.warning("File not exists : " + file); }
		}
	}
	public void convert(String path) throws org.jodconverter.core.office.OfficeException {
		File file = new File(path);
		if(file.exists()) {
			if(file.isDirectory()) {
				this.convert(file.listFiles());
			} else {
				this.convert(file);
			}
		} else {
			if(logger.isLoggable(Level.WARNING)) { logger.warning("File not exists : " + path); }
		}
	}
	public void convert(File[] files) throws org.jodconverter.core.office.OfficeException {
		if(files != null && files.length > 0) {
			checkInternal();
			for(int i = 0; i < files.length; i++) {
				File inputFile = files[i];
				String p = inputFile.getPath();
				File outputFile = null;
				if(p.indexOf(".") > 0) {
					outputFile = new File(p.substring(0, p.lastIndexOf(".")) + ".pdf");
				} else {
					outputFile = new File(p + ".pdf");
				}
				long before = System.currentTimeMillis();
				if(logger.isLoggable(Level.FINER)) { logger.finest("before convert " + inputFile.getPath() + " to " + outputFile.getPath()); }
				this.documentConverter.convert(inputFile).to(outputFile).execute();
				long after = System.currentTimeMillis();
				if(logger.isLoggable(Level.FINER)) { logger.finest(Long.toString(after - before) + "ms : " + "after convert " + inputFile.getPath() + " to " + outputFile.getPath()); }
			}
		}
	}
	public static void main(String[] args) throws Exception {
		if(args == null || args.length == 0) {
			System.err.println("{Calc|Excel File Path} is not exists!!!");
			System.err.println();
			System.err.println("Usage :");
			System.err.println("java \\");
			System.err.println("-classpath ./bin/:lib/JODConverter/jodconverter-local-4.4.6.jar:lib/JODConverter/jodconverter-core-4.4.6.jar \\");
			System.err.println("kr.graha.app.pdf.JODConverterManager \\");
			System.err.println("{Calc|Excel File Path}");
			return;
		}
		String officeHome = null;
		if(JODConverterManager.searcheOfficeHome != null) {
			for(int i = 0; i < JODConverterManager.searcheOfficeHome.length; i++) {
				if((new File(JODConverterManager.searcheOfficeHome[i])).exists()) {
					officeHome = JODConverterManager.searcheOfficeHome[i];
					break;
				}
			}
		}
		if(officeHome == null) {
			System.err.println("OpenOffice | LibreOffice not exists!!!");
			System.err.println("create Symbolic link");
			System.err.println("	/opt/libreoffice");
			System.err.println("	/opt/openoffice");
			System.err.println("	/opt/libreoffice7.5");
			System.err.println("	/opt/openoffice4");
			System.err.println("	/usr/lib/openoffice");
			System.err.println("	/usr/lib/libreoffice");
			return;
		}
		System.out.println("convert " + args[0] + " using " + officeHome);
		JODConverterManager converter = JODConverterManager.getInstance();
		converter.init(	officeHome);
		converter.convert(args[0]);
		converter.destory();
	}
}

