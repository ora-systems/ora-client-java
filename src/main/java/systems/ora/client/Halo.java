package systems.ora.client;

import java.security.InvalidParameterException;

public class Halo {
  private int appId;
  private int id = 0;
  private double size = 0.0;
  private double complexity = 0.0;
  private double brightness = 0.0;
  private double wobble = 0.0;
  private double color = 0.0;
  private double speed = 0.0;

  /**
   * Create an empty halo object.
   * @param iAppId The appplication id.
   */
  public Halo(int iAppId) {
    appId = iAppId;
  }

  /**
   * Create a halo object with all settings pre-set.
   * @param iAppId      The applicaation id.
   * @param iSize       The size value (0-1)
   * @param iColor      The color value (0-1)
   * @param iComplexity The complexity value
   * @param iSpeed      The speed value
   * @param iBrightness The brightness value
   * @param iWobble     The wobble value
   */
  public Halo(int iAppId, 
      double iSize, double iColor, double iComplexity, double iSpeed, double iBrightness, double iWobble) 
      throws InvalidParameterException 
  {
    appId = iAppId;

    validate(iSize);
    validate(iColor);
    validate(iComplexity);
    validate(iSpeed);
    validate(iBrightness);
    validate(iWobble);

    size       = iSize;
    color      = iColor;
    complexity = iComplexity;
    speed      = iSpeed;
    brightness = iBrightness;
    wobble     = iWobble;
  }

  /**
   * Provides a displayable string of halo information.
   */
  public String toString() {
    return "haloId: " + Integer.toString(id) +
        "\nappId: " + Integer.toString(appId) +
        "\nsize: " + Double.toString(size) +
        "\ncolor: " + Double.toString(color) +
        "\ncomplexity: " + Double.toString(complexity) +
        "\nspeed: " + Double.toString(speed) +
        "\nbrightness: " + Double.toString(brightness) +
        "\nwobble: " + Double.toString(wobble) + "\n";
  }

  /**
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * @return size value (0-1)
   */
  public double getSize() {
    return size;
  }

  /**
   * @return color value (0-1)
   */
  public double getColor() {
    return color;
  }

  /**
   * @return complexity value (0-1)
   */
  public double getComplexity() {
    return complexity;
  }

  /**
   * @return speed value (0-1)
   */
  public double getSpeed() {
    return speed;
  }

  /**
   * @return brightness value (0-1)
   */
  public double getBrightness() {
    return brightness;
  }

  /**
   * @return wobble value (0-1)
   */
  public double getWobble() {
    return wobble;
  }

  /**
   * @param haloId    halo id. Changing this will store it under a new id, and is not recommended.
   */
  public void setId(int haloId) {
    id = haloId;
  }

  /**
   * @param s size value (0-1)
   */
  public void setSize(double s) throws InvalidParameterException {
    validate(s);
    size = s;
  }

  /**
   * @param c color value (0-1)
   */
  public void setColor(double c) throws InvalidParameterException {
    validate(c);
    color = c;
  }

  /**
   * @param c complexity value (0-1)
   */
  public void setComplexity(double c) throws InvalidParameterException {
    validate(c);
    complexity = c;
  }

  /**
   * @param s speed value (0-1)
   */
  public void setSpeed(double s) throws InvalidParameterException {
    validate(s);
    speed = s;
  }

  /**
   * @param b brightness value (0-1)
   */
  public void setBrightness(double b) throws InvalidParameterException {
    validate(b);
    brightness = b;
  }

  /**
   * @param w wobble value (0-1)
   */
  public void setWobble(double w) throws InvalidParameterException {
    validate(w);
    wobble = w;
  }

  /**
   * @param val any value to verify as being within the correct range.
   */
  private void validate(double val) throws InvalidParameterException {
    if (val < 0.0 || val > 1.0) throw new InvalidParameterException();
  }
}