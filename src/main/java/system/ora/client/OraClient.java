package system.ora.client;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import org.json.*;

class OraClient {

  int    appId = 1000401;
  String baseURL = "http://sandbox.ora.me:6196/halo";

  public OraClient() {}

  private String jsonifyHalo(Halo h) {
    try {
      JSONObject job = new JSONObject();
      job.put("app", appId);
      if (h.getId() > 0) {
        job.put("id", h.getId());
      }
      JSONObject jvrt = new JSONObject();
      jvrt.put("size", h.getSize());
      jvrt.put("color", h.getColor());
      jvrt.put("complexity", h.getComplexity());
      jvrt.put("speed", h.getSpeed());
      jvrt.put("brightness", h.getBrightness());
      jvrt.put("wobble", h.getWobble());
      job.put("vertices", jvrt);
      return job.toString();
    } catch (JSONException ex) {
      return null;
    }
  }

  private Halo readHalo(HttpURLConnection conn) throws IOException {

    // Read it.
    BufferedReader in = new BufferedReader(
      new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    while((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    // Parse it.
    try {
      JSONObject job = new JSONObject(response.toString());
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
    } catch (JSONException ex) {
      return null;
    }
  }

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

  public Halo insertHalo(Halo halo) throws IOException {
    URL url = new URL(baseURL);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    String json = jsonifyHalo(halo);

    conn.setRequestMethod("POST");
    conn.setRequestProperty("User-Agent", "OraClient 0.1");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("Content-Length", Integer.toString(json.getBytes().length));
    conn.setUseCaches(false);
    conn.setDoInput(true);
    conn.setDoOutput(true);

    // Write body
    DataOutputStream out = new DataOutputStream(
      conn.getOutputStream());
    out.writeBytes(json);
    out.flush();
    out.close();

    Halo h = readHalo(conn);

    return h;

  }

  public Halo updateHalo(Halo halo) throws IOException {
    URL url = new URL(baseURL + "/" + halo.getId());
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    String json = jsonifyHalo(halo);

    conn.setRequestMethod("PUT");
    conn.setRequestProperty("User-Agent", "OraClient 0.1");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("Content-Length", Integer.toString(json.getBytes().length));
    conn.setUseCaches(false);
    conn.setDoInput(true);
    conn.setDoOutput(true);

    // Write body
    DataOutputStream out = new DataOutputStream(
      conn.getOutputStream());
    out.writeBytes(json);
    out.flush();
    out.close();

    Halo h = readHalo(conn);

    return h;
  }

}