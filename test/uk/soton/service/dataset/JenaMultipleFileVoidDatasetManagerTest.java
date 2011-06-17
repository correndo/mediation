package uk.soton.service.dataset;

import java.util.List;

import org.junit.Test;

public class JenaMultipleFileVoidDatasetManagerTest {

	@Test
	public void testGetModel() {
		JenaMultipleFileVoidDatasetManager jmfdsm = new JenaMultipleFileVoidDatasetManager("resources/void");
		List<String> ids = jmfdsm.getDatasetsID();
		System.out.println(ids);
		String spep = jmfdsm.getSPARQLEndpointURL("http://kisti.rkbexplorer.com/id/void");
		System.out.println(spep );
	}
}
