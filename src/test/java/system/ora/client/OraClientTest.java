package system.ora.client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Unit test for simple the Ora Client.
 */
public class OraClientTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public OraClientTest( String testName )
    {
      super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
      return new TestSuite( OraClientTest.class );
    }

    public void testOraClientConfiguration()
    {        
      assertNotNull( new OraClient() );
    }

    public void testCreateHalo() {
      Halo h = new Halo(1000401);
      assertNotNull(h);
    }

    public void testCreateHaloWithData() {
      Halo h = new Halo(1000401, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6);
      assertNotNull(h);
    }

    public void testCreateHaloWithBadData() {
      try {
        Halo h = new Halo(1000401, 1.01, 0.2, 0.3, 0.4, 0.5, 0.6);        
        assertNull(h);
      } catch (InvalidParameterException ex) {
        assertNotNull(ex);
      }
    }

    public void testModifyHaloWithBadData() {
      Halo h = null;
      try {
        h = new Halo(1000401, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6);        
        assertNotNull(h);                
      } catch (InvalidParameterException ex) {
        assertNull(ex);
      }

      try {
        h.setSize(1.01);
      } catch (InvalidParameterException ex) {
        assertNotNull(ex);
      }
    }

    public void testOraGetHaloSuccess()
    {
      OraClient cli = new OraClient();
      try {
        Halo h = cli.getHalo(10000216);
        assertNotNull(h);
      } catch (IOException ex) {
        assertNull(ex);
      }
    }

    public void testOraGetHaloFailure()
    {
      OraClient cli = new OraClient();
      try {
        Halo h = cli.getHalo(-77);
        assertNull(h);
      } catch (IOException ex) {
        assertNull(ex);
      }
    }

    public void testOraCreateHaloSuccess()
    {
      OraClient cli = new OraClient();
      Halo halo = new Halo(1000401, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6);
      try {
        Halo h = cli.insertHalo(halo);
        assertNotNull(h);
        assertTrue(h.getId() > 0);

      } catch (IOException ex) {
        assertNull(ex);
      }     
    }

    public void testOraUpdateHaloSuccess() 
    {
      OraClient cli = new OraClient();
      Halo halo = new Halo(1000401, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6);
      try {
        Halo h = cli.insertHalo(halo);
        assertNotNull(h);
        assertTrue(h.getId() > 0);
        h.setSize(1.0);
        cli.updateHalo(h);
        h = cli.getHalo(h.getId());
        assertTrue(h.getSize() == 1.0);

      } catch (IOException ex) {
        assertNull(ex);
      }     
    }

}
