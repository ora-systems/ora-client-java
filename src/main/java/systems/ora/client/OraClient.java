package systems.ora.client;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.util.List;
import java.util.ArrayList;
import org.json.*;

public class OraClient {

  int    appId = 1000401;
  String baseURL = "http://sandbox.ora.me:6196/halo";

  public OraClient() {}

  private JSONObject jsonifyVertices(Halo h) {
    try {
      JSONObject jvrt = new JSONObject();
      jvrt.put("size", h.getSize());
      jvrt.put("color", h.getColor());
      jvrt.put("complexity", h.getComplexity());
      jvrt.put("speed", h.getSpeed());
      jvrt.put("brightness", h.getBrightness());
      jvrt.put("wobble", h.getWobble());
      return jvrt;
    } catch (JSONException ex) {
      return null;
    }
  }

  private String jsonifyHalo(Halo h) {
    try {
      JSONObject job = new JSONObject();
      job.put("app", appId);
      if (h.getId() > 0) {
        job.put("id", h.getId());
      }
      job.put("vertices", jsonifyVertices(h));
      return job.toString();
    } catch (JSONException ex) {
      return null;
    }
  }

  private String jsonifyList(List<Halo> halos) {
    try {
      JSONObject job = new JSONObject();
      job.put("app", appId);
      JSONArray list = new JSONArray();
      for (Halo h : halos) {
        list.put(jsonifyVertices(h));
      }
      job.put("vertices", list);
      return job.toString();
    } catch (JSONException ex) {
      return null;
    }
  }

  private String readResponse(HttpURLConnection conn) throws IOException {
    // Read it.
    BufferedReader in = new BufferedReader(
      new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    while((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    return response.toString();
  }

  private HttpURLConnection sendData(URL url, String method, String data) throws IOException {

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setRequestMethod(method);
    conn.setRequestProperty("User-Agent", "OraClient 0.1");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));
    conn.setUseCaches(false);
    conn.setDoInput(true);
    conn.setDoOutput(true);

    // Write body
    DataOutputStream out = new DataOutputStream(
      conn.getOutputStream());
    out.writeBytes(data);
    out.flush();
    out.close();

    return conn;
  }

  private Halo parseHalo(JSONObject job) throws JSONException {
    JSONObject vert = job.getJSONObject("vertices");

    Halo h = new Halo(appId,
      vert.getDouble("size"),
      vert.getDouble("color"),
      vert.getDouble("complexity"),
      vert.getDouble("speed"),
      vert.getDouble("brightness"),
      vert.getDouble("wobble")
      );
    h.setId(job.getInt("id"));
    return h;
  }

  private Halo readHalo(HttpURLConnection conn) throws IOException {

    String response = readResponse(conn);

    // Parse it.
    try {
      JSONObject job = new JSONObject(response);
      return parseHalo(job);
    } catch (JSONException ex) {
      return null;
    }
  }

  /**
   * Fetches the halo with specified id from the server.
   * If the server is unavailable or otherwise reports an error, an exception will be thrown.
   * If there is no such halo on the server, this returns null.
   * @param id    The halo id
   * @return      The halo, if found. null if not. 
   * @throws IOException  On server error
   */
  public Halo getHalo(int id) throws IOException {
    String route = baseURL + "/" + Integer.toString(id);

    URL url = new URL(route);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setRequestMethod("GET");
    conn.setRequestProperty("User-Agent", "OraClient 0.1");

    // Should we handle anything separate here?
    if (conn.getResponseCode() != 200) {
      return null;
    }

    Halo h = readHalo(conn);

    if (conn != null) {
      conn.disconnect();
    }

    return h;
  }

  /**
   * Sends a halo to the server.
   * @param halo    A halo object to store.
   * @return        Updated halo.
   * @throws IOException On server failure.
   */
  public Halo insertHalo(Halo halo) throws IOException {
    URL url = new URL(baseURL);

    String json = jsonifyHalo(halo);

    HttpURLConnection conn = sendData(url, "POST", json);

    Halo h = readHalo(conn);

    return h;

  }

  /**
   * Persists the new state of a halo to the server.
   * @param halo    A halo object to ubdate on the server.
   * @return        Updated halo.
   * @throws IOException  On server failure.
   */
  public Halo updateHalo(Halo halo) throws IOException {
    URL url = new URL(baseURL + "/" + halo.getId());

    String json = jsonifyHalo(halo);

    HttpURLConnection conn = sendData(url, "PUT", json);

    Halo h = readHalo(conn);

    return h;
  }
  
  /**
   * Sends a list of halos to the server.
   * @param  halos    A list of halos.
   * @return          the id for the collection of halos.
   * @throws IOException On server failure.
   */
  public int insertHaloList(List<Halo> halos) throws IOException {
    URL url = new URL(baseURL + "/collection");

    String json = jsonifyList(halos);

    HttpURLConnection conn = sendData(url, "POST", json);

    String response = readResponse(conn);

    // Parse it.
    try {
      JSONObject job = new JSONObject(response);
      int collectionId = job.getInt("id");
      JSONArray jalos = job.getJSONArray("halos");

      for (int i = 0; i < jalos.length(); i++ ) {
        JSONObject jhob = jalos.getJSONObject(i);
        Halo h = halos.get(i);
        h.setId(jhob.getInt("id"));
      }
      return collectionId;
    } catch (JSONException ex) {
      return -1;
    }

  }
  
  /**
   * get a list of halos from the server.
   * @param collectionId    The id of the collection.
   * @return                List of halos, if found.
   * @throws IOException    On server failure.   
   */
  public ArrayList<Halo> getHaloCollection(int collectionId) throws IOException {
    URL url = new URL(baseURL + "/collection/" + Integer.toString(collectionId));

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setRequestMethod("GET");
    conn.setRequestProperty("User-Agent", "OraClient 0.1");

    // Should we handle anything separate here?
    if (conn.getResponseCode() != 200) {
      return null;
    }

    String response = readResponse(conn);

    ArrayList<Halo> halos = new ArrayList<Halo>();

    try {
      JSONArray idList = new JSONArray(response);
      for (int i = 0; i < idList.length(); i++ ) {
        halos.add(getHalo(idList.getInt(i)));
      }
      return halos;
    } catch (JSONException ex) {
      return null;
    }

  }

}