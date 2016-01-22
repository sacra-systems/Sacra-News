package com.sacra_news.scheduler;

import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRTemplate;
import org.jahia.services.scheduler.BackgroundJob;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rvt on 1/21/16.
 * Job Scheduler to set's the orderDate of snt:News items
 */
public class ReorderNewsSchedulerJob extends BackgroundJob {
    private static final Logger logger = LoggerFactory
            .getLogger(ReorderNewsSchedulerJob.class);

    private JCRCallback<Object> callback() {
        return new JCRCallback<Object>() {
            @Override
            public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
                Query q = session.getWorkspace().getQueryManager().createQuery("SELECT * FROM [snt:news]", Query.JCR_SQL2);
                NodeIterator ni = q.execute().getNodes();
                final Date now = new Date();
                boolean changed = false;
                while (ni.hasNext()) {

                    JCRNodeWrapper node = (JCRNodeWrapper) ni.next();

                    final Date republishDate3 = getDate(node, "republishDate3");
                    final Date republishDate2 = getDate(node, "republishDate2");
                    final Date republishDate1 = getDate(node, "republishDate1");

                    final Boolean isRepublished3 = getBoolean(node, "isRePublished3");
                    final Boolean isRepublished2 = getBoolean(node, "isRePublished2");
                    final Boolean isRepublished1 = getBoolean(node, "isRePublished1");

                    if (republishDate3 != null && now.compareTo(republishDate3) > 0 && (isRepublished3 == null || !isRepublished3)) {
                        node.setProperty("isRePublished3", true);
                        final Calendar cal = Calendar.getInstance();
                        cal.setTime(republishDate3);
                        node.setProperty("sortingDate", cal);
                        logger.info("Setting sortingDate to {} on node {} for republishdate 3", republishDate3, node.getPath());
                        changed = true;
                    } else if (republishDate2 != null && now.compareTo(republishDate2) > 0 && (isRepublished2 == null || !isRepublished2)) {
                        node.setProperty("isRePublished2", true);
                        final Calendar cal = java.util.Calendar.getInstance();
                        cal.setTime(republishDate2);
                        node.setProperty("sortingDate", cal);
                        logger.info("Setting sortingDate to {} on node {} for republishdate 2", republishDate2, node.getPath());
                        changed = true;
                    } else if (republishDate1 != null && now.compareTo(republishDate1) > 0 && (isRepublished1 == null || !isRepublished1)) {
                        node.setProperty("isRePublished1", true);
                        final Calendar cal = java.util.Calendar.getInstance();
                        cal.setTime(republishDate1);
                        node.setProperty("sortingDate", cal);
                        logger.info("Setting sortingDate to {} on node {} for republishdate 1", republishDate1, node.getPath());
                        changed = true;
                    }
                }
                if (changed) {
                    session.save();
                }
                return null;
            }
        };
    }


    @Override
    public void executeJahiaJob(JobExecutionContext jobExecutionContext) throws Exception {
        JCRTemplate.getInstance().doExecuteWithSystemSessionAsUser(null, "default", Locale.GERMAN, callback());
        JCRTemplate.getInstance().doExecuteWithSystemSessionAsUser(null, "live", Locale.GERMAN, callback());
    }


    /**
     * Helper function that get's a Date from a property and ignore a missing property and reutns null
     *
     * @param node
     * @param propName
     * @return
     */
    private Date getDate(JCRNodeWrapper node, String propName) {
        try {
            return node.getProperty(propName).getDate().getTime();
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }

    /**
     * Helper function that get's a Boolean from a property and ignore a missing property and reutns null
     *
     * @param node
     * @param propName
     * @return
     */
    private Boolean getBoolean(JCRNodeWrapper node, String propName) {
        try {
            return node.getProperty(propName).getBoolean();
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }
}
