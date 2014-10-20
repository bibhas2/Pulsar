package com.mobiarch.pulsar;

import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(loadOnStartup = 1, name="SchedulerServlet")
public class SchedulerServlet extends HttpServlet {
	private static final long serialVersionUID = 6475966902867139531L;
	Logger logger = Logger.getLogger(getClass().getName());
	private Engine engine;

	public void init(ServletConfig cfg) throws ServletException {
		super.init(cfg);

		logger.info("Scheduler servlet loading...");
		String cfgFile = cfg.getServletContext().getRealPath(
				"WEB-INF/schedule.xml");
		try {
			this.engine = new Engine(cfgFile);
		} catch (Exception e) {
			logger.severe("Scheduler failed to initialize. Will shutdown scheduler.");
			if (this.engine != null) {
				this.engine.cancel();
				this.engine = null;
			}
			throw new ServletException(e);
		}
	}

	public void destroy() {
		logger.info("Scheduler servlet shutting down...");
		if (this.engine != null) {
			this.engine.cancel();
		}
	}
}
