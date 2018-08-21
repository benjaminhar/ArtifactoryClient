package com.ben.example.arifactory.downloads;

import org.junit.Test;

import com.ben.example.artifactory.downloads.Util;

import junit.framework.TestCase;


/**
 * Unit test for simple App.
 */
public class UtilTest extends TestCase {

	@Test
    public void testGenerateQueryPartForArtifact()
    {
    	String artifactName = "junit-3.8.1.jar";
    	String artifactPath = "junit/junit/3.8.1";
    	int pageSizeLimit = 1000;
    	int index1 = 1;
    	int index2 = 1000;
    	String expectedCaseNormal = "{   \"$and\":[   {\"name\":{\"$eq\":\"" + artifactName +"\"}}," +
                "   {\"path\":{\"$eq\":\"" + artifactPath + "\"}} ] },"; 
    	String expectedCaseUpperLimit = "{   \"$and\":[   {\"name\":{\"$eq\":\"" + artifactName +"\"}}," +
                "   {\"path\":{\"$eq\":\"" + artifactPath + "\"}} ] }"; 
    	assertEquals(expectedCaseNormal, Util.generateQueryPartForArtifact(artifactName, artifactPath, index1, pageSizeLimit));
    	assertEquals(expectedCaseUpperLimit, Util.generateQueryPartForArtifact(artifactName, artifactPath, index2, pageSizeLimit));
    }
}
