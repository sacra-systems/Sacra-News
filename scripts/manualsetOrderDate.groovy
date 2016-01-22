import org.jahia.services.content.*
import org.slf4j.LoggerFactory

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = LoggerFactory.getLogger("setsntNewsorderDate");

JCRTemplate.getInstance().doExecuteWithSystemSessionAsUser(null, "default", Locale.GERMAN, new JCRCallback() {
    public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
        Query q = session.getWorkspace().getQueryManager().createQuery("SELECT * FROM [snt:news]", Query.JCR_SQL2);
        NodeIterator ni = q.execute().getNodes();
        def now = new Date()
        def changed = false;
        while (ni.hasNext()) {
            JCRNodeWrapper node = (JCRNodeWrapper) ni.next();

            def Date republishDate3 = getDate(node, "republishDate3")
            def Date republishDate2 = getDate(node, "republishDate2")
            def Date republishDate1 = getDate(node, "republishDate1")

            def Boolean isRepublished3 = getBoolean(node, "isRePublished3")
            def Boolean isRepublished2 = getBoolean(node, "isRePublished2")
            def Boolean isRepublished1 = getBoolean(node, "isRePublished1")

            if (republishDate3 != null && now >= republishDate3 && !isRepublished3) {
                node.setProperty("isRePublished3", true);
                def c = java.util.Calendar.getInstance(); c.setTime(republishDate3)
                node.setProperty("sortingDate", c);
                logger.info("Setting sortingDate to ${republishDate3} on node ${node.getName()} for republishdate 3");
                changed = true;
            } else if (republishDate2 != null && now >= republishDate2 && !isRepublished2) {
                node.setProperty("isRePublished2", true);
                def c = java.util.Calendar.getInstance(); c.setTime(republishDate2)
                node.setProperty("sortingDate", c);
                logger.info("Setting sortingDate to ${republishDate2} on node ${node.getName()} for republishdate 2");
                changed = true;
            } else if (republishDate1 != null && now >= republishDate1 && !isRepublished1) {
                node.setProperty("isRePublished1", true);
                def c = java.util.Calendar.getInstance(); c.setTime(republishDate1)
                node.setProperty("sortingDate", c);
                logger.info("Setting sortingDate to ${republishDate1} on node ${node.getName()} for republishdate 1");
                changed = true;
            }
        }
        if (changed) {
            session.save();
        }
    }
});

def getDate(JCRNodeWrapper node, String propName) {
    try {
        return node.getProperty(propName).getDate().getTime()
    } catch (e) {
        // Ignore
    }
    return null;
}

def getBoolean(JCRNodeWrapper node, String propName) {
    try {
        return node.getProperty(propName).getBoolean();
    } catch (e) {
        // Ignore
    }
    return null;
}
