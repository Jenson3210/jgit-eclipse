package org.eclipse.jgit.ignore;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ShowcaseBehaviour1Test {

    //Let's load a "file" containing a single rule for /directory/nested/.
    //This should exclude the directory /directory/nested/again
    @Test
    public void IgnoreNodeUsesPathMatchTrue() throws IOException {
        IgnoreNode ignoreNode = new IgnoreNode();
        ignoreNode.parse(new ByteArrayInputStream("/directory/nested/".getBytes()));

        IgnoreNode.MatchResult isIgnored = ignoreNode.isIgnored("/directory/nested/again", true);

        assertEquals(IgnoreNode.MatchResult.IGNORED, isIgnored);
        // The result should not be CHECK_PARENT?
    }

    //IgnoreNode.MatchResult isIgnored = ignoreNode.isIgnored("/directory/nested/again", true);
    //                                              ^^^^^^^^^
    //                                              Path match is always sent as true (see difference in next test)
    //                                              The result is NOT correct

    @Test
    public void fastIgnoreRuleForNestedDirectory() {
        //This PASSES as isMatch returns true
        FastIgnoreRule ignoreRule1 = new FastIgnoreRule("/directory/nested/");
        assertTrue(ignoreRule1.isMatch("/directory/nested/again", true, false)); // It matches the directory
        //                                                                                     ^^^^^

        //This FAILS as isMatch returns false
        FastIgnoreRule ignoreRule2 = new FastIgnoreRule("/directory/nested/");
        assertTrue(ignoreRule2.isMatch("/directory/nested/again", true, true)); // It matches the directory
        //                                                                                     ^^^^^
    }
}
