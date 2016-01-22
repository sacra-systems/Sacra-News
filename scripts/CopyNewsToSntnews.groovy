import org.jahia.api.Constants
import org.jahia.services.content.*

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

sysout = out;

def copyProperty(JCRNodeWrapper to, JCRNodeWrapper from, String prop) {
    try {
        if (from.hasProperty(prop)) {
            Value v = from.getProperty(prop).getValue();
            if (v!=null) {
                to.setProperty(prop, v);
            }
        }
    } catch (javax.jcr.PathNotFoundException e) {
        // Ignore this
    } catch (java.lang.Exception e) {
        sysout << " Failed to set property " << e << "\n"
    }
}

JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, Locale.GERMAN, new JCRCallback() {
    public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
        JCRNodeWrapper newsStorage = session.getNode("/sites/hlmartin/contents/news");
        Query q = session.getWorkspace().getQueryManager().createQuery("SELECT * FROM [jnt:news]", Query.JCR_SQL2);
        NodeIterator ni = q.execute().getNodes();
        while (ni.hasNext()) {
            JCRNodeWrapper next = (JCRNodeWrapper) ni.next();
            JCRNodeWrapper newNode = newsStorage.addNode(next.getName(), "snt:news")
            sysout << "Copied node" << next.getPath() << " to " << newNode.path << "\n";

            copyProperty(newNode, next, "jcr:title");
            copyProperty(newNode, next, "desc");
            copyProperty(newNode, next, "image");
            copyProperty(newNode, next, "imageTitle");
            copyProperty(newNode, next, "date");
            copyProperty(newNode, next, "sortingDate");
            copyProperty(newNode, next, "republishDate1");
            copyProperty(newNode, next, "isRePublished1");
            copyProperty(newNode, next, "republishDate2");
            copyProperty(newNode, next, "isRePublished2");
            copyProperty(newNode, next, "republishDate3");
            copyProperty(newNode, next, "isRePublished3");
            copyProperty(newNode, next, "jcr:lastModified");
            copyProperty(newNode, next, "jcr:lastPublishedBy");
            //copyProperty(newNode, next, "jcr:createdBy");
            //copyProperty(newNode, next, "jcr:created");
        }
        session.save();
    }
});


