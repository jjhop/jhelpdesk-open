package com.buzzlers.jhelpdesk.web.tools;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.buzzlers.jhelpdesk.utils.FileUtils;

public class SessionListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent evt) {
        evt.getSession().setAttribute("paths", new ConcurrentLinkedQueue<String>());
    }

    public void sessionDestroyed(HttpSessionEvent evt) {
        Collection<String> paths = (Collection<String>) evt.getSession().getAttribute("paths");
        FileUtils.cleanPaths(paths);
    }
}

